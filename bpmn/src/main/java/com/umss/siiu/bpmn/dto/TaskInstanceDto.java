/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.dto;

import com.umss.siiu.bpmn.model.processes.*;
import com.umss.siiu.core.dto.DtoBase;
import com.umss.siiu.core.dto.EmployeeDto;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
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
    private String observation;
    private String relatedAreaCode;
    private List<ResourceDocumentDto> resourceDocuments;

    private List<ObservationDto> observations;

    private Long userProcessId;
    private String processCode;
    private long processInstanceId;

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

    public List<ResourceDocumentDto> getResourceDocuments() {
        return resourceDocuments;
    }

    public void setResourceDocuments(List<ResourceDocumentDto> resourceDocuments) {
        this.resourceDocuments = resourceDocuments;
    }

    public List<ObservationDto> getObservations() {
        return observations;
    }

    public void setObservations(List<ObservationDto> observations) {
        this.observations = observations;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Long getUserProcessId() {
        return userProcessId;
    }

    public void setUserProcessId(Long userProcessId) {
        this.userProcessId = userProcessId;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Override
    protected void afterConversion(TaskInstance taskInstance, ModelMapper mapper) {
        setName(taskInstance.getTask().getName());
        setCode(taskInstance.getTask().getCode());
        setComplete(taskInstance.getTaskStatus().equals(TaskStatus.DONE));
        setEmployee(getEmployeeFromResource(taskInstance, mapper));
        setTaskId(taskInstance.getTask().getId());
        setRelatedAreaCode(taskInstance.getTask().getRelatedAreaCode());
        setResourceDocuments(getDocumentsFromResource(taskInstance, mapper));
        setObservations(new ObservationDto().toListDto(taskInstance.getObservations(), mapper));
        setUserProcessId(taskInstance.getProcessInstance().getUser().getId());
        setProcessCode(taskInstance.getProcessInstance().getProcess().getCode());
        setProcessInstanceId(taskInstance.getProcessInstance().getId());
    }

    private Optional<EmployeeDto> getEmployeeFromResource(TaskInstance taskInstance, ModelMapper mapper) {
        List<ResourceInstance> resources = taskInstance.getResourceInstances().stream()
                .filter(resource -> !resource.getEmployee().getFirstName().equalsIgnoreCase("System"))
                .collect(Collectors.toList());
        if (resources.iterator().hasNext()) {
            return Optional.of( new EmployeeDto()
                    .toDto(resources.iterator().next().getEmployee(), mapper));
        }
        return Optional.ofNullable(null);
    }

    private List<ResourceDocumentDto> getDocumentsFromResource(TaskInstance taskInstance, ModelMapper mapper) {
       List<ResourceDocumentDto> documents = new ArrayList<>();
       List<ResourceInstance> resources = taskInstance.getResourceInstances().stream()
                .filter(resource -> resource.getResource().getResourceType().equals(ResourceType.DOCUMENT))
                .collect(Collectors.toList());
       for (ResourceInstance resource : resources) {
           documents.add(new ResourceDocumentDto().toDto(resource.getResource().getDocument(), mapper));
       }
       return documents;
    }

}
