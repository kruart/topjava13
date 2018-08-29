package ru.javawebinar.topjava.config;

import org.springframework.context.annotation.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
@ComponentScan(value = {
        "ru.javawebinar.topjava.**.repository.mock",
        "ru.javawebinar.topjava.**.web"
})
@Import({SpringToolsConfiguration.class, SpringSecurityConfiguration.class})
public class MockConfig {

    @Bean
    public MappingJackson2HttpMessageConverter messageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(Collections.singletonList(messageConverter()));
    }
}
