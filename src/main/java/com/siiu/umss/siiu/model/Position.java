/**
 * @author: Edson A. Terceros T.
 */

package com.siiu.umss.siiu.model;

import javax.persistence.Entity;

@Entity
public class Position extends ModelBase {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
