package com.umss.siiu.bpmn.repository;

import com.umss.siiu.bpmn.model.processes.Task;
import com.umss.siiu.core.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends GenericRepository<Task> {
    Task findByCode(String code);

    @Query("Select t from Task t where t.code= :code")
    List<Task> findAllByCode(String code);

}
