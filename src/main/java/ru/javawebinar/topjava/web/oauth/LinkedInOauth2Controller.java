package ru.javawebinar.topjava.web.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.web.oauth.provider.Oauth2Provider;

/**
 * Handles requests for linkedin oauth2 authorization
 *
 * @author kruart on 22.08.2018.
 */
@Controller
@RequestMapping("/oauth/linkedin")
public class LinkedInOauth2Controller extends AbstractOauth2Controller {

    @Autowired
    @Qualifier("linkedinOauth2Provider")
    private Oauth2Provider provider;

    /**
     * Performs redirect to linkedin authorize url
     * more about: hhttps://developer.linkedin.com/docs/oauth2#
     */
    @RequestMapping("/authorize")
    @Override
    public String authorize() {
        return "redirect:" + provider.getAuthorizeUrl() +
                "?client_id=" + provider.getClientId() +
                "&redirect_uri=" + provider.getRedirectUri() +
                "&scope=" + provider.getScope() +
                "&state=" + Oauth2Provider.getState() +
                "&response_type=code";
    }

    /**
     * Delegates to another method to obtain the access token
     */
    @Override
    String getAccessToken(String code) {
        return getAccessToken(code, provider);
    }

    /**
     * Gets Access Token from 'Resource Provider'
     */
    @Override
    protected String getAccessToken(String code, Oauth2Provider provider) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(provider.getAccessTokenUrl())
                .queryParam("client_id", provider.getClientId())
                .queryParam("client_secret", provider.getSecretId())
                .queryParam("code", code)
                .queryParam("redirect_uri", provider.getRedirectUri())
                .queryParam("grant_type", "authorization_code");

        ResponseEntity<JsonNode> tokenEntity = template.postForEntity(builder.build().encode().toUri(), null, JsonNode.class);

        return tokenEntity.getBody().get("access_token").asText();
    }

    /**
     * Using an access token to receive necessary data from linkedin
     */
    @Override
    UserTo getData(String token) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(provider.getUserDataUrl());

        ResponseEntity<JsonNode> tokenEntity = template.exchange(
                builder.build().encode().toUri(), HttpMethod.GET, new HttpEntity<>(createHeader(token)), JsonNode.class);
        String firstName = tokenEntity.getBody().get("firstName").asText();
        String lastName = tokenEntity.getBody().get("lastName").asText();
        String email = tokenEntity.getBody().get("emailAddress").asText();
        return new UserTo(firstName + " " + lastName, email);
    }
}
