/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.model.processes;

import com.umss.siiu.bpmn.model.EmployeeTask;
import com.umss.siiu.core.dto.DtoBase;
import com.umss.siiu.core.model.ModelBase;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Task extends ModelBase<DtoBase<?>> {
    private String name;
    private String code;
    // this property is used when 2 parallel Tasks need to be worked by the same resource
    private String parallelGroupingCode;
    private String relatedAreaCode;

    @OneToMany(mappedBy = "task")
    private Set<EmployeeTask> employeeTasks;

    @Enumerated(EnumType.STRING)
    private EntryLogicGatePolicyType entryLogicGatePolicyType = EntryLogicGatePolicyType.OR;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task", fetch = FetchType.EAGER)
    private Set<Resource> resourceList = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task", fetch = FetchType.LAZY)
    private Set<TaskAction> taskActions = new HashSet<>();

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

    public Set<Resource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(Set<Resource> resourceList) {
        this.resourceList = resourceList;
    }

    public Set<TaskAction> getTaskActions() {
        return taskActions;
    }

    public void setTaskActions(Set<TaskAction> taskActions) {
        this.taskActions = taskActions;
    }

    public EntryLogicGatePolicyType getEntryLogicGatePolicyType() {
        return entryLogicGatePolicyType;
    }

    public void setEntryLogicGatePolicyType(EntryLogicGatePolicyType entryLogicGatePolicyType) {
        this.entryLogicGatePolicyType = entryLogicGatePolicyType;
    }

    public String getParallelGroupingCode() {
        return parallelGroupingCode;
    }

    public void setParallelGroupingCode(String parallelGroupingCode) {
        this.parallelGroupingCode = parallelGroupingCode;
    }

    public String getRelatedAreaCode() {
        return relatedAreaCode;
    }

    public void setRelatedAreaCode(String relatedAreaCode) {
        this.relatedAreaCode = relatedAreaCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) &&
                Objects.equals(code, task.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }
}
