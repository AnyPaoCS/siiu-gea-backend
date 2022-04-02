/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.model.processes;

import com.umss.siiu.bpmn.dto.ProcessDto;
import com.umss.siiu.core.model.ModelBase;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "code", name = "UK_PROCESS_CODE"))
public class Process extends ModelBase<ProcessDto> {
    private String code;

    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_PROCESS_TASK"))
    private Task task;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "process", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();


    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

}
