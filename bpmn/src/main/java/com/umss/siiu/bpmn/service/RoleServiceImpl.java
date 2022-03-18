package com.umss.siiu.bpmn.service;

import com.umss.siiu.core.model.Role;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.repository.RoleRepository;
import com.umss.siiu.core.service.GenericServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends GenericServiceImpl<Role> implements RoleService {

    private RoleRepository repository;

    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    protected GenericRepository<Role> getRepository() {
        return repository;
    }

}
