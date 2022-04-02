package com.umss.siiu.bpmn.model.processes;


import com.umss.siiu.bpmn.dto.ResourceDocumentDto;
import com.umss.siiu.core.model.ModelBase;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class ResourceDocument extends ModelBase<ResourceDocumentDto> {
    private String name; // Si resourceType es DOCUMENT necesita name
    private String code;

    @OneToOne(mappedBy = "document")
    private Resource resource;

    public ResourceDocument(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public ResourceDocument() {

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
}
