/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.model;

import com.umss.siiu.core.dto.EmployeeDto;

import javax.persistence.*;

@Entity
public class Employee extends ModelBase<EmployeeDto> {
    private String firstName;
    private String lastName;
    private Boolean featured;
    private Byte[] image;

    @Column(length = 1, nullable = false)
    private boolean available = true;

    @OneToOne(mappedBy = "employee")
    private User user;

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

    public String getFullName(boolean lastFirst) {
        return String.format("%s %s", lastFirst ? getLastName() : getFirstName(), lastFirst ? getFirstName() :
                getLastName());
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Byte[] getImage() {
        return image;
    }

    public void setImage(Byte[] image) {
        this.image = image;
    }
}
