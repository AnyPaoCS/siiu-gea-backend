/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.repository;

import com.umss.siiu.core.model.SubCategory;

import java.util.List;

public interface SubCategoryRepository extends GenericRepository<SubCategory> {
    List<SubCategory> findAllByCategoryId(Long categoryId);
}
