/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.dto;

import com.umss.siiu.bpmn.model.processes.ResourceInstance;
import com.umss.siiu.bpmn.model.processes.TaskInstance;
import com.umss.siiu.bpmn.model.processes.TaskStatus;
import com.umss.siiu.core.dto.DtoBase;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskInstanceDto extends DtoBase<TaskInstance> {

    private TaskStatus taskStatus;
    private List<String> actionNames;
    private Long employeeId;
    private Optional<EmployeeDto> employee;
    private String name;
    private String code;
    private Boolean isComplete;
    private long taskId;
    private String relatedAreaCode;

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public List<String> getActionNames() {
        return actionNames;
    }

    public void setActionNames(List<String> actionNames) {
        this.actionNames = actionNames;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean isComplete() {
        return isComplete;
    }

    public void setComplete(Boolean isComplete) {
        this.isComplete = isComplete;
    }

    public Optional<EmployeeDto> getEmployee() {
        return employee;
    }

    public void setEmployee(Optional<EmployeeDto> employee) {
        this.employee = employee;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getRelatedAreaCode() {
        return relatedAreaCode;
    }

    public void setRelatedAreaCode(String relatedAreaCode) {
        this.relatedAreaCode = relatedAreaCode;
    }

    @Override
    protected void afterConversion(TaskInstance taskInstance, ModelMapper mapper) {
        setName(taskInstance.getTask().getName());
        setCode(taskInstance.getTask().getCode());
        setComplete(taskInstance.getTaskStatus().equals(TaskStatus.DONE));
        setEmployee(getEmployeeFromResource(taskInstance, mapper));
        setTaskId(taskInstance.getTask().getId());
        setRelatedAreaCode(taskInstance.getTask().getRelatedAreaCode());
    }

    private Optional<EmployeeDto> getEmployeeFromResource(TaskInstance taskInstance, ModelMapper mapper) {
        List<ResourceInstance> resources = taskInstance.getResourceInstances().stream()
                .filter(resource -> !resource.getEmployee().getFirstName().equalsIgnoreCase("System"))
                .collect(Collectors.toList());
        if (resources.iterator().hasNext()) {
            return Optional.of((EmployeeDto) new EmployeeDto()
                    .toDto(resources.iterator().next().getEmployee(), mapper));
        }
        return Optional.ofNullable(null);
    }

}
