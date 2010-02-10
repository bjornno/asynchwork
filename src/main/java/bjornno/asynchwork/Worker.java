package bjornno.asynchwork;

/**
 */
public class Worker implements Runnable {
    private Message message;
    ReceivingService receiver;

    public Worker(Message message, ReceivingService receiver) {
        this.message = message;
        this.receiver = receiver;
    }

    public Message getMessage() {
        return message;
    }

    public void run() {
        receiver.receive(message);
    }
}
