/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.dto.ProcessInstanceDto;
import com.umss.siiu.bpmn.model.processes.Process;
import com.umss.siiu.bpmn.model.processes.ProcessInstance;
import com.umss.siiu.bpmn.model.processes.TaskInstance;
import com.umss.siiu.bpmn.model.processes.TaskStatus;
import com.umss.siiu.bpmn.repository.ProcessInstanceRepository;
import com.umss.siiu.core.dto.DtoBase;
import com.umss.siiu.core.model.User;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.service.GenericServiceImpl;
import com.umss.siiu.core.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessInstanceServiceImpl extends GenericServiceImpl<ProcessInstance> implements ProcessInstanceService {

    private ProcessInstanceRepository repository;

    private ProcessService processService;
    private TaskInstanceService taskInstanceService;
    private UserService userService;

    private Process defaultJobProcess;

    @Value("${scheduler.allocation.process}")
    private String defaultProcess;

    public ProcessInstanceServiceImpl(ProcessInstanceRepository repository, ProcessService processService,
            TaskInstanceService taskInstanceService, UserService userService) {
        this.repository = repository;
        this.processService = processService;
        this.taskInstanceService = taskInstanceService;
        this.userService = userService;
    }

    @Override
    public ProcessInstance createProcessInstance() {
        var processInstance = new ProcessInstance();
        processInstance.setProcess(getDefaultJobProcess());
        processInstance.setUser(getDefaultUserSolicitante());
        processInstance = save(processInstance);

        var taskInstance = taskInstanceService.createTaskInstance(getDefaultJobProcess(), processInstance);
        List<TaskInstance> taskInstances = new ArrayList<>();
        taskInstances.add(taskInstance);
        processInstance.setTaskInstances(taskInstances);
        return processInstance;

    }

    @Override
    public ProcessInstance createProcessInstance(Process process, User user) {
        var processInstance = new ProcessInstance();
        processInstance.setProcess(process);
        processInstance.setUser(user);
        processInstance = save(processInstance);

        var taskInstance = taskInstanceService.createTaskInstance(process, processInstance);
        List<TaskInstance> taskInstances = new ArrayList<>();
        taskInstances.add(taskInstance);
        processInstance.setTaskInstances(taskInstances);
        processInstance = save(processInstance);
        return processInstance;
    }

    private Process getDefaultJobProcess() {
        if (null == defaultJobProcess) {
            defaultJobProcess = processService.findByCode(defaultProcess);
        }
        return defaultJobProcess;
    }

    private User getDefaultUserSolicitante() {
        return userService.findByEmail("csorialopez11+1@gmail.com");
    }

    @Override
    protected GenericRepository<ProcessInstance> getRepository() {
        return repository;
    }

    @Override
    public ProcessInstance findById(Long id) {
        var processInstance = super.findById(id);
        processInstance.setTaskInstances(taskInstanceService.findByProcessInstance(processInstance));
        return processInstance;
    }

    @Override
    public ProcessInstance findByJobId(Long id) {
        var processInstance = repository.findByJobBpmJobId(id);
        processInstance.setTaskInstances(taskInstanceService.findByProcessInstance(processInstance));
        return processInstance;
    }

    @Override
    public ProcessInstance patch(DtoBase dto, ProcessInstance model) {
        if (((ProcessInstanceDto) dto).isManual() && !model.isManual()) {
            setManual(model);
        }
        return super.patch(dto, model);
    }

    public void setManual(ProcessInstance processInstance) {
        List<TaskInstance> taskInstances = taskInstanceService.findByProcessInstance(processInstance);
        if (!CollectionUtils.isEmpty(taskInstances)) {
            taskInstances.forEach(taskInstance -> {
                if (taskInstance.getTaskStatus().equals(TaskStatus.ALLOCATED) ||
                        taskInstance.getTaskStatus().equals(TaskStatus.IN_PROGRESS) ||
                        taskInstance.getTaskStatus().equals(TaskStatus.ON_HOLD)) {
                    taskInstanceService.deAllocateResources(taskInstance);
                }
            });
        }
    }

    @Override
    public List<ProcessInstance> findByUserEmail(String email) {
        return repository.findByUserEmail(email);
    }
}
