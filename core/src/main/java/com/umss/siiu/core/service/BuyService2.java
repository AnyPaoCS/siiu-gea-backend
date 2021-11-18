/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.service;

import com.umss.siiu.core.model.Buy;
import com.umss.siiu.core.repository.BuyRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BuyService2 {
    private final BuyRepository repository;

    public BuyService2(@Qualifier("buyRepository") BuyRepository repository) {
        this.repository = repository;
    }

    public Buy getById(Long id) {
        return repository.getById(id);
    }
}
