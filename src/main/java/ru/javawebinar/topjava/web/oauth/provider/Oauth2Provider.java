package ru.javawebinar.topjava.web.oauth.provider;

public class Oauth2Provider {
    private String clientId;
    private String secretId;
    private String authorizeUrl;
    private String redirectUri;
    private String accessTokenUrl;
    private String userDataUrl;
    private String scope;
    private final static String state = "tjua_csrf_token_oauth_Lk33j";

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getAuthorizeUrl() {
        return authorizeUrl;
    }

    public void setAuthorizeUrl(String authorizeUrl) {
        this.authorizeUrl = authorizeUrl;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getAccessTokenUrl() {
        return accessTokenUrl;
    }

    public void setAccessTokenUrl(String accessTokenUrl) {
        this.accessTokenUrl = accessTokenUrl;
    }

    public String getUserDataUrl() {
        return userDataUrl;
    }

    public void setUserDataUrl(String userDataUrl) {
        this.userDataUrl = userDataUrl;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public static String getState() {
        return state;
    }
}
