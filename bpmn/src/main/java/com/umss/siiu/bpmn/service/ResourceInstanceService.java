/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.model.processes.ResourceInstance;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.service.GenericService;

import java.util.List;

public interface ResourceInstanceService extends GenericService<ResourceInstance> {
    List<ResourceInstance> findByEmployeeAndActive(Employee employee);

    List<ResourceInstance> findByTaskInstanceId(Long id);
}
