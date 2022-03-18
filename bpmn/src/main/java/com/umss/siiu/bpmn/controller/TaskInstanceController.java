/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.controller;

import com.umss.siiu.bpmn.dto.OperationResultDto;
import com.umss.siiu.bpmn.dto.TaskInstanceDto;
import com.umss.siiu.bpmn.model.processes.TaskInstance;
import com.umss.siiu.bpmn.schedulers.JobScheduler;
import com.umss.siiu.bpmn.service.JobBpmService;
import com.umss.siiu.bpmn.service.JobService;
import com.umss.siiu.bpmn.service.TaskInstanceService;
import com.umss.siiu.core.controller.GenericController;
import com.umss.siiu.core.service.GenericService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/taskInstances")
public class TaskInstanceController extends GenericController<TaskInstance, TaskInstanceDto> {

    private TaskInstanceService service;
    private JobBpmService jobBpmService;
    private JobService jobService;

    public TaskInstanceController(TaskInstanceService service, JobBpmService jobBpmService, JobService jobService,
            JobScheduler jobScheduler) {
        this.service = service;
        this.jobBpmService = jobBpmService;
        this.jobService = jobService;
    }

    @GetMapping("/{name}/jobs/{jobId}")
    public TaskInstanceDto findByNameAndJobId(@PathVariable("name") String name, @PathVariable("jobId") Long jobId) {
        return toDto(service.findByCodeAndJobId(name, jobId));
    }

    @GetMapping("/jobs/{jobId}")
    public List<TaskInstanceDto> findByJob(@PathVariable("jobId") Long jobId) {
        return toDto(service.findByProcessInstance(jobService.findById(jobId).getJobBpm().getProcessInstance()));
    }

    @GetMapping("/processInstance/{processInstanceId}")
    public List<TaskInstanceDto> findByProcessInstance(@PathVariable("processInstanceId") Long processInstanceId) {
        return toDto(service.findByProcessInstanceId(processInstanceId));
    }

    @GetMapping("/jobs/{jobId}/employees/{employeeId}")
    public TaskInstanceDto findByJobAndEmployee(@PathVariable("jobId") Long jobId,
            @PathVariable("employeeId") Long employeeId) {
        return toDto(service.findByJobIdAndEmployeeId(jobId, employeeId));
    }

    @GetMapping("/user/{username:.+}/id/{id}")
    public Boolean isUserAssigneed(@PathVariable(ID) Long id, @PathVariable("username") String username) {
        return service.isUserAssigneed(id, username);
    }

    @PutMapping("/changeStatus")
    public TaskInstanceDto changeStatus(@RequestBody TaskInstanceDto taskInstanceDto,
            @RequestParam(value = "exceptionTrigger", required = false) boolean exceptionTrigger) {
        return toDto(jobBpmService.changeStatus(taskInstanceDto, exceptionTrigger));
    }

    @PutMapping("/reassignResources")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OperationResultDto<TaskInstanceDto> reassignResources(@RequestBody TaskInstanceDto taskInstanceDto) {
        return new OperationResultDto<>("messages.taskInstance.userReassigned",
                toDto(service.reassignResources(taskInstanceDto.getId(), taskInstanceDto.getEmployeeId())));

    }

    @Override
    protected GenericService<TaskInstance> getService() {
        return service;
    }
}
