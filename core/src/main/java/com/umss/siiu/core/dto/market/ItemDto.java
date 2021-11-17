package com.umss.siiu.core.dto.market;

import com.umss.siiu.core.dto.DtoBase;
import com.umss.siiu.core.model.Item;

public class ItemDto extends DtoBase<Item> {
    private String name;
    private String code;
    private Byte[] image;

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

    public Byte[] getImage() {
        return image;
    }

    public void setImage(Byte[] image) {
        this.image = image;
    }
}
