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

import org.springframework.web.bind.annotation.*;

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
    var employee = employeeService.findById(Long.parseLong(employeeId));
    Set<EmployeeTask> employeeTasks = employeeTaskService.findByEmployee(employee);
    return new EmployeeTaskDto().toListDto(employeeTasks, modelMapper);
  }


  @PostMapping(value = "deleteByCode")
  public void deleteElement(@RequestBody EmployeeTaskDto element) {
    employeeTaskService.deleteByCode(toModel(element));
  }

  @PostMapping(value="saveAllByCode")
  public void saveAll(@RequestBody EmployeeTaskDto element) {
    employeeTaskService.saveAllTask(toModel(element));
  }

  @Override
  protected GenericService<EmployeeTask> getService() {
    return employeeTaskService;
  }
}