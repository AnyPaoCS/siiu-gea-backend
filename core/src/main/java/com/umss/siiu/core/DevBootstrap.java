/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core;

import com.umss.siiu.core.model.Buy;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.repository.*;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private CategoryRepository categoryRepository;
    private SubCategoryRepository subCategoryRepository;
    private ItemRepository itemRepository;
    private EmployeeRepository employeeRepository;
    private PositionRepository positionRepository;
    private ContractRepository contractRepository;
    private BuyRepository buyRepository;

    public DevBootstrap(CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository,
            ItemRepository itemRepository, EmployeeRepository employeeRepository,
            PositionRepository positionRepository, ContractRepository contractRepository, BuyRepository buyRepository) {
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.itemRepository = itemRepository;
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.contractRepository = contractRepository;
        this.buyRepository = buyRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createEmployees();
        persistBuy(BigDecimal.TEN);
        persistBuy(BigDecimal.ONE);
    }

    private void persistBuy(BigDecimal value) {
        Buy buy = new Buy();
        buy.setValue(value);
        buyRepository.save(buy);
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
