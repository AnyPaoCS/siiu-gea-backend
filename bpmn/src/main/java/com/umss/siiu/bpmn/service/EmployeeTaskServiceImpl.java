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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
            tasks.add(taskService.findByCode(TaskType.REQUEST_PROCESS.getCode()));
            tasks.add(taskService.findByCode(TaskType.VALIDATION_PAYMENT.getCode()));
        }catch (Exception e) {
            return tasks;
        }
        return tasks;
    }
}
