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
public class   DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private static final String COMPLETE_ACTION = "DONE";
    private static final String CRM_JOB = "CRMJob";

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
    private Task cancellProcessTask;

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
            edsonTasks.add(validationDocumentsTask);
            createEmployee("Edson", "Terceros", edsonTasks, "edson@maecre.com");

            Set<Task> arielTasks = new HashSet<>();
            arielTasks.add(enablePaymentTask);
            createEmployee("Ariel", "Terceros", arielTasks, "ariel@maecre.com");

            Set<Task> victorTasks = new HashSet<>();
            victorTasks.add(validationDocumentsTask);
            victorTasks.add(validationPaymentTask);
            victorTasks.add(cancellProcessTask);
            victorTasks.add(reviewRequirementsTask);
            createEmployee("Victor", "Linero", victorTasks, "victor@maecre.com");

            Set<Task> jasonTasks = new HashSet<>();
            jasonTasks.add(cancellProcessTask);
            createEmployee("Jason", "Gonzalez", jasonTasks, "jason@maecre.com");

            Set<Task> marioTasks = new HashSet<>();
            marioTasks.add(validationPaymentTask);
            createEmployee("Mario", "Montero", marioTasks, "mario@maecre.com");

            Set<Task> systemTasks = new HashSet<>();
            systemTasks.add(requestProcessTask);
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

        requestProcessTask = createProcessTask(TaskType.REQUEST_PROCESS.getName(), TaskType.REQUEST_PROCESS.getCode(),false, Area.NONE.getCode());
        //addResourceDocument(requestProcessTask, false, "Documento Original", "DOC-ORIGINAL");
        //addResourceDocument(requestProcessTask, false, "Fotocopia del documento original a legalizar", "DOC-COPY");

        // La tarea necesita de dos recursos.
        //uploadFilesTask = createProcessTask(TaskType.UPLOAD_FILES.getName(), TaskType.UPLOAD_FILES.getCode(), Area.FILES_AREA.getCode());
        reviewRequirementsTask = createProcessTask(TaskType.REVIEW_REQUIREMENTS.getName(), TaskType.REVIEW_REQUIREMENTS.getCode(), false, Area.FILES_AREA.getCode());


        enablePaymentTask = createProcessTask(TaskType.ENABLE_PAYMENT.getName(), TaskType.ENABLE_PAYMENT.getCode(), false,Area.FILES_AREA.getCode());


        validationPaymentTask = createProcessTask(TaskType.VALIDATION_PAYMENT.getName(), TaskType.VALIDATION_PAYMENT.getCode(), false,Area.CASH_AREA.getCode());
        //addResourceDocument(validationPaymentTask, false, "Valorado de Legalizacion Fotocopia", "VALORADO");

        //validationPaymentTask.setEntryLogicGatePolicyType(EntryLogicGatePolicyType.AND);//revisar
        validationDocumentsTask = createProcessTask(TaskType.VALIDATION_DOCUMENTS.getName(), TaskType.VALIDATION_DOCUMENTS.getCode(),false, Area.DEPARTMENT_AREA.getCode());


        //validationDocumentsTask = createProcessTask(TaskType.VALIDATION_DOCUMENTS.getName(), TaskType.VALIDATION_DOCUMENTS.getCode(), Area.FACULTY_AREA.getCode());
        signatureDocumentsTask = createProcessTask(TaskType.SIGNATURE_DOCUMENTS.getName(), TaskType.SIGNATURE_DOCUMENTS.getCode(),false, Area.FACULTY_AREA.getCode());


        concludeProcessTask= createProcessTask(TaskType.CONCLUDE_PROCESS.getName(), TaskType.CONCLUDE_PROCESS.getCode(), true,Area.FILES_AREA.getCode());

        //addResourceDocument(concludeProcessTask, true, "Documento Legalizado", "DOC-LEGALIZADO");

        cancellProcessTask = createProcessTask(TaskType.CANCELL_PROCESS.getName(), TaskType.CANCELL_PROCESS.getCode(), false, Area.FILES_AREA.getCode());

        //Primer nodo
        addTaskAction(requestProcessTask, TaskType.REQUEST_PROCESS.getCode(), reviewRequirementsTask, ActionFlowType.AUTOMATIC);

        //Segundo nodo
        addTaskAction(reviewRequirementsTask, TaskType.REVIEW_REQUIREMENTS.getCode(), enablePaymentTask, ActionFlowType.AUTOMATIC);
        addTaskAction(reviewRequirementsTask, TaskType.CANCELL_PROCESS.getCode(), cancellProcessTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(reviewRequirementsTask, TaskType.OBSERVATIONS.getCode(), requestProcessTask, ActionFlowType.FORCE_GATE_ENTRY);

        //Tercer nodo
        addTaskAction(enablePaymentTask, TaskType.ENABLE_PAYMENT.getCode(), validationPaymentTask, ActionFlowType.AUTOMATIC);
        addTaskAction(enablePaymentTask, TaskType.CANCELL_PROCESS.getCode(), cancellProcessTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(enablePaymentTask, TaskType.OBSERVATIONS.getCode(), reviewRequirementsTask, ActionFlowType.FORCE_GATE_ENTRY);

        addTaskAction(validationPaymentTask, TaskType.VALIDATION_PAYMENT.getCode(), validationDocumentsTask, ActionFlowType.AUTOMATIC);
        addTaskAction(validationPaymentTask, TaskType.CANCELL_PROCESS.getCode(), cancellProcessTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(validationPaymentTask, TaskType.OBSERVATIONS.getCode(), enablePaymentTask, ActionFlowType.FORCE_GATE_ENTRY);

        addTaskAction(validationDocumentsTask, TaskType.VALIDATION_DOCUMENTS.getCode(), signatureDocumentsTask, ActionFlowType.AUTOMATIC);
        addTaskAction(validationDocumentsTask, TaskType.CANCELL_PROCESS.getCode(), cancellProcessTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(validationDocumentsTask, TaskType.OBSERVATIONS.getCode(), validationPaymentTask, ActionFlowType.FORCE_GATE_ENTRY);

        addTaskAction(signatureDocumentsTask, TaskType.CONCLUDE_PROCESS.getCode(), concludeProcessTask, ActionFlowType.AUTOMATIC);
        addTaskAction(signatureDocumentsTask, TaskType.CANCELL_PROCESS.getCode(), cancellProcessTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(signatureDocumentsTask, TaskType.OBSERVATIONS.getCode(), validationDocumentsTask, ActionFlowType.FORCE_GATE_ENTRY);


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
