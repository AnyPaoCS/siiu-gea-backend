package com.umss.siiu.bpmn.controller;

import com.umss.siiu.bpmn.dto.ProcessInstanceDto;
import com.umss.siiu.bpmn.model.processes.ProcessInstance;
import com.umss.siiu.bpmn.service.ProcessInstanceService;
import com.umss.siiu.core.controller.GenericController;
import com.umss.siiu.core.service.GenericService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/processes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProcessInstanceController extends GenericController<ProcessInstance, ProcessInstanceDto> {

    private ProcessInstanceService service;

    ProcessInstanceController(ProcessInstanceService service) {
        this.service = service;
    }

    @GetMapping(value = "/job/{id}")
    public ResponseEntity<ProcessInstanceDto> findProcessByJobId(@PathVariable(ID) Long jobId) {
        return new ResponseEntity<>(toDto(service.findByJobId(jobId)), HttpStatus.OK);
    }

    @Override
    protected GenericService<ProcessInstance> getService() {
        return this.service;
    }

}
