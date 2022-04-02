package com.umss.siiu.filestorage.dto;

public class FileUploadDto {

    private Long userId;
    private String fileTypeAbbreviation;
    private String description;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFileTypeAbbreviation() {
        return fileTypeAbbreviation;
    }

    public void setFileTypeAbbreviation(String fileTypeAbbreviation) {
        this.fileTypeAbbreviation = fileTypeAbbreviation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
