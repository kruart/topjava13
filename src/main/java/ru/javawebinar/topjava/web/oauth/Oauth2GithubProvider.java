package ru.javawebinar.topjava.web.oauth;

public class Oauth2GithubProvider extends Oauth2Provider {
    private String emailDataUrl;

    public String getEmailDataUrl() {
        return emailDataUrl;
    }

    public void setEmailDataUrl(String emailDataUrl) {
        this.emailDataUrl = emailDataUrl;
    }
}
