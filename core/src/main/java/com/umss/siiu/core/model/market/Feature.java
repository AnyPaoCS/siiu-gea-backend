/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.model.market;

import com.umss.siiu.core.model.ModelBase;

import javax.persistence.Entity;

@Entity
public class Feature extends ModelBase {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
