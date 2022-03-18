/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.model.EmployeeTask;
import com.umss.siiu.bpmn.repository.EmployeeTaskRepository;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.service.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class EmployeeTaskServiceImpl extends GenericServiceImpl<EmployeeTask> implements EmployeeTaskService {

    private EmployeeTaskRepository repository;

    public EmployeeTaskServiceImpl(EmployeeTaskRepository repository) {
        this.repository = repository;
    }

    @Override
    protected GenericRepository<EmployeeTask> getRepository() {
        return repository;
    }

    @Override
    public Set<EmployeeTask> findByEmployee(Employee employee) {
        return repository.findByEmployee(employee);
    }
}
