package com.umss.siiu.bpmn.repository;

import com.umss.siiu.bpmn.model.EmployeeTask;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.repository.GenericRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface EmployeeTaskRepository extends GenericRepository<EmployeeTask> {
    Set<EmployeeTask> findByEmployee(Employee employee);

    @Modifying
    @Query("DELETE FROM EmployeeTask WHERE employee.id = :employeeId and task.id in :idsTask")
    void deleteTaskEmployeeIdAndListTaskId(Long employeeId , List<Long> idsTask);
}
