package ru.javawebinar.topjava.config.security_profiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import ru.javawebinar.topjava.config.SpringSecurityConfiguration;
import ru.javawebinar.topjava.web.oauth.provider.Oauth2Provider;

@Configuration
@Profile({"postgres", "hsqldb", "tomcat"})
@PropertySources({
        @PropertySource(name = "githubProperty", value = "classpath:/oauth/github.properties"),
        @PropertySource(name = "facebookProperty", value = "classpath:/oauth/facebook.properties"),
        @PropertySource(name = "googleProperty", value = "classpath:/oauth/google.properties"),
        @PropertySource(name = "linkedinProperty", value = "classpath:/oauth/linkedin.properties")
})
public class CommonProfile {

    @Autowired
    private SpringSecurityConfiguration conf;

    @Bean
    public Oauth2Provider githubOauth2Provider() {
        return conf.createOauth2Provider("githubProperty");
    }

    @Bean
    public Oauth2Provider facebookOauth2Provider() {
        return conf.createOauth2Provider("facebookProperty");
    }

    @Bean
    public Oauth2Provider googleOauth2Provider() {
        return conf.createOauth2Provider("googleProperty");
    }

    @Bean
    public Oauth2Provider linkedinOauth2Provider() {
        return conf.createOauth2Provider("linkedinProperty");
    }
}
