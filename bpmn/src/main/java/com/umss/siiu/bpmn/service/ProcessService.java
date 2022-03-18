/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.model.processes.Process;
import com.umss.siiu.core.service.GenericService;

public interface ProcessService extends GenericService<Process> {
    Process findByName(String name);
}
