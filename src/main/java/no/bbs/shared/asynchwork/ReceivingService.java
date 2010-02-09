package no.bbs.shared.asynchwork;

/**
 */
public interface ReceivingService  {
    void receive(Message message);
    String getReceiverName();
}
