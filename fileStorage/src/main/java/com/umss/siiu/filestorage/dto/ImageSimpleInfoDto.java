package com.umss.siiu.filestorage.dto;

import com.umss.siiu.filestorage.model.JackRabbitNode;

public class ImageSimpleInfoDto {

    private long fileId;
    private String imageName;
    private String path;
    private long userId;

    public ImageSimpleInfoDto() {
    }

    public ImageSimpleInfoDto(JackRabbitNode node) {
        this.fileId = node.getId();
        this.imageName = node.getFileName();
        this.path = node.getPath();
        this.userId = node.getOwnerId();
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
