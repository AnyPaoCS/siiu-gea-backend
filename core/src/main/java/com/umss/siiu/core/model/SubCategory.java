/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.model;

import com.umss.siiu.core.dto.SubCategoryDto;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class SubCategory extends ModelBase<SubCategoryDto> {
    private String name;
    private String code;
    @ManyToOne
    private Category category;

    private Long cateId;  /// DTO

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getCateId() {
        return cateId;
    }

    public void setCateId(Long cateId) {
        this.cateId = cateId;
    }
}
