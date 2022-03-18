package com.umss.siiu.bpmn.repository;

import com.umss.siiu.bpmn.model.JobBpm;
import com.umss.siiu.core.repository.GenericRepository;

public interface JobBpmRepository extends GenericRepository<JobBpm> {
    JobBpm findByJobId(long jobId);
}
