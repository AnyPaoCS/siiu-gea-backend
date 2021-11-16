package com.umss.siiu.bpmn.dto;

import com.umss.siiu.bpmn.model.processes.ResourceInstance;
import com.umss.siiu.core.dto.DtoBase;
import org.modelmapper.ModelMapper;

public class ResourceInstanceDto extends DtoBase<ResourceInstance> {
    private Long employeeId;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public void afterConversion(ResourceInstance resourceInstance, ModelMapper mapper) {
        this.setEmployeeId(resourceInstance.getEmployee().getId());
    }
}
