package com.umss.siiu.filestorage.service;

import com.umss.siiu.filestorage.dto.JackRabbitNodeDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


@RunWith(SpringRunner.class)
@SpringBootTest
public class FileServiceIT {
    @Autowired
    private FileService fileService;

    @Test
    public void uploadFile() throws IOException {
        JackRabbitNodeDto jackRabbitNodeDto = new JackRabbitNodeDto();
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "fileToUpload.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        jackRabbitNodeDto.setFile(file);
        jackRabbitNodeDto.setDescription("testFile");
        jackRabbitNodeDto.setOwnerId(1L);
        jackRabbitNodeDto.setOwnerClass("user");
        jackRabbitNodeDto.setFileTypeId(1L);//None
        jackRabbitNodeDto.setParentPath("");
        fileService.saveFile(jackRabbitNodeDto, jackRabbitNodeDto.getFile().getOriginalFilename());
        fileService.saveFile(jackRabbitNodeDto, jackRabbitNodeDto.getFile().getOriginalFilename());
        InputStream jackRabbitStream = fileService.getInputStreamFromNode("fileToUpload.txt");
        File targetFile = new File("./targetFile.txt");
        targetFile.createNewFile();
        org.apache.commons.io.FileUtils.copyInputStreamToFile(jackRabbitStream, targetFile);
        Assert.assertNotNull(jackRabbitNodeDto);
    }
}
