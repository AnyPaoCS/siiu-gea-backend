package com.umss.siiu.bpmn.dto;

import com.umss.siiu.core.dto.DtoBase;
import com.umss.siiu.core.model.Employee;
import org.modelmapper.ModelMapper;

import java.util.List;

public class EmployeeDto extends DtoBase<Employee> {

    private static final boolean BY_LASTNAME_FIRST = false;

    private String firstName;
    private String lastName;
    private String fullName;
    private List<TaskDto> tasks;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<TaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDto> tasks) {
        this.tasks = tasks;
    }

    @Override
    protected void afterConversion(Employee employee, ModelMapper mapper) {
        setFirstName(employee.getFirstName());
        setLastName(employee.getLastName());
        setFullName(employee.getFullName(BY_LASTNAME_FIRST));
    }
}
