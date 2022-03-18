/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.model.processes.Task;
import com.umss.siiu.bpmn.model.processes.TaskAction;
import com.umss.siiu.bpmn.repository.TaskActionRepository;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.service.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskActionServiceImpl extends GenericServiceImpl<TaskAction> implements TaskActionService {

    private TaskActionRepository repository;

    public TaskActionServiceImpl(TaskActionRepository repository) {
        this.repository = repository;
    }

    @Override
    protected GenericRepository<TaskAction> getRepository() {
        return repository;
    }

    @Override
    public List<TaskAction> findByNextTask(Task nexTask) {
        return repository.findByNextTask(nexTask);
    }

    @Override
    public List<TaskAction> findByTask(Task task) {
        return repository.findByTask(task);
    }

}
