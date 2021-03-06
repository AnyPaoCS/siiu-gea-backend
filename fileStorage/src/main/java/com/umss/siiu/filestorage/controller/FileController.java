package com.umss.siiu.filestorage.controller;

import com.google.gson.Gson;
import com.umss.siiu.core.service.UserService;
import com.umss.siiu.filestorage.dto.FileSimpleInfoDto;
import com.umss.siiu.filestorage.dto.FileUploadDto;
import com.umss.siiu.filestorage.dto.ImageSimpleInfoDto;
import com.umss.siiu.filestorage.dto.JackRabbitNodeDto;
import com.umss.siiu.filestorage.model.FileType;
import com.umss.siiu.filestorage.model.FileTypeCategory;
import com.umss.siiu.filestorage.model.JackRabbitNode;
import com.umss.siiu.filestorage.service.FileService;
import com.umss.siiu.filestorage.service.FileTypeService;
import org.apache.poi.util.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/files")
public class FileController {

    private final FileService fileService;
    private final FileTypeService fileTypeService;
    private UserService userService;
    private final Gson gson;

    private static final String FILE_ID_NOT_EXISTS = "El archivo con el id solicitado no existe.";
    private static final String USER_HAS_NO_FILES = "El usuario no tiene archivos registrados.";

    public FileController(FileService fileService, FileTypeService fileTypeService, Gson gson, UserService userService) {
        this.fileService = fileService;
        this.fileTypeService = fileTypeService;
        this.gson = gson;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFile (@RequestParam("file")MultipartFile file, @RequestParam("fileInfoDto")String fileUpload) {
        var fileUploadDto = gson.fromJson(fileUpload, FileUploadDto.class);
        var user = userService.findByEmail(fileUploadDto.getEmail());
        var dto = new JackRabbitNodeDto();
        dto.setFile(file);
        dto.setDescription(fileUploadDto.getDescription());
        dto.setOwnerId(user.getId());
        dto.setOwnerClass("user");
        var fileType = fileTypeService.findByAbbreviation(fileUploadDto.getFileTypeAbbreviation());
        dto.setFileTypeId(fileType.getId());
        String folderPath = user.getId() + File.separator + fileType.getFileTypeCategory().name();
        dto.setParentPath(folderPath);
        var jackRabbitNode = fileService.getNodeByUserIdAndFileTypeId(user.getId(), fileType.getId());
        if (jackRabbitNode != null) {
            if (jackRabbitNode.isVerified()) {
                return new ResponseEntity<>("El tipo de archivo que desea subir ya se encuentra verificado.", HttpStatus.BAD_REQUEST);
            } else {
                dto.setId(jackRabbitNode.getId());
                fileService.replaceFile(dto);
                return new ResponseEntity<>(String.format("El archivo %s con ruta %s ha sido modificado correctamente con la nueva ruta %s/%s", file.getOriginalFilename(), jackRabbitNode.getPath(), dto.getParentPath(), file.getOriginalFilename()), HttpStatus.OK);
            }
        }
        fileService.createFolderStructure(folderPath);
        fileService.saveFile(dto, file.getOriginalFilename());
        JackRabbitNode node = fileService.getNodeByUserIdAndFileTypeId(user.getId(), fileType.getId());
        return new ResponseEntity<>(new FileSimpleInfoDto(node), HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<Object> downloadFile (@RequestParam("fileId")long fileId, HttpServletResponse httpServletResponse) {
        JackRabbitNode node = fileService.findByFileId(fileId);
        if (node == null) {
            return new ResponseEntity<>("El documento con el id provisto no existe.", HttpStatus.NOT_FOUND);
        }
        fileService.downloadFile(httpServletResponse, node.getPath());
        return new ResponseEntity<>("El documento ha sido obtenido exitosamente.", HttpStatus.OK);
    }

    @GetMapping("/file/{fileId}")
    public ResponseEntity<Object> getDocumentFileContent(@RequestParam("fileId") long fileId) {
        JackRabbitNode node = fileService.findByFileId(fileId);
        if (node == null) {
            return new ResponseEntity<>(FILE_ID_NOT_EXISTS, HttpStatus.BAD_REQUEST);
        }
        var is = fileService.getInputStreamFromNode(node.getPath());
        byte[] documentContent = null;
        try {
            documentContent = IOUtils.toByteArray(is);
        } catch (IOException e) {
            return new ResponseEntity<>("Error de servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(documentContent, HttpStatus.OK);
    }

    @PatchMapping("/setVerified")
    public ResponseEntity<Object> setFileIsVerified (@RequestParam("fileId")long fileId, @RequestParam("verified")boolean verified, @RequestParam("physicCode")String physicCode) {
        ResponseEntity<Object> responseEntity;
        JackRabbitNode node = fileService.updateFileVerified(fileId, verified, physicCode);
        if (node != null) {
            responseEntity = new ResponseEntity<>(fileId, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("EL archivo con el id solicitado no existe", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @PatchMapping("/setPermanent")
    public ResponseEntity<Object> setFileIsPermanent (@RequestParam("fileId")long fileId, @RequestParam("permanent")boolean permanent) {
        System.out.println("entro");
        ResponseEntity<Object> responseEntity;
        JackRabbitNode node = fileService.updateFileIsPermanent(fileId, permanent);
        if (node != null) {
            responseEntity = new ResponseEntity<>(fileId, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("EL archivo con el id solicitado no existe", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteFile (@RequestParam("fileId")long fileId) {
        JackRabbitNode node = fileService.findByFileId(fileId);
        if (node != null) {
            if (node.isPermanent()) {
                return new ResponseEntity<>("El archivo a eliminar esta marcado como permanente, no puede ser eliminado.", HttpStatus.BAD_REQUEST);
            } else {
                fileService.deleteFile(node.getPath());
                return new ResponseEntity<>("El archivo ha sido eliminado exitosamente", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("El archivo a eliminar no existe.", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/list/userId/{userId}")
    public ResponseEntity<Object> getFilesByUserId (@PathVariable("userId")long userId) {
        List<JackRabbitNode> nodeList = fileService.getFilesByUserId(userId);
        List<FileSimpleInfoDto> list = new ArrayList<>();
        if (nodeList != null && !nodeList.isEmpty()) {
            for (JackRabbitNode node : nodeList) {
                var dto = new FileSimpleInfoDto(node);
                list.add(dto);
            }
        } else {
            return new ResponseEntity<>(USER_HAS_NO_FILES, HttpStatus.OK);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/userId/{userId}/abbreviation/{fileTypeAbbreviation}")
    public ResponseEntity<Object> getFileByUserIdAndFileTypeAbbreviation (@PathVariable("userId")long userId, @PathVariable("fileTypeAbbreviation")String abbreviation) {
        var fileType = fileTypeService.findByAbbreviation(abbreviation);
        if (fileType == null) {
            return new ResponseEntity<>("La abreviaci??n ingresada no existe.", HttpStatus.BAD_REQUEST);
        }
        JackRabbitNode node = fileService.getNodeByUserIdAndFileTypeId(userId, fileType.getId());
        if (node != null) {
            var dto = new FileSimpleInfoDto(node);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        return new ResponseEntity<>(USER_HAS_NO_FILES, HttpStatus.OK);
    }

    @GetMapping("/list/userId/{userId}/fileTypeCategory/{fileTypeCategory}")
    public ResponseEntity<Object> getFilesByUserIdAndFileTypeCategory (@PathVariable("userId")long userId, @PathVariable("fileTypeCategory")String category) {
        List<FileType> fileTypes = fileTypeService.findByFileTypeCategory(FileTypeCategory.valueOf(category));
        if (fileTypes == null || fileTypes.isEmpty()) {
            return new ResponseEntity<>("El tipo de categor??a ingresado no existe.", HttpStatus.BAD_REQUEST);
        }
        List<JackRabbitNode> nodeList = fileService.getFilesByUserId(userId);
        if (nodeList == null || nodeList.isEmpty()) {
            return new ResponseEntity<>(USER_HAS_NO_FILES, HttpStatus.OK);
        }
        List<FileSimpleInfoDto> list = new ArrayList<>();
        for (FileType fileType: fileTypes) {
            for (JackRabbitNode node : nodeList) {
                if (node.getFileType().equals(fileType)) {
                    var dto = new FileSimpleInfoDto(node);
                    list.add(dto);
                }
            }
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/verify")
    public ResponseEntity<Object> verifyQRLegalizedDocument(@RequestParam("fileId") long fileId, @RequestParam("qrValue") String qrValue) {
        JackRabbitNode node = fileService.findByFileId(fileId);
        if (node == null) {
            return new ResponseEntity<>(FILE_ID_NOT_EXISTS, HttpStatus.BAD_REQUEST);
        }
        boolean res = node.getNodeId().equals(qrValue);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/upload/image")
    public ResponseEntity<Object> uploadSignatureImage(@RequestParam("userId") long userId, @RequestParam("image") MultipartFile file) {
        var dto = new JackRabbitNodeDto();
        dto.setFile(file);
        dto.setOwnerId(userId);
        dto.setDescription("imagen subida");
        dto.setOwnerClass("user");


        var fileType = fileTypeService.findByFileTypeCategory(FileTypeCategory.IMAGEN_FIRMA).get(0);
        dto.setFileTypeId(fileType.getId());
        String folderPath = userId + File.separator + fileType.getFileTypeCategory().name();
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
        return new ResponseEntity<>(new ImageSimpleInfoDto(node), HttpStatus.OK);
    }

    @GetMapping("/signature/{userId}")
    public ResponseEntity<Object> getSignatureImage(@PathVariable("userId") long userId) {
        var fileType = fileTypeService.findByFileTypeCategory(FileTypeCategory.IMAGEN_FIRMA).get(0);
        JackRabbitNode node = fileService.getNodeByUserIdAndFileTypeId(userId, fileType.getId());
        if (node == null) {
            return new ResponseEntity<>(FILE_ID_NOT_EXISTS, HttpStatus.BAD_REQUEST);
        }
        var is = fileService.getInputStreamFromNode(node.getPath());
        byte[] signatureImage = null;
        try {
            signatureImage = IOUtils.toByteArray(is);
        } catch (IOException e) {
            return new ResponseEntity<>("Error de servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(signatureImage, HttpStatus.OK);
    }

}
