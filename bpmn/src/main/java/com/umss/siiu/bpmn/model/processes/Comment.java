package com.umss.siiu.bpmn.model.processes;

import com.umss.siiu.bpmn.dto.CommentDto;
import com.umss.siiu.core.model.ModelBase;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Comment extends ModelBase<CommentDto> {

    private String author;

    private String statement;

    private Long rating;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_COMMENT_PROCESS"), nullable = false)
    @NotNull
    private Process process;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }
}