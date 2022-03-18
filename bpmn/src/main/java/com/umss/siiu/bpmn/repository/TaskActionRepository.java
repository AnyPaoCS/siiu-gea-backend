package com.umss.siiu.bpmn.repository;

import com.umss.siiu.bpmn.model.processes.Task;
import com.umss.siiu.bpmn.model.processes.TaskAction;
import com.umss.siiu.core.repository.GenericRepository;

import java.util.List;
import java.util.Set;

public interface TaskActionRepository extends GenericRepository<TaskAction> {
    List<TaskAction> findByNextTask(Task nexTask);

    Set<TaskAction> findAllByTaskCode(String taskCode);

    List<TaskAction> findByTask(Task task);
}
