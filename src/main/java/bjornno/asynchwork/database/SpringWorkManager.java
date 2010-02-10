package bjornno.asynchwork.database;

import bjornno.asynchwork.*;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 */
public class SpringWorkManager implements WorkManager {
    private Map<String, ReceivingService> receivingServices;

    private AsynchReceiver asynchReceiver;
    private TransactionTemplate transactionTemplate;
    private int numberOfThreads = 6;

    public SpringWorkManager(Map receivingServices, AsynchReceiver receiver, TransactionTemplate transactionTemplate) {
        this.receivingServices = receivingServices;
        this.asynchReceiver = receiver;
        this.transactionTemplate = transactionTemplate;
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public void setAsynchReceiver(AsynchReceiver asynchReceiver) {
        this.asynchReceiver = asynchReceiver;
    }

    public void setReceivingServices(Map receivingServices) {
        this.receivingServices = receivingServices;
    }

    //  something like this? @Scheduled(fixedRate=#{myprops.scanningInterval})
    //@Scheduled(fixedRate=5000)
    public void run() {
        final Executor executor = Executors.newFixedThreadPool(numberOfThreads);
        List<Worker> allWork = transactionTemplate.execute(new TransactionCallback<List<Worker>>() {
            public List<Worker> doInTransaction(TransactionStatus status) {
                return asynchReceiver.receiveAllReadyForAllReceiversPrioritized(receivingServices);
            }
        });

        for (Iterator<Worker> queueIterator = allWork.iterator(); queueIterator.hasNext();) {
            final Worker work = queueIterator.next();
            final Message message = work.getMessage();
            try {
                message.setStartTime();
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        executor.execute(work);
                    }
                });
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        asynchReceiver.updateProcessingState(message, 3);
                    }
                });

                //work.setFinishedTime();// this is wrong, should be after processed, but it's in a thread..
            } catch (Exception e) {
                 transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        asynchReceiver.updateProcessingState(message, 2);
                    }
                });
                //work.setErrorMessage(e.getMessage());
                //work.setFinishedTime();
                //work.increaseNumberOfRetries();
            }
        }
    }

   


}
