/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.repository;

import com.umss.siiu.core.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
}