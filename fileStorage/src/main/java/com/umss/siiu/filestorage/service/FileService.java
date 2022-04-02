package com.umss.siiu.filestorage.service;


import com.umss.siiu.bpmn.model.Job;
import com.umss.siiu.core.model.ModelBase;
import com.umss.siiu.filestorage.dto.JackRabbitNodeDto;
import com.umss.siiu.filestorage.model.JackRabbitNode;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public interface FileService {

    void downloadFile(HttpServletResponse response, String filename);

    void downloadFilesByJobIdAndCategory(HttpServletResponse response, long jobId, String category);

    void lockAndDownloadFilesByJobIdAndCategory(HttpServletResponse response, long jobId, String category,
                                                String employeeEmail);

    InputStream getInputStreamFromNode(String file);

    List<JackRabbitNode> getJackRabbitNodes(Object owner, long ownerId);

    void deleteFile(String path);

    void saveFile(JackRabbitNodeDto jackRabbitNodeDto, String fileName);


    void saveFile(String fileName, long fileTypeCode, String description, InputStream stream,
                  ModelBase<?> modelBase, String nodePath, boolean flush);

    /*void deleteOutdatedFiles(JobCrm jobCrm, JackRabbitNode jackRabbitNode);

    boolean isUpToDate(JobCrm jobCrm, JackRabbitNode jackRabbitNode);*/

    void createJobStructure(Object owner, List<String> entries);

    void createAdditionalJobStructure(Object owner, List<String> entries);

    void updateJackRabbitNodeFile(JackRabbitNodeDto jackRabbitNodeDto);

    void replaceFile(JackRabbitNodeDto jackRabbitNodeDto);

    JackRabbitNode replaceFile(JackRabbitNodeDto jackRabbitNodeDto, JackRabbitNode jackRabbitNode);

    void visualizeFile(HttpServletResponse response, String filename);

    List<String> getFilePathsByOwnerAndFileType(String ownerName, long ownerId, List<Long> fileTypesId);

    List<String> getFilePathsByJobAndCategoryType(long jobId, String category);

    public void saveWordDocument(long jobId, long fileTypeId, JackRabbitNode jackRabbitNode, XWPFDocument xwpfDocument,
                                 String removedWordFromFile);

    void saveTemplate(Job job, JackRabbitNode jackRabbitNode, ByteArrayOutputStream baos, String removedWordFromFile);

    void unlockAndSaveFile(JackRabbitNodeDto jackRabbitNodeDto, String fileName, String employeeEmail, String category);

    void createFolderEntry(ModelBase<?> owner, String rootNode, String description, String path);

    List<String> getFilePathsByOwnerAndFileTypeIdsAndOwnerIds(String ownerName, List<Long> ownerIds,
                                                              List<Long> fileTypeIds);

    List<String> getFilePathsByJobIdsAndCategoryType(List<Long> jobIds, String category);

    void downloadPublicDocument(HttpServletResponse response, String name);

    void createFolderStructure(String path);

    List<JackRabbitNode> getFilesByJobAndCategoryType(long jobId, String category);

    JackRabbitNode findByFileId (long fileId);

    JackRabbitNode findByPath (String path);

    List<JackRabbitNode> getFilesByUserId(long userId);

    JackRabbitNode getNodeByUserIdAndFileTypeId(long userId, long fileTypeId);

    JackRabbitNode updateFileVerified (long fileId, boolean isVerified, String physicCode);

    JackRabbitNode updateFileIsPermanent (long fileId, boolean isPermanent);
}
