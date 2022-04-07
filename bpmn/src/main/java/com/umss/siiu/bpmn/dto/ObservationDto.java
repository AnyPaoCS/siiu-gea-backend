package com.umss.siiu.bpmn.dto;

import com.umss.siiu.bpmn.model.processes.Observation;
import com.umss.siiu.core.dto.DtoBase;

public class ObservationDto extends DtoBase<Observation> {

    private String observation;

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

}
