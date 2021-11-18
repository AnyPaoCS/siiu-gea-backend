/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.controller;

import com.umss.siiu.core.dto.SubCategoryDto;
import com.umss.siiu.core.model.SubCategory;
import com.umss.siiu.core.service.GenericService;
import com.umss.siiu.core.service.SubCategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subcategories")
public class SubCategoryController extends GenericController<SubCategory, SubCategoryDto> {
    private SubCategoryService service;

    public SubCategoryController(SubCategoryService service) {
        this.service = service;
    }

    @Override
    protected GenericService getService() {
        return service;
    }
}
