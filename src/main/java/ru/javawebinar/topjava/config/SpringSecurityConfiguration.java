package ru.javawebinar.topjava.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.javawebinar.topjava.web.oauth.provider.Oauth2Provider;

@Configuration
@EnableWebSecurity
@ComponentScan("ru.javawebinar.topjava.config.security_profiles")
public class SpringSecurityConfiguration {

    @Autowired
    @Qualifier("userService")
    private UserDetailsService userDetailsService;

    @Autowired
    private StandardEnvironment env;

    public Oauth2Provider createOauth2Provider(String propertySourceName) {
        PropertySource<?> propertySource = env.getPropertySources().get(propertySourceName);

        Oauth2Provider oauth2Provider = new Oauth2Provider();
        oauth2Provider.setClientId(propertySource.getProperty("clientId").toString());
        oauth2Provider.setSecretId(propertySource.getProperty("secretId").toString());
        oauth2Provider.setAuthorizeUrl(propertySource.getProperty("authorizeUrl").toString());
        oauth2Provider.setAccessTokenUrl(propertySource.getProperty("accessTokenUrl").toString());
        oauth2Provider.setRedirectUri(propertySource.getProperty("redirectUri").toString());
        oauth2Provider.setUserDataUrl(propertySource.getProperty("userDataUrl").toString());
        oauth2Provider.setScope(propertySource.getProperty("scope").toString());

        return oauth2Provider;
    }

    @Bean
    public PasswordEncoder passwordEncoderFactories() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoderFactories());
    }

    @Configuration
    @Order(1)
    public static class RestWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/rest/**")
                    .httpBasic()
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/rest/admin/**").hasRole("ADMIN")
                    .antMatchers("/**").authenticated()
                    .and()
                    .csrf().disable();
        }
    }

    @Configuration
    @Order(2)
    public static class CommonWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/resources/**", "/webjars/**");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/oauth/**", "/login", "/register").anonymous()
                    .antMatchers("/**/admin/**").hasRole("ADMIN")
                    .antMatchers("/**").authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/meals")
                    .failureUrl("/login?error=true")
                    .loginProcessingUrl("/spring_security_check")
                    .and()
                    .logout().logoutSuccessUrl("/login");
//                    .and()
//                    .csrf();  //csrf default true
        }

//        @Bean
//        public PasswordEncoder passwordEncoderFactories() {
//            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        }
//
//        @Override
//        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoderFactories());
//        }
    }
}
