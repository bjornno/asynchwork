package bjornno.asynchwork;

import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;

/**
 */
public interface WorkManager extends Runnable {
    void setReceivingServices(Map receivingServices);
    void setAsynchReceiver(AsynchReceiver asynchReceiver);
    void setTransactionTemplate(TransactionTemplate transactionTemplate);
}
