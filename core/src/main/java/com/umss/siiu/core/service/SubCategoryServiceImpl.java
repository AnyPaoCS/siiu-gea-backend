/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.service;

import com.umss.siiu.core.model.Category;
import com.umss.siiu.core.model.SubCategory;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.repository.SubCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class SubCategoryServiceImpl extends GenericServiceImpl<SubCategory> implements SubCategoryService {
    private final SubCategoryRepository repository;
    private final CategoryService categoryService;

    public SubCategoryServiceImpl(SubCategoryRepository repository, CategoryService categoryService) {
        this.repository = repository;
        this.categoryService = categoryService;
    }

    @Override
    public SubCategory save(SubCategory model) {
        SubCategory subCategory = super.save(model);
        Category category = subCategory.getCategory();
        if (category != null && category.getId() != null) {
            subCategory.setCategory(categoryService.findById(category.getId()));
        }
        return subCategory;
    }

    @Override
    protected GenericRepository<SubCategory> getRepository() {
        return repository;
    }

    @Override
    public Set<SubCategory> findAllByCategoryId(Long id) {
        return new HashSet<>(repository.findAllByCategoryId(id));
    }

    @Override
    public Set<SubCategory> findAllById(Long id) {
        return new HashSet<>(repository.findAllById(Collections.singleton(id)));
    }
}
