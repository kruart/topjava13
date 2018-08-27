package ru.javawebinar.topjava.config.db_profiles;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Profile("datajpa")
@Configuration
@EnableJpaRepositories("ru.javawebinar.**.repository.datajpa")
@ComponentScan("ru.javawebinar.topjava.repository.datajpa")
public class DataJpaProfile extends AbstractJpaProfile {
}
