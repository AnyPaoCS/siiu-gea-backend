package com.umss.siiu.core.repository;

import com.umss.siiu.core.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void saveTest() {
        Category category = new Category();
        String expectedCode = "TR-1";
        String expectedCategoryName = "LEGALIZACION";
        category.setCode(expectedCode);
        category.setName(expectedCategoryName);
        Category categoryPersisted = categoryRepository.save(category);
        assertEquals(expectedCategoryName, categoryPersisted.getName());
        assertEquals(expectedCode, categoryPersisted.getCode());
        System.out.println("count= " + categoryRepository.count());
        assertTrue(categoryRepository.count() == 1);
        assertEquals(1, categoryRepository.count());
        assertEquals(0, categoryPersisted.getVersion());
    }


}
