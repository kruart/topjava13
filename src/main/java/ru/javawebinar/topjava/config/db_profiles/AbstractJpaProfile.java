package ru.javawebinar.topjava.config.db_profiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.javawebinar.topjava.repository.JpaUtil;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@EnableTransactionManagement
@Profile({"jpa", "datajpa"})
abstract public class AbstractJpaProfile {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("ru.javawebinar.**.model");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(Boolean.valueOf(env.getRequiredProperty("jpa.showSql")));
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaPropertyMap(additionalProperties());

        return em;
    }

    Map<String, Object> additionalProperties() {
        Map<String, Object> jpaMap = new HashMap<>();
        jpaMap.put(org.hibernate.cfg.AvailableSettings.FORMAT_SQL, "true");
        jpaMap.put(org.hibernate.cfg.AvailableSettings.USE_SQL_COMMENTS, "true");
        jpaMap.put(org.hibernate.cfg.AvailableSettings.CACHE_REGION_FACTORY, "org.hibernate.cache.jcache.JCacheRegionFactory");
        jpaMap.put(org.hibernate.cache.jcache.JCacheRegionFactory.PROVIDER, "org.ehcache.jsr107.EhcacheCachingProvider");
        jpaMap.put(org.hibernate.cfg.AvailableSettings.USE_SECOND_LEVEL_CACHE, "true");
        jpaMap.put(org.hibernate.cfg.AvailableSettings.USE_QUERY_CACHE, "false");
        jpaMap.put(org.hibernate.cfg.AvailableSettings.JPA_PERSIST_VALIDATION_GROUP, "ru.javawebinar.topjava.View$Persist");
        jpaMap.put(org.hibernate.cfg.AvailableSettings.JPA_UPDATE_VALIDATION_GROUP, "ru.javawebinar.topjava.View$Persist");
        return jpaMap;
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public JpaUtil jpaUtil() {
        return new JpaUtil();
    }
}
