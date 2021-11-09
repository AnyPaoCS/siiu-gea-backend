/**
 * @author: Edson A. Terceros T.
 */

package com.siiu.umss.siiu.repository;


import com.siiu.umss.siiu.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByName(String name);// select * from employee;
}
