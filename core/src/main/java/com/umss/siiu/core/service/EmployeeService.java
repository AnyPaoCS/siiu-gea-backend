/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.service;

import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public String parseName(String name) {
        Employee employee = employeeRepository.findByFirstName(name);
        return employee.getFirstName().toUpperCase();
        // muchas operaciones tal forma que se cumple con los criterios de aceptacion de una tarea de jira
    }
}
