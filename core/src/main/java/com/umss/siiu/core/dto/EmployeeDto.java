/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.dto;

import com.umss.siiu.core.model.Employee;

public class EmployeeDto extends DtoBase<Employee> {

    private String firstName;
    private String lastName;

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
}
