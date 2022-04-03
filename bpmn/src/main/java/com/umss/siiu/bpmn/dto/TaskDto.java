package com.umss.siiu.bpmn.dto;

import com.umss.siiu.bpmn.model.processes.Task;
import com.umss.siiu.core.dto.DtoBase;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Set;

public class TaskDto extends DtoBase<Task> {

    private String name;
    private String code;
    private Set<ResourceDocumentDto> listResourceDocuments;

    public TaskDto() {
        super();
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

    public Set<ResourceDocumentDto> getListResourceDocuments() {
        return listResourceDocuments;
    }

    public void setListResourceDocuments(Set<ResourceDocumentDto> listResourceDocuments) {
        this.listResourceDocuments = listResourceDocuments;
    }

    @Override
    public TaskDto toDto(Task element, ModelMapper mapper) {
        super.toDto(element, mapper);
        setListResourceDocuments(new ResourceDocumentDto().toDocumentListByTask(element, mapper));
        return this;
    }
}
