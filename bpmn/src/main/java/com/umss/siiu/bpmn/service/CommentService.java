package com.umss.siiu.bpmn.service;


import com.umss.siiu.bpmn.model.processes.Comment;
import com.umss.siiu.core.service.GenericService;

public interface CommentService extends GenericService<Comment> {

    public Comment save( String author, String commentAuthor, Long rating, Long idProcess);
}
