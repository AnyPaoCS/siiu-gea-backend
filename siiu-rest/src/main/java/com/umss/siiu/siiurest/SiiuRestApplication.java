package com.umss.siiu.siiurest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.umss.siiu"})
public class SiiuRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SiiuRestApplication.class, args);
    }

}
