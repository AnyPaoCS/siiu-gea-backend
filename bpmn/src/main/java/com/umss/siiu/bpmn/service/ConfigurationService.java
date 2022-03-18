/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.service;

import com.umss.siiu.core.model.Configuration;
import com.umss.siiu.core.service.GenericService;

import java.util.List;

public interface ConfigurationService extends GenericService<Configuration> {
    List<Configuration> findByTypeAndEntryKeyAndActiveTrue(String type, String entryKey);

    List<Configuration> findByTypeAndActiveTrue(String type);

    List<Configuration> findByTypeNotLikeAndActiveTrue(String type);
}
