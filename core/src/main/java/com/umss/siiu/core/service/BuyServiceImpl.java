/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.service;

import com.umss.siiu.core.model.Buy;
import com.umss.siiu.core.repository.BuyRepository;
import com.umss.siiu.core.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BuyServiceImpl extends GenericServiceImpl<Buy> implements BuyService {
    private final BuyRepository repository;

    public BuyServiceImpl(@Qualifier("buyRepository") BuyRepository repository) {
        this.repository = repository;
    }

    @Override
    protected GenericRepository<Buy> getRepository() {
        return repository;
    }
}
