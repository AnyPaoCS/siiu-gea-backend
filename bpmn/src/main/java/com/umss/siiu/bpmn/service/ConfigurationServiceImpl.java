/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.service;

import com.umss.siiu.core.model.Configuration;
import com.umss.siiu.core.repository.ConfigurationRepository;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.service.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigurationServiceImpl extends GenericServiceImpl<Configuration> implements ConfigurationService {

    private ConfigurationRepository repository;

    public ConfigurationServiceImpl(ConfigurationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Configuration> findByTypeAndEntryKeyAndActiveTrue(String type, String entryKey) {
        return repository.findByTypeAndEntryKeyAndActiveTrue(type, entryKey);
    }

    @Override
    public List<Configuration> findByTypeAndActiveTrue(String type) {
        return repository.findByTypeAndActiveTrue(type);
    }

    @Override
    protected GenericRepository<Configuration> getRepository() {
        return repository;
    }

    @Override
    public List<Configuration> findByTypeNotLikeAndActiveTrue(String type) {
        return repository.findByTypeNotLikeAndActiveTrueOrderByEntryKeyAsc(type);
    }
}
