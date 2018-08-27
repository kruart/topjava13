package ru.javawebinar.topjava.config.db_profiles;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.sql.DataSource;

@Configuration
@Profile("tomcat")
@PropertySource(value = {"classpath:db/tomcat.properties"}, encoding = "UTF-8")
public class TomcatProfile {

    @Bean
    public DataSource tomcatDataSource() {
        JndiDataSourceLookup lookup = new JndiDataSourceLookup();
        return lookup.getDataSource("java:comp/env/jdbc/topjava");
    }
}
