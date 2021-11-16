/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.dto;

import com.umss.siiu.core.model.Employee;
import org.modelmapper.ModelMapper;

public class EmployeeDto extends DtoBase<Employee> {

    private static final boolean BY_LASTNAME_FIRST = false;

    private String firstName;
    private String lastName;
    private String fullName;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    protected void afterConversion(Employee employee, ModelMapper mapper) {
        setFirstName(employee.getFirstName());
        setLastName(employee.getLastName());
        setFullName(employee.getFullName(BY_LASTNAME_FIRST));
    }
}
