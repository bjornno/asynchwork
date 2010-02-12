package bjornno.asynchwork.database;

import bjornno.asynchwork.Scheduler;
import bjornno.asynchwork.WorkManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 */
public class StupidScheduler implements Scheduler {
    private WorkManager workManager;

    public StupidScheduler(WorkManager workManager) {
        this.workManager = workManager;
    }

    public void setWorkManager(WorkManager workManager) {
        this.workManager = workManager;
    }

    public void start() {
        Executor executor = Executors.newFixedThreadPool(1);
        while(true) {
            executor.execute(workManager);
            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
