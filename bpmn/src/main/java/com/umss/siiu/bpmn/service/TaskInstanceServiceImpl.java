/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.model.EmployeeTask;
import com.umss.siiu.bpmn.model.JobBpm;
import com.umss.siiu.bpmn.model.processes.Process;
import com.umss.siiu.bpmn.model.processes.*;
import com.umss.siiu.bpmn.repository.TaskInstanceRepository;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.model.User;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.service.EmployeeService;
import com.umss.siiu.core.service.GenericServiceImpl;
import com.umss.siiu.core.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskInstanceServiceImpl extends GenericServiceImpl<TaskInstance> implements TaskInstanceService {

    private static final String COMPLETE_ACTION_NAME = "DONE";
    private static final String STATUS_CHANGE_MSG = "You can't change the task status manually from state %s";
    private static final String SYSTEM_EMPLOYEE_NAME = "System";
    private static final String REQUEST_PROCESS = "req_pro";

    private TaskInstanceRepository repository;

    private EmployeeService employeeService;
    private EmployeeTaskService employeeTaskService;
    private JobService jobService;
    private ResourceInstanceService resourceInstanceService;
    private TaskActionService taskActionService;
    private UserService userService;
    private NotificationService notificationService;

    public TaskInstanceServiceImpl(TaskInstanceRepository repository, EmployeeService employeeService,
            EmployeeTaskService employeeTaskService, JobService jobService,
            ResourceInstanceService resourceInstanceService, TaskActionService taskActionService,
            UserService userService, NotificationService notificationService) {
        this.repository = repository;
        this.employeeService = employeeService;
        this.employeeTaskService = employeeTaskService;
        this.jobService = jobService;
        this.resourceInstanceService = resourceInstanceService;
        this.taskActionService = taskActionService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @Override
    public TaskInstance findById(Long id) {
        TaskInstance model = super.findById(id);
        model.setResourceInstances(resourceInstanceService.findByTaskInstanceId(id));
        return model;
    }

    @Override
    @Transactional
    public TaskInstance reassignResources(long taskInstanceId, long employeeId) {
        TaskInstance taskInstance = findById(taskInstanceId);
        taskInstance.setTaskStatus(TaskStatus.PENDING);
        deAllocateResources(taskInstance);
        taskInstance = findById(taskInstanceId);

        if (employeeId > 0) {
            Long taskId = taskInstance.getTask().getId();
            Employee employee = employeeService.findById(employeeId);
            Set<EmployeeTask> employeeTasks =
                    employeeTaskService.findByEmployee(employee);
            if (employeeTasks.stream().anyMatch(employeeTask -> employeeTask.getTask().getId().equals(taskId))) {
                allocateResource(taskInstance, taskInstance.getResourceInstances().iterator().next().getResource(),
                        taskInstance.getResourceInstances().iterator().next(), employeeService.findById(employeeId));
            } else {
                throw new RuntimeException(String.format("The employee %s does not have the skill to %s1",
                        employee.getFullName(false), taskInstance.getTask().getName()));
            }
        }
        return taskInstance;
    }

    @Override
    public TaskInstance createTaskInstance(Process defaultJobProcess, ProcessInstance processInstance) {
        TaskInstance taskInstance = new TaskInstance();
        taskInstance.setTask(processInstance.getProcess().getTask());
        taskInstance.setTaskStatus(TaskStatus.PENDING);
        taskInstance.setProcessInstance(processInstance);
        return save(taskInstance);
    }

    @Override
    public TaskInstance findByJobIdAndEmployeeId(Long jobId, Long employeeId) {
        TaskInstance task = findAssignedTask(jobId, employeeId);
        if (task != null) {
            return task;
        }
        throw new NoSuchElementException(
                String.format("User by id %s is not assigned to a task in job by id %s", jobId, employeeId));
    }

    @Override
    public TaskInstance findByCodeAndJobId(String taskCode, Long jobId) {
        List<TaskInstance> taskInstances = repository.findByTaskCodeAndProcessInstanceJobBpmJobId(taskCode, jobId);
        return taskInstances != null && !taskInstances.isEmpty() ? taskInstances.get(taskInstances.size() - 1) : null;
    }

    @Override
    public boolean isUserAssigneed(Long id, String username) {
        Employee employee = getEmployee(username);
        TaskInstance task = findById(id);
        if (task != null && employee != null) {
            ResourceInstance fetched = task.getResourceInstances().stream()
                    .filter(resource -> resource.getEmployee().getId().equals(employee.getId())).findFirst().orElse(null);
            return fetched != null;
        }
        return false;

    }

    private Employee getEmployee(String username) {
        User user = userService.findByEmail(username);
        if (user != null) {
            return user.getEmployee();
        }
        return null;
    }

    private TaskInstance findAssignedTask(Long jobId, Long employeeId) {
        return fetchInResource(jobService.findById(jobId).getJobBpm().getProcessInstance().getTaskInstances(),
                employeeId);
    }

    private TaskInstance fetchInResource(List<TaskInstance> tasks, Long employeeId) {
        return tasks.stream().filter(task -> isEmployeeAssigned(task.getResourceInstances(), employeeId)
                && !task.getTaskStatus().equals(TaskStatus.DONE)).findAny().orElse(null);
    }

    private Boolean isEmployeeAssigned(List<ResourceInstance> resources, Long employeeId) {
        return resources.stream().filter(resource -> resource.getEmployee().getId().equals(employeeId)).findAny()
                .orElse(null) != null;
    }

    @Override
    public void validateChangeStatus(TaskStatus currentTaskStatus, TaskStatus nextTaskStatus) {
        if (!isTransitional(currentTaskStatus)) {
            throw new ValidationException(String.format(STATUS_CHANGE_MSG, currentTaskStatus));
        }
        if (isIntermidiateStatus(nextTaskStatus)) {
            throw new ValidationException(
                    String.format("%s %s to state %s", STATUS_CHANGE_MSG, currentTaskStatus, nextTaskStatus));
        }
        if (nextTaskStatus.equals(currentTaskStatus)) {
            throw new ValidationException(String.format("The task is already in state %s", currentTaskStatus));
        }
    }

    private boolean isIntermidiateStatus(TaskStatus nextTaskStatus) {
        return nextTaskStatus.equals(TaskStatus.ALLOCATED) || nextTaskStatus.equals(TaskStatus.PENDING);
    }

    private boolean isTransitional(TaskStatus currentTaskStatus) {
        return currentTaskStatus.equals(TaskStatus.IN_PROGRESS) || currentTaskStatus.equals(TaskStatus.ON_HOLD)
                || currentTaskStatus.equals(TaskStatus.ALLOCATED);
    }

    @Override
    public void complete(TaskInstance taskInstance, List<String> actionNames) {
        List<TaskAction> taskActions = setUpTaskActions(taskInstance, actionNames);
        taskInstance.complete();
        for (TaskAction taskAction : taskActions) {
            Task nextTask = taskAction.getNextTask();
            if (null != nextTask) {
                createTaskInstance(taskInstance, taskAction);
            }
        }
        deAllocateResources(taskInstance);
    }

    private List<TaskAction> setUpTaskActions(TaskInstance taskInstance, List<String> actionNames) {
        if (!CollectionUtils.isEmpty(taskInstance.getTask().getTaskActions()) && !actionNames.isEmpty()) {
            return findTaskActionsByNames(taskInstance, actionNames);
        }
        return Collections.emptyList();
    }

    private List<TaskAction> findTaskActionsByNames(TaskInstance taskInstance, List<String> actionNames) {
        Set<TaskAction> taskActions = taskInstance.getTask().getTaskActions();
        if (actionNames.size() == 1 && actionNames.get(0).equals(COMPLETE_ACTION_NAME)) {
            return taskActions.stream()
                    .filter(taskAction -> taskAction.getActionFlowType().equals(ActionFlowType.AUTOMATIC))
                    .collect(Collectors.toList());
        } else {
            return taskActions.stream().filter(taskAction -> isNextTask(actionNames, taskAction))
                    .collect(Collectors.toList());
        }
    }

    private boolean isNextTask(List<String> actionNames, TaskAction taskAction) {
        boolean actionFound = actionNames.contains(taskAction.getName());
        if (actionFound && taskAction.getActionFlowType().equals(ActionFlowType.AUTOMATIC)) {
            throw new IllegalArgumentException(
                    String.format("the task: %s cannot be specified as target by the user", taskAction.getName()));
        }
        return actionFound;
    }

    private void createTaskInstance(TaskInstance taskInstance, TaskAction taskAction) {
        Task nextTask = taskAction.getNextTask();
        if (createNextTaskCheck(taskInstance, nextTask)) {
            boolean isRework = taskInstance.getProcessInstance().getTaskInstances().stream()
                    .anyMatch(taskInstanceItem -> taskInstanceItem.getTask().getId().equals(nextTask.getId()));
            TaskInstance newTaskInstance = new TaskInstance();
            newTaskInstance.setTaskStatus(TaskStatus.PENDING);
            newTaskInstance.setProcessInstance(taskInstance.getProcessInstance());
            newTaskInstance.setTask(nextTask);
            List<ResourceInstance> outputResourceInstances = taskInstance.getResourceInstances().stream()
                    .filter(resourceInstance -> resourceInstance.getResource().isOutput()).collect(Collectors.toList());
            save(newTaskInstance);
            List<ResourceInstance> fitNewTaskResources = outputResourceInstances.stream()
                    .filter(resourceInstance -> {
                        Set<EmployeeTask> employeeTasks =
                                employeeTaskService.findByEmployee(resourceInstance.getEmployee());
                       /* return resourceInstance.getEmployee().getTasks().stream().map(Task::getId)
                                .collect(Collectors.toList()).contains(newTaskInstance.getTask().getId());*/
                        return employeeTasks.stream().map((EmployeeTask t) -> t.getTask().getId())
                                .collect(Collectors.toList()).contains(newTaskInstance.getTask().getId());
                    })
                    .collect(Collectors.toList());
            if (!isRework && !CollectionUtils.isEmpty(fitNewTaskResources)) {
                allocateResource(newTaskInstance, fitNewTaskResources.get(0).getResource(), null,
                        outputResourceInstances.get(0).getEmployee());
            }
        }
    }

    private boolean createNextTaskCheck(TaskInstance taskInstance, Task nextTask) {
        boolean createTask;
        if (nextTask.getEntryLogicGatePolicyType().equals(EntryLogicGatePolicyType.OR)
                || nextTask.getEntryLogicGatePolicyType().equals(EntryLogicGatePolicyType.USER_SPECIFIED)) {
            boolean isNotWorkedYet = repository
                    .findByProcessInstanceAndTask(taskInstance.getProcessInstance(), nextTask).stream()
                    .anyMatch(persistedInstance -> persistedInstance.getTaskStatus().equals(TaskStatus.PENDING)
                            || persistedInstance.getTaskStatus().equals(TaskStatus.ALLOCATED));
            createTask = !isNotWorkedYet;
        } else if (nextTask.getEntryLogicGatePolicyType().equals(EntryLogicGatePolicyType.AND)) {
            List<TaskAction> otherPredecessorsTaskActions = taskActionService.findByNextTask(nextTask).stream()
                    .filter(predecessorTaskAction -> !predecessorTaskAction.getTask().getId()
                            .equals(taskInstance.getTask().getId()))
                    .collect(Collectors.toList());
            List<Task> otherPredecessorTasks = otherPredecessorsTaskActions.stream().map(TaskAction::getTask)
                    .collect(Collectors.toList());
            List<TaskInstance> uncompletedPredecessorTaskInstances = repository
                    .findByProcessInstanceAndTaskInAndTaskStatusIsNot(taskInstance.getProcessInstance(),
                            otherPredecessorTasks, TaskStatus.DONE);

            createTask = uncompletedPredecessorTaskInstances.isEmpty();
        } else {
            throw new UnsupportedOperationException("Complex nodes are not supported!");
        }
        return createTask;
    }

    @Override
    public List<TaskInstance> findNotAllocatedTasks(TaskStatus taskStatus, boolean manual) {
        return repository.findByHasDownloadedTemplateTrueAndTaskStatusAndProcessInstanceManualOrderByPriority(
                taskStatus, manual);
    }

    @Override
    public List<TaskInstance> findByProcessInstance(ProcessInstance processInstance) {
        return repository.findByProcessInstance(processInstance);
    }

    @Override
    public ResourceInstance allocateResource(TaskInstance taskInstance, Resource resource,
            ResourceInstance resourceInstance, Employee employee) {
        if (resourceInstance == null) {
            JobBpm jobBpm = taskInstance.getProcessInstance().getJobBpm();
            resourceInstance = createNewResourceInstance(taskInstance, resource, employee);
            // todo check if we need to send notification when system user
            notificationService.sendNotifications(employee.getUser().getEmail(), 1L, "New job assigned",
                    MessageFormat.format("Job number {0} assigned to the task of {1}",
                            jobBpm.getJob().getId(),
                            resourceInstance.getTaskInstance().getTask().getName()));
        } else {
            resourceInstance = updateResourceInstanceEmployee(taskInstance, resource, resourceInstance, employee);
            notificationService.sendNotifications(employee.getUser().getEmail(), 2L, "New job reassigned",
                    MessageFormat.format("Job number {0} reassigned to the task of {1}",
                            taskInstance.getProcessInstance().getJobBpm().getJob().getId(),
                            resourceInstance.getTaskInstance().getTask().getName()));
        }
        boolean isSystem = employee.getFirstName().equals(SYSTEM_EMPLOYEE_NAME);
        if (taskInstance.getTask().getCode().compareTo(REQUEST_PROCESS) == 0) {
            taskInstance.setTaskStatus(TaskStatus.ALLOCATED);
        }

        if (!isSystem) {
            taskInstance.setTaskStatus(TaskStatus.ALLOCATED);
        }
        assignEmployee(employee);
        save(taskInstance);
        return resourceInstance;
    }

    @Override
    protected GenericRepository<TaskInstance> getRepository() {
        return repository;
    }

    private void assignEmployee(Employee employee) {
        allocateEmployee(employee);
    }

    private void allocateEmployee(Employee employee) {
        Employee databaseEmployee = employeeService.findById(employee.getId());
        if (!employee.getFirstName().equals(SYSTEM_EMPLOYEE_NAME)) {
            databaseEmployee.setAvailable(false);
            employeeService.save(databaseEmployee);
        }
    }

    private ResourceInstance createNewResourceInstance(TaskInstance taskInstance, Resource resource,
            Employee employee) {
        ResourceInstance resourceInstance = new ResourceInstance();
        resourceInstance.setActive(true);
        resourceInstance.setTaskInstance(taskInstance);
        resourceInstance.setResource(resource);
        resourceInstance.setEmployee(employee);
        resourceInstance = resourceInstanceService.save(resourceInstance);
        return resourceInstance;
    }

    private ResourceInstance updateResourceInstanceEmployee(TaskInstance taskInstance, Resource resource,
            ResourceInstance resourceInstance, Employee employee) {
        resourceInstance.setActive(true);
        resourceInstance.setEmployee(employee);
        return resourceInstanceService.save(resourceInstance);
    }

    @Override
    public void deAllocateResources(TaskInstance taskInstance) {
        for (ResourceInstance resourceInstance : taskInstance.getResourceInstances()) {
            deAllocateResource(resourceInstance);
        }
        repository.save(taskInstance);
    }

    private void deAllocateResource(ResourceInstance resourceInstance) {
        resourceInstance.setActive(false);
        Employee employee = resourceInstance.getEmployee();
        List<ResourceInstance> otherResourceInstances = resourceInstanceService.findByEmployeeAndActive(employee)
                .stream()
                .filter(resourceInstanceElement -> !resourceInstanceElement.getId().equals(resourceInstance.getId()))
                .collect(Collectors.toList());
        if (otherResourceInstances.isEmpty()) {
            employee.setAvailable(true);
            employeeService.save(employee);
        }
    }

    @Override
    public List<TaskInstance> findByCodeNotAndJobIdAndTaskStatusNot(String taskCode, Long jobId, String taskStatus) {
        return repository.findByTaskCodeNotAndProcessInstanceJobBpmJobIdAndTaskStatusNot(taskCode, jobId, taskStatus);
    }

    @Override
    public Optional<Employee> getEmployeeOf(Long id) {
        Optional<ResourceInstance> resourceInstance = resourceInstanceService.findByTaskInstanceId(id).stream()
                .findFirst();
        return resourceInstance.map(instance -> Optional.of(instance.getEmployee()))
                .orElseGet(() -> Optional.ofNullable(null));
    }

    @Override
    public List<TaskInstance> findByProcessInstanceId(Long processInstanceId) {
        return repository.findByProcessInstanceId(processInstanceId);
    }
}
