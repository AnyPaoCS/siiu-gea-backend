package com.umss.siiu.filestorage.repository;

import com.umss.siiu.bpmn.model.Job;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.filestorage.model.FileType;
import com.umss.siiu.filestorage.model.JobFileTypeLock;

import java.util.List;

public interface JobFileTypeLockRepository extends GenericRepository<JobFileTypeLock> {
    JobFileTypeLock findByJobAndFileType(Job job, FileType fileType);

    JobFileTypeLock findByJobIdAndFileTypeId(long jobId, long fileTypeId);

    JobFileTypeLock findByJobAndEmployeeAndFileType(Job job, Employee employee, FileType fileType);

    List<JobFileTypeLock> findByJobId(Long jobId);
}
