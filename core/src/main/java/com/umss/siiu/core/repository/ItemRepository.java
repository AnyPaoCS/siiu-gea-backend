/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.repository;

import com.umss.siiu.core.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
