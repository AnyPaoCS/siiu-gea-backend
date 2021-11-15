/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.dto;

import com.umss.siiu.core.model.Category;

public class CategoryDto extends DtoBase<Category> {
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
