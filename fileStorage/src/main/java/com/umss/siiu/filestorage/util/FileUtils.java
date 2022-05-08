/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.filestorage.util;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
    private FileUtils() {
    }

    public static MultipartFile toMultipart(String path) {
        var pathObject = Paths.get(path);
        URL resource = TypeReference.class.getResource(path);
        var file = new File(resource.getPath());
        String name = null;
        var contentType = "application/octet-stream";
        byte[] content = null;
        try {
            name = URLDecoder.decode(file.getName(), "UTF-8");
            content = Files.readAllBytes(pathObject);
        } catch (final IOException e) {
            // Do Nothing
        }
        return new MockMultipartFile(name, name, contentType, content);
    }

}
