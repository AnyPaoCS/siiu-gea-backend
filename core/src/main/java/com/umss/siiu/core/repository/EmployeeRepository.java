/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.repository;


import com.umss.siiu.core.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByFirstName(String name);// select * from employee;
}
