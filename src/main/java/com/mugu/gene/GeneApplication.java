package com.mugu.gene;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.TimeZone;

@SpringBootApplication
@EnableAsync
public class GeneApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeneApplication.class, args);
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }

}
