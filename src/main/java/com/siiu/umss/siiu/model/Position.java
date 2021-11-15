/**
 * @author: Edson A. Terceros T.
 */

package com.siiu.umss.siiu.model;

import com.siiu.umss.siiu.dto.PositionDto;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Position extends ModelBase<PositionDto> {
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
