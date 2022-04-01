/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.model;

import com.umss.siiu.core.dto.EmployeeDto;

import javax.persistence.*;
import java.util.List;

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

    //    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "employee_task", joinColumns = {
//            @JoinColumn(name = "employee_id")}, inverseJoinColumns = {
//            @JoinColumn(name = "task_id")})
//    private Set<Task> tasks;
    /*@OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
    private Set<EmployeeTask> employeeTasks;*/

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
