/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.repository;

import com.umss.siiu.core.model.Buy;

import java.math.BigDecimal;

public interface BuyRepository extends GenericRepository<Buy> {
    Buy findAllByValue(BigDecimal value);
}
