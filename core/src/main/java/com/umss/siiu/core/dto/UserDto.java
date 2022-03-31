package com.umss.siiu.core.dto;

import com.umss.siiu.core.model.User;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

public class UserDto extends DtoBase<User> {

    private String firstName;
    private String lastName;
    @Email(message = "{messages.validation.emailFormatError}")
    @NotBlank(message = "{messages.validation.emptyEmail}")
    private String email;
    private String password;
    private List<RoleDto> roles;
    private EmployeeDto employee;
    private String type;

    public UserDto() {
        super();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDto> roles) {
        this.roles = roles;
    }

    public EmployeeDto getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDto employee) {
        this.employee = employee;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    @Override
    public DtoBase<User> toDto(User user, ModelMapper mapper) {
        super.toDto(user, mapper);
        this.setPassword("");
        setEmployee(new EmployeeDto());
        mapper.map(user.getEmployee(), getEmployee());
        return this;
    }
}
