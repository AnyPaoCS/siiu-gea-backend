/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.repository;


import com.umss.siiu.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByFirstName(String name);// select * from employee;
}
