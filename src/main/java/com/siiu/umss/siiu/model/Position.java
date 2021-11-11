/**
 * @author: Edson A. Terceros T.
 */

package com.siiu.umss.siiu.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Position extends ModelBase {
    private String name;

    @OneToOne
    private Funcion funcion;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
