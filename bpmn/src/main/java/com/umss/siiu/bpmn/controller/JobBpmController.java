/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.controller;

import java.util.List;

import com.umss.siiu.bpmn.dto.JobBpmDto;
import com.umss.siiu.bpmn.dto.OperationResultDto;
import com.umss.siiu.bpmn.model.JobBpm;
import com.umss.siiu.bpmn.service.JobBpmService;
import com.umss.siiu.bpmn.service.ProcessInstanceService;
import com.umss.siiu.bpmn.service.ProcessService;
import com.umss.siiu.core.controller.GenericController;
import com.umss.siiu.core.dto.UserDto;
import com.umss.siiu.core.service.GenericService;
import com.umss.siiu.core.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/jobBpms", produces = MediaType.APPLICATION_JSON_VALUE)
public class JobBpmController extends GenericController<JobBpm, JobBpmDto> {
    private JobBpmService service;
    private UserService userService;
    private ProcessService processService;
    private ProcessInstanceService processInstanceService;

    public JobBpmController(JobBpmService service, UserService userService, ProcessService processService,
            ProcessInstanceService processInstanceService) {
        this.service = service;
        this.userService = userService;
        this.processService = processService;
        this.processInstanceService = processInstanceService;
    }

    @PostMapping(value = "/byUser")
    public List<JobBpmDto> findProcessByJobId(@RequestBody UserDto userDto) {
        return new JobBpmDto().toListDto(service.findByUserEmail(userDto.getEmail()), modelMapper);
    }

    @PostMapping(value = "/byAssignedEmployee")
    public List<JobBpmDto> findProcessByAssignedTasks(@RequestBody UserDto userDto) {
        return new JobBpmDto().toListDto(service.findByTaskAssigned(userDto.getEmail()), modelMapper);
    }

    @PostMapping("/createProcessInstances/{idProcess}")
    public ResponseEntity<Object> createProcessInstanceByUser(@PathVariable String idProcess,
            @RequestBody UserDto userDto) {
        ResponseEntity<Object> responseEntity = null;
        try {
            var user = userService.findByEmail(userDto.getEmail());
            var processUser = processService.findById(Long.parseLong(idProcess));
            var instance = processInstanceService.createProcessInstance(processUser, user);
            var processCreated = service.createJobBpm(instance);
            service.allocateResources();
            responseEntity = new ResponseEntity<>(toDto(processCreated),
                    HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity<>(new OperationResultDto<>("invalid.process"),
                    HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    @Override
    protected GenericService<JobBpm> getService() {
        return service;
    }
}
