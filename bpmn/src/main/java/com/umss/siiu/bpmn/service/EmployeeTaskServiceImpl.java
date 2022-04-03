/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.model.EmployeeTask;
import com.umss.siiu.bpmn.model.processes.Task;
import com.umss.siiu.bpmn.model.processes.TaskType;
import com.umss.siiu.bpmn.repository.EmployeeTaskRepository;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.service.GenericServiceImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeTaskServiceImpl extends GenericServiceImpl<EmployeeTask> implements EmployeeTaskService {

    private final String TYPE_APPLICANT = "APPLICANT";

    private TaskService taskService;

    private EmployeeTaskRepository repository;

    public EmployeeTaskServiceImpl(EmployeeTaskRepository repository, TaskService taskService) {
        this.repository = repository;
        this.taskService = taskService;
    }

    @Override
    protected GenericRepository<EmployeeTask> getRepository() {
        return repository;
    }

    @Override
    public Set<EmployeeTask> findByEmployee(Employee employee) {
        return repository.findByEmployee(employee);
    }

    @Override
    public void setEmployeeTaskForUser(Employee employee, String typeUser) {
        // Solo asignar estas tareas al usuario solicitante
        if (typeUser.equals(TYPE_APPLICANT)) {
            for (Task task : getUserTask()) {
                EmployeeTask employeeTask = new EmployeeTask();
                employeeTask.setTask(task);
                employeeTask.setEmployee(employee);
                save(employeeTask);
            }
        }
    }

    private List<Task> getUserTask (){
        List<Task> tasks = new ArrayList<>();
        try {
            tasks.addAll(taskService.findAllTaskWithCode(TaskType.REQUEST_PROCESS.getCode()));
            tasks.addAll(taskService.findAllTaskWithCode(TaskType.VALIDATION_PAYMENT.getCode()));
        }catch (Exception e) {
            return tasks;
        }
        return tasks;
    }

    public void saveAllTask(EmployeeTask model) {
        List<Task> taskWithTaskCode = taskService.findAllTaskWithCode(model.getTask().getCode());
        if (!taskWithTaskCode.isEmpty()) {
            for (Task taskR : taskWithTaskCode) {
                EmployeeTask employeeTask = new EmployeeTask();
                employeeTask.setTask(taskR);
                employeeTask.setEmployee(model.getEmployee());
                repository.save(employeeTask);
            }
        }
    }

    @Transactional
    @Override
    public void deleteByCode(EmployeeTask employeeTask) {
        List<Long> taskWithTaskCode = taskService.findAllTaskWithCode(employeeTask.getTask().getCode()).stream().map(Task::getId).collect(Collectors.toList());
        if (!taskWithTaskCode.isEmpty()) {
           repository.deleteTaskEmployeeIdAndListTaskId(employeeTask.getEmployee().getId(), taskWithTaskCode);
        }
    }
}
