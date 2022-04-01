package com.umss.siiu.core.model;

import com.umss.siiu.core.dto.UserDto;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="siiuuser", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User extends ModelBase<UserDto> {

    @Column(unique = true, nullable = false, length = 50)
    private String email;
    @Column(nullable = false, length = 60)
    private String password;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean enabled;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean systemUser = false;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "siiuuser_role", joinColumns = {
            @JoinColumn(name = "siiuuser_id", nullable = false, updatable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "role_id", nullable = false, updatable = false)})
    private Set<Role> roles;

    @OneToOne(optional = false)
    private Employee employee;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(Boolean system) {
        this.systemUser = system;
    }

}
