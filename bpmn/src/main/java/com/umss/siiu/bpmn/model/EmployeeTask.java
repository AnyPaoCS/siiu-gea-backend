package com.umss.siiu.bpmn.model;

import com.umss.siiu.bpmn.model.processes.Task;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.model.ModelBase;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class EmployeeTask extends ModelBase {
    @ManyToOne
    private Employee employee;
    @ManyToOne
    private Task task;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
