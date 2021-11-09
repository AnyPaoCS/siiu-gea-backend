/**
 * @author: Edson A. Terceros T.
 */

package com.siiu.umss.siiu.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Employee {
    @Id
    private Long id;

    private String firstName;
    private String lastName;
    private Byte[] image;
    private List<Contract> contracts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }
}
