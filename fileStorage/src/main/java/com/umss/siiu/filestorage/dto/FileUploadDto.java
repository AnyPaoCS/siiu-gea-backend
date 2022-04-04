package com.umss.siiu.filestorage.dto;

public class FileUploadDto {

    private String email;
    private String fileTypeAbbreviation;
    private String description;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
