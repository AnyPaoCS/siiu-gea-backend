/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.model;

import com.umss.siiu.core.dto.CategoryDto;

import javax.persistence.Entity;

@Entity
public class Category extends ModelBase<CategoryDto> {
    //    @Column(length = 200,name = "nombre", nullable = false, unique = true)
    private String name;
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
