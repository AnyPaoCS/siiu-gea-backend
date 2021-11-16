/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.siiurest.controller;

import com.umss.siiu.core.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("uppercase")
    public String endpoint() {
        return employeeService.parseName("edson");
    }
}

