package ru.javawebinar.topjava.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan(basePackages = "ru.javawebinar.**.service")
@ImportResource({
        "classpath:spring/spring-tools.xml",
        "classpath:spring/spring-security.xml",
        "classpath:spring/spring-db.xml"})
public class SpringAppConfiguration {}
