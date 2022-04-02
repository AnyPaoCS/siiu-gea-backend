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
import com.umss.siiu.core.model.*;
import com.umss.siiu.core.repository.*;
import com.umss.siiu.core.service.*;
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
    private static final String PROC1_CODE = "TRA-LEG-VARIOS";
    private static final String PROC1_NAME = "LEGALIZACION DOCUMENTOS VARIOS";


    private EmployeeRepository employeeRepository;
    private EmployeeTaskService employeeTaskService;
    private final UserService userService;
    private ProcessRepository processRepository;
    private RoleService roleService;
    private NotificationTypeRepository notificationTypeRepository;

    private Task requestProcessTask;
    private Task reviewRequirementsTask;
    private Task enablePaymentTask;
    private Task validationPaymentTask;
    private Task validationDocumentsTask;
    private Task signatureDocumentsTask;
    private Task concludeProcessTask;

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
        createRole(RoleType.APPLICANT.getId(), RoleType.APPLICANT.getType());
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
            edsonTasks.add(validationDocumentsTask);
            createEmployee("Edson", "Terceros", edsonTasks, "edson@maecre.com", RoleType.GENERAL.getId());

            Set<Task> arielTasks = new HashSet<>();
            arielTasks.add(enablePaymentTask);
            createEmployee("Ariel", "Terceros", arielTasks, "ariel@maecre.com", RoleType.GENERAL.getId());

            Set<Task> victorTasks = new HashSet<>();
            victorTasks.add(validationDocumentsTask);
            victorTasks.add(reviewRequirementsTask);
            victorTasks.add(concludeProcessTask);
            victorTasks.add(signatureDocumentsTask);
            createEmployee("Victor", "Linero", victorTasks, "victor@maecre.com", RoleType.GENERAL.getId());

            Set<Task> jasonTasks = new HashSet<>();
            jasonTasks.add(signatureDocumentsTask);
            createEmployee("Jason", "Gonzalez", jasonTasks, "jason@maecre.com", RoleType.GENERAL.getId());

            Set<Task> systemTasks = new HashSet<>();
            systemTasks.add(signatureDocumentsTask);
            createEmployee("System", "", systemTasks, "maecre.appgestion@gmail.com", RoleType.GENERAL.getId());

            //Usuario solicitante puede hacer esos recursos porque necesita subir documentos
            Set<Task> user1Tasks = new HashSet<>();
            user1Tasks.add(requestProcessTask);
            user1Tasks.add(validationPaymentTask);
            createEmployee("Solicitante", "1", user1Tasks, "csorialopez11@gmail.com", RoleType.APPLICANT.getId());

            Set<Task> user2Tasks = new HashSet<>();
            user2Tasks.add(requestProcessTask);
            user2Tasks.add(validationPaymentTask);
            createEmployee("Solicitante2", "2", user2Tasks, "csorialopez11+1@gmail.com", RoleType.APPLICANT.getId());
        }
    }

    private void initializeJobProcess() {
        Process crmJob = processRepository.findByCode(PROC1_CODE);
        if (null == crmJob) {
            Process process = createDefaultProcess();
            processRepository.save(process);
        }
    }

    private Process createDefaultProcess() {
        Process process = new Process();
        process.setCode(PROC1_CODE);
        process.setName(PROC1_NAME);

        requestProcessTask = createProcessTask(TaskType.REQUEST_PROCESS.getName(), TaskType.REQUEST_PROCESS.getCode(),false, Area.NONE.getCode());
        addResourceDocument(requestProcessTask, false, ResourceDocumentType.ORIGINAL_DOCUMENT.getCode(), ResourceDocumentType.ORIGINAL_DOCUMENT.getName());
        addResourceDocument(requestProcessTask, false, ResourceDocumentType.COPY_ORIGINAL_DOCUMENT.getCode(), ResourceDocumentType.COPY_ORIGINAL_DOCUMENT.getName());

        // La tarea necesita de dos recursos.
        //uploadFilesTask = createProcessTask(TaskType.UPLOAD_FILES.getName(), TaskType.UPLOAD_FILES.getCode(), Area.FILES_AREA.getCode());
        reviewRequirementsTask = createProcessTask(TaskType.REVIEW_REQUIREMENTS.getName(), TaskType.REVIEW_REQUIREMENTS.getCode(), false, Area.FILES_AREA.getCode());


        enablePaymentTask = createProcessTask(TaskType.ENABLE_PAYMENT.getName(), TaskType.ENABLE_PAYMENT.getCode(), false,Area.FILES_AREA.getCode());

        validationPaymentTask = createProcessTask(TaskType.VALIDATION_PAYMENT.getName(), TaskType.VALIDATION_PAYMENT.getCode(), false,Area.CASH_AREA.getCode());
        addResourceDocument(validationPaymentTask, false, ResourceDocumentType.VALUED.getCode(), ResourceDocumentType.VALUED.getName());

        validationDocumentsTask = createProcessTask(TaskType.VALIDATION_DOCUMENTS.getName(), TaskType.VALIDATION_DOCUMENTS.getCode(),false, Area.DEPARTMENT_AREA.getCode());

        signatureDocumentsTask = createProcessTask(TaskType.SIGNATURE_DOCUMENTS.getName(), TaskType.SIGNATURE_DOCUMENTS.getCode(),false, Area.FACULTY_AREA.getCode());


        concludeProcessTask= createProcessTask(TaskType.CONCLUDE_PROCESS.getName(), TaskType.CONCLUDE_PROCESS.getCode(), false, Area.FILES_AREA.getCode());
        addResourceDocument(concludeProcessTask, true, ResourceDocumentType.LEGALIZED_DOCUMENT.getCode(), ResourceDocumentType.LEGALIZED_DOCUMENT.getName());

        //Primer nodo
        addTaskAction(requestProcessTask, TaskType.REQUEST_PROCESS.getCode(), reviewRequirementsTask, ActionFlowType.AUTOMATIC);

        //Segundo nodo
        addTaskAction(reviewRequirementsTask, TaskType.REVIEW_REQUIREMENTS.getCode(), enablePaymentTask, ActionFlowType.AUTOMATIC);
        addTaskAction(reviewRequirementsTask, TaskType.OBSERVATIONS.getCode(), requestProcessTask, ActionFlowType.FORCE_GATE_ENTRY);

        //Tercer nodo
        addTaskAction(enablePaymentTask, TaskType.ENABLE_PAYMENT.getCode(), validationPaymentTask, ActionFlowType.AUTOMATIC);
        addTaskAction(enablePaymentTask, TaskType.OBSERVATIONS.getCode(), reviewRequirementsTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(enablePaymentTask, TaskType.OBSERVATIONS.getCode(), requestProcessTask, ActionFlowType.FORCE_GATE_ENTRY);

        addTaskAction(validationPaymentTask, TaskType.VALIDATION_PAYMENT.getCode(), validationDocumentsTask, ActionFlowType.AUTOMATIC);
        addTaskAction(validationPaymentTask, TaskType.OBSERVATIONS.getCode(), enablePaymentTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(validationPaymentTask, TaskType.OBSERVATIONS.getCode(), reviewRequirementsTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(validationPaymentTask, TaskType.OBSERVATIONS.getCode(), requestProcessTask, ActionFlowType.FORCE_GATE_ENTRY);


        addTaskAction(validationDocumentsTask, TaskType.VALIDATION_DOCUMENTS.getCode(), signatureDocumentsTask, ActionFlowType.AUTOMATIC);
        addTaskAction(validationDocumentsTask, TaskType.OBSERVATIONS.getCode(), validationPaymentTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(validationDocumentsTask, TaskType.OBSERVATIONS.getCode(), enablePaymentTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(validationDocumentsTask, TaskType.OBSERVATIONS.getCode(), reviewRequirementsTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(validationDocumentsTask, TaskType.OBSERVATIONS.getCode(), requestProcessTask, ActionFlowType.FORCE_GATE_ENTRY);


        addTaskAction(signatureDocumentsTask, TaskType.CONCLUDE_PROCESS.getCode(), concludeProcessTask, ActionFlowType.AUTOMATIC);
        addTaskAction(signatureDocumentsTask, TaskType.OBSERVATIONS.getCode(), validationDocumentsTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(signatureDocumentsTask, TaskType.OBSERVATIONS.getCode(), validationPaymentTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(signatureDocumentsTask, TaskType.OBSERVATIONS.getCode(), enablePaymentTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(signatureDocumentsTask, TaskType.OBSERVATIONS.getCode(), reviewRequirementsTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(signatureDocumentsTask, TaskType.OBSERVATIONS.getCode(), requestProcessTask, ActionFlowType.FORCE_GATE_ENTRY);

        addTaskAction(concludeProcessTask, TaskType.OBSERVATIONS.getCode(), signatureDocumentsTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(concludeProcessTask, TaskType.OBSERVATIONS.getCode(), validationDocumentsTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(concludeProcessTask, TaskType.OBSERVATIONS.getCode(), validationPaymentTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(concludeProcessTask, TaskType.OBSERVATIONS.getCode(), enablePaymentTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(concludeProcessTask, TaskType.OBSERVATIONS.getCode(), reviewRequirementsTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(concludeProcessTask, TaskType.OBSERVATIONS.getCode(), requestProcessTask, ActionFlowType.FORCE_GATE_ENTRY);


        process.setTask(requestProcessTask);
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

    private void addResourceDocument(Task task, boolean isOutput, String code, String name) {
        Resource resource = new Resource();
        resource.setResourceType(ResourceType.DOCUMENT);
        resource.setTask(task);
        resource.setOutput(isOutput);
        resource.setDocument(new ResourceDocument(code, name));
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

    private void createUser(String email, Employee employee, Long roleId) {
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
        role.setId(roleId);
        roles.add(role);
        user.setRoles(roles);
        userService.save(user);
    }

    private void createEmployee(String firstName, String lastName, Set<Task> tasks, String email, Long roleId) {
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

        createUser(email, employee, roleId);
    }
}