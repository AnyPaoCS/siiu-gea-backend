/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.dto;

import java.util.Arrays;

import com.umss.siiu.core.model.Employee;
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;

public class EmployeeDto extends DtoBase<Employee> {

    private static final boolean BY_LASTNAME_FIRST = false;

    private String firstName;
    private String lastName;
    private String fullName;

    private String image;
    private String jobPosition;
    private String jobCode;
    private Boolean featured;
    private String jobDescription;

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


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    @Override
    public EmployeeDto toDto(Employee employee, ModelMapper mapper) {
        super.toDto(employee, mapper);
        setImageBase64(employee);
        jobDescription = "Descripcion de job";
        return this;
    }

    @Override
    protected void afterConversion(Employee employee, ModelMapper mapper) {
        setFirstName(employee.getFirstName());
        setLastName(employee.getLastName());
        setFullName(employee.getFullName(BY_LASTNAME_FIRST));
    }

    private void setImageBase64(Employee employee) {
        if (employee.getImage() != null) {
            var bytes = new byte[employee.getImage().length];
            for (var i = 0; i < employee.getImage().length; i++) {
                bytes[i] = employee.getImage()[i];
            }
            var imageStr = Base64.encodeBase64String(bytes);
            this.setImage(imageStr);
        }
    }
}
