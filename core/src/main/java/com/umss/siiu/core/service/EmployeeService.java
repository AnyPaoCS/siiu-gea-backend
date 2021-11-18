/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.service;

import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Value("${EmployeeService.urlSSU:www.ssu.com}")
    private String urlSSU;

    public String parseName(String name) {
        Employee employee = employeeRepository.findByFirstName(name);
        return employee.getFirstName().toUpperCase();
        // muchas operaciones tal forma que se cumple con los criterios de aceptacion de una tarea de jira
    }

    @PostConstruct
    void post() {
        System.out.println("hola");
    }
}
