/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.siiurest;

import com.umss.siiu.bpmn.model.EmployeeTask;
import com.umss.siiu.bpmn.model.NotificationType;
import com.umss.siiu.bpmn.model.processes.Process;
import com.umss.siiu.bpmn.model.processes.*;
import com.umss.siiu.bpmn.repository.NotificationTypeRepository;
import com.umss.siiu.bpmn.repository.ProcessRepository;
import com.umss.siiu.bpmn.service.EmployeeTaskService;
import com.umss.siiu.bpmn.service.RoleService;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.model.Role;
import com.umss.siiu.core.model.RoleType;
import com.umss.siiu.core.model.User;
import com.umss.siiu.core.repository.EmployeeRepository;
import com.umss.siiu.core.service.UserService;
import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private static final String COMPLETE_ACTION = "DONE";
    private static final String CRM_JOB = "CRMJob";
    private static final String VALIDATE_PARCEL_ROI_GROUP = "ValidateParcel_ROI";

    private EmployeeRepository employeeRepository;
    private EmployeeTaskService employeeTaskService;
    private final UserService userService;
    private ProcessRepository processRepository;
    private RoleService roleService;
    private NotificationTypeRepository notificationTypeRepository;

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
                        UserService userService, ProcessRepository processRepository, RoleService roleService, NotificationTypeRepository notificationTypeRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeTaskService = employeeTaskService;
        this.userService = userService;
        this.processRepository = processRepository;
        this.roleService = roleService;
        this.notificationTypeRepository = notificationTypeRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initializeNotificationType();
        initializeJobProcess();
        initializeRoles();
        initializeEmployees();
    }
    private void initializeNotificationType(){
        NotificationType n1 = new NotificationType();
        n1.setName("Tipo 1");
        NotificationType n2 = new NotificationType();
        n2.setName("Tipo 2");
        notificationTypeRepository.save(n1);
        notificationTypeRepository.save(n2);
    }

    private void initializeRoles() {
        createRole(RoleType.ADMIN.getId(), RoleType.ADMIN.getType());
        createRole(RoleType.GENERAL.getId(), RoleType.GENERAL.getType());
        createRole(RoleType.SUPERVISOR.getId(), RoleType.SUPERVISOR.getType());
    }

    private Role createRole(long id, String roleName) {
        Role role = new Role();
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

    private void initializeJobProcess() {
        Process crmJob = processRepository.findByName(CRM_JOB);
        if (null == crmJob) {
            Process process = createDefaultProcess();
            processRepository.save(process);
        }
    }

    private Process createDefaultProcess() {
        Process process = new Process();
        process.setName(CRM_JOB);

        uploadFilesTask = createProcessTask(TaskType.UPLOAD_FILES.getName(), TaskType.UPLOAD_FILES.getCode(), false,
                Area.QA.getCode());
        civasQaTask = createProcessTask(TaskType.RE_REVISE.getName(), TaskType.RE_REVISE.getCode(), true,
                Area.QA.getCode());
        reformattingTask = createProcessTask(TaskType.REFORMATTING.getName(), TaskType.REFORMATTING.getCode(), false,
                Area.FORMATTING.getCode());
        qaTask = createProcessTask(TaskType.QA.getName(), TaskType.QA.getCode(), true, Area.QA.getCode());
        formattingTask = createProcessTask(TaskType.FORMATTING.getName(), TaskType.FORMATTING.getCode(), false,
                Area.FORMATTING.getCode());
        formattingTask.setEntryLogicGatePolicyType(EntryLogicGatePolicyType.AND);
        narrativeTask = createProcessTask(TaskType.NARRATIVE.getName(), TaskType.NARRATIVE.getCode(), false,
                Area.NARRATIVE.getCode());
        taxingTask = createProcessTask(TaskType.TAXES.getName(), TaskType.TAXES.getCode(), false, Area.TAXES.getCode());
        zoningTask = createProcessTask(TaskType.ZONING.getName(), TaskType.ZONING.getCode(), false,
                Area.ZONING.getCode());
        roiTask = createProcessTask(TaskType.ROI.getName(), TaskType.ROI.getCode(), false, Area.ROI.getCode());
        roiTask.setParallelGroupingCode(VALIDATE_PARCEL_ROI_GROUP);
        relomaTask = createProcessTask(TaskType.RELOMA.getName(), TaskType.RELOMA.getCode(), false,
                Area.RELOMA.getCode());
        validateParcelTask = createProcessTask(TaskType.VALIDATE_PARCEL.getName(), TaskType.VALIDATE_PARCEL.getCode(),
                false, Area.ROI.getCode());
        validateParcelTask.setParallelGroupingCode(VALIDATE_PARCEL_ROI_GROUP);
        bootstrapTask = createProcessTask(TaskType.BOOTSTRAP.getName(), TaskType.BOOTSTRAP.getCode(), false,
                Area.NONE.getCode());

        addTaskAction(bootstrapTask, COMPLETE_ACTION, validateParcelTask, ActionFlowType.AUTOMATIC);
        addTaskAction(bootstrapTask, COMPLETE_ACTION, roiTask, ActionFlowType.AUTOMATIC);
        addTaskAction(bootstrapTask, COMPLETE_ACTION, zoningTask, ActionFlowType.AUTOMATIC);
        addTaskAction(bootstrapTask, COMPLETE_ACTION, taxingTask, ActionFlowType.AUTOMATIC);
        addTaskAction(bootstrapTask, COMPLETE_ACTION, relomaTask, ActionFlowType.AUTOMATIC);
        addTaskAction(bootstrapTask, COMPLETE_ACTION, narrativeTask, ActionFlowType.AUTOMATIC);

        addTaskAction(validateParcelTask, COMPLETE_ACTION, formattingTask, ActionFlowType.AUTOMATIC);
        addTaskAction(validateParcelTask, TaskType.ROI.getCode(), roiTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(validateParcelTask, TaskType.ZONING.getCode(), zoningTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(validateParcelTask, TaskType.TAXES.getCode(), taxingTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(validateParcelTask, TaskType.RELOMA.getCode(), relomaTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(validateParcelTask, TaskType.NARRATIVE.getCode(), narrativeTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(validateParcelTask, TaskType.BOOTSTRAP.getCode(), bootstrapTask, ActionFlowType.FORCE_GATE_ENTRY);

        addTaskAction(roiTask, COMPLETE_ACTION, formattingTask, ActionFlowType.AUTOMATIC);
        addTaskAction(zoningTask, COMPLETE_ACTION, formattingTask, ActionFlowType.AUTOMATIC);
        addTaskAction(taxingTask, COMPLETE_ACTION, formattingTask, ActionFlowType.AUTOMATIC);
        addTaskAction(relomaTask, COMPLETE_ACTION, formattingTask, ActionFlowType.AUTOMATIC);
        addTaskAction(narrativeTask, COMPLETE_ACTION, formattingTask, ActionFlowType.AUTOMATIC);

        addTaskAction(formattingTask, COMPLETE_ACTION, qaTask, ActionFlowType.AUTOMATIC);
        addTaskAction(formattingTask, TaskType.VALIDATE_PARCEL.getCode(), validateParcelTask,
                ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(formattingTask, TaskType.ROI.getCode(), roiTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(formattingTask, TaskType.ZONING.getCode(), zoningTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(formattingTask, TaskType.TAXES.getCode(), taxingTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(formattingTask, TaskType.RELOMA.getCode(), relomaTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(formattingTask, TaskType.NARRATIVE.getCode(), narrativeTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(formattingTask, TaskType.BOOTSTRAP.getCode(), bootstrapTask, ActionFlowType.FORCE_GATE_ENTRY);

        addTaskAction(qaTask, COMPLETE_ACTION, reformattingTask, ActionFlowType.AUTOMATIC);
        addTaskAction(qaTask, TaskType.FORMATTING.getCode(), formattingTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(qaTask, TaskType.VALIDATE_PARCEL.getCode(), validateParcelTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(qaTask, TaskType.ROI.getCode(), roiTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(qaTask, TaskType.ZONING.getCode(), zoningTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(qaTask, TaskType.TAXES.getCode(), taxingTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(qaTask, TaskType.RELOMA.getCode(), relomaTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(qaTask, TaskType.NARRATIVE.getCode(), narrativeTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(qaTask, TaskType.BOOTSTRAP.getCode(), bootstrapTask, ActionFlowType.FORCE_GATE_ENTRY);

        addTaskAction(reformattingTask, COMPLETE_ACTION, civasQaTask, ActionFlowType.AUTOMATIC);
        addTaskAction(reformattingTask, TaskType.QA.getCode(), qaTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(reformattingTask, TaskType.FORMATTING.getCode(), formattingTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(reformattingTask, TaskType.VALIDATE_PARCEL.getCode(), validateParcelTask,
                ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(reformattingTask, TaskType.ROI.getCode(), roiTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(reformattingTask, TaskType.ZONING.getCode(), zoningTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(reformattingTask, TaskType.TAXES.getCode(), taxingTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(reformattingTask, TaskType.RELOMA.getCode(), relomaTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(reformattingTask, TaskType.NARRATIVE.getCode(), narrativeTask, ActionFlowType.FORCE_GATE_ENTRY);

        addTaskAction(civasQaTask, COMPLETE_ACTION, uploadFilesTask, ActionFlowType.AUTOMATIC);
        addTaskAction(civasQaTask, TaskType.REFORMATTING.getCode(), reformattingTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(civasQaTask, TaskType.QA.getCode(), qaTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(civasQaTask, TaskType.FORMATTING.getCode(), formattingTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(civasQaTask, TaskType.VALIDATE_PARCEL.getCode(), validateParcelTask,
                ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(civasQaTask, TaskType.ROI.getCode(), roiTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(civasQaTask, TaskType.ZONING.getCode(), zoningTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(civasQaTask, TaskType.TAXES.getCode(), taxingTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(civasQaTask, TaskType.RELOMA.getCode(), relomaTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(civasQaTask, TaskType.NARRATIVE.getCode(), narrativeTask, ActionFlowType.FORCE_GATE_ENTRY);

        addTaskAction(uploadFilesTask, TaskType.RE_REVISE.getCode(), civasQaTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(uploadFilesTask, TaskType.REFORMATTING.getCode(), reformattingTask,
                ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(uploadFilesTask, TaskType.QA.getCode(), qaTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(uploadFilesTask, TaskType.FORMATTING.getCode(), formattingTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(uploadFilesTask, TaskType.VALIDATE_PARCEL.getCode(), validateParcelTask,
                ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(uploadFilesTask, TaskType.ROI.getCode(), roiTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(uploadFilesTask, TaskType.ZONING.getCode(), zoningTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(uploadFilesTask, TaskType.TAXES.getCode(), taxingTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(uploadFilesTask, TaskType.RELOMA.getCode(), relomaTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(uploadFilesTask, TaskType.NARRATIVE.getCode(), narrativeTask, ActionFlowType.FORCE_GATE_ENTRY);

        process.setTask(bootstrapTask);
        return process;
    }

    private Task createProcessTask(String taskName, String code, boolean isOutput, String relatedAreaCode) {
        Task task = new Task();
        task.setName(taskName);
        task.setCode(code);
        task.setRelatedAreaCode(relatedAreaCode);
        addResource(task, isOutput);
        return task;
    }

    private void addResource(Task task, boolean isOutput) {
        Resource resource = new Resource();
        resource.setResourceType(ResourceType.EMPLOYEE);
        resource.setTask(task);
        resource.setOutput(isOutput);
        task.getResourceList().add(resource);
    }

    private Task addTaskAction(Task task, String actionName, Task nextTask, ActionFlowType actionFlowType) {
        TaskAction taskAction = new TaskAction();
        taskAction.setName(actionName);
        taskAction.setTask(task);
        taskAction.setNextTask(nextTask);
        taskAction.setActionFlowType(actionFlowType);

        Set<TaskAction> taskActions = task.getTaskActions();
        taskActions.add(taskAction);
        return task;
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

    private void createUser(String email, Employee employee) {
        User user = new User();
        Role role = new Role();
        HashSet<Role> roles = new HashSet<>();

        user.setEmail(email);
        user.setEnabled(true);
        user.setPassword("$2a$10$XURPShQNCsLjp1ESc2laoObo9QZDhxz73hJPaEv7/cBha4pk0AgP."); //el password es: password
        user.setEmployee(employee);

        if (employee.getFirstName()=="System") {
            user.setSystemUser(true);
        }
        role.setId(1L);
        roles.add(role);
        user.setRoles(roles);
        userService.save(user);
    }

    private void createEmployee(String firstName, String lastName, Set<Task> tasks, String email) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employeeRepository.save(employee);
        for (Task task : tasks) {
            EmployeeTask employeeTask = new EmployeeTask();
            employeeTask.setTask(task);
            employeeTask.setEmployee(employee);
            employeeTaskService.save(employeeTask);
        }

        createUser(email, employee);
    }

}
