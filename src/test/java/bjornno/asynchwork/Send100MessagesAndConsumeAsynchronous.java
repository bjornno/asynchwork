package bjornno.asynchwork;

import bjornno.asynchwork.database.JdbcReceiver;
import bjornno.asynchwork.database.JdbcSender;
import bjornno.asynchwork.database.SpringWorkManager;
import org.junit.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 */
public class Send100MessagesAndConsumeAsynchronous {
    //@Test
    public void sendAndRetrieve100MessagesAsynch() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.HSQL).addScript("db-provision.sql").build();

        send100Messages(db);
        Map map = createASimpleToStringService();
        startSingleThreadedFixedTimeWorkManager(db, map);


    }

    private void startSingleThreadedFixedTimeWorkManager(EmbeddedDatabase db, Map map) {
        AsynchReceiver receiver = new JdbcReceiver(db);
        WorkManager mngr = new SpringWorkManager(map, receiver, db);
        Executors.newSingleThreadExecutor().execute(mngr);

        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.exit(0);
    }

    private Map createASimpleToStringService() {
        Map map = new HashMap();
        map.put("service1", new ReceivingService() {
            private Message message;
            public synchronized void receive(Message message) {
                this.message = message;
                System.out.println(getMessage());
            }
            private synchronized String getMessage() {
               return message.getPayload();
            }

            public String getReceiverName() {
                return "service1";
            }
        });
        return map;
    }

    private void send100Messages(EmbeddedDatabase db) {
        AsynchSender sndr = new JdbcSender(db);
        for (int i = 0; i < 100; i++) {
            sndr.send("en melding " + i, "service1");
        }
    }
}
