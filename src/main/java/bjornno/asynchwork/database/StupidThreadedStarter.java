package bjornno.asynchwork.database;

import bjornno.asynchwork.Scheduler;
import bjornno.asynchwork.WorkManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 */
public class StupidThreadedStarter implements Scheduler {
    private WorkManager workManager;

    public StupidThreadedStarter(WorkManager workManager) {
        this.workManager = workManager;
    }

    public void setWorkManager(WorkManager workManager) {
        this.workManager = workManager;
    }

    public void start() {
        System.out.println("Starting asynchwork scanner.");
        Executor executor = Executors.newFixedThreadPool(1);
        executor.execute(workManager);
    }

}
