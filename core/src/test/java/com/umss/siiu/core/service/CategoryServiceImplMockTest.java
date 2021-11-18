package com.umss.siiu.core.service;

import com.umss.siiu.core.model.Category;
import com.umss.siiu.core.repository.CategoryRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class CategoryServiceImplMockTest {
    @Mock
    CategoryRepository repository;
    @Mock
    ModelMapper modelMapper;
    @Mock
    Logger logger;
    @InjectMocks
    CategoryServiceImpl categoryServiceImpl;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindById() {
        Category categoryI = new Category();
        categoryI.setId(-1L);
        when(repository.findById(-1L)).thenReturn(java.util.Optional.of(categoryI));
        Category result = categoryServiceImpl.findById(-1L);
        Assert.assertEquals(result, categoryI);
    }

    @Test
    public void testSave() {
        Category categoryI = new Category();
        categoryI.setId(-1L);
        when(repository.save(any())).thenReturn(categoryI);
        when(repository.findById(-1L)).thenReturn(java.util.Optional.of(categoryI));
        Category result = categoryServiceImpl.save(new Category());
        Assert.assertEquals(result, categoryI);
    }
}
