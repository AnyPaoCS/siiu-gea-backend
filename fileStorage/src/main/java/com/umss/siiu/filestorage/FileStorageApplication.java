package com.umss.siiu.filestorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.umss.siiu"
})
public class FileStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileStorageApplication.class, args);
    }

}
