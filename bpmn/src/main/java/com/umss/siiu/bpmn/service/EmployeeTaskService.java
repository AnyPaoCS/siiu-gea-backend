/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.model.EmployeeTask;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.service.GenericService;

import java.util.Set;

public interface EmployeeTaskService extends GenericService<EmployeeTask> {
    Set<EmployeeTask> findByEmployee(Employee employee);
}
