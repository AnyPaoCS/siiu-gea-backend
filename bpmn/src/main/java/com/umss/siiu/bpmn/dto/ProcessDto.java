package com.umss.siiu.bpmn.dto;

import com.umss.siiu.bpmn.model.processes.Process;
import com.umss.siiu.core.dto.DtoBase;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Set;

public class ProcessDto extends DtoBase<Process> {
    private String code;
    private String name; //nombre tramite
    private TaskDto task; //tarea inicial
    private List<NodeDto> listNodes;
    private Set<ResourceDocumentDto> listResourceDocuments;
    private List<CommentDto> comments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskDto getTask() {
        return task;
    }

    public void setTask(TaskDto task) {
        this.task = task;
    }

    public List<NodeDto> getListNodes() {
        return listNodes;
    }

    public void setListNodes(List<NodeDto> listNodes) {
        this.listNodes = listNodes;
    }

    public Set<ResourceDocumentDto> getListResourceDocuments() {
        return listResourceDocuments;
    }

    public void setListResourceDocuments(Set<ResourceDocumentDto> listResourceDocuments) {
        this.listResourceDocuments = listResourceDocuments;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }


    @Override
    public ProcessDto toDto(Process process, ModelMapper mapper) {
        super.toDto(process, mapper);
        setTask(new TaskDto().toDto(process.getTask(), mapper));
        setListNodes(new NodeDto().toNodeList(process.getTask(), mapper));
        setListResourceDocuments(new ResourceDocumentDto().toDocumentListDto(process.getTask(), mapper));
        setComments(new CommentDto().toListDto(process.getComments(), mapper));
        return this;
    }
}
