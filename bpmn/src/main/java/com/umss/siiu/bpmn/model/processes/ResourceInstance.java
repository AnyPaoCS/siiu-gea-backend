/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.model.processes;


import com.umss.siiu.bpmn.dto.ResourceInstanceDto;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.model.ModelBase;

import javax.persistence.*;

@SuppressWarnings("rawtypes")
@Entity
@Table(indexes = {@Index(name = "RESOURCEINSTANCE_ACTIVE_INDX", columnList = "active")})
public class ResourceInstance extends ModelBase<ResourceInstanceDto> {

    @Column(length = 1, nullable = false)
    private boolean active = true;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_RESOURCEINSTANCE_TASKINSTANCE"))
    private TaskInstance taskInstance;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_RESOURCEINSTANCE_EMPLOYEE"))
    private Employee employee;

    @OneToOne
    private Resource resource;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public TaskInstance getTaskInstance() {
        return taskInstance;
    }

    public void setTaskInstance(TaskInstance taskInstance) {
        this.taskInstance = taskInstance;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
