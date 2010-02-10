package bjornno.asynchwork;

/**
 */
public interface ReceivingService  {
    void receive(Message message);
    String getReceiverName();
}
