/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.model;

import javax.persistence.Entity;

@Entity
public class Funcion extends ModelBase {
    private String name;

    /*@OneToOne(mappedBy = "funcion")
    private Position position;*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}