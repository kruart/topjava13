package ru.javawebinar.topjava.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.cache.jcache.JCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;

@Configuration
@EnableCaching
public class SpringToolsConfiguration {

    @Bean(name = "cacheManager")
    public JCacheCacheManager cacheManager() throws URISyntaxException {
        JCacheCacheManager cacheManager = new JCacheCacheManager();
        cacheManager.setCacheManager(ehCacheCacheManager().getObject());
        return cacheManager;
    }

    @Bean
    public JCacheManagerFactoryBean ehCacheCacheManager() throws URISyntaxException {
        JCacheManagerFactoryBean cmfb = new JCacheManagerFactoryBean();
        cmfb.setCacheManagerUri(getClass().getResource("/cache/ehcache.xml").toURI());
        return cmfb;
    }

}
