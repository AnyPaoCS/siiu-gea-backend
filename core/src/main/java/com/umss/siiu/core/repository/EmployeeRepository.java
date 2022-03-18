/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.repository;


import com.umss.siiu.core.model.Employee;

import java.util.List;

public interface EmployeeRepository extends GenericRepository<Employee> {
    List<Employee> findByAvailableTrue();

//    List<Employee> findByEmployeeTasksIs(long taskId);

    Employee findByUserEmail(String email);

    Employee findByFirstName(String name);// select * from employee;

    Employee findByUserSystemUserTrue();
}
