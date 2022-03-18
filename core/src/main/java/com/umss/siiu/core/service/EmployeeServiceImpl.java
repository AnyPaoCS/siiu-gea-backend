/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.service;

import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.repository.EmployeeRepository;
import com.umss.siiu.core.repository.GenericRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("EmployeeService")
public class EmployeeServiceImpl extends GenericServiceImpl<Employee> implements EmployeeService {

    private EmployeeRepository repository;

    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Employee> findAvailableEmployees() {
        return repository.findByAvailableTrue();
    }

    /*@Override
    public List<Employee> findByTasksId(long taskId) {
        return repository.findByEmployeeTasksIs(taskId);
    }*/

    @Override
    protected GenericRepository<Employee> getRepository() {
        return repository;
    }

    @Override
    public Employee findByEmail(String email) {
        return repository.findByUserEmail(email);
    }

    @Override
    public Employee findSystemEmployee() {
        return repository.findByUserSystemUserTrue();
    }

    @Override
    public String parseName(String name) {
        return name.toUpperCase();
    }
}
