package com.umss.siiu.bpmn.controller;

import com.umss.siiu.bpmn.service.RoleService;
import com.umss.siiu.core.controller.GenericController;
import com.umss.siiu.core.dto.RoleDto;
import com.umss.siiu.core.model.Role;
import com.umss.siiu.core.service.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/roles")
public class RoleController extends GenericController<Role, RoleDto> {

    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    protected GenericService<Role> getService() {
        return roleService;
    }
}
