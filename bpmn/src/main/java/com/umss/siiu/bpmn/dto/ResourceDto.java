package com.umss.siiu.bpmn.dto;

import com.umss.siiu.bpmn.model.processes.Resource;
import com.umss.siiu.core.dto.DtoBase;
import org.modelmapper.ModelMapper;

public class ResourceDto extends DtoBase<Resource> {

    private String resourceType;

    private ResourceDocumentDto document;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public ResourceDocumentDto getDocument() {
        return document;
    }

    public void setDocument(ResourceDocumentDto document) {
        this.document = document;
    }

    @Override
    protected void afterConversion(Resource element,   ModelMapper mapper) {
        if (element.getDocument() != null) {
            setDocument(new ResourceDocumentDto().toDto(element.getDocument(), mapper));
        }
    }

}
