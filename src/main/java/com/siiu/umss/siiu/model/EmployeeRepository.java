/**
 * @author: Edson A. Terceros T.
 */

package com.siiu.umss.siiu.model;


import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByName(String name);// select * from employee;
}
