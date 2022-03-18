package com.umss.siiu.bpmn.controller;

import com.umss.siiu.bpmn.dto.JobDetailDto;
import com.umss.siiu.bpmn.dto.JobDto;
import com.umss.siiu.bpmn.model.Job;
import com.umss.siiu.bpmn.service.JobBpmService;
import com.umss.siiu.bpmn.service.JobService;
import com.umss.siiu.core.controller.GenericController;
import com.umss.siiu.core.service.GenericService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/jobs", produces = MediaType.APPLICATION_JSON_VALUE)
public class JobController extends GenericController<Job, JobDto> {

    private JobService service;

    private JobBpmService jobBpmService;

    public JobController(JobService service, JobBpmService jobBpmService) {
        this.service = service;
        this.jobBpmService = jobBpmService;
    }

    @Override
    protected GenericService<Job> getService() {
        return service;
    }

    @Override
    @GetMapping("/generic")
    public List<JobDto> findAll(@RequestParam(FILTER) String filter) {
        return super.findAll(filter);
    }

    @GetMapping("/paginated")
    public Page<JobDto> findPaginated(@RequestParam(PAGE) int page, @RequestParam(SIZE) int size,
            @RequestParam(value = FILTER, required = false) String filter) {
        return service.findAll(filter, getPageable(page, size)).map(this::convertFrom);
    }

    private JobDetailDto convertFrom(Job job) {
        job.setJobBpm(jobBpmService.findByJobId(job.getId()));
        JobDetailDto jobDto = new JobDetailDto().toDto(job, modelMapper);
        if (job.getJobBpm() != null) {
            jobDto.setAssignees(jobBpmService.getAssigneesByProcessInstanceId(job.getJobBpm().getProcessInstance().getId()));
        }
        return jobDto;
    }
}
