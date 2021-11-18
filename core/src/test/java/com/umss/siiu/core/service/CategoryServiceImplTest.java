package com.umss.siiu.core.service;

import com.umss.siiu.core.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;

@SpringBootTest
public class CategoryServiceImplTest {

    @Autowired
    CategoryServiceImpl categoryServiceImpl;


    @Test
    public void testFindById() {
        Category result = categoryServiceImpl.findById(Long.valueOf(1));
        Assert.assertEquals(result, new Category());
    }

    @Test
    public void testSave() {
        Category category = new Category();
        category.setCode("test_ABC");
        category.setName("test_ABC");
        Category result = categoryServiceImpl.save(new Category());
        Assert.assertNotNull(result.getId());
    }
}
