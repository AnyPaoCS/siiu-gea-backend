/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.schedulers;

import com.umss.siiu.bpmn.model.Job;
import com.umss.siiu.bpmn.service.ConfigurationService;
import com.umss.siiu.bpmn.service.JobBpmService;
import com.umss.siiu.core.model.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class JobScheduler {

    private static final String DOWNLOAD_POLICY = "DOWNLOAD_POLICY";
    private static final String MAX_RETRIES = "maxRetries";
    private boolean allocationInProgress = false;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ReentrantLock lock = new ReentrantLock();
    private volatile AtomicBoolean runningDaemon = new AtomicBoolean(false);
    private Condition noRunningCondition = lock.newCondition();
    private ConfigurationService configurationService;
    private JobBpmService jobBpmService;

    private static final String CURRENT_THREAD_TEXT = "Current Thread :%s";

    public JobScheduler(ConfigurationService configurationService
            , JobBpmService jobBpmService) {
        this.configurationService = configurationService;
        this.jobBpmService = jobBpmService;
    }

   // @Scheduled(fixedDelayString = "${scheduler.jobFetch.interval}")
    public synchronized void execFetchJobsFromCrm() throws InterruptedException {
        try {
            lock.lock();
            while (runningDaemon.get()) {
                noRunningCondition.await();
            }
            runningDaemon.set(true);
            logger.info("FetchJobs ini");
            var lockMessage = lock.toString();
            logger.info(lockMessage);
            var threadMessage = String.format(CURRENT_THREAD_TEXT, Thread.currentThread().getName());
            logger.info(threadMessage);
            List<Job> jobs = new ArrayList<>();
            if (!jobs.isEmpty()) {
                jobBpmService.createJobBpms(jobs);
            }
            logger.info("FetchJobs end");
        } finally {
            runningDaemon.set(false);
            noRunningCondition.signalAll();
            lock.unlock();
        }

    }

    //@Scheduled(fixedDelayString = "${scheduler.allocation.interval}")
    // por el momento se ejecuta en 2min para ver el funcionamiento
    @Scheduled(cron = "0 */2 * * * *")
    public synchronized void execAllocation() throws InterruptedException {
        logger.info("Se esta ejecutando la asignacion de tareas a empleados disponibles");
        try {
            lock.lock();
            while (runningDaemon.get()) {
                noRunningCondition.await();
            }
            runningDaemon.set(true);
            allocationInProgress = true;
            logger.info("assign ini");
            var threadMessage = String.format(CURRENT_THREAD_TEXT, Thread.currentThread().getName());
            logger.info(threadMessage);
            jobBpmService.allocateResources();
            logger.info("assign end");
        } finally {
            runningDaemon.set(false);
            allocationInProgress = false;
            noRunningCondition.signalAll();
            lock.unlock();
        }
    }

   // @Scheduled(fixedDelayString = "${scheduler.downloadTemplates.interval}")
    public synchronized void downloadJobInformation() throws InterruptedException {
        try {
            lock.lock();
            while (runningDaemon.get()) {
                noRunningCondition.await();
            }
            runningDaemon.set(true);
            logger.info("download ini");
            var threadMessage = String.format(CURRENT_THREAD_TEXT, Thread.currentThread().getName());
            logger.info(threadMessage);
            downloadJobsFiles();
            logger.info("download end");
        } finally {
            runningDaemon.set(false);
            noRunningCondition.signalAll();
            lock.unlock();
        }
    }

    public void downloadJobsFiles() {
        List<Configuration> maxRetriesConfigurations = configurationService.findByTypeAndEntryKeyAndActiveTrue(
                DOWNLOAD_POLICY, MAX_RETRIES);
                Integer.parseInt(maxRetriesConfigurations.get(0).getValue());
    }

    public boolean isAllocationInProgress() {
        return allocationInProgress;
    }
}
