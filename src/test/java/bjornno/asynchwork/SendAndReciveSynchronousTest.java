package bjornno.asynchwork;

import junit.framework.Assert;
import bjornno.asynchwork.database.JdbcReceiver;
import bjornno.asynchwork.database.JdbcSender;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 */
public class SendAndReciveSynchronousTest {

    @Test
    public void testSendAndReceive() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.HSQL).addScript("db-provision.sql").build();

        AsynchSender sender = new JdbcSender(db);
        AsynchReceiver receiver = new JdbcReceiver(db);

        sender.send("hei", "test");
        Assert.assertEquals("hei", receiver.receiveNext("test").getPayload());
    }
}
