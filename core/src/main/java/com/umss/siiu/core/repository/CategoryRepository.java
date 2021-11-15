/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.repository;


import com.umss.siiu.core.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
