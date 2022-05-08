package com.umss.siiu.bpmn.dto;

import com.umss.siiu.bpmn.model.processes.Observation;
import com.umss.siiu.core.dto.DtoBase;

public class ObservationDto extends DtoBase<Observation> {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
