/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.model;

import com.umss.siiu.core.dto.SubCategoryDto;
import org.modelmapper.ModelMapper;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class SubCategory extends ModelBase<SubCategoryDto> {
    private String name;
    private String code;
    @ManyToOne
    private Category category;

    @Transient
    private String completeName;

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

    public String getCompleteName() {
        return completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    @Override
    public SubCategory toDomain(SubCategoryDto subCategoryDto, ModelMapper mapper) {
        SubCategory subCategory = super.toDomain(subCategoryDto, mapper);
        // establecer la relacion // SQL establecer el FK
        Category category = new Category();
        category.setId(subCategoryDto.getCategoryId());
        subCategory.setCategory(category);
        return subCategory;
    }
}
