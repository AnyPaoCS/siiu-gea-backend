/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.service;

import com.umss.siiu.core.model.Employee;

import java.util.List;

public interface EmployeeService extends GenericService<Employee> {
    List<Employee> findAvailableEmployees();

//    List<Employee> findByTasksId(long taskId);

    Employee findByEmail(String email);

    Employee findSystemEmployee();

    String parseName(String name);
}
