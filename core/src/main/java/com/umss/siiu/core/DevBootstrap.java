/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core;

import com.umss.siiu.core.model.*;
import com.umss.siiu.core.repository.*;
import com.umss.siiu.core.service.*;
import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

//@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private EmployeeRepository employeeRepository;

    public DevBootstrap(EmployeeRepository employeeRepository) {

        this.employeeRepository = employeeRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createEmployees();
    }

    private String getResourceAsString(String resourceName) {
        try (InputStream inputStream = this.getClass().getResourceAsStream(resourceName)) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private InputStream getResourceAsStream(String resourceName) {
        try (InputStream inputStream = this.getClass().getResourceAsStream(resourceName)) {
            return inputStream;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void createEmployees() {
        createEmployee("edson", "terceros");
        createEmployee("valentin", "laime");
    }

    private void createEmployee(String name, String lastName) {
        Employee employee = new Employee();
        employee.setFirstName(name);
        employee.setLastName(lastName);
        employeeRepository.save(employee);
    }
}
