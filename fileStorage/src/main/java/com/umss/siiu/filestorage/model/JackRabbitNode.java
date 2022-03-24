/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.filestorage.model;

import com.umss.siiu.core.model.ModelBase;
import com.umss.siiu.filestorage.dto.JackRabbitNodeDto;

import javax.persistence.*;

@Entity
@Table(indexes = {@Index(name = "JACKRABBITNODE_OWNERCLASS_INDX", columnList = "ownerClass")})
public class JackRabbitNode extends ModelBase<JackRabbitNodeDto> {
    private Long ownerId;
    private String ownerClass;
    @ManyToOne
    @JoinColumn(nullable = false)
    private FileType fileType;
    @Column(nullable = false)
    private boolean isFolder = false;
    @Column(nullable = false, length = 500)
    private String path;
    @Column(nullable = false)
    private String nodeId;
    private String jackRabbitVersion;
    private String description;
    private String fileName;
    private String parentPath;

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getJackRabbitVersion() {
        return jackRabbitVersion;
    }

    public void setJackRabbitVersion(String jackRabbitVersion) {
        this.jackRabbitVersion = jackRabbitVersion;
    }

    public String getOwnerClass() {
        return ownerClass;
    }

    public void setOwnerClass(String ownerClass) {
        this.ownerClass = ownerClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

}
