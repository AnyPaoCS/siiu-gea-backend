package com.umss.siiu.bpmn.repository;

import com.umss.siiu.bpmn.model.processes.ProcessInstance;
import com.umss.siiu.bpmn.model.processes.Task;
import com.umss.siiu.bpmn.model.processes.TaskInstance;
import com.umss.siiu.bpmn.model.processes.TaskStatus;
import com.umss.siiu.core.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskInstanceRepository extends GenericRepository<TaskInstance> {

    @Query("SELECT taskInstance FROM TaskInstance taskInstance "
            + "LEFT JOIN taskInstance.processInstance processInstance "
            + "LEFT JOIN processInstance.jobBpm jobBpm "
            + "LEFT JOIN jobBpm.job job "
            + "WHERE taskInstance.taskStatus = :taskStatus " +
            "AND processInstance.manual = :manual " +
            "order by jobBpm.priority asc ")
    List<TaskInstance> findByHasDownloadedTemplateTrueAndTaskStatusAndProcessInstanceManualOrderByPriority(
            @Param("taskStatus") TaskStatus taskStatus, @Param("manual") boolean manual);

    @Query("SELECT t FROM TaskInstance t JOIN FETCH t.resourceInstances WHERE t.id = (:id)")
    TaskInstance findByIdWithResources(@Param("id") Long id);

    List<TaskInstance> findByTaskStatus(TaskStatus taskStatus);

    List<TaskInstance> findByProcessInstanceAndTaskInAndTaskStatusIsNot(ProcessInstance processInstance,
            List<Task> tasks, TaskStatus taskStatus);

    List<TaskInstance> findByProcessInstance(ProcessInstance processInstance);

    List<TaskInstance> findByProcessInstanceAndTask(ProcessInstance processInstance, Task task);

    List<TaskInstance> findByTaskCodeAndProcessInstanceJobBpmJobId(String taskCode, long jobId);

    List<TaskInstance> findByTaskCodeNotAndProcessInstanceJobBpmJobIdAndTaskStatusNot(String taskCode, long jobId,
            String taskStatus);

    List<TaskInstance> findByProcessInstanceId(Long processInstanceId);
}
