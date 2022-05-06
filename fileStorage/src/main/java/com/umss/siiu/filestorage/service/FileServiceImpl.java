package com.umss.siiu.filestorage.service;

import com.umss.siiu.bpmn.model.Job;
import com.umss.siiu.core.exceptions.BlockedFileException;
import com.umss.siiu.core.exceptions.NotFoundException;
import com.umss.siiu.core.exceptions.RepositoryException;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.model.ModelBase;
import com.umss.siiu.core.service.EmployeeService;
import com.umss.siiu.core.util.ApplicationConstants;
import com.umss.siiu.filestorage.dto.JackRabbitNodeDto;
import com.umss.siiu.filestorage.factory.ModelBaseFactory;
import com.umss.siiu.filestorage.model.FileType;
import com.umss.siiu.filestorage.model.FileTypeCategory;
import com.umss.siiu.filestorage.model.JackRabbitNode;
import com.umss.siiu.filestorage.model.JobFileTypeLock;
import com.umss.siiu.filestorage.util.StreamUtils;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;

import javax.jcr.Node;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileServiceImpl implements FileService {

    private static final String BRACKET = "[";
    private static final String SLASH_SEPARATOR = "/";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private JackRabbitService jackRabbitService;
    private JackRabbitNodeService jackRabbitNodeService;
    private JobFileTypeLockService jobFileTypeLockService;
    private FileTypeService fileTypeService;
    private EmployeeService employeeService;


    public FileServiceImpl(JackRabbitService jackRabbitService, JackRabbitNodeService jackRabbitNodeService,
                           JobFileTypeLockService jobFileTypeLockService,
                           FileTypeService fileTypeService, EmployeeService employeeService) {
        this.jackRabbitService = jackRabbitService;
        this.jackRabbitNodeService = jackRabbitNodeService;
        this.jobFileTypeLockService = jobFileTypeLockService;
        this.fileTypeService = fileTypeService;
        this.employeeService = employeeService;
    }

    @Override
    public void downloadPublicDocument(HttpServletResponse response, String name) {
        try {
            ByteArrayOutputStream outputStream = getFileFromNode("public/" + name);
            exportByteArrayOutputStream(response, outputStream, name);
        } catch (IOException e) {
            throw new RepositoryException("Could not retrieve file " + name, e);
        }
    }


    private void exportByteArrayOutputStream(HttpServletResponse response, ByteArrayOutputStream document,
                                             String filename) {
        try {
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setContentType(URLConnection.guessContentTypeFromName(filename));
            response.addHeader(ApplicationConstants.CONTENT_DISPOSITION, "attachment; filename=\"" + filename);
            response.setContentLength(document.size());
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            document.writeTo(bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            throw new RepositoryException(String.format("Could not export %s", filename));
        }
    }

    @Override
    public void downloadFile(HttpServletResponse response, String filename) {
        String slashFilename = filename.charAt(0) != '/' ? SLASH_SEPARATOR + filename : filename;
        JackRabbitNode jackRabbitNode = jackRabbitNodeService.findByFilePath(slashFilename);
        ByteArrayOutputStream file = getFile(filename);
        exportByteArrayOutputStream(response, file, jackRabbitNode.getFileName());
    }

    @Override
    public void downloadFilesByJobIdAndCategory(HttpServletResponse response, long jobId, String category) {
        // Getting all the files
        List<JackRabbitNode> jackRabbitNodes = getFilesByJobAndCategoryType(jobId, category);
        if (jackRabbitNodes.size() == 1) {
            downloadFile(response, jackRabbitNodes.get(0).getPath());
        } else if (jackRabbitNodes.size() > 1) {
            downloadFilesAsZip(response, jackRabbitNodes, category);
        }
    }

    private void downloadFilesAsZip(HttpServletResponse response, List<JackRabbitNode> jackRabbitNodes,
                                    String fileNameOutput) {
        try {
            response.setContentType("application/zip");
            response.setHeader(ApplicationConstants.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + fileNameOutput + " .zip\"");
            ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
            for (JackRabbitNode jackRabbitNode : jackRabbitNodes) {
                // Getting file from JackRabbit
                InputStream inputStream = getInputStreamFromNode(jackRabbitNode.getPath());
                zipOutputStream.putNextEntry(new ZipEntry(jackRabbitNode.getFileName()));
                // Write the contents of the file
                int data = 0;
                while ((data = inputStream.read()) != -1) {
                    zipOutputStream.write(data);
                }
                zipOutputStream.closeEntry();
                inputStream.close();
            }
            zipOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ByteArrayOutputStream getFile(String filename) {
        String noneSlashFilename = filename.charAt(0) == '/' ? filename.substring(1) : filename;
        try {
            if (jackRabbitService.getRootNode().hasNode(noneSlashFilename)) {
                return getFileFromNode(noneSlashFilename);
            }
        } catch (javax.jcr.RepositoryException | IOException e) {
            throw new RepositoryException(String.format("Error while processing %s", noneSlashFilename), e);
        }
        throw new RepositoryException(String.format("Could not find file %s", noneSlashFilename));
    }

    private ByteArrayOutputStream getFileFromNode(String file) throws IOException {
        final byte[] content = IOUtils.toByteArray(getInputStreamFromNode(file));
        ByteArrayOutputStream baos = new ByteArrayOutputStream(content.length);
        baos.write(content);
        return baos;
    }

    @Override
    public InputStream getInputStreamFromNode(String file) {
        String noneSlashFilename = file.charAt(0) == '/' ? file.substring(1) : file;
        try {
            return jackRabbitService.getFileNode(noneSlashFilename).getProperty(JcrConstants.JCR_DATA).getBinary()
                    .getStream();
        } catch (javax.jcr.RepositoryException e) {
            throw new RepositoryException(String.format("Could not find file %s", noneSlashFilename));
        }

    }

    @Override
    public List<JackRabbitNode> getJackRabbitNodes(Object owner, long ownerId) {
        return jackRabbitNodeService.findByOwnerClassAndOwnerId(owner, ownerId);
    }

    @Override
    public void deleteFile(String path) {
        try {
            if (jackRabbitService.getRootNode().hasNode(path.substring(1))) {
                jackRabbitService.getRootNode().getNode(path.substring(1)).remove();
                jackRabbitService.save();
                jackRabbitNodeService.deleteByPath(path);
            } else {
                throw new RepositoryException(String.format("Could not find file %s", path));
            }
        } catch (javax.jcr.RepositoryException e) {
            throw new RepositoryException(String.format("Error while deleting file %s", path), e);
        }
    }

    @Override
    @Transactional
    public void unlockAndSaveFile(JackRabbitNodeDto jackRabbitNodeDto, String fileName, String employeeEmail,
                                  String category) {
        try {
            JobFileTypeLock jobFileTypeLock = null;
            Job job = new Job();
            job.setId(jackRabbitNodeDto.getOwnerId());
            Set<FileType> fileTypes = fileTypeService.getFileTypesByJobIdAndCategory(jackRabbitNodeDto.getOwnerId(),
                    FileTypeCategory.valueOf(category));
            Employee employee = employeeService.findByEmail(employeeEmail);
            for (FileType fileType : fileTypes) {
                jobFileTypeLock = jobFileTypeLockService.findByJobAndFileType(job, fileType);
                if (jobFileTypeLock != null) {
                    // Unlock file(s)
                    jobFileTypeLockService.releaseLock(jobFileTypeLock.getId(), job, employee, fileType);
                }
            }
            saveFile(jackRabbitNodeDto, fileName);
        } catch (BlockedFileException e) {
            throw new BlockedFileException("Upload not allowed. " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void saveFile(JackRabbitNodeDto jackRabbitNodeDto, String fileName) {
        try {
            ModelBase<?> modelBase = ModelBaseFactory.createModelBase(jackRabbitNodeDto.getOwnerClass().trim(),
                    jackRabbitNodeDto.getOwnerId());
            String parentPath = jackRabbitNodeDto.getParentPath();
            JackRabbitNode jackRabbitNode = jackRabbitNodeService
                    .findByFilePath((!StringUtils.isEmpty(parentPath) ? ("/" + parentPath) : "") + "/" + fileName);

            saveFile(fileName, jackRabbitNodeDto.getFileTypeId(), jackRabbitNodeDto.getDescription(),
                    jackRabbitNodeDto.getFile().getInputStream(), modelBase, parentPath, true);
        } catch (Exception e) {
            throw new RepositoryException(
                    String.format("Error while storing file %s", jackRabbitNodeDto.getFile().getOriginalFilename()), e);
        }
    }

    @Override
    @Transactional
    public void saveFile(String fileName, long fileTypeCode, String description, InputStream stream,
                         ModelBase<?> modelBase, String nodePath, boolean flush) {
        try {
            if (nodePath.equals("public") && !jackRabbitService.getRootNode().hasNode(nodePath)) {
                jackRabbitService.createFolderNode(nodePath, "");
            }
            Node targetNode = jackRabbitService.getNode(nodePath);
            boolean hasNode = false;
            try {
                hasNode = targetNode.hasNode(fileName);
            } catch (Exception ignored) {

            }
            if (hasNode) {
                jackRabbitService.deleteNode(fileName, nodePath);
            }
            Node node = jackRabbitService.createBinaryNodeFromStream(fileName, nodePath, stream,
                    MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE);
            JackRabbitNode jackRabbitNode;
            jackRabbitNode = jackRabbitNodeService.findByFilePath(
                    (!StringUtils.isEmpty(nodePath) ? ("/" + nodePath) : "") + "/" + fileName);
            if (hasNode && jackRabbitNode != null) {
                jackRabbitNode.setNodeId(node.getIdentifier());
                jackRabbitNodeService.save(jackRabbitNode);
            } else {
                jackRabbitNodeService.createJackRabbitNode(modelBase, fileTypeCode, false, fileName, description.trim(),
                        modelBase.getId(), node, nodePath, flush);
            }
        } catch (Exception e) {
            throw new RepositoryException(String.format("Error while storing file %s", fileName), e);
        }
    }

    @Override
    @Transactional
    public void createJobStructure(Object owner, List<String> entries) {
        Job job = (Job) owner;
        Node node;

        try {
            String yearNode = Integer.toString(job.getCreatedAt().getYear());
            String monthNode = Integer.toString(job.getCreatedAt().getMonth());
            String nodePath = String.format("%s/%s/%s", yearNode, monthNode, job.getId());
            String parentNodePath = String.format("%s/%s", yearNode, monthNode);
            if (!jackRabbitService.getRootNode().hasNode(yearNode)) {
                node = jackRabbitService.createFolderNode(yearNode, "");
                jackRabbitNodeService.createJackRabbitNode(owner, 1, false, node.getName(),
                        String.format("year %s entry level", yearNode), null, node, "", true);
            }
            if (!jackRabbitService.getNode(yearNode).hasNode(monthNode)) {
                node = jackRabbitService.createFolderNode(monthNode, yearNode);
                jackRabbitNodeService.createJackRabbitNode(owner, 1, false, node.getName(),
                        String.format("year %s, month %s entry level", yearNode, monthNode), null, node, yearNode,
                        true);
            }

            // todo this should never happens if so something is wrong and see
            if (jackRabbitService.getRootNode().hasNode(nodePath)) {
                jackRabbitService.getRootNode().getNode(nodePath).remove();
            }
            jackRabbitService.save();
            if (jackRabbitService.getRootNode().hasNode(nodePath)) {
                logger.error("File structure for job %s already exists\", job.getId()");
            } else {
                node = jackRabbitService.createFolderNode(job.getId().toString(), parentNodePath);
                jackRabbitNodeService.createJackRabbitNode(owner, 1, false, node.getName(),
                        String.format("year %s, month %s, job %s entry " + "level", yearNode, monthNode,
                                job.getId().toString()),
                        job.getId(), node, parentNodePath, true);
                createFolderEntries(owner, entries, job, yearNode, monthNode, nodePath);
            }
        } catch (javax.jcr.RepositoryException e) {
            throw new RepositoryException(String.format("Error while storing file for job %s", job.getId()), e);
        }
    }

    @Transactional
    public void createAdditionalJobStructure(Object owner, List<String> entries) {
        Job job = (Job) owner;
        try {
            String yearNode = Integer.toString(job.getCreatedAt().getYear());
            String monthNode = Integer.toString(job.getCreatedAt().getMonth());
            String nodePath = String.format("%s/%s/%s", yearNode, monthNode, job.getId());
            if (!jackRabbitService.getRootNode().hasNode(nodePath)) {
                logger.error("File structure for job %s already exists\", job.getId()");
                throw new RepositoryException(String.format("File structure for job %s does not exists", job.getId()));
            } else {
                createFolderEntries(owner, entries, job, yearNode, monthNode, nodePath);
            }
        } catch (javax.jcr.RepositoryException e) {
            throw new RepositoryException(String.format("Error while storing file for job %s", job.getId()), e);
        }
    }

    private void createFolderEntries(Object owner, List<String> entries, Job job, String yearNode, String monthNode,
                                     String nodePath) {
        for (String entry : entries) {
            createFolderEntry((ModelBase<?>) owner, nodePath,
                    String.format("year %s, month %s, job %s, " + "folder %s entry level", yearNode, monthNode,
                            job.getId().toString(), entry),
                    entry);
        }
    }

    @Override
    public void createFolderEntry(ModelBase<?> owner, String rootNode, String description, String path) {
        try {
            if (!jackRabbitService.getRootNode().getNode(rootNode).hasNode(path)) {
                Node node = jackRabbitService.createFolderNode(path, rootNode);
                jackRabbitNodeService.createJackRabbitNode(owner, 1, true, node.getName(), description, owner.getId(),
                        node, rootNode, false);
            }
        } catch (javax.jcr.RepositoryException e) {
            throw new RepositoryException(String.format("Error while storing file entries for owner %s", owner.getId()),
                    e);
        }
    }

    @Override
    public void updateJackRabbitNodeFile(JackRabbitNodeDto jackRabbitNodeDto) {
        jackRabbitNodeService.updateNode(jackRabbitNodeDto);
    }

    @Override
    @Transactional
    public void replaceFile(JackRabbitNodeDto jackRabbitNodeDto) {
        // Getting the JackRabbitNode to be updated
        JackRabbitNode jackRabbitNode = jackRabbitNodeService.findById(jackRabbitNodeDto.getId());
        replaceFile(jackRabbitNodeDto, jackRabbitNode);
    }

    @Override
    @Transactional
    public JackRabbitNode replaceFile(JackRabbitNodeDto jackRabbitNodeDto, JackRabbitNode jackRabbitNode) {
        try {
            if (jackRabbitNode != null) {
                try {
                    // Deleting the old node
                    jackRabbitService.getRootNode().getNode(jackRabbitNode.getPath().replaceFirst(SLASH_SEPARATOR, ""))
                            .remove();
                    jackRabbitService.save();
                } catch (javax.jcr.RepositoryException ignored) {
                }
                // Saving the new file
                Node node = jackRabbitService.createBinaryNodeFromStream(jackRabbitNodeDto.getFile().getOriginalFilename(),
                        jackRabbitNode.getParentPath(), jackRabbitNodeDto.getFile().getInputStream(),
                        MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE);
                // Updating the previous JackRabbitNode
                jackRabbitNode.setNodeId(node.getIdentifier());
                jackRabbitNode.setPath(node.getPath());
                jackRabbitNode.setFileName(jackRabbitNodeDto.getFile().getOriginalFilename());
                jackRabbitNode.setDescription(jackRabbitNodeDto.getFile().getOriginalFilename());
            }
            return jackRabbitNodeService.save(jackRabbitNode);
        } catch (javax.jcr.RepositoryException e) {
            throw new RepositoryException("Error updating the file", e);
        } catch (IOException e) {
            throw new RepositoryException(
                    String.format("Error while updating file %s", jackRabbitNodeDto.getFile().getOriginalFilename()),
                    e);
        }
    }

    @Override
    public void visualizeFile(HttpServletResponse response, String filename) {
        try {
            InputStream in = getInputStreamFromNode(filename);
            String ext = filename.substring(filename.lastIndexOf('.'));
            response.setHeader(ApplicationConstants.CONTENT_DISPOSITION,
                    String.format(ApplicationConstants.VIEW_AND_DOWNLOAD_FILE, filename));

            switch (ext.toLowerCase()) {
                case ".docx":
                    response.setContentType(MediaType.APPLICATION_PDF_VALUE);
                    convertDocxToPdfOutputStream(response, in);
                    break;
                case ".png":
                case ".jpeg":
                case ".jpg":
                case ".gif":
                case ".pdf":
                    response.setContentType(URLConnection.guessContentTypeFromName(filename));
                    convertByteArrayToOutputStream(response, IOUtils.toByteArray(in));
                    break;
                default:
                    response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                    String documentContent = new Tika().parseToString(in);
                    if (documentContent.trim().isEmpty())
                        documentContent = ApplicationConstants.FILE_WITHOUT_PREVIEW;
                    convertByteArrayToOutputStream(response, documentContent.getBytes(Charset.forName("UTF-8")));
                    break;
            }
        } catch (Exception e) {
            logger.error("Error visualizing the file", e);
            throw new NotFoundException();
        }
    }

    private void convertDocxToPdfOutputStream(HttpServletResponse response, InputStream in) throws IOException {
        XWPFDocument document = new XWPFDocument(in);
        PdfOptions options = PdfOptions.create();
        OutputStream out = new BufferedOutputStream(response.getOutputStream());
        PdfConverter.getInstance().convert(document, out, options);
        document.close();
        out.close();
    }


    private void convertByteArrayToOutputStream(HttpServletResponse response, byte[] b) throws IOException {
        OutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(b);
        out.flush();
        out.close();
    }

    @Override
    public List<String> getFilePathsByOwnerAndFileType(String ownerName, long ownerId, List<Long> fileTypesId) {
        return jackRabbitNodeService
                .findByOwnerClassAndFileTypeIdAndOwnerId(ModelBaseFactory.createModelBase(ownerName, ownerId),
                        fileTypesId, ownerId)
                .stream().map(this::getNameFromNode).collect(Collectors.toList());
    }

    @Override
    public List<String> getFilePathsByOwnerAndFileTypeIdsAndOwnerIds(String ownerName, List<Long> ownerIds,
                                                                     List<Long> fileTypeIds) {
        return jackRabbitNodeService
                .findByOwnerClassAndFileTypeIdsAndOwnerIds(ModelBaseFactory.createModelBase(ownerName, 0L), fileTypeIds,
                        ownerIds)
                .stream().map(this::getNameFromNode).collect(Collectors.toList());
    }

    @Override
    public List<String> getFilePathsByJobAndCategoryType(long jobId, String category) {
        return jackRabbitNodeService.findByOwnerClassAndFileCategoryTypeAndOwnerId(new Job(), category, jobId).stream()
                .map(this::getNameFromNode).collect(Collectors.toList());
    }

    @Override
    public List<JackRabbitNode> getFilesByJobAndCategoryType(long jobId, String category) {
        return jackRabbitNodeService.findByOwnerClassAndFileCategoryTypeAndOwnerId(new Job(), category, jobId);
    }

    @Override
    public JackRabbitNode findByFileId(long fileId) {
        return jackRabbitNodeService.findById(fileId);
    }

    @Override
    public JackRabbitNode findByPath(String path) {
        return jackRabbitNodeService.findByFilePath(path);
    }

    @Override
    public List<JackRabbitNode> getFilesByUserId(long userId) {
        return jackRabbitNodeService.findByOwnerId(userId);
    }

    @Override
    public JackRabbitNode getNodeByUserIdAndFileTypeId(long userId, long fileTypeId) {
        return jackRabbitNodeService.findByOwnerIdAndFileTypeId(userId, fileTypeId);
    }

    @Override
    public JackRabbitNode updateFileVerified(long fileId, boolean isVerified, String physicCode) {
        JackRabbitNode jackRabbitNode = jackRabbitNodeService.findById(fileId);
        if (jackRabbitNode != null) {
            jackRabbitNode.setVerified(isVerified);
            if (isVerified) {
                jackRabbitNode.setPhysicCode(physicCode);
            } else {
                jackRabbitNode.setPhysicCode(null);
            }
            return jackRabbitNodeService.save(jackRabbitNode);
        } else {
            return null;
        }
    }

    @Override
    public JackRabbitNode updateFileIsPermanent(long fileId, boolean isPermanent) {
        JackRabbitNode jackRabbitNode = jackRabbitNodeService.findById(fileId);
        if (jackRabbitNode != null) {
            jackRabbitNode.setPermanent(isPermanent);
            return jackRabbitNodeService.save(jackRabbitNode);
        }
        return null;
    }

    @Override
    public List<String> getFilePathsByJobIdsAndCategoryType(List<Long> jobIds, String category) {
        return jackRabbitNodeService.findByOwnerClassAndFileCategoryTypeAndOwnerIds(new Job(), category, jobIds)
                .stream().map(this::getNameFromNode).collect(Collectors.toList());
    }

    private String getNameFromNode(JackRabbitNode node) {
        String name = node.getPath();
        return name.contains(BRACKET) ? name.substring(1, name.indexOf(BRACKET)) : name;
    }

    @Override
    @Transactional
    public void saveWordDocument(long jobId, long fileTypeId, JackRabbitNode jackRabbitNode, XWPFDocument xwpfDocument,
                                 String removedWordFromFile) {
        try {
            Job job = new Job();
            job.setId(jobId);
            // Getting the name of the file
            String fileName = removedWordFromFile != null
                    ? jackRabbitNode.getFileName().replaceFirst("(?i)" + removedWordFromFile, "")
                    : jackRabbitNode.getFileName();
            // Getting the path of the file
            String parentPath = jackRabbitNode.getParentPath().substring(0, 1).equals(SLASH_SEPARATOR)
                    ? jackRabbitNode.getParentPath().substring(1)
                    : jackRabbitNode.getParentPath();
            // Writing the document
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            xwpfDocument.write(byteArrayOutputStream);
            xwpfDocument.close();
            byteArrayOutputStream.close();
            // Saving the file
            InputStream inputStreamConverted = StreamUtils.toInputStream(byteArrayOutputStream);
            saveFile("Formatted - " + fileName, fileTypeId, "", inputStreamConverted, job, parentPath, false);
        } catch (Exception e) {
            throw new RuntimeException("Error writing the document");
        }
    }


    @Override
    public void lockAndDownloadFilesByJobIdAndCategory(HttpServletResponse response, long jobId, String category,
                                                       String employeeEmail) {
        // Return file(s) to the user (it doesn't matter if the file is blocked)
        downloadFilesByJobIdAndCategory(response, jobId, category);
        try {
            Job job = new Job();
            job.setId(jobId);

            Set<FileType> fileTypes = fileTypeService.getFileTypesByJobIdAndCategory(jobId,
                    FileTypeCategory.valueOf(category));
            Employee employee = employeeService.findByEmail(employeeEmail);
            for (FileType fileType : fileTypes) {
                // Blocking ROI file(s)
                jobFileTypeLockService.acquireLock(job, employee, fileType);
            }
        } catch (Exception e) {
            // Silence the exception through when a file already is blocked
            logger.info("File is being edited by another user, the lock cannot be applied again", e);
        }
    }


/*    private JackRabbitNode obtainNode(Job job) {
        List<FileType> fileTypes = fileTypeService.findByFileTypeCategory(FileTypeCategory.ROI);
        List<JackRabbitNode> nodes = jackRabbitNodeService.findByOwnerClassAndOwnerIdAndFileTypeIn(job, fileTypes,
                job.getId());
        return nodes.stream().filter(node -> node.getPath().contains("xls")).findFirst().get();
    }*/


    @Override
    @Transactional
    public void saveTemplate(Job job, JackRabbitNode jackRabbitNode, ByteArrayOutputStream baos,
                             String removedWordFromFile) {
        try {
            // Getting the name of the file
            String fileName = removedWordFromFile != null
                    ? jackRabbitNode.getFileName().replaceFirst("(?i)" + removedWordFromFile, "")
                    : jackRabbitNode.getFileName();
            // Getting the path of the file
            String parentPath = jackRabbitNode.getParentPath().substring(0, 1).equals(SLASH_SEPARATOR)
                    ? jackRabbitNode.getParentPath().substring(1)
                    : jackRabbitNode.getParentPath();
            // Writing the document
            baos.close();
            // Saving the file
            InputStream inputStreamConverted = StreamUtils.toInputStream(baos);
            saveFile("Formatted - " + fileName, 1, "", inputStreamConverted, job, parentPath, false);
        } catch (Exception e) {
            throw new RuntimeException("Error writing the document");
        }
    }

    @Override
    public void createFolderStructure(String path) {
        try {
            String[] splitPath = path.split(SLASH_SEPARATOR);
            for (int i = 1; i <= splitPath.length; i++) {
                String parentPath = concatWord(splitPath, i);
                if (!jackRabbitService.getRootNode().hasNode(parentPath)) {
                    jackRabbitService.createFolderNode(splitPath[i - 1], i == 1 ? "" : concatWord(splitPath, i - 1));
                }
            }
        } catch (javax.jcr.RepositoryException e) {
            throw new RepositoryException("Error creating the folder", e);
        }
    }

    private String concatWord(String[] splitPath, int limit) {
        StringBuilder stringBuilder = new StringBuilder().append(splitPath[0]);
        for (int i = 1; i < limit; i++) {
            stringBuilder.append(SLASH_SEPARATOR).append(splitPath[i]);
        }
        return stringBuilder.toString();
    }
}
