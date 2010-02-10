package bjornno.asynchwork;

import java.util.Calendar;
import java.util.Date;

/**
 */
public class Message  {
    private long id;
    private String message;
    private int processingState;
    private int priority;
    private String receiverBeanName;
    private String createdByServerId;
    private String processingServerId;
    private Date createdTime;
    private Date startTime;
    private Date finishedTime;
    private Date earliestExecutionTime; 
    private String errorMessage;
    private int numberOfRetries;

    public Message(int id, String message, String receiverBeanName) {
        this(id, message, receiverBeanName, null, 0);
    }
    public Message(int id, String message, String receiverBeanName, Date earliestExecutionTime) {
        this(id, message, receiverBeanName, earliestExecutionTime, 0);
    }

    public Message(int id, String message, String receiverBeanName, Date earliestExecutionTime, int priority ) {
        this.id = id;
        this.message = message;
        this.receiverBeanName = receiverBeanName;
        this.earliestExecutionTime = earliestExecutionTime;
        this.createdTime = Calendar.getInstance().getTime();
        this.priority = priority;
    }

    public long getId() {
        return id;
    }

    public synchronized String getPayload() {
        return message;
    }

    public int getProcessingState() {
        return processingState;
    }

    public int getPriority() {
        return priority;
    }

    public String getCreatedByServerId() {
        return createdByServerId;
    }

    public String getProcessingServerId() {
        return processingServerId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime() {
        startTime = Calendar.getInstance().getTime();
    }

    public Date getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime() {
        finishedTime = Calendar.getInstance().getTime();
    }

    public Date getEarliestExecutionTime() {
        return earliestExecutionTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getNumberOfRetries() {
        return numberOfRetries;
    }

    public void increaseNumberOfRetries() {
        numberOfRetries += 1;
        if (numberOfRetries >= 3) {
            processingState = 2;
        }
    }
    public String getReceiverBeanName() {
        return receiverBeanName;
    }

    public void markAsProcessed() {
        processingState = 2;
    }
}
