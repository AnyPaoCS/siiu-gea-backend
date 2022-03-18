package com.umss.siiu.core.repository;

import com.umss.siiu.core.model.Configuration;

import java.util.List;

public interface ConfigurationRepository extends GenericRepository<Configuration> {
    List<Configuration> findByTypeAndEntryKeyAndActiveTrue(String type, String entryKey);

    List<Configuration> findByTypeAndValueAndActiveTrue(String type, String value);

    List<Configuration> findByTypeAndActiveTrue(String type);

    List<Configuration> findByTypeNotLikeAndActiveTrueOrderByEntryKeyAsc(String type);
}
