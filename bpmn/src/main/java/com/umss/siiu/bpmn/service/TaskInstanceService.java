/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.model.processes.Process;
import com.umss.siiu.bpmn.model.processes.*;
import com.umss.siiu.core.exceptions.MyException;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.service.GenericService;

import java.util.List;
import java.util.Optional;

public interface TaskInstanceService extends GenericService<TaskInstance> {

    boolean isUserAssigneed(Long id, String username);

    TaskInstance findByJobIdAndEmployeeId(Long jobId, Long employeeId);

    TaskInstance findByCodeAndJobId(String taskCode, Long jobId);

    void deAllocateResources(TaskInstance taskInstance);

    List<TaskInstance> findByCodeNotAndJobIdAndTaskStatusNot(String taskCode, Long jobId, String taskStatus);

    TaskInstance reassignResources(long taskInstanceId, long employeeId, String observation) throws MyException;

    TaskInstance createTaskInstance(Process defaultJobProcess, ProcessInstance processInstance);

    ResourceInstance allocateResource(TaskInstance taskInstance, Resource resource,
            ResourceInstance resourceInstance,
            Employee employee);

    void validateChangeStatus(TaskStatus currentTaskStatus, TaskStatus nextTaskStatus);

    void complete(TaskInstance taskInstance, List<String> actionNames);

    List<TaskInstance> findNotAllocatedTasks(TaskStatus taskStatus,
            boolean manual);

    List<TaskInstance> findByProcessInstance(ProcessInstance processInstance);

    Optional<Employee> getEmployeeOf(Long id);

    List<TaskInstance> findByProcessInstanceId(Long processInstanceId);
}
