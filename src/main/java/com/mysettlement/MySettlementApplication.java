package com.mysettlement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MySettlementApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySettlementApplication.class, args);
    }
}
