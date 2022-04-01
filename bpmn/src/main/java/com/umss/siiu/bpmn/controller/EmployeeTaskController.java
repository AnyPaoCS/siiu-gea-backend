package com.umss.siiu.bpmn.controller;

import java.util.List;
import java.util.Set;

import com.umss.siiu.bpmn.dto.EmployeeTaskDto;
import com.umss.siiu.bpmn.model.EmployeeTask;
import com.umss.siiu.bpmn.service.EmployeeTaskService;
import com.umss.siiu.core.controller.GenericController;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.service.EmployeeService;
import com.umss.siiu.core.service.GenericService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/employees-tasks")
public class EmployeeTaskController extends GenericController<EmployeeTask, EmployeeTaskDto> {

  private EmployeeTaskService employeeTaskService;
  private EmployeeService employeeService;

  public EmployeeTaskController(EmployeeTaskService employeeTaskService, EmployeeService employeeService) {
    this.employeeTaskService = employeeTaskService;
    this.employeeService = employeeService;
  }

  @GetMapping("findByEmployee/{employeeId}")
  public List<EmployeeTaskDto> findUserTaskByUSer(@PathVariable String employeeId) {
    Employee employee = employeeService.findById(Long.parseLong(employeeId));
    Set<EmployeeTask> employeeTasks = employeeTaskService.findByEmployee(employee);
    return (List<EmployeeTaskDto>) super.toDto(employeeTasks);
  }

  @Override
  protected GenericService<EmployeeTask> getService() {
    return employeeTaskService;
  }
}