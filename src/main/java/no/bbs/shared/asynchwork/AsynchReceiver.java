package no.bbs.shared.asynchwork;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 */
public interface AsynchReceiver {
    Message receiveNext(String receiverName);
    List<Worker> receiveAllReadyPrioritized(ReceivingService receiver);
    List<Worker> receiveAllReadyForAllReceiversPrioritized(Map<String, ReceivingService> receiverServices);
    List<Worker> receiveAllReadyForAllReceiversPrioritized(Map<String, ReceivingService> receiverServices, int maxResult);
    void updateProcessingState(Message work, int status);
}
