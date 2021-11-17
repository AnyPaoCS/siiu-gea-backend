/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.dto;

import com.umss.siiu.core.model.Category;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.Set;

public class CategoryDto extends DtoBase<Category> {
    private String name;
    private String code;
    private Set<SubCategoryDto> subCategories = new HashSet<>();

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

    public Set<SubCategoryDto> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(Set<SubCategoryDto> subCategories) {
        this.subCategories = subCategories;
    }

    @Override
    public CategoryDto toDto(Category category, ModelMapper mapper) {
        CategoryDto categoryDto = super.toDto(category, mapper);
        categoryDto.setSubCategories(new SubCategoryDto().toSetDto(category.getSubCategories(), mapper));
        return categoryDto;
    }
}
