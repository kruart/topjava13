package ru.javawebinar.topjava.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "ru.javawebinar.**.service")
//@Import({SpringToolsConfiguration.class, SpringSecurityConfiguration.class})
public class SpringAppConfiguration {}
