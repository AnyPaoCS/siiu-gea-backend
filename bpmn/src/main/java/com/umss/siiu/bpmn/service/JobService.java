/**
 * @author: Edson A. Terceros T.
 */
package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.model.Job;
import com.umss.siiu.core.service.GenericService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobService extends GenericService<Job> {

    Page<Job> findAll(String filter, Pageable pageable);

}
