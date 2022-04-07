package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.model.processes.ActionFlowType;
import com.umss.siiu.bpmn.model.processes.Task;
import com.umss.siiu.bpmn.model.processes.TaskAction;
import com.umss.siiu.bpmn.repository.TaskActionRepository;
import com.umss.siiu.bpmn.repository.TaskRepository;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.service.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl extends GenericServiceImpl<Task> implements TaskService {

    private TaskRepository repository;
    private TaskActionRepository taskActionRepository;

    public TaskServiceImpl(TaskRepository repository, TaskActionRepository taskActionRepository) {
        this.repository = repository;
        this.taskActionRepository = taskActionRepository;
    }

    @Override
    public Task findByCode(String code) {
        return repository.findByCode(code);
    }

    @Override
    public List<Task> findAll() {
        return repository.findAll().stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<Task> findAssociatedTasksByTaskCode(String taskCode) {
        Set<TaskAction> taskActions = taskActionRepository.findAllByTaskCode(taskCode);
        List<Task> tasks = new ArrayList<>();
        getTasks(taskActions, tasks);
        return tasks;
    }

    /**
     * Recursive function to get all the associated tasks of a specific task
     */
    private void getTasks(Set<TaskAction> taskActions, List<Task> tasks) {
        for (TaskAction taskAction : taskActions) {
            if (taskAction.getActionFlowType().equals(ActionFlowType.AUTOMATIC)) {
                Task task = taskAction.getNextTask();
                if (task.getTaskActions() != null && !task.getTaskActions().isEmpty() && !tasks.contains(task)) {
                    tasks.add(task);
                    getTasks(task.getTaskActions(), tasks);
                }
            }
        }
    }

    @Override
    public List<Task> findAllTaskWithCode(String code) {
        return repository.findAllByCode(code);
    }

    @Override
    protected GenericRepository<Task> getRepository() {
        return repository;
    }

}
