package com.umss.siiu.filestorage.model;

import com.umss.siiu.core.model.ModelBase;
import com.umss.siiu.filestorage.dto.FileTypeDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class FileType extends ModelBase<FileTypeDto> {

    @Column(nullable = false, length = 100)
    private String abbreviation;
    @Column(nullable = false, length = 100)
    private String name;
    @Enumerated(EnumType.STRING)
    private FileTypeCategory fileTypeCategory;

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

    public FileTypeCategory getFileTypeCategory() {
        return fileTypeCategory;
    }

    public void setFileTypeCategory(FileTypeCategory fileTypeCategory) {
        this.fileTypeCategory = fileTypeCategory;
    }
}
