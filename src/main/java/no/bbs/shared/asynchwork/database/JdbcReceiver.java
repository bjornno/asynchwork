package no.bbs.shared.asynchwork.database;

import no.bbs.shared.asynchwork.AsynchReceiver;
import no.bbs.shared.asynchwork.Message;
import no.bbs.shared.asynchwork.ReceivingService;
import no.bbs.shared.asynchwork.Worker;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 */
public class JdbcReceiver implements AsynchReceiver {
    private JdbcTemplate jdbcTemplate;

    public JdbcReceiver(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Message receiveNext(String receiverName) {
         List<Message> results = jdbcTemplate.query("select * from messages where processing_state = 0 and receiver_bean_name = '" + receiverName +"'", new RowMapper() {
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Message(rs.getInt("id"), rs.getString("message"), rs.getString("receiver_bean_name"));
            }
        });
        return results.get(0);
    }

    public List<Worker> receiveAllReadyPrioritized(final ReceivingService receiver) {
        List<Worker> results = jdbcTemplate.query("select * from messages where processing_state = 0 and receiver_bean_name = '" + receiver.getReceiverName() +"'", new RowMapper() {
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Worker(new Message(rs.getInt("id"), rs.getString("message"), rs.getString("receiver_bean_name")), receiver);
            }
        });
        return results;
    }

    public List<Worker> receiveAllReadyForAllReceiversPrioritized(Map<String, ReceivingService> receivers, int maxResult) {
        List<Worker> results = new LinkedList<Worker>();
        List<Worker> allResults = receiveAllReadyForAllReceiversPrioritized(receivers);
        for (int i = 0; i < maxResult; i++) {
            Worker message =  allResults.get(i);
            results.add(message);
        }
        return results;
    }

    public List<Worker> receiveAllReadyForAllReceiversPrioritized(final Map<String, ReceivingService> receivers) {
        //jdbcTemplate.queryForList()
        jdbcTemplate.execute("update messages set processing_state = 1 where processing_state = 0");
        List<Worker> results = jdbcTemplate.query("select * from messages where processing_state = 1", new RowMapper() {
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Worker(new Message(rs.getInt("id"), rs.getString("message"), rs.getString("receiver_bean_name")), receivers.get(rs.getString("receiver_bean_name")));
            }
        });
        return results;
    }

    public void updateProcessingState(Message work, int status) {
        jdbcTemplate.execute("update messages set processing_state = "+status+" where id = " +work.getId());
    }
}
