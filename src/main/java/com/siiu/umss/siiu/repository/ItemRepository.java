/**
 * @author: Edson A. Terceros T.
 */

package com.siiu.umss.siiu.repository;

import com.siiu.umss.siiu.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
