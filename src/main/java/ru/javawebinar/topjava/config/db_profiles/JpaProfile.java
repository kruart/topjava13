package ru.javawebinar.topjava.config.db_profiles;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("jpa")
@Configuration
@ComponentScan("ru.javawebinar.topjava.repository.jpa")
public class JpaProfile extends AbstractJpaProfile {
}
