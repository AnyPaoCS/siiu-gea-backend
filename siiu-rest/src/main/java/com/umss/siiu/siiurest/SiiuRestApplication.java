package com.umss.siiu.siiurest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication(scanBasePackages = {"com.umss.siiu"})
@EnableScheduling
public class SiiuRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SiiuRestApplication.class, args);
    }

}
