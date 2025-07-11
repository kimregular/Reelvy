package com.reelvy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ReelvyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReelvyApplication.class, args);
    }
}
