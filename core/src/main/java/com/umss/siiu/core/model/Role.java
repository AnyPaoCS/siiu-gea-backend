package com.umss.siiu.core.model;

import com.umss.siiu.core.dto.RoleDto;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Role extends ModelBase<RoleDto> {

    @Column(nullable = false, length = 25)
    private String name;
    @ManyToMany
    @JoinTable(name = "siiuuser_role", joinColumns = {
            @JoinColumn(name = "role_id", nullable = false, updatable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "siiuuser_id", nullable = false, updatable = false)})
    private Set<User> users;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}
