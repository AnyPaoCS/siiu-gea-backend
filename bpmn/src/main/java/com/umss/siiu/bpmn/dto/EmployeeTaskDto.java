package com.umss.siiu.bpmn.dto;

import com.umss.siiu.bpmn.model.EmployeeTask;
import com.umss.siiu.core.dto.DtoBase;
import com.umss.siiu.core.dto.EmployeeDto;

import org.modelmapper.ModelMapper;

public class EmployeeTaskDto extends DtoBase<EmployeeTask> {

    private EmployeeDto employee;
    private TaskDto task;

    public EmployeeTaskDto() {
        super();
    }

    public EmployeeDto getEmployee() {
      return employee;
  }

  public void setEmployee(EmployeeDto employee) {
      this.employee = employee;
  }

  public TaskDto getTask() {
      return task;
  }

  public void setTask(TaskDto task) {
      this.task = task;
  }

    @Override
    public EmployeeTaskDto toDto(EmployeeTask element, ModelMapper mapper) {
        super.toDto(element, mapper);
        setEmployee((EmployeeDto) new EmployeeDto().toDto(element.getEmployee(), mapper));
        setTask(new TaskDto().toDto(element.getTask(), mapper));
        return this;
    }
}