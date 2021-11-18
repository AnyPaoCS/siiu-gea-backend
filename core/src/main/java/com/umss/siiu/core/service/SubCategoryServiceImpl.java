/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.service;

import com.umss.siiu.core.model.SubCategory;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.repository.SubCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SubCategoryServiceImpl extends GenericServiceImpl<SubCategory> implements SubCategoryService {
    private final SubCategoryRepository repository;

    public SubCategoryServiceImpl(SubCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    protected GenericRepository<SubCategory> getRepository() {
        return repository;
    }

    @Override
    public Set<SubCategory> findAllByCategoryId(Long id) {
        return new HashSet<>(repository.findAllByCategoryId(id));
    }
}
