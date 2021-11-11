/**
 * @author: Edson A. Terceros T.
 */

package com.siiu.umss.siiu.repository;

import com.siiu.umss.siiu.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
}
