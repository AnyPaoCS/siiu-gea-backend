/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.service;

import java.util.List;

import com.umss.siiu.bpmn.model.processes.Process;
import com.umss.siiu.bpmn.model.processes.ProcessInstance;
import com.umss.siiu.core.model.User;
import com.umss.siiu.core.service.GenericService;

public interface ProcessInstanceService extends GenericService<ProcessInstance> {
    ProcessInstance createProcessInstance();
    ProcessInstance createProcessInstance(Process process, User user);

    ProcessInstance findByJobId(Long id);

    List<ProcessInstance> findByUserId(Long id);
}
