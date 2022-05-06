/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.filestorage;

import com.umss.siiu.bpmn.model.EmployeeTask;
import com.umss.siiu.bpmn.model.processes.*;
import com.umss.siiu.bpmn.service.EmployeeTaskService;
import com.umss.siiu.bpmn.service.RoleService;
import com.umss.siiu.core.model.*;
import com.umss.siiu.core.repository.*;
import com.umss.siiu.core.service.*;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private EmployeeRepository employeeRepository;
    private EmployeeTaskService employeeTaskService;
    private final UserService userService;
    private RoleService roleService;


    private Task bootstrapTask;
    private Task validateParcelTask;
    private Task roiTask;
    private Task qaTask;
    private Task civasQaTask;
    private Task uploadFilesTask;
    private Task narrativeTask;
    private Task taxingTask;
    private Task zoningTask;
    private Task relomaTask;
    private Task formattingTask;
    private Task reformattingTask;

    public DevBootstrap(EmployeeRepository employeeRepository,
            EmployeeTaskService employeeTaskService,
            UserService userService, RoleService roleService) {
        this.employeeRepository = employeeRepository;
        this.employeeTaskService = employeeTaskService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initializeRoles();
        initializeEmployees();
    }

    private void initializeRoles() {
        createRole(RoleType.ADMIN.getId(), RoleType.ADMIN.getType());
        createRole(RoleType.GENERAL.getId(), RoleType.GENERAL.getType());
        createRole(RoleType.SUPERVISOR.getId(), RoleType.SUPERVISOR.getType());
    }

    private Role createRole(long id, String roleName) {
        var role = new Role();
        role.setId(id);
        role.setName(roleName);
        roleService.save(role);
        return role;
    }


    private void initializeEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            Set<Task> edsonTasks = new HashSet<>();
            edsonTasks.add(uploadFilesTask);
            createEmployee("Edson", "Terceros", edsonTasks, "edson@maecre.com");

            Set<Task> arielTasks = new HashSet<>();
            arielTasks.add(qaTask);
            createEmployee("Ariel", "Terceros", arielTasks, "ariel@maecre.com");

            Set<Task> victorTasks = new HashSet<>();
            victorTasks.add(validateParcelTask);
            victorTasks.add(relomaTask);
            victorTasks.add(taxingTask);
            victorTasks.add(narrativeTask);
            victorTasks.add(formattingTask);
            victorTasks.add(zoningTask);
            victorTasks.add(roiTask);
            victorTasks.add(qaTask);
            victorTasks.add(reformattingTask);
            victorTasks.add(civasQaTask);
            victorTasks.add(uploadFilesTask);
            createEmployee("Victor", "Linero", victorTasks, "victor@maecre.com");

            Set<Task> jasonTasks = new HashSet<>();
            jasonTasks.add(zoningTask);
            createEmployee("Jason", "Gonzalez", jasonTasks, "jason@maecre.com");

            Set<Task> marioTasks = new HashSet<>();
            marioTasks.add(uploadFilesTask);
            createEmployee("Mario", "Montero", marioTasks, "mario@maecre.com");

            Set<Task> systemTasks = new HashSet<>();
            systemTasks.add(bootstrapTask);
            createEmployee("System", "", systemTasks, "maecre.appgestion@gmail.com");
        }
    }

    private void createUser(String email, Employee employee) {
        var user = new User();
        var role = new Role();
        HashSet<Role> roles = new HashSet<>();

        user.setEmail(email);
        user.setEnabled(true);
        user.setPassword("$2a$10$XURPShQNCsLjp1ESc2laoObo9QZDhxz73hJPaEv7/cBha4pk0AgP."); //el password es: password
        user.setEmployee(employee);

        role.setId(1L);
        roles.add(role);
        user.setRoles(roles);
        userService.save(user);
    }

    private void createEmployee(String firstName, String lastName, Set<Task> tasks, String email) {
        var employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employeeRepository.save(employee);
        for (Task task : tasks) {
            var employeeTask = new EmployeeTask();
            employeeTask.setTask(task);
            employeeTask.setEmployee(employee);
            employeeTaskService.save(employeeTask);
        }

        createUser(email, employee);
    }


}
