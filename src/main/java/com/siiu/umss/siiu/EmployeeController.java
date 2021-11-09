/**
 * @author: Edson A. Terceros T.
 */

package com.siiu.umss.siiu;

import com.siiu.umss.siiu.service.EmployeeService;
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

