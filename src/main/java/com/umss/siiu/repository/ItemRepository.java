/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.repository;

import com.umss.siiu.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
