package com.umss.siiu.filestorage.service;

import com.umss.siiu.filestorage.dto.FileSimpleInfoDto;
import com.umss.siiu.filestorage.dto.ImageSimpleInfoDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface FileStorageService {

    FileSimpleInfoDto uploadFile (MultipartFile file, String fileUpload);

    void downloadFile (long fileId, HttpServletResponse httpServletResponse);

    byte[] getDocumentFileContent (long fileId);

    FileSimpleInfoDto setFileIsVerified (long fileId, boolean verified, String physicCode);

    FileSimpleInfoDto setFileIsPermanent (long fileId, boolean permanent);

    long deleteFile (long fileId);

    List<FileSimpleInfoDto> getFilesByUserId (long userId);

    FileSimpleInfoDto getFileByUserIdAndFileTypeAbbreviation (long userId, String abbreviation);

    List<FileSimpleInfoDto> getFilesByUserIdAndFileTypeCategory (long userId, String category);

    boolean verifyQRLegalizedDocument (long fileId, String qrValue);

    ImageSimpleInfoDto uploadSignatureImage (long userId, MultipartFile file);

    byte[] getSignatureImage (long userId);

}
