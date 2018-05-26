package ru.javawebinar.topjava.web.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.oauth.provider.Oauth2GithubProvider;
import ru.javawebinar.topjava.web.oauth.provider.Oauth2Provider;

/**
 * Handles requests for github oauth2 authorization
 *
 * @author kruart 26.05.2018.
 */
@Controller
@RequestMapping("/oauth/github")
public class Oauth2GithubController extends AbstractOauth2Controller {

    @Autowired
    private Oauth2GithubProvider provider;

    /**
     * Performs redirect to github authorize url
     * more about: https://developer.github.com/apps/building-integrations/setting-up-and-registering-oauth-apps/
     */
    @RequestMapping("/authorize")
    @Override
    public String authorize() {
        return "redirect:" + provider.getAuthorizeUrl() +
                "?client_id=" + provider.getClientId() +
                "&redirect_uri=" + provider.getRedirectUri() +
                "&scope=" + provider.getScope() +
                "&state=" + Oauth2Provider.getState();
    }

    @Override
    /**
     * Calls the parent method to obtain the access token
     */
    String getAccessToken(String code) {
        return super.getAccessToken(code, provider);
    }

    /**
     * Using an access token to receive necessary data from github
     */
    @Override
    UserTo getData(String token) {
        String name = getUserData(token);
        String email = getEmail(token);

        if (name == null || email == null) {
            throw new NotFoundException("Unsuccessful Github authentication");
        }

        return new UserTo(name, email);
    }

    /**
     * Using an access token to receive username from github
     */
    private String getUserData(String token) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(provider.getUserDataUrl())
                .queryParam("access_token", token);

        ResponseEntity<JsonNode> tokenEntity = template.exchange(
                builder.build().encode().toUri(), HttpMethod.GET, new HttpEntity<>(createHeader(token)), JsonNode.class);

        return tokenEntity.getBody().get("login").asText();
    }

    /**
     * Using an access token to receive email from github
     */
    private String getEmail(String token) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(provider.getEmailDataUrl());

        ResponseEntity<JsonNode> tokenEntity = template.exchange(
                builder.build().encode().toUri(), HttpMethod.GET, new HttpEntity<>(createHeader(token)), JsonNode.class);

        return tokenEntity.getBody().get(0).get("email").asText();
    }
}
