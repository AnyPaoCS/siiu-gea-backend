/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core;

import com.umss.siiu.core.model.*;
import com.umss.siiu.core.model.market.ItemInstance;
import com.umss.siiu.core.repository.*;
import com.umss.siiu.core.service.*;
import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

//@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private CategoryRepository categoryRepository;
    private SubCategoryRepository subCategoryRepository;
    private ItemRepository itemRepository;
    private EmployeeRepository employeeRepository;
    private PositionRepository positionRepository;
    private ContractRepository contractRepository;
    private BuyRepository buyRepository;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final ItemService itemService;
    private final ItemInstanceService itemInstanceService;

    SubCategory beverageSubCat = null;

    public DevBootstrap(CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository,
            ItemRepository itemRepository, EmployeeRepository employeeRepository,
            PositionRepository positionRepository, ContractRepository contractRepository, BuyRepository buyRepository
            , CategoryService categoryService, SubCategoryService subCategoryService, ItemService itemService,
            ItemInstanceService itemInstanceService, UserService userService) {
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.itemRepository = itemRepository;
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.contractRepository = contractRepository;
        this.buyRepository = buyRepository;
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
        this.itemService = itemService;
        this.itemInstanceService = itemInstanceService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createEmployees();
        persistBuy(BigDecimal.TEN);
        persistBuy(BigDecimal.ONE);
        persistCategoriesAndSubCategories();
        Item maltinItem = persistItems(beverageSubCat);
        persistItemInstances(maltinItem);
    }

    private void persistItemInstances(Item maltinItem) {
        ItemInstance maltinItem1 = createItem(maltinItem, "SKU-77721106006158", 5D);
        ItemInstance maltinItem2 = createItem(maltinItem, "SKU-77721106006159", 5D);
        ItemInstance maltinItem3 = createItem(maltinItem, "SKU-77721106006160", 5D);
        ItemInstance maltinItem4 = createItem(maltinItem, "SKU-77721106006161", 5D);
        itemInstanceService.save(maltinItem1);
        itemInstanceService.save(maltinItem2);
        itemInstanceService.save(maltinItem3);
        itemInstanceService.save(maltinItem4);
    }

    private ItemInstance createItem(Item maltinItem, String sku, double price) {
        ItemInstance itemInstance = new ItemInstance();
        itemInstance.setItem(maltinItem);
        itemInstance.setFeatured(true);
        itemInstance.setPrice(price);
        itemInstance.setIdentifier(sku);
        return itemInstance;
    }

    private Item persistItems(SubCategory subCategory) {
        Item item = new Item();
        item.setCode("B-MALTIN");
        item.setName("MALTIN");
        item.setSubCategory(subCategory);
        /*try {
            item.setImage(ImageUtils.inputStreamToByteArray(getResourceAsStream("/images/maltin.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return itemService.save(item);
    }

    private String getResourceAsString(String resourceName) {
        try (InputStream inputStream = this.getClass().getResourceAsStream(resourceName)) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private InputStream getResourceAsStream(String resourceName) {
        try (InputStream inputStream = this.getClass().getResourceAsStream(resourceName)) {
            return inputStream;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void persistCategoriesAndSubCategories() {
        Category category = persistCategory();
        persistSubCategory("SUBCAT1-NAME", "SUBCAT1-CODE", category);
        beverageSubCat = persistSubCategory("BEVERAGE", "BEVERAGE-CODE", category);
    }

    private Category persistCategory() {
        Category category = new Category();
        category.setName("CAT1-NAME");
        category.setCode("CAT1-CODE");
        return categoryService.save(category);
    }

    private SubCategory persistSubCategory(String name, String code, Category category) {
        SubCategory subCategory = new SubCategory();
        subCategory.setName(name);
        subCategory.setCode(code);
        subCategory.setCategory(category);
        return subCategoryService.save(subCategory);
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
