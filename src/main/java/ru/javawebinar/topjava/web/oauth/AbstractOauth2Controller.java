package ru.javawebinar.topjava.web.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.web.oauth.provider.Oauth2Provider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * @author kruart 26.05.2018.
 */
abstract public class AbstractOauth2Controller {

    @Autowired(required = false)
    RestTemplate template;

    @Autowired
    UserDetailsService service;

    /**
     * Users are redirected back to this endpoint by Resource Provider.
     * The Resource Provider passes us the authorization code and then we requests access token from github for futher authorization.
     */
    @RequestMapping("/callback")
    public ModelAndView authenticate(@RequestParam String code, @RequestParam String state, HttpServletRequest req) {
        if (Oauth2Provider.getState().equals(state)) {
            String token = getAccessToken(code);
            UserTo user = getData(token);

            try {
                UserDetails authorizedUser = service.loadUserByUsername(user.getEmail());
                SecurityContext sc = SecurityContextHolder.getContext();
                sc.setAuthentication(
                        new UsernamePasswordAuthenticationToken(authorizedUser, null, authorizedUser.getAuthorities())
                );

                // In order to set the authentication on the request and hence,
                // make it available for all subsequent requests from the client,
                // we need to manually set the SecurityContext containing the Authentication in the HTTP session
                HttpSession session = req.getSession(true);
                session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
                return new ModelAndView("redirect:/meals");
            }
            catch (UsernameNotFoundException e) {
                return new ModelAndView("profile")
                        .addObject("userTo", user)
                        .addObject("register", true);
            }
        }
        return new ModelAndView("redirect:/login");
    }

    /**
     * Gets Access Token from 'Resource Provider'
     */
    protected String getAccessToken(String code, Oauth2Provider provider) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(provider.getAccessTokenUrl())
                .queryParam("client_id", provider.getClientId())
                .queryParam("client_secret", provider.getSecretId())
                .queryParam("code", code)
                .queryParam("redirect_uri", provider.getRedirectUri())
                .queryParam("state", Oauth2Provider.getState());

        ResponseEntity<JsonNode> tokenEntity = template.postForEntity(builder.build().encode().toUri(), null, JsonNode.class);

        return tokenEntity.getBody().get("access_token").asText();
    }

    /**
     * Creates and returns http header with Access Token inside
     */
    HttpHeaders createHeader(String token) {
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + token);
        return header;
    }

    abstract String getAccessToken(String code);

    /**
     * Using an access token to receive necessary data from Resource Provider
     */
    abstract UserTo getData(String token);

    /**
     * Performs redirect to Resource Provider authorize url
     */
    abstract String authorize();
}
