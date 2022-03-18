/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.model.processes.ProcessInstance;
import com.umss.siiu.core.service.GenericService;

public interface ProcessInstanceService extends GenericService<ProcessInstance> {
    ProcessInstance createProcessInstance();

    ProcessInstance findByJobId(Long id);
}
