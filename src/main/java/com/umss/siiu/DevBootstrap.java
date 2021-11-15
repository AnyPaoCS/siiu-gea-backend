/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu;

import com.umss.siiu.model.Employee;
import com.umss.siiu.repository.*;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private CategoryRepository categoryRepository;
    private SubCategoryRepository subCategoryRepository;
    private ItemRepository itemRepository;
    private EmployeeRepository employeeRepository;
    private PositionRepository positionRepository;
    private ContractRepository contractRepository;

    public DevBootstrap(CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository,
            ItemRepository itemRepository, EmployeeRepository employeeRepository,
            PositionRepository positionRepository, ContractRepository contractRepository) {
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.itemRepository = itemRepository;
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.contractRepository = contractRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createEmployees();
    }

    private void createEmployees() {
        createEmployee("edson", "terceros");
        createEmployee("valentin", "laime");
    }

    private void createEmployee(String name, String lastName) {
        Employee employee = new Employee();
        employee.setFirstName(name);
        employee.setLastName(lastName);
        employeeRepository.save(employee);
    }
}
