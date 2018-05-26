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
 * Handles requests for facebook oauth2 authorization
 *
 * @author kruart on 26.05.2018.
 */
@Controller
@RequestMapping("/oauth/facebook")
public class FacebookOauth2Controller extends AbstractOauth2Controller {

    @Autowired
    @Qualifier("facebookOauth2Provider")
    private Oauth2Provider provider;

    /**
     * Performs redirect to facebook authorize url
     * more about: https://developers.facebook.com/docs/facebook-login/manually-build-a-login-flow
     * check: https://developers.facebook.com/tools/explorer
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

    /**
     * Calls the parent method to obtain the access token
     */
    @Override
    String getAccessToken(String code) {
        return super.getAccessToken(code, provider);
    }

    /**
     * Using an access token to receive necessary data from facebook
     */
    @Override
    UserTo getData(String token) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(provider.getUserDataUrl());

        ResponseEntity<JsonNode> tokenEntity = template.exchange(
                builder.build().encode().toUri(), HttpMethod.GET, new HttpEntity<>(createHeader(token)), JsonNode.class);
        String name = tokenEntity.getBody().get("name").asText();
        String email = tokenEntity.getBody().get("email").asText();
        return new UserTo(name, email);
    }
}
