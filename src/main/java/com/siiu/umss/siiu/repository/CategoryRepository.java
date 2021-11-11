/**
 * @author: Edson A. Terceros T.
 */

package com.siiu.umss.siiu.repository;


import com.siiu.umss.siiu.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
