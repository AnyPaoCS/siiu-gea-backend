package com.umss.siiu.bpmn.controller;

import com.umss.siiu.bpmn.dto.CommentDto;
import com.umss.siiu.bpmn.dto.OperationResultDto;
import com.umss.siiu.bpmn.model.processes.Comment;
import com.umss.siiu.bpmn.service.CommentService;
import com.umss.siiu.core.controller.GenericController;
import com.umss.siiu.core.service.GenericService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/comments")
public class CommentController extends GenericController<Comment, CommentDto> {
    private CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @Override
    protected GenericService<Comment> getService() {
        return service;
    }

    @PostMapping("/process/{idProcess}")
    public ResponseEntity<Object> addCommentToProcess(@RequestBody CommentDto comment, @PathVariable Long idProcess) {
        ResponseEntity<Object> responseEntity = null;
        try {
            Comment res = service.save(comment.getAuthor(), comment.getComment(), comment.getRating(), idProcess);
            responseEntity = new ResponseEntity<>(toDto(res), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity<>(new OperationResultDto<>("invalid.process"),
                    HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }
}