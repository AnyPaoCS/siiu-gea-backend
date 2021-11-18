/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.controller;

import com.umss.siiu.core.dto.CategoryDto;
import com.umss.siiu.core.model.Category;
import com.umss.siiu.core.service.CategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController extends GenericController<Category, CategoryDto> {
    private CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @Override
    protected CategoryService getService() {
        return service;
    }
}
