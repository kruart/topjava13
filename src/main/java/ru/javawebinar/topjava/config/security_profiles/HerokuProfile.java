package ru.javawebinar.topjava.config.security_profiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import ru.javawebinar.topjava.config.SpringSecurityConfiguration;
import ru.javawebinar.topjava.web.oauth.provider.Oauth2Provider;

@Configuration
@Profile({"heroku"})
@PropertySources({
        @PropertySource(name = "herokuGithubProperty", value = "classpath:/oauth/heroku_github.properties"),
        @PropertySource(name = "herokuFacebookProperty", value = "classpath:/oauth/heroku_facebook.properties"),
        @PropertySource(name = "herokuGoogleProperty", value = "classpath:/oauth/heroku_google.properties"),
        @PropertySource(name = "herokuLinkedinProperty", value = "classpath:/oauth/heroku_linkedin.properties")
})
public class HerokuProfile {

    @Autowired
    private SpringSecurityConfiguration conf;

    @Bean
    public Oauth2Provider herokuGithubOauth2Provider() {
        return conf.createOauth2Provider("herokuGithubProperty");
    }

    @Bean
    public Oauth2Provider herokuFacebookOauth2Provider() {
        return conf.createOauth2Provider("herokuFacebookProperty");
    }

    @Bean
    public Oauth2Provider herokuGoogleOauth2Provider() {
        return conf.createOauth2Provider("herokuGoogleProperty");
    }

    @Bean
    public Oauth2Provider herokuLinkedinOauth2Provider() {
        return conf.createOauth2Provider("herokuLinkedinProperty");
    }
}
