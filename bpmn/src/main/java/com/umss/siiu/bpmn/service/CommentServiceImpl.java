package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.model.processes.Comment;
import com.umss.siiu.bpmn.repository.CommentRepository;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.service.GenericServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends GenericServiceImpl<Comment> implements CommentService {
    private final CommentRepository repository;
    private ProcessService processService;

    public CommentServiceImpl(CommentRepository repository, ProcessService processService) {
        this.repository = repository;
        this.processService = processService;
    }

    @Override
    protected GenericRepository<Comment> getRepository() {
        return repository;
    }

    @Override
    public Comment save(String author, String commentAuthor, Long rating, Long idProcess) {
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setComment(commentAuthor);
        comment.setRating(rating);
        comment.setProcess(processService.findById(idProcess));
        return repository.save(comment);
    }
}
