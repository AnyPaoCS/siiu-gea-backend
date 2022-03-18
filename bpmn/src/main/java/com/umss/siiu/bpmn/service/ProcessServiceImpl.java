/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.model.processes.Process;
import com.umss.siiu.bpmn.repository.ProcessRepository;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.service.GenericServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ProcessServiceImpl extends GenericServiceImpl<Process> implements ProcessService {

    private ProcessRepository repository;

    public ProcessServiceImpl(ProcessRepository repository) {
        this.repository = repository;
    }

    @Override
    protected GenericRepository<Process> getRepository() {
        return repository;
    }

    @Override
    public Process findByName(String name) {
        return repository.findByName(name);
    }
}
