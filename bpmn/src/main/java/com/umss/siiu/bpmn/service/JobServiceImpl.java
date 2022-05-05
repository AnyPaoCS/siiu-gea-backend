/**
 * @author: Edson A. Terceros T.
 */
package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.model.Job;
import com.umss.siiu.bpmn.repository.JobRepository;
import com.umss.siiu.bpmn.repository.spceifications.JobSpecificationsBuilder;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.service.GenericServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class JobServiceImpl extends GenericServiceImpl<Job> implements JobService {

    private JobRepository repository;
    private JobBpmService jobBpmService;

    public JobServiceImpl(JobRepository repository,
            @Lazy JobBpmService jobBpmService) {
        this.repository = repository;
        this.jobBpmService = jobBpmService;
    }

    @Override
    public GenericRepository<Job> getRepository() {
        return repository;
    }

    @Override
    public Page<Job> findAll(String filter, Pageable pageable) {
        return repository.findAll(getQueryFrom(filter), pageable);
    }

    @Override
    protected Specification<Job> getQueryFrom(String filter) {
        return new JobSpecificationsBuilder().build(filter);
    }

    @Override
    public Job findById(Long id) {
        var job = super.findById(id);
        job.setJobBpm(jobBpmService.findByJobId(job.getId()));
        return job;
    }

}
