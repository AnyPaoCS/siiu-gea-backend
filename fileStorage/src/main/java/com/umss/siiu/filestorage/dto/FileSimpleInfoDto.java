package com.umss.siiu.filestorage.dto;

import com.umss.siiu.filestorage.model.JackRabbitNode;

public class FileSimpleInfoDto {

    private long fileId;
    private String documentName;
    private String description;
    private long fileTypeId;
    private String path;
    private boolean isVerified;
    private boolean isPermanent;

    public FileSimpleInfoDto () {}

    public FileSimpleInfoDto (JackRabbitNode node) {
        this.fileId = node.getId();
        this.documentName = node.getFileName();
        this.description = node.getDescription();
        this.path = node.getPath();
        this.isPermanent = node.isPermanent();
        this.isVerified = node.isVerified();
        this.fileTypeId = node.getFileType().getId();
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public long getFileTypeId() {
        return fileTypeId;
    }

    public void setFileTypeId(long fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean isPermanent() {
        return isPermanent;
    }

    public void setPermanent(boolean permanent) {
        isPermanent = permanent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
