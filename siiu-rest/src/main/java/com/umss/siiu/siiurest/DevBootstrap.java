/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.siiurest;

import com.umss.siiu.bpmn.model.*;
import com.umss.siiu.bpmn.model.processes.Process;
import com.umss.siiu.bpmn.model.processes.*;
import com.umss.siiu.bpmn.repository.NotificationTypeRepository;
import com.umss.siiu.bpmn.repository.ProcessRepository;
import com.umss.siiu.bpmn.repository.TaskRepository;
import com.umss.siiu.bpmn.service.EmployeeTaskService;
import com.umss.siiu.bpmn.service.RoleService;
import com.umss.siiu.core.model.*;
import com.umss.siiu.core.repository.*;
import com.umss.siiu.core.service.*;
import com.umss.siiu.filestorage.model.FileType;
import com.umss.siiu.filestorage.model.FileTypeCategory;
import com.umss.siiu.filestorage.service.FileService;
import com.umss.siiu.filestorage.service.FileTypeService;
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
    private static final String PROC1_CODE = "TRA-LEG-VARIOS";
    private static final String PROC1_NAME = "LEGALIZACION DOCUMENTOS VARIOS";

    private static final String PROC2_CODE = "TRA-LEG-DIPLOMA";
    private static final String PROC2_NAME = "LEGALIZACION DIPLOMA DE BACHILLER/TITULO PROFESIONAL/DIPLOMA ACADEMICO/TITULO POSTGRADO/ RR.HOMOLOGACIÓN DIPLOMABACHILLER, POLICÍA O MILITAR";


    private EmployeeRepository employeeRepository;
    private EmployeeTaskService employeeTaskService;
    private final UserService userService;
    private ProcessRepository processRepository;
    private RoleService roleService;
    private NotificationTypeRepository notificationTypeRepository;
    private final FileTypeService fileTypeService;

    private TaskRepository taskRepository;

    private Task requestProcessTask;
    private Task reviewRequirementsTask;
    private Task enablePaymentTask;
    private Task validationPaymentTask;
    private Task validationDocumentsTask;
    private Task signatureDocumentsTask;
    private Task concludeProcessTask;

    public DevBootstrap(EmployeeRepository employeeRepository,
                        EmployeeTaskService employeeTaskService,
                        TaskRepository taskRepository,
                        UserService userService, ProcessRepository processRepository, RoleService roleService, NotificationTypeRepository notificationTypeRepository, FileTypeService fileTypeService) {
        this.employeeRepository = employeeRepository;
        this.employeeTaskService = employeeTaskService;
        this.userService = userService;
        this.processRepository = processRepository;
        this.roleService = roleService;
        this.notificationTypeRepository = notificationTypeRepository;
        this.fileTypeService = fileTypeService;

        this.taskRepository = taskRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initializeFileType();
        initializeNotificationType();
        initializeJobProcess();
        createProcessDos();
        initializeRoles();
        initializeEmployees();
    }
    private void initializeNotificationType(){
        var n1 = new NotificationType();
        n1.setName("Tipo 1");
        var n2 = new NotificationType();
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
            edsonTasks.add(validationDocumentsTask);
            edsonTasks.add(reviewRequirementsTask);
            createEmployee("Edson", "Terceros", edsonTasks, "edson@maecre.com", RoleType.SUPERVISOR.getId());

            Set<Task> arielTasks = new HashSet<>();
            arielTasks.add(enablePaymentTask);
            arielTasks.add(reviewRequirementsTask);
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
            createEmployee("System", "", systemTasks, "maecre.appgestion@gmail.com", RoleType.ADMIN.getId());

            //Usuario solicitante puede hacer esos recursos porque necesita subir documentos
            Set<Task> user1Tasks = new HashSet<>();
            user1Tasks.add(requestProcessTask);
            user1Tasks.add(validationPaymentTask);
            createEmployee("Daniel", "Soliz", user1Tasks, "danielsoliz80@gmail.com", RoleType.APPLICANT.getId());

            Set<Task> user2Tasks = new HashSet<>();
            user2Tasks.add(requestProcessTask);
            user2Tasks.add(validationPaymentTask);
            createEmployee("Solicitante2", "2", user2Tasks, "csorialopez11+1@gmail.com", RoleType.APPLICANT.getId());

            Set<Task> user3Tasks = new HashSet<>();
            user3Tasks.add(requestProcessTask);
            user3Tasks.add(validationPaymentTask);
            createEmployee("Antony", "Maceda", user3Tasks, "antonycc.123+1@gmail.com", RoleType.APPLICANT.getId());
        }
    }

    private void initializeJobProcess() {
        Process crmJob = processRepository.findByCode(PROC1_CODE);
        if (null == crmJob) {
            var process = createDefaultProcess();
            processRepository.save(process);
        }
    }

    private Process createDefaultProcess() {
        var process = new Process();
        process.setCode(PROC1_CODE);
        process.setName(PROC1_NAME);

        requestProcessTask = createProcessTask(TaskType.REQUEST_PROCESS.getName(), TaskType.REQUEST_PROCESS.getCode(),false, Area.NONE.getCode());
        addResourceDocument(requestProcessTask, false, ResourceDocumentType.DOCUMENTO_VARIOS_DOCUMENTO_ORIGINAL.getCode(), ResourceDocumentType.DOCUMENTO_VARIOS_DOCUMENTO_ORIGINAL.getName(), PROC1_CODE);
        addResourceDocument(requestProcessTask, false, ResourceDocumentType.DOCUMENTOS_VARIOS_FOTOCOPIA.getCode(), ResourceDocumentType.DOCUMENTOS_VARIOS_FOTOCOPIA.getName(), PROC1_CODE);
        // La tarea necesita de dos recursos.

        reviewRequirementsTask = createProcessTask(TaskType.REVIEW_REQUIREMENTS.getName(), TaskType.REVIEW_REQUIREMENTS.getCode(), false, Area.FILES_AREA.getCode());

        enablePaymentTask = createProcessTask(TaskType.ENABLE_PAYMENT.getName(), TaskType.ENABLE_PAYMENT.getCode(), false,Area.FILES_AREA.getCode());

        validationPaymentTask = createProcessTask(TaskType.VALIDATION_PAYMENT.getName(), TaskType.VALIDATION_PAYMENT.getCode(), false,Area.CASH_AREA.getCode());
        addResourceDocument(validationPaymentTask, false, ResourceDocumentType.DOCUMENTOS_VARIOS_VALORADO.getCode(), ResourceDocumentType.DOCUMENTOS_VARIOS_VALORADO.getName(), PROC1_CODE);

        signatureDocumentsTask = createProcessTask(TaskType.SIGNATURE_DOCUMENTS.getName(), TaskType.SIGNATURE_DOCUMENTS.getCode(),false, Area.FACULTY_AREA.getCode());

        concludeProcessTask= createProcessTask(TaskType.CONCLUDE_PROCESS.getName(), TaskType.CONCLUDE_PROCESS.getCode(), false, Area.FILES_AREA.getCode());
        addResourceDocument(concludeProcessTask, true, ResourceDocumentType.DOCUMENTOS_VARIOS_DOCUMENTO_LEGALIZADO.getCode(), ResourceDocumentType.DOCUMENTOS_VARIOS_DOCUMENTO_LEGALIZADO.getName(), PROC1_CODE);
        //Primer nodo
        addTaskAction(requestProcessTask, TaskType.REQUEST_PROCESS.getCode(), reviewRequirementsTask, ActionFlowType.AUTOMATIC);

        //Segundo nodo
        addTaskAction(reviewRequirementsTask, TaskType.REVIEW_REQUIREMENTS.getCode(), enablePaymentTask, ActionFlowType.AUTOMATIC);
        addTaskAction(reviewRequirementsTask, TaskType.OBSERVATIONS.getCode(), requestProcessTask, ActionFlowType.FORCE_GATE_ENTRY);

        //Tercer nodo
        addTaskAction(enablePaymentTask, TaskType.ENABLE_PAYMENT.getCode(), validationPaymentTask, ActionFlowType.AUTOMATIC);
        addTaskAction(enablePaymentTask, TaskType.OBSERVATIONS.getCode(), reviewRequirementsTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(enablePaymentTask, TaskType.OBSERVATIONS.getCode(), requestProcessTask, ActionFlowType.FORCE_GATE_ENTRY);

        addTaskAction(validationPaymentTask, TaskType.VALIDATION_PAYMENT.getCode(), signatureDocumentsTask, ActionFlowType.AUTOMATIC);
        addTaskAction(validationPaymentTask, TaskType.OBSERVATIONS.getCode(), enablePaymentTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(validationPaymentTask, TaskType.OBSERVATIONS.getCode(), reviewRequirementsTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(validationPaymentTask, TaskType.OBSERVATIONS.getCode(), requestProcessTask, ActionFlowType.FORCE_GATE_ENTRY);

        addTaskAction(signatureDocumentsTask, TaskType.CONCLUDE_PROCESS.getCode(), concludeProcessTask, ActionFlowType.AUTOMATIC);
        addTaskAction(signatureDocumentsTask, TaskType.OBSERVATIONS.getCode(), validationPaymentTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(signatureDocumentsTask, TaskType.OBSERVATIONS.getCode(), enablePaymentTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(signatureDocumentsTask, TaskType.OBSERVATIONS.getCode(), reviewRequirementsTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(signatureDocumentsTask, TaskType.OBSERVATIONS.getCode(), requestProcessTask, ActionFlowType.FORCE_GATE_ENTRY);

        addTaskAction(concludeProcessTask, TaskType.OBSERVATIONS.getCode(), signatureDocumentsTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(concludeProcessTask, TaskType.OBSERVATIONS.getCode(), validationPaymentTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(concludeProcessTask, TaskType.OBSERVATIONS.getCode(), enablePaymentTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(concludeProcessTask, TaskType.OBSERVATIONS.getCode(), reviewRequirementsTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(concludeProcessTask, TaskType.OBSERVATIONS.getCode(), requestProcessTask, ActionFlowType.FORCE_GATE_ENTRY);


        process.setTask(requestProcessTask);
        return process;
    }

    private void createProcessDos() {
        var process2 = new Process();
        process2.setCode(PROC2_CODE);
        process2.setName(PROC2_NAME);
        requestProcessTask = createProcessTask(TaskType.REQUEST_PROCESS.getName(), TaskType.REQUEST_PROCESS.getCode(),false, Area.NONE.getCode());
        addResourceDocument(requestProcessTask, false, ResourceDocumentType.DIPLOMA_BACHILLER_DOCUMENTO_ORIGINAL.getCode(), ResourceDocumentType.DIPLOMA_BACHILLER_DOCUMENTO_ORIGINAL.getName(), PROC2_CODE);

        reviewRequirementsTask = createProcessTask(TaskType.REVIEW_REQUIREMENTS.getName(), TaskType.REVIEW_REQUIREMENTS.getCode(), false, Area.FILES_AREA.getCode());

        enablePaymentTask = createProcessTask(TaskType.ENABLE_PAYMENT.getName(), TaskType.ENABLE_PAYMENT.getCode(), false,Area.FILES_AREA.getCode());

        validationPaymentTask = createProcessTask(TaskType.VALIDATION_PAYMENT.getName(), TaskType.VALIDATION_PAYMENT.getCode(), false,Area.CASH_AREA.getCode());
        addResourceDocument(validationPaymentTask, false, ResourceDocumentType.DIPLOMA_BACHILLER_VALORADO.getCode(), ResourceDocumentType.DIPLOMA_BACHILLER_VALORADO.getName(), PROC2_CODE);

        validationDocumentsTask = createProcessTask(TaskType.VALIDATION_DOCUMENTS.getName(), TaskType.VALIDATION_DOCUMENTS.getCode(),false, Area.DEPARTMENT_AREA.getCode());

        signatureDocumentsTask = createProcessTask(TaskType.SIGNATURE_DOCUMENTS.getName(), TaskType.SIGNATURE_DOCUMENTS.getCode(),false, Area.FACULTY_AREA.getCode());

        concludeProcessTask= createProcessTask(TaskType.CONCLUDE_PROCESS.getName(), TaskType.CONCLUDE_PROCESS.getCode(), false, Area.FILES_AREA.getCode());
        addResourceDocument(concludeProcessTask, true, ResourceDocumentType.DIPLOMA_BACHILLER_DOCUMENTO_LEGALIZADO.getCode(), ResourceDocumentType.DIPLOMA_BACHILLER_DOCUMENTO_LEGALIZADO.getName(), PROC2_CODE);
        //Primer nodo
        addTaskAction(requestProcessTask, TaskType.REQUEST_PROCESS.getCode(), reviewRequirementsTask, ActionFlowType.AUTOMATIC);

        //Segundo nodo
        addTaskAction(reviewRequirementsTask, TaskType.REVIEW_REQUIREMENTS.getCode(), enablePaymentTask, ActionFlowType.AUTOMATIC);
        addTaskAction(reviewRequirementsTask, TaskType.OBSERVATIONS.getCode(), requestProcessTask, ActionFlowType.FORCE_GATE_ENTRY);

        //Tercer nodo
        addTaskAction(enablePaymentTask, TaskType.ENABLE_PAYMENT.getCode(), validationPaymentTask, ActionFlowType.AUTOMATIC);
        addTaskAction(enablePaymentTask, TaskType.OBSERVATIONS.getCode(), reviewRequirementsTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(enablePaymentTask, TaskType.OBSERVATIONS.getCode(), requestProcessTask, ActionFlowType.FORCE_GATE_ENTRY);

        addTaskAction(validationPaymentTask, TaskType.VALIDATION_PAYMENT.getCode(), signatureDocumentsTask, ActionFlowType.AUTOMATIC);
        addTaskAction(validationPaymentTask, TaskType.OBSERVATIONS.getCode(), enablePaymentTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(validationPaymentTask, TaskType.OBSERVATIONS.getCode(), reviewRequirementsTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(validationPaymentTask, TaskType.OBSERVATIONS.getCode(), requestProcessTask, ActionFlowType.FORCE_GATE_ENTRY);

        addTaskAction(signatureDocumentsTask, TaskType.CONCLUDE_PROCESS.getCode(), validationDocumentsTask, ActionFlowType.AUTOMATIC);
        addTaskAction(signatureDocumentsTask, TaskType.OBSERVATIONS.getCode(), validationPaymentTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(signatureDocumentsTask, TaskType.OBSERVATIONS.getCode(), enablePaymentTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(signatureDocumentsTask, TaskType.OBSERVATIONS.getCode(), reviewRequirementsTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(signatureDocumentsTask, TaskType.OBSERVATIONS.getCode(), requestProcessTask, ActionFlowType.FORCE_GATE_ENTRY);

        addTaskAction(validationDocumentsTask, TaskType.VALIDATION_DOCUMENTS.getCode(), concludeProcessTask, ActionFlowType.AUTOMATIC);
        addTaskAction(validationDocumentsTask, TaskType.OBSERVATIONS.getCode(), signatureDocumentsTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(validationDocumentsTask, TaskType.OBSERVATIONS.getCode(), validationPaymentTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(validationDocumentsTask, TaskType.OBSERVATIONS.getCode(), enablePaymentTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(validationDocumentsTask, TaskType.OBSERVATIONS.getCode(), reviewRequirementsTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(validationDocumentsTask, TaskType.OBSERVATIONS.getCode(), requestProcessTask, ActionFlowType.FORCE_GATE_ENTRY);

        addTaskAction(concludeProcessTask, TaskType.OBSERVATIONS.getCode(), validationDocumentsTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(concludeProcessTask, TaskType.OBSERVATIONS.getCode(), signatureDocumentsTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(concludeProcessTask, TaskType.OBSERVATIONS.getCode(), validationPaymentTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(concludeProcessTask, TaskType.OBSERVATIONS.getCode(), enablePaymentTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(concludeProcessTask, TaskType.OBSERVATIONS.getCode(), reviewRequirementsTask, ActionFlowType.FORCE_GATE_ENTRY);
        addTaskAction(concludeProcessTask, TaskType.OBSERVATIONS.getCode(), requestProcessTask, ActionFlowType.FORCE_GATE_ENTRY);

        process2.setTask(requestProcessTask);
        processRepository.save(process2);
    }

    private Task createProcessTask(String taskName, String code, boolean isOutput, String relatedAreaCode) {
        var task = new Task();
        task.setName(taskName);
        task.setCode(code);
        task.setRelatedAreaCode(relatedAreaCode);
        addResource(task, isOutput);
        return task;
    }

    private void addResource(Task task, boolean isOutput) {
        var resource = new Resource();
        resource.setResourceType(ResourceType.EMPLOYEE);
        resource.setTask(task);
        resource.setOutput(isOutput);
        task.getResourceList().add(resource);
    }

    private void addResourceDocument(Task task, boolean isOutput, String code, String name, String processCode) {
        var resource = new Resource();
        resource.setResourceType(ResourceType.DOCUMENT);
        resource.setTask(task);
        resource.setOutput(isOutput);
        resource.setDocument(new ResourceDocument(code, name));
        task.getResourceList().add(resource);
    }

    private Task addTaskAction(Task task, String actionName, Task nextTask, ActionFlowType actionFlowType) {
        var taskAction = new TaskAction();
        taskAction.setName(actionName);
        taskAction.setTask(task);
        taskAction.setNextTask(nextTask);
        taskAction.setActionFlowType(actionFlowType);

        Set<TaskAction> taskActions = task.getTaskActions();
        taskActions.add(taskAction);
        return task;
    }

    private String getResourceAsString(String resourceName) {
        try (var inputStream = this.getClass().getResourceAsStream(resourceName)) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private InputStream getResourceAsStream(String resourceName) {
        try (var inputStream = this.getClass().getResourceAsStream(resourceName)) {
            return inputStream;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void createUser(String email, Employee employee, Long roleId) {
        var user = new User();
        var role = new Role();
        HashSet<Role> roles = new HashSet<>();

        user.setEmail(email);
        user.setEnabled(true);
        user.setPassword("$2a$10$XURPShQNCsLjp1ESc2laoObo9QZDhxz73hJPaEv7/cBha4pk0AgP."); //el password es: password
        user.setEmployee(employee);

        if (employee.getFirstName().equals("System")) {
            user.setSystemUser(true);
        }
        role.setId(roleId);
        roles.add(role);
        user.setRoles(roles);
        userService.save(user);
    }

    private void createEmployee(String firstName, String lastName, Set<Task> tasks, String email, Long roleId) {
        var employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employeeRepository.save(employee);
        for (Task task : tasks) {
            if (task != null) {
                List<Task> taskOfTaskCode = taskRepository.findAllByCode(task.getCode());
                if (!taskOfTaskCode.isEmpty()) {
                    for (Task taskR : taskOfTaskCode) {
                        var employeeTask = new EmployeeTask();
                        employeeTask.setTask(taskR);
                        employeeTask.setEmployee(employee);
                        employeeTaskService.save(employeeTask);
                    }
                }
            }
        }

        createUser(email, employee, roleId);
    }

    private void initializeFileType() {
        if (fileTypeService.findAll().isEmpty()) {

            createFileType("R_Diploma_Bachiller", "Requisito Diploma Bachiller", FileTypeCategory.DIPLOMA_BACHILLER);
            createFileType("R_V_Diploma_Bachiller", "Requisito Valorado Diploma Bachiller", FileTypeCategory.DIPLOMA_BACHILLER);
            createFileType("L_Diploma_Bachiller", "Legalizado Diploma Bachiller", FileTypeCategory.DIPLOMA_BACHILLER);

            createFileType("R_Titulo_Provision_Nacional", "Requisito Titulo Provision Nacional", FileTypeCategory.TITULO_PROVISION_NACIONAL);
            createFileType("R_V_Titulo_Provision_Nacional", "Requisito Valorado Titulo Provision Nacional", FileTypeCategory.TITULO_PROVISION_NACIONAL);
            createFileType("L_Titulo_Provision_Nacional", "Legalizado Titulo Provision Nacional", FileTypeCategory.TITULO_PROVISION_NACIONAL);

            createFileType("R_Diploma_Academico", "Requisito Diploma Academico", FileTypeCategory.DIPLOMA_ACADEMICO);
            createFileType("R_V_Diploma_Academico", "Requisito Valorado Diploma Academico", FileTypeCategory.DIPLOMA_ACADEMICO);
            createFileType("L_Diploma_Academico", "Legalizado Diploma Academico", FileTypeCategory.DIPLOMA_ACADEMICO);

            createFileType("R_Titulo_Postgrado", "Requisito Titulo Postgrado", FileTypeCategory.TITULO_POSTGRADO);
            createFileType("R_V_Titulo_Postgrado", "Requisito Valorado Titulo Postgrado", FileTypeCategory.TITULO_POSTGRADO);
            createFileType("L_Titulo_Postgrado", "Legalizado Titulo Postgrado", FileTypeCategory.TITULO_POSTGRADO);

            createFileType("R_RR_Homologacion_Diploma_Bachiler", "Requisito RR Homologacion Diploma Bachiller", FileTypeCategory.RR_HOMOLOGACION_DIPLOMA_BACHILLER);
            createFileType("R_V_RR_Homologacion_Diploma_Bachiler", "Requisito Valorado RR Homologacion Diploma Bachiller", FileTypeCategory.RR_HOMOLOGACION_DIPLOMA_BACHILLER);
            createFileType("L_RR_Homologacion_Diploma_Bachiler", "Legalizado RR Homologacion Diploma Bachiller", FileTypeCategory.RR_HOMOLOGACION_DIPLOMA_BACHILLER);

            createFileType("R_RR_Homologacion_Diploma_Policia", "Requisito RR Homologacion Diploma Policia", FileTypeCategory.RR_HOMOLOGACION_DIPLOMA_POLICIA);
            createFileType("R_V_RR_Homologacion_Diploma_Policia", "Requisito Valorado RR Homologacion Diploma Policia", FileTypeCategory.RR_HOMOLOGACION_DIPLOMA_POLICIA);
            createFileType("L_RR_Homologacion_Diploma_Policia", "Legalizado RR Homologacion Diploma Policia", FileTypeCategory.RR_HOMOLOGACION_DIPLOMA_POLICIA);

            createFileType("R_RR_Homologacion_Diploma_Militar", "Requisito RR Homologacion Diploma Militar", FileTypeCategory.RR_HOMOLOGACION_DIPLOMA_MILITAR);
            createFileType("R_V_RR_Homologacion_Diploma_Militar", "Requisito Valorado RR Homologacion Diploma Militar", FileTypeCategory.RR_HOMOLOGACION_DIPLOMA_MILITAR);
            createFileType("L_RR_Homologacion_Diploma_Militar", "Legalizado RR Homologacion Diploma Militar", FileTypeCategory.RR_HOMOLOGACION_DIPLOMA_MILITAR);

            createFileType("R_Documentos_Varios", "Requisito Documentos Varios", FileTypeCategory.DOCUMENTO_VARIOS);
            createFileType("R_V_Documentos_Varios", "Requisito Valorado Documentos Varios", FileTypeCategory.DOCUMENTO_VARIOS);
            createFileType("L_Documentos_Varios", "Legalizado Documentos Varios", FileTypeCategory.DOCUMENTO_VARIOS);
            createFileType("R_F_Documentos_Varios", "Requisito Fotocopia Documentos Varios", FileTypeCategory.DOCUMENTO_VARIOS);

            createFileType("I_Firma", "Firma Digital", FileTypeCategory.IMAGEN_FIRMA);
        }
    }

    private void createFileType(String abbreviation, String name, FileTypeCategory fileTypeCategory) {
        var fileType = new FileType();
        fileType.setAbbreviation(abbreviation);
        fileType.setName(name);
        fileType.setFileTypeCategory(fileTypeCategory);
        fileTypeService.save(fileType);
    }

}
