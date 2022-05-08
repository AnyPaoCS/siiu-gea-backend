/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.filestorage.service;

import com.umss.siiu.bpmn.model.Job;
import com.umss.siiu.core.exceptions.BlockedFileException;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.service.GenericServiceImpl;
import com.umss.siiu.filestorage.model.FileType;
import com.umss.siiu.filestorage.model.JobFileTypeLock;
import com.umss.siiu.filestorage.repository.JobFileTypeLockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class JobFileTypeLockServiceImpl extends GenericServiceImpl<JobFileTypeLock> implements JobFileTypeLockService {

    private JobFileTypeLockRepository repository;

    public JobFileTypeLockServiceImpl(JobFileTypeLockRepository repository) {
        this.repository = repository;
    }

    @Override
    public JobFileTypeLock findByJobAndEmployeeAndFileType(Job job, Employee employee, FileType fileType) {
        return repository.findByJobAndEmployeeAndFileType(job, employee, fileType);
    }

    @Override
    public JobFileTypeLock findByJobAndFileType(Job job, FileType fileType) {
        return repository.findByJobAndFileType(job, fileType);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public JobFileTypeLock acquireLock(Job job, Employee employee, FileType fileType) throws RuntimeException {
        JobFileTypeLock jobLock = findByJobAndFileType(job, fileType);
        if (null == jobLock) {
                jobLock = new JobFileTypeLock();
                jobLock.setJob(job);
                jobLock.setEmployee(employee);
                jobLock.setFileType(fileType);
                // lock
                jobLock = save(jobLock);
                return jobLock;
        } else {
            throw new BlockedFileException(String.format("The file is being modified by %s",
                    jobLock.getEmployee().getFullName(true)));
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void releaseLock(Long jobLockId, Job job, Employee employee, FileType fileType) {
        var jobFileTypeLock = validateLockExists(jobLockId);
        validateLockEmployee(employee, jobFileTypeLock);
        try {
            // releases the lock
            deleteById(jobLockId);
        } catch (Exception e) {
            throw new RuntimeException(String.format(
                    "There was an error trying to delete the lock for job: %s file type: %s " + "employee: %s",
                    job.getId(), fileType.getName(), employee.getFullName(true)));
        }
    }

    @Override
    public List<JobFileTypeLock> findByJob(Long jobId) {
        return repository.findByJobId(jobId);
    }

    private JobFileTypeLock validateLockExists(Long jobLockId) {
        var jobFileTypeLock = findById(jobLockId);
        if (jobFileTypeLock == null) {
            throw new RuntimeException("Lock id is missing. The lock may have never been persisted or acquired.");
        }
        return jobFileTypeLock;
    }

    private void validateLockEmployee(Employee employee, JobFileTypeLock jobFileTypeLock) {
        var lockEmployee = jobFileTypeLock.getEmployee();
        if (lockEmployee != null && !lockEmployee.getId().equals(employee.getId())) {
            throw new BlockedFileException(String.format("The lock is owned by %s", lockEmployee.getFullName(true)));
        }
    }

    @Override
    protected GenericRepository<JobFileTypeLock> getRepository() {
        return repository;
    }

}
