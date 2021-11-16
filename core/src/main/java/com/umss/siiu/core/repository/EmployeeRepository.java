/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.repository;


import com.umss.siiu.core.model.Employee;

public interface EmployeeRepository extends GenericRepository<Employee> {
    Employee findByFirstName(String name);// select * from employee;
}
