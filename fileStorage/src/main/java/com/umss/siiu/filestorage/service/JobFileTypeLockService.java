/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.filestorage.service;

import com.umss.siiu.bpmn.model.Job;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.service.GenericService;
import com.umss.siiu.filestorage.model.FileType;
import com.umss.siiu.filestorage.model.JobFileTypeLock;

import java.util.List;

public interface JobFileTypeLockService extends GenericService<JobFileTypeLock> {
    JobFileTypeLock findByJobAndEmployeeAndFileType(Job job, Employee employee, FileType fileType);

    JobFileTypeLock acquireLock(Job job, Employee employee, FileType fileType);

    void releaseLock(Long jobLockId, Job job, Employee employee, FileType fileType);

    List<JobFileTypeLock> findByJob(Long jobId);

    JobFileTypeLock findByJobAndFileType(Job job, FileType fileType);
}
