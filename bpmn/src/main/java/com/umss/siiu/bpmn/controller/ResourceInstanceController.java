/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.controller;

import com.umss.siiu.bpmn.dto.ResourceInstanceDto;
import com.umss.siiu.bpmn.model.processes.ResourceInstance;
import com.umss.siiu.bpmn.service.ResourceInstanceService;
import com.umss.siiu.core.controller.GenericController;
import com.umss.siiu.core.service.GenericService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "api/resourceInstances", produces = MediaType.APPLICATION_JSON_VALUE)
public class ResourceInstanceController extends GenericController<ResourceInstance, ResourceInstanceDto> {
    private ResourceInstanceService service;

    public ResourceInstanceController(ResourceInstanceService service) {
        this.service = service;
    }

    @Override
    protected GenericService<ResourceInstance> getService() {
        return service;
    }
}
