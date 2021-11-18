/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.service;

import com.umss.siiu.core.model.SubCategory;

import java.util.Set;

public interface SubCategoryService extends GenericService<SubCategory> {

    Set<SubCategory> findAllByCategoryId(Long id);
}
