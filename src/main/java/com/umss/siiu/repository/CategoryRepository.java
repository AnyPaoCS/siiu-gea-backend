/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.repository;


import com.umss.siiu.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
