package com.umss.siiu.bpmn.controller;

import com.umss.siiu.bpmn.dto.ProcessDto;
import com.umss.siiu.bpmn.model.processes.Process;
import com.umss.siiu.bpmn.service.ProcessService;
import com.umss.siiu.core.controller.GenericController;
import com.umss.siiu.core.service.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/processes")
public class ProcessController extends GenericController<Process, ProcessDto> {
    private ProcessService service;

    public ProcessController(ProcessService service) {
        this.service = service;
    }

    @Override
    protected GenericService getService() {
        return service;
    }
}
