package ru.javawebinar.topjava.web.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
@RequestMapping("/oauth/github")
public class Oauth2GithubController {

    @Autowired
    private RestTemplate template;

    @Autowired
    private Oauth2GithubProvider provider;

    @Autowired
    private UserDetailsService service;


    @RequestMapping("/authorize")
    public String authorize() {
        return "redirect:" + provider.getAuthorizeUrl() +
                "?client_id=" + provider.getClientId() +
                "&redirect_uri=" + provider.getRedirectUri() +
                "&scope=" + provider.getScope() +
                "&state=" + provider.getState();
    }

    @RequestMapping("/callback")
    public ModelAndView authenticate(@RequestParam String code, @RequestParam String state, HttpServletRequest req) {
        if (provider.getState().equals(state)) {
            String token = getAccessToken(code, state);

            String name = getUserData(token);
            String email = getEmail(token);

            if (name == null || email == null) {
                throw new NotFoundException("Unsuccessful Githab authentication");
            }

            UserTo user = new UserTo(name, email);
            System.out.println();

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
        return new ModelAndView("login");
    }

    public String getAccessToken(String code, String state) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(provider.getAccessTokenUrl())
                .queryParam("client_id", provider.getClientId())
                .queryParam("client_secret", provider.getSecretId())
                .queryParam("code", code)
                .queryParam("redirect_url", provider.getRedirectUri())
                .queryParam("state", state);

        ResponseEntity<JsonNode> tokenEntity = template.postForEntity(builder.build().encode().toUri(), null, JsonNode.class);

        return tokenEntity.getBody().get("access_token").asText();
    }

    private String getUserData(String token) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(provider.getUserDataUrl())
                .queryParam("access_token", token);

        ResponseEntity<JsonNode> tokenEntity = template.exchange(
                builder.build().encode().toUri(), HttpMethod.GET, null, JsonNode.class);

        return tokenEntity.getBody().get("login").asText();
    }

    private String getEmail(String token) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(provider.getEmailDataUrl())
                .queryParam("access_token", token);

        ResponseEntity<JsonNode> tokenEntity = template.exchange(
                builder.build().encode().toUri(), HttpMethod.GET, null, JsonNode.class);

        return tokenEntity.getBody().get(0).get("email").asText();
    }
}
