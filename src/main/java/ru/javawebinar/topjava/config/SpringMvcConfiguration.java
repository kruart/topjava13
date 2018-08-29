package ru.javawebinar.topjava.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import ru.javawebinar.topjava.web.converter.DateTimeFormatters;
import ru.javawebinar.topjava.web.json.JacksonObjectMapper;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Configuration
@ComponentScan("ru.javawebinar.**.web")
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SpringMvcConfiguration implements WebMvcConfigurer {

    @Value("#{systemEnvironment['TOPJAVA_ROOT']}")
    private String envRoot;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        <!--  all resources inside folder src/main/webapp/resources are mapped so they can be referred to inside JSP files -->
//    <mvc:resources mapping="/resources/**" location="/resources/"/>
//
//    <!-- use WebJars so Javascript and CSS libs can be declared as Maven dependencies (Bootstrap, jQuery...) -->
//    <mvc:resources mapping="/webjars/**" location="classpath:/META-INF/resources/webjars/"/>
        registry.addResourceHandler("/resources/**", "/webjars/**")
                .addResourceLocations("/resources/", "classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("common.properties"));
        configurer.setPlaceholderPrefix("${common.");
        return configurer;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonObjectMapper.getMapper();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateTimeFormatters.LocalDateFormatter());
        registry.addFormatter(new DateTimeFormatters.LocalTimeFormatter());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(createStringHttpMessageConverter());
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper()));

        WebMvcConfigurer.super.configureMessageConverters(converters);
    }

    private HttpMessageConverter<String> createStringHttpMessageConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter();
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(new MediaType("text", "plain", Charset.forName("UTF-8")));
        mediaTypes.add(new MediaType("text", "html", Charset.forName("UTF-8")));
        converter.setSupportedMediaTypes(mediaTypes);
        return converter;
    }

    @Bean
    public MappingJackson2HttpMessageConverter messageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(Collections.singletonList(messageConverter()));
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
        resource.setDefaultEncoding("UTF-8");
        resource.setBasename("file:///" + envRoot + "/config/messages/app");
        //change locale on default locale. Spring will search file messages.properties - is default locale
        resource.setFallbackToSystemLocale(false);
        return resource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(new Locale("ru"));
        return resolver;
    }

    @Override   //mvc:interceptors
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        registry.addInterceptor(interceptor);
    }
}
