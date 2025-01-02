package com.mysettlement;

import com.mysettlement.global.configs.WebSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;

@Import(WebSecurityConfig.class)
@SpringBootApplication
@ConfigurationPropertiesScan
public class MySettlementApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySettlementApplication.class, args);
    }

}
