package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.dto.TaskInstanceDto;
import com.umss.siiu.bpmn.model.EmployeeTask;
import com.umss.siiu.bpmn.model.Job;
import com.umss.siiu.bpmn.model.JobBpm;
import com.umss.siiu.bpmn.model.JobStatus;
import com.umss.siiu.bpmn.model.processes.*;
import com.umss.siiu.bpmn.repository.JobBpmRepository;
import com.umss.siiu.core.exceptions.RepositoryValidationException;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.model.ModelBase;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.service.EmployeeService;
import com.umss.siiu.core.service.GenericServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobBpmServiceImpl extends GenericServiceImpl<JobBpm> implements JobBpmService {

    private static final String HIGH = "high";
    private static final String NORMAL = "normal";
    private static final String COMPLETE_ACTION_NAME = "DONE";
    private static final String REQUEST_PROCESS = "req_pro";

    private JobBpmRepository repository;

    private EmployeeService employeeService;
    private EmployeeTaskService employeeTaskService;
    private JobService jobService;
    private ProcessInstanceService processInstanceService;
    private TaskInstanceService taskInstanceService;
    private TaskActionService taskActionService;

    public JobBpmServiceImpl(JobBpmRepository repository, EmployeeService employeeService,
            EmployeeTaskService employeeTaskService, JobService jobService,
            ProcessInstanceService processInstanceService, TaskInstanceService taskInstanceService,
            TaskActionService taskActionService) {
        this.repository = repository;
        this.employeeService = employeeService;
        this.employeeTaskService = employeeTaskService;
        this.jobService = jobService;
        this.processInstanceService = processInstanceService;
        this.taskInstanceService = taskInstanceService;
        this.taskActionService = taskActionService;
    }

    @Override
    protected void validateSave(JobBpm model) {
        super.validateSave(model);
        String priority = model.getPriority();
        if (!priority.equals(HIGH) && !priority.equals(NORMAL)) {
            throw new RepositoryValidationException("Priority can be only High or Low");
        }
    }

    @Override
    protected GenericRepository<JobBpm> getRepository() {
        return repository;
    }

    @Override
    @Transactional
    public void allocateResources() {
        logger.debug("allocating resources");
        List<TaskInstance> unallocatedTasks = taskInstanceService.findNotAllocatedTasks(TaskStatus.PENDING, false);
        List<Employee> unassignedEmployees = employeeService.findAvailableEmployees();
        Map<Long, List<Employee>> taskEmployeeMap = mapEmployeesByTask(unassignedEmployees);
        allocateTasks(unallocatedTasks, unassignedEmployees, taskEmployeeMap);
    }

    private Map<Long, List<Employee>> mapEmployeesByTask(List<Employee> unassignedEmployees) {
        Map<Long, List<Employee>> taskEmployeeMap = new HashMap<>();
        unassignedEmployees.forEach(
                employee -> {
                    Set<EmployeeTask> employeeTasks = employeeTaskService.findByEmployee(employee);
                    employeeTasks.forEach(employeeTask -> addToTaskEmployeeMap(taskEmployeeMap, employee,
                            employeeTask.getTask()));
                });
        return taskEmployeeMap;
    }

    private void addToTaskEmployeeMap(Map<Long, List<Employee>> taskEmployeeMap, Employee employee, Task task) {
        if (taskEmployeeMap.containsKey(task.getId())) {
            taskEmployeeMap.get(task.getId()).add(employee);
        } else {
            List<Employee> employees = new ArrayList<>();
            employees.add(employee);
            taskEmployeeMap.put(task.getId(), employees);
        }
    }

    private void allocateTasks(List<TaskInstance> unallocatedTasks, List<Employee> unassignedEmployees,
            Map<Long, List<Employee>> taskEmployeeMap) {
        Map<Long, Map<String, List<TaskInstance>>> groupingByProcessId = new HashMap<>();
        unallocatedTasks.stream().map(taskInstance -> {
            taskInstance.getTask().setTaskActions(
                    taskActionService.findByTask(taskInstance.getTask()).stream().collect(Collectors.toSet()));
            return taskInstance;
        }).forEach(unallocatedTask -> processTaskAssignation(unassignedEmployees, taskEmployeeMap, unallocatedTask,
                groupingByProcessId));
    }

    private void processTaskAssignation(List<Employee> unassignedEmployees, Map<Long, List<Employee>> taskEmployeeMap,
            TaskInstance unallocatedTaskInstance,
            Map<Long, Map<String, List<TaskInstance>>> groupingByProcessId) {
        var task = unallocatedTaskInstance.getTask();
        Long taskId = task.getId();
        var processInstance = unallocatedTaskInstance.getProcessInstance();

        Employee groupingEmployee = null;
        var isGroupingEmployeeQualified = false;
        String parallelGroupingCode = task.getParallelGroupingCode();
        if (null != parallelGroupingCode) {
            if (groupingByProcessId.containsKey(processInstance.getId())) {
                Map<String, List<TaskInstance>> groupingByCode = groupingByProcessId.get(processInstance.getId());
                if (groupingByCode.containsKey(parallelGroupingCode)) {
                    List<TaskInstance> tasksGrouped = groupingByCode.get(parallelGroupingCode);
                    for (TaskInstance groupedTaskInstance : tasksGrouped) {
                        for (ResourceInstance resourceInstance : groupedTaskInstance.getResourceInstances()) {
                            if (resourceInstance.isActive()) {
                                groupingEmployee = resourceInstance.getEmployee();
                            }
                        }
                    }
                    tasksGrouped.add(unallocatedTaskInstance);
                    // override employee and use if it has privileges
                    if (groupingEmployee != null) {

                        Set<EmployeeTask> employeeTasks = employeeTaskService.findByEmployee(groupingEmployee);
                        for (EmployeeTask groupingEmployeeTask : employeeTasks) {
                            if (groupingEmployeeTask.getTask().getId().equals(taskId)) {
                                isGroupingEmployeeQualified = true;
                                break;
                            }
                        }
                    }
                } else {
                    List<TaskInstance> tasksGrouped = new ArrayList<>();
                    tasksGrouped.add(unallocatedTaskInstance);
                    groupingByCode.put(parallelGroupingCode, tasksGrouped);
                }
            } else {
                HashMap<String, List<TaskInstance>> groupingByCode = new HashMap<>();
                ArrayList<TaskInstance> tasksGrouped = new ArrayList<>();
                tasksGrouped.add(unallocatedTaskInstance);
                groupingByCode.put(parallelGroupingCode, tasksGrouped);
                groupingByProcessId.put(processInstance.getId(), groupingByCode);
            }
        }

        List<Employee> employeesForTaskList = taskEmployeeMap.get(taskId);
        List<Employee> taskGroupedEmployeesForTaskList = new ArrayList<>();
        List<Employee> reworkEmployeeList = unallocatedTaskInstance.getProcessInstance().getTaskInstances().stream()
                .filter(taskInstance -> taskInstance.getTask().getId().equals(taskId)
                        && taskInstance.getTaskStatus().equals(TaskStatus.DONE)
                        && taskInstance.getResourceInstances().stream()
                                .anyMatch(
                                        resourceInstance -> !resourceInstance.getEmployee().getUser().getSystemUser()))
                .sorted(Comparator.comparing(ModelBase::getId))
                .map(taskInstance -> taskInstance.getResourceInstances().stream()
                        .filter(resourceInstance -> !resourceInstance.getEmployee().getUser().getSystemUser())
                        .findFirst()
                        .get().getEmployee())
                .collect(Collectors.toList());
        if (isGroupingEmployeeQualified) {
            taskGroupedEmployeesForTaskList.add(groupingEmployee);
            HashMap<Long, List<Employee>> taskEmployeeMapGrouping = new HashMap<>();
            taskEmployeeMapGrouping.put(taskId, taskGroupedEmployeesForTaskList);
            ArrayList<Employee> employeesForGroupingTaskList = new ArrayList<>(taskGroupedEmployeesForTaskList);
            assignTask(taskGroupedEmployeesForTaskList, taskEmployeeMapGrouping, unallocatedTaskInstance,
                    employeesForGroupingTaskList, groupingByProcessId);
        } else if (!CollectionUtils.isEmpty(reworkEmployeeList)) {
            HashMap<Long, List<Employee>> taskEmployeeMapRework = new HashMap<>();
            taskEmployeeMapRework.put(taskId, reworkEmployeeList);
            ArrayList<Employee> employeesForReworkTaskList = new ArrayList<>(reworkEmployeeList);
            assignTask(reworkEmployeeList, taskEmployeeMapRework, unallocatedTaskInstance, employeesForReworkTaskList,
                    groupingByProcessId);
        } else if (!CollectionUtils.isEmpty(employeesForTaskList)) {
            assignTask(unassignedEmployees, taskEmployeeMap, unallocatedTaskInstance, employeesForTaskList,
                    groupingByProcessId);
        } else {
            ArrayList<Employee> systemUserList = new ArrayList<>();
            systemUserList.add(employeeService.findSystemEmployee());
            var skipSystemReallocationAllocation = false;
            if (!CollectionUtils.isEmpty(unallocatedTaskInstance.getResourceInstances())) {
                for (ResourceInstance resourceInstance : unallocatedTaskInstance.getResourceInstances()) {
                    if (resourceInstance.isActive() && Boolean.TRUE.equals(resourceInstance.getEmployee().getUser().getSystemUser())) {
                        skipSystemReallocationAllocation = true;
                    }
                }
            }
            if (!skipSystemReallocationAllocation) {
                assignTask(systemUserList, taskEmployeeMap, unallocatedTaskInstance, systemUserList,
                        groupingByProcessId);
            }
        }
    }

    private void assignTask(List<Employee> unassignedEmployees, Map<Long, List<Employee>> taskEmployeeMap,
            TaskInstance taskInstance, List<Employee> employeesForTask,
            Map<Long, Map<String, List<TaskInstance>>> groupingByProcessId) {
        var task = taskInstance.getTask();
        var processInstance = taskInstance.getProcessInstance();
        for (Resource resource : task.getResourceList()) {
            if (resource.getResourceType().equals(ResourceType.EMPLOYEE)) {
                var employee = employeesForTask.get(0);
                boolean getSystem = employee.getUser().getSystemUser();
                if (!getSystem) {
                    employeesForTask.remove(0);
                    unassignedEmployees.remove(employee);
                    for (List<Employee> employeesByTasks : taskEmployeeMap.values()) {
                        employeesByTasks.remove(employee);
                    }
                    setJobBpmInProgress(processInstance.getJobBpm());
                }
                var resourceInstance = taskInstanceService.allocateResource(taskInstance, resource, null,
                        employee);
                String parallelGroupingCode = taskInstance.getTask().getParallelGroupingCode();
                if (parallelGroupingCode != null) {
                    for (TaskInstance groupedTaskInstance : groupingByProcessId.get(processInstance.getId())
                            .get(parallelGroupingCode)) {
                        if (groupedTaskInstance.getId().equals(taskInstance.getId())) {
                            groupedTaskInstance.getResourceInstances().add(resourceInstance);
                        }
                    }
                }
                if (getSystem && taskInstance.getTask().getCode().compareTo(REQUEST_PROCESS) == 0) {
                    taskInstance = taskInstanceService.findById(taskInstance.getId());
                    taskInstanceService.complete(taskInstance, Collections.singletonList(COMPLETE_ACTION_NAME));
                }
            } else {
                var employee = processInstance.getUser().getEmployee();
                var resourceInstance = taskInstanceService.allocateResource(taskInstance, resource, null,
                        employee);
                String parallelGroupingCode = taskInstance.getTask().getParallelGroupingCode();
                if (parallelGroupingCode != null) {
                    for (TaskInstance groupedTaskInstance : groupingByProcessId.get(processInstance.getId())
                            .get(parallelGroupingCode)) {
                        if (groupedTaskInstance.getId().equals(taskInstance.getId())) {
                            groupedTaskInstance.getResourceInstances().add(resourceInstance);
                        }
                    }
                }
                if (taskInstance.getTask().getCode().compareTo(REQUEST_PROCESS) == 0) {
                    taskInstance = taskInstanceService.findById(taskInstance.getId());
                    taskInstanceService.complete(taskInstance, Collections.singletonList(COMPLETE_ACTION_NAME));
                }
            }
        }
    }

    @Override
    @Transactional
    public List<JobBpm> createJobBpms(List<Job> jobs) {
        return jobs.stream().map(this::createJobBpm).collect(Collectors.toList());
    }

    private JobBpm createJobBpm(Job job) {
        var processInstance = processInstanceService.createProcessInstance();
        var jobBpm = new JobBpm();
        jobBpm.setProcessInstance(processInstance);
        jobBpm.setStatus(JobStatus.QUEUED.toString());
        jobBpm.setPriority(NORMAL);
        jobBpm = save(jobBpm);
        job.setJobBpm(jobBpm);
        jobService.save(job);
        return jobBpm;
    }

    @Override
    public JobBpm createJobBpm(ProcessInstance process) {
        var job = new Job();
        job = jobService.save(job);
        var job1Bpm = new JobBpm();
        job1Bpm.setProcessInstance(process);
        job1Bpm.setStatus(JobStatus.PREPARED.toString());
        job1Bpm.setPriority(NORMAL);
        job1Bpm = save(job1Bpm);
        job.setJobBpm(job1Bpm);
        job.getJobBpm().setJob(job);
        jobService.save(job);
        setJobBpmInProcessInstance(job1Bpm, process);
        return job1Bpm;
    }

    private void setJobBpmInProcessInstance(JobBpm jobBpm, ProcessInstance instance) {
        instance.setJobBpm(jobBpm);
        processInstanceService.save(instance);
    }

    private JobBpm setJobBpmInProgress(JobBpm jobBpm) {
        jobBpm.setStatus(JobStatus.IN_PROGRESS.toString());
        return save(jobBpm);
    }

    @Override
    @Transactional
    public TaskInstance changeStatus(TaskInstanceDto task, Boolean exceptionTrigger) {
        if (Boolean.TRUE.equals(exceptionTrigger)) {
            return exceptionFlow(taskInstanceService.findById(task.getId()));
        }
        return flow(task);
    }

    private TaskInstance exceptionFlow(TaskInstance task) {
        if (task.getTask().getCode().equals(TaskType.VALIDATION_DOCUMENTS.getCode())) {
            restartProcess(task);
            taskInstanceService.createTaskInstance(task.getProcessInstance().getProcess(), task.getProcessInstance());
            return taskInstanceService.findById(task.getId());
        }
        throw new IllegalArgumentException(
                String.format("%s does not have an exception flow", task.getTask().getName()));
    }

    private void restartProcess(TaskInstance task) {
        task.getProcessInstance().getTaskInstances().stream()
                .forEach(instance -> taskInstanceService.complete(instance, Collections.emptyList()));
    }

    private TaskInstance flow(TaskInstanceDto task) {
        var currentTask = taskInstanceService.findById(task.getId());
        taskInstanceService.validateChangeStatus(currentTask.getTaskStatus(), task.getTaskStatus());
        if (task.getTaskStatus().equals(TaskStatus.DONE)) {
            finishWith(currentTask, task.getActionNames());
        }
        if (task.getTaskStatus().equals(TaskStatus.IN_PROGRESS) || task.getTaskStatus().equals(TaskStatus.ON_HOLD)) {
            currentTask.setTaskStatus(task.getTaskStatus());
            currentTask = taskInstanceService.save(currentTask);
        }
        updateJobStatus(currentTask);
        return currentTask;
    }

    private void finishWith(TaskInstance task, List<String> actions) {
        taskInstanceService.complete(task, actions);
    }

    private void updateJobStatus(TaskInstance taskInstance) {
        Map<TaskStatus, List<TaskInstance>> statusListMap = taskInstanceService
                .findByProcessInstance(taskInstance.getProcessInstance()).stream()
                .collect(Collectors.groupingBy(TaskInstance::getTaskStatus));
        var jobBpm = taskInstance.getProcessInstance().getJobBpm();
        if (statusListMap.containsKey(TaskStatus.ON_HOLD)) {
            jobBpm.setStatus(TaskStatus.ON_HOLD.toString());
        } else if (statusListMap.containsKey(TaskStatus.IN_PROGRESS) || statusListMap.containsKey(TaskStatus.PENDING)
                || statusListMap.containsKey(TaskStatus.ALLOCATED)) {
            jobBpm.setStatus(TaskStatus.IN_PROGRESS.toString());
        } else {
            jobBpm.setCompletedTime(LocalDateTime.now());
            jobBpm.setStatus(TaskStatus.DONE.toString());
        }
        save(jobBpm);
    }

    @Override
    public JobBpm findByJobId(long jobId) {
        return repository.findByJobId(jobId);
    }

    @Override
    public List<String> getAssigneesByProcessInstanceId(Long id) {
        var processInstance = new ProcessInstance();
        processInstance.setId(id);
        return assigneesFrom(taskInstanceService.findByProcessInstance(processInstance));
    }

    private List<String> assigneesFrom(List<TaskInstance> tasks) {
        return tasks.parallelStream().map(
                task -> formatAssignation(taskInstanceService.getEmployeeOf(task.getId()), task.getTask().getName()))
                .sorted().collect(Collectors.toList());
    }

    private String formatAssignation(Optional<Employee> employee, String name) {
        if (employee.isPresent()) {
            return String.format("%s >> %s", name, employee.get().getFullName(false));
        }
        return "";
    }

    @Override
    public List<JobBpm> findByUserEmail(String email) {
        var employee = employeeService.findByEmail(email);
        if (employee != null) {
            List<ProcessInstance> processInstances = processInstanceService
                    .findByUserEmail(employee.getUser().getEmail());
            return processInstances.parallelStream().map(this::findByProcessInstance).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public JobBpm findByProcessInstance(ProcessInstance processInstance) {
        return repository.findByProcessInstance(processInstance);
    }

    @Override
    public List<JobBpm> findByTaskAssigned(String email) {
        var employee = employeeService.findByEmail(email);
        List<JobBpm> result = new ArrayList<>();
        if (!email.isEmpty()) {
            List<JobBpm> jobBpms = this.findAll();
            for (JobBpm jobBpm : jobBpms) {
                Long jobId = jobBpm.getJob().getId();
                var taskInstance = taskInstanceService.findByJobIdAndEmployeeId(jobId, employee.getId());
                if (taskInstance != null) {
                    result.add(jobBpm);
                }
            }
        }
        return result;
    }

}
