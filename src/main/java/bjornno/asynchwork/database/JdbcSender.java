package bjornno.asynchwork.database;

import bjornno.asynchwork.AsynchSender;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.UUID;

/**
 */
public class JdbcSender implements AsynchSender { 
    private JdbcTemplate jdbcTemplate;

    public JdbcSender(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void send(String message, String service) {
        jdbcTemplate.execute("insert into messages values("+ UUID.randomUUID().hashCode()+", 0,'" + message + "', '"+service+"')");
    }
}
