package com.umss.siiu.filestorage.dto;

import com.umss.siiu.core.dto.DtoBase;
import com.umss.siiu.filestorage.model.FileType;

public class FileTypeDto extends DtoBase<FileType> {

    private String abbreviation;
    private String name;

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
