package com.umss.siiu.core.dto;

import com.umss.siiu.core.model.SubCategory;

public class SubCategoryDto extends DtoBase<SubCategory> {
    private String name;
    private String code;
    private CategoryDto category;
    // otra forma
    private Long categoryId;

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

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
