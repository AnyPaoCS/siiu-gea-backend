/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.repository;

import com.umss.siiu.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
}
