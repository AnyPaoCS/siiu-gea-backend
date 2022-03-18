/*
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn;

import com.umss.siiu.bpmn.dto.TaskInstanceDto;
import com.umss.siiu.bpmn.model.Job;
import com.umss.siiu.bpmn.model.JobBpm;
import com.umss.siiu.bpmn.model.JobStatus;
import com.umss.siiu.bpmn.model.processes.ResourceInstance;
import com.umss.siiu.bpmn.model.processes.TaskInstance;
import com.umss.siiu.bpmn.model.processes.TaskStatus;
import com.umss.siiu.bpmn.schedulers.JobScheduler;
import com.umss.siiu.bpmn.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class ProcessCycleTest extends AbstractTestNGSpringContextTests {
    @Autowired
    ProcessInstanceService processInstanceService;
    @Autowired
    JobService jobService;
    @Autowired
    JobBpmService jobBpmService;
    @Autowired
    TaskInstanceService taskInstanceService;
    @Autowired
    ResourceInstanceService resourceInstanceService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    JobScheduler jobScheduler;

    @Test
    public void testCycle() throws InterruptedException {
        Job job = new Job();
        jobService.save(job);
        ArrayList<Job> jobs = new ArrayList<>();
        jobs.add(job);
        List<JobBpm> jobBpms = jobBpmService.createJobBpms(jobs);
        JobBpm jobBpm = jobBpms.get(0);

        // complete system bootstrap task
        jobBpm.setStatus(JobStatus.PREPARED.toString());
        jobBpmService.save(jobBpm);
        jobScheduler.execAllocation();// complete bootstrap task and open 5 parallel tasks

        jobScheduler.execAllocation();// allocate some of the 5 tasks

        List<TaskInstance> allocatedTaskInstances;
        allocatedTaskInstances =
                taskInstanceService.findByProcessInstance(job.getJobBpm().getProcessInstance());

        while (allocatedTaskInstances.size() > 0) {
            allocatedTaskInstances =
                    allocatedTaskInstances.stream().filter(taskInstance -> taskInstance.getTaskStatus().equals(TaskStatus.ALLOCATED)).collect(Collectors.toList());
            allocatedTaskInstances.forEach(taskInstance -> {
                TaskInstanceDto taskInstanceDtoElement = new TaskInstanceDto().toDto(taskInstance, modelMapper);
                buildTaskInstanceDtoCompleteRequest(taskInstanceDtoElement);
                jobBpmService.changeStatus(taskInstanceDtoElement, false);
            });
            jobScheduler.execAllocation();
            allocatedTaskInstances = taskInstanceService.findByProcessInstance(job.getJobBpm().getProcessInstance());
            allocatedTaskInstances =
                    allocatedTaskInstances.stream().filter(taskInstance -> taskInstance.getTaskStatus().equals(TaskStatus.ALLOCATED)).collect(Collectors.toList());
        }

        //cleanup
        List<TaskInstance> taskInstanceList =
                taskInstanceService.findByProcessInstance(job.getJobBpm().getProcessInstance());
        taskInstanceList.forEach(taskInstance -> {
            List<ResourceInstance> resourceInstances = taskInstance.getResourceInstances();
            resourceInstances.forEach(resourceInstance -> resourceInstanceService.deleteById(resourceInstance.getId()));
            taskInstanceService.deleteById(taskInstance.getId());
        });
        processInstanceService.deleteById(jobBpm.getProcessInstance().getId());
        jobService.deleteById(job.getId());
        jobBpmService.deleteById(job.getJobBpm().getId());

    }

    private void buildTaskInstanceDtoCompleteRequest(TaskInstanceDto taskInstanceDto) {
        ArrayList<String> actionNames = new ArrayList<>();
        actionNames.add("DONE");
        taskInstanceDto.setActionNames(actionNames);
        taskInstanceDto.setTaskStatus(TaskStatus.DONE);
    }
}
