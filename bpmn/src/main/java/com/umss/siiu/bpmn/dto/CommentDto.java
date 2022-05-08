package com.umss.siiu.bpmn.dto;


import com.umss.siiu.bpmn.model.processes.Comment;
import com.umss.siiu.core.dto.DtoBase;

public class CommentDto extends DtoBase<Comment> {

    private String author;

    private String statement;

    private Long rating;

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

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }
}