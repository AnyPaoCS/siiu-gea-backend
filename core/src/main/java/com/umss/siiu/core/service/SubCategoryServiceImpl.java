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

    /*  Multicapa Spring  MVC       PHP-> employee.php   sql(injectado)+ logica negocio(tucodigo)+ http
    Controlador {1 solo Servicio}
        y si necesito mas Servicios en este controller?
            -> Servicio principal injecte todos los otros servicios que requieras
    Servicio  { 1 solo repositorio y N-Servicios(link repo)}
        -> Mesclar datos deSubCategory y Category   ... N recursos siguiendo la regla de arriba
    Repositorio (consultas de BD)  donde esa tabla sea la principal  haber joins    // no injecta otros repositorios
    */


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
        subCategory.setCompleteName(subCategory.getCode() + subCategory.getName() + category.getCode());
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
