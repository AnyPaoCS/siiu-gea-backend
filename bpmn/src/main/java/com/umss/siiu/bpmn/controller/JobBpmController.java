/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.controller;

import com.umss.siiu.bpmn.dto.JobBpmDto;
import com.umss.siiu.bpmn.model.JobBpm;
import com.umss.siiu.bpmn.service.JobBpmService;
import com.umss.siiu.core.controller.GenericController;
import com.umss.siiu.core.service.GenericService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/jobBpms", produces = MediaType.APPLICATION_JSON_VALUE)
public class JobBpmController extends GenericController<JobBpm, JobBpmDto> {
    private JobBpmService service;

    public JobBpmController(JobBpmService service) {
        this.service = service;
    }

    @Override
    protected GenericService<JobBpm> getService() {
        return service;
    }
}
