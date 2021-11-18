/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.repository;

import com.umss.siiu.core.model.Buy;

public interface BuyDelayRepository extends BuyRepository {
    default Buy getByIdDelay(Long id) {
        return getById(id);
    }
}
