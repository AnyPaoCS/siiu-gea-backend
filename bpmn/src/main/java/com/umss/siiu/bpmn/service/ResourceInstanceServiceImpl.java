/*
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.dto.ResourceInstanceDto;
import com.umss.siiu.bpmn.model.processes.ResourceInstance;
import com.umss.siiu.bpmn.repository.ResourceInstanceRepository;
import com.umss.siiu.core.dto.DtoBase;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.service.EmployeeService;
import com.umss.siiu.core.service.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceInstanceServiceImpl extends GenericServiceImpl<ResourceInstance> implements ResourceInstanceService {

    private ResourceInstanceRepository repository;
    private EmployeeService employeeService;

    public ResourceInstanceServiceImpl(ResourceInstanceRepository repository, EmployeeService employeeService) {
        this.repository = repository;
        this.employeeService = employeeService;
    }

    @Override
    protected GenericRepository<ResourceInstance> getRepository() {
        return repository;
    }

    @Override
    public List<ResourceInstance> findByEmployeeAndActive(Employee employee) {
        return repository.findByEmployeeAndActiveTrue(employee);
    }

    @Override
    protected void processDtoToDomainPatch(DtoBase dto, ResourceInstance updatedDomain) {
        var resourceInstanceDto = (ResourceInstanceDto) dto;
        if (null == resourceInstanceDto.getEmployeeId()) {
            updatedDomain.setEmployee(null);
        } else if (!updatedDomain.getEmployee().getId().equals(resourceInstanceDto.getEmployeeId())) {
            updatedDomain.setEmployee(employeeService.findById(resourceInstanceDto.getEmployeeId()));
        }
    }

    @Override
    public List<ResourceInstance> findByTaskInstanceId(Long id) {
        return repository.findByTaskInstanceId(id);
    }
}
