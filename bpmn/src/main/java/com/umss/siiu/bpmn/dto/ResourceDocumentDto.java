package com.umss.siiu.bpmn.dto;


import com.umss.siiu.bpmn.model.processes.ActionFlowType;
import com.umss.siiu.bpmn.model.processes.ResourceDocument;
import com.umss.siiu.bpmn.model.processes.ResourceType;
import com.umss.siiu.bpmn.model.processes.Task;
import com.umss.siiu.core.dto.DtoBase;
import org.modelmapper.ModelMapper;

import java.util.*;

public class  ResourceDocumentDto extends DtoBase<ResourceDocument> {
    private String name;
    private String code;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceDocumentDto that = (ResourceDocumentDto) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        int result = 15;
        result = 31 * result + Objects.hashCode(this.getId());
        return result;
    }

    public Set<ResourceDocumentDto> toDocumentListDto(Task task, ModelMapper mapper){
        Set<ResourceDocumentDto> resourceList = new HashSet<>();
        getResourceDocument(resourceList, task, mapper);
        return resourceList;
    }

    private void getResourceDocument(Set<ResourceDocumentDto> list, Task task, ModelMapper mapper) {
        task.getTaskActions().forEach(taskAction -> {
            taskAction.getTask().getResourceList().forEach(resource -> {
                if (resource.getResourceType() == ResourceType.DOCUMENT && !resource.isOutput()) {
                    list.add(new ResourceDocumentDto().toDto(resource.getDocument(), mapper));
                }
            });
            if (taskAction.getNextTask().getCode() != task.getCode() && taskAction.getActionFlowType() != ActionFlowType.FORCE_GATE_ENTRY ) {
                getResourceDocument(list, taskAction.getNextTask(), mapper);
            }
        });
    }

}
