/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.model.processes;

import com.umss.siiu.bpmn.dto.ResourceDto;
import com.umss.siiu.core.model.ModelBase;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Resource extends ModelBase<ResourceDto> {
    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    private boolean isOutput = false;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_RESOURCE_TASK"), nullable = false)
    @NotNull
    private Task task;

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public boolean isOutput() {
        return isOutput;
    }

    public void setOutput(boolean output) {
        isOutput = output;
    }
}
