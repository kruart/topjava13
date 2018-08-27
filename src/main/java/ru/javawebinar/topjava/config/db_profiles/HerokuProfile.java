package ru.javawebinar.topjava.config.db_profiles;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@Profile("heroku")
@PropertySource(value = {"classpath:db/heroku.properties"}, encoding = "UTF-8")
public class HerokuProfile {

    @Autowired
    private Environment env;

    @Bean
    public DataSource herokuDataSource() throws URISyntaxException {
        DataSource dataSource = new DataSource();
        URI dbUrl = new URI(env.getRequiredProperty("DATABASE_URL"));
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://" + dbUrl.getHost() + dbUrl.getPath());
        dataSource.setUsername(dbUrl.getUserInfo().split(":")[0]);
        dataSource.setPassword(dbUrl.getUserInfo().split(":")[1]);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setMaxActive(10);
        dataSource.setMinIdle(2);
        dataSource.setMaxWait(20000);
        dataSource.setInitialSize(2);
        dataSource.setMaxIdle(5);
        dataSource.setTestOnBorrow(true);
        dataSource.setRemoveAbandoned(true);
        dataSource.setTestOnConnect(true);
        dataSource.setTestWhileIdle(true);
        return dataSource;
    }

}
