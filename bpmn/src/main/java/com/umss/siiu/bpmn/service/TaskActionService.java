/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.model.processes.Task;
import com.umss.siiu.bpmn.model.processes.TaskAction;
import com.umss.siiu.core.service.GenericService;

import java.util.List;

public interface TaskActionService extends GenericService<TaskAction> {
    List<TaskAction> findByNextTask(Task nexTask);

    List<TaskAction> findByTask(Task task);
}
