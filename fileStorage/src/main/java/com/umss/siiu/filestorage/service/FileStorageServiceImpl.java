package com.umss.siiu.filestorage.service;

import com.google.gson.Gson;
import com.umss.siiu.core.service.UserService;
import com.umss.siiu.filestorage.dto.FileSimpleInfoDto;
import com.umss.siiu.filestorage.dto.FileUploadDto;
import com.umss.siiu.filestorage.dto.ImageSimpleInfoDto;
import com.umss.siiu.filestorage.dto.JackRabbitNodeDto;
import com.umss.siiu.filestorage.model.FileType;
import com.umss.siiu.filestorage.model.FileTypeCategory;
import com.umss.siiu.filestorage.model.JackRabbitNode;
import org.apache.poi.util.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final FileService fileService;
    private final FileTypeService fileTypeService;
    private UserService userService;
    private final Gson gson;

    public FileStorageServiceImpl(FileService fileService, FileTypeService fileTypeService, Gson gson) {
        this.fileService = fileService;
        this.fileTypeService = fileTypeService;
        this.gson = gson;
    }

    @Override
    public FileSimpleInfoDto uploadFile(MultipartFile file, String fileUpload) {
        var fileUploadDto = gson.fromJson(fileUpload, FileUploadDto.class);
        var user = userService.findByEmail(fileUploadDto.getEmail());
        var dto = new JackRabbitNodeDto();
        dto.setFile(file);
        dto.setDescription(fileUploadDto.getDescription());
        dto.setOwnerId(user.getId());
        dto.setOwnerClass("user");
        var fileType = fileTypeService.findByAbbreviation(fileUploadDto.getFileTypeAbbreviation());
        dto.setFileTypeId(fileType.getId());
        String folderPath = user.getId() + "/" + fileType.getFileTypeCategory().name();
        dto.setParentPath(folderPath);
        var jackRabbitNode = fileService.getNodeByUserIdAndFileTypeId(user.getId(), fileType.getId());
        if (jackRabbitNode != null) {
            if (jackRabbitNode.isVerified()) {
                return null;
            } else {
                dto.setId(jackRabbitNode.getId());
                fileService.replaceFile(dto);
            }
        } else {
            fileService.createFolderStructure(folderPath);
            fileService.saveFile(dto, file.getOriginalFilename());
        }
        JackRabbitNode node = fileService.getNodeByUserIdAndFileTypeId(user.getId(), fileType.getId());
        return new FileSimpleInfoDto(node);
    }

    @Override
    public void downloadFile(long fileId, HttpServletResponse httpServletResponse) {
        JackRabbitNode node = fileService.findByFileId(fileId);
        if (node == null) {
            return;
        }
        fileService.downloadFile(httpServletResponse, node.getPath());
    }

    @Override
    public byte[] getDocumentFileContent(long fileId) {
        JackRabbitNode node = fileService.findByFileId(fileId);
        if (node == null) {
            return new byte[0];
        }
        var is = fileService.getInputStreamFromNode(node.getPath());
        byte[] documentContent = null;
        try {
            documentContent = IOUtils.toByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documentContent;
    }

    @Override
    public FileSimpleInfoDto setFileIsVerified(long fileId, boolean verified, String physicCode) {
        JackRabbitNode node = fileService.updateFileVerified(fileId, verified, physicCode);
        if (node != null) {
            return new FileSimpleInfoDto(node);
        } else {
            return null;
        }
    }

    @Override
    public FileSimpleInfoDto setFileIsPermanent(long fileId, boolean permanent) {
        JackRabbitNode node = fileService.updateFileIsPermanent(fileId, permanent);
        if (node != null) {
            return new FileSimpleInfoDto(node);
        } else {
            return null;
        }
    }

    @Override
    public long deleteFile(long fileId) {
        JackRabbitNode node = fileService.findByFileId(fileId);
        if (node != null && !node.isPermanent()) {
            fileService.deleteFile(node.getPath());
            return fileId;
        }
        return 0;
    }

    @Override
    public List<FileSimpleInfoDto> getFilesByUserId(long userId) {
        List<JackRabbitNode> nodeList = fileService.getFilesByUserId(userId);
        List<FileSimpleInfoDto> list = new ArrayList<>();
        if (nodeList != null && !nodeList.isEmpty()) {
            for (JackRabbitNode node : nodeList) {
                var dto = new FileSimpleInfoDto(node);
                list.add(dto);
            }
        }
        return list;
    }

    @Override
    public FileSimpleInfoDto getFileByUserIdAndFileTypeAbbreviation(long userId, String abbreviation) {
        var fileType = fileTypeService.findByAbbreviation(abbreviation);
        if (fileType == null) {
            return null;
        }
        JackRabbitNode node = fileService.getNodeByUserIdAndFileTypeId(userId, fileType.getId());
        FileSimpleInfoDto dto = null;
        if (node != null) {
            dto = new FileSimpleInfoDto(node);
        }
        return dto;
    }

    @Override
    public List<FileSimpleInfoDto> getFilesByUserIdAndFileTypeCategory(long userId, String category) {
        List<FileType> fileTypes = fileTypeService.findByFileTypeCategory(FileTypeCategory.valueOf(category));
        if (fileTypes == null || fileTypes.isEmpty()) {
            return Collections.emptyList();
        }
        List<JackRabbitNode> nodeList = fileService.getFilesByUserId(userId);
        List<FileSimpleInfoDto> list = new ArrayList<>();
        if (nodeList != null && !nodeList.isEmpty()) {
            for (FileType fileType: fileTypes) {
                for (JackRabbitNode node : nodeList) {
                    if (node.getFileType().equals(fileType)) {
                        var dto = new FileSimpleInfoDto(node);
                        list.add(dto);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public boolean verifyQRLegalizedDocument(long fileId, String qrValue) {
        JackRabbitNode node = fileService.findByFileId(fileId);
        return node.getNodeId().equals(qrValue);
    }

    @Override
    public ImageSimpleInfoDto uploadSignatureImage(long userId, MultipartFile file) {
        var dto = new JackRabbitNodeDto();
        dto.setFile(file);
        dto.setOwnerId(userId);
        var fileType = fileTypeService.findByFileTypeCategory(FileTypeCategory.IMAGEN_FIRMA).get(0);
        dto.setFileTypeId(fileType.getId());
        String folderPath = userId + "/" + fileType.getFileTypeCategory().name();
        dto.setParentPath(folderPath);
        var jackRabbitNode = fileService.getNodeByUserIdAndFileTypeId(userId, fileType.getId());
        if (jackRabbitNode != null) {
            dto.setId(jackRabbitNode.getId());
            fileService.replaceFile(dto);
        } else {
            fileService.createFolderStructure(folderPath);
            fileService.saveFile(dto, file.getOriginalFilename());
        }
        JackRabbitNode node = fileService.getNodeByUserIdAndFileTypeId(userId, fileType.getId());
        return new ImageSimpleInfoDto(node);
    }

    @Override
    public byte[] getSignatureImage(long userId) {
        var fileType = fileTypeService.findByFileTypeCategory(FileTypeCategory.IMAGEN_FIRMA).get(0);
        JackRabbitNode node = fileService.getNodeByUserIdAndFileTypeId(userId, fileType.getId());
        if (node == null) {
            return new byte[0];
        }
        var is = fileService.getInputStreamFromNode(node.getPath());
        byte[] signatureImage = null;
        try {
            signatureImage = IOUtils.toByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return signatureImage;
    }
}
