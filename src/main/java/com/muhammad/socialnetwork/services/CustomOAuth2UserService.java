package com.muhammad.socialnetwork.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    
    private final GithubEmailFetcher emailFetcher;

    public CustomOAuth2UserService(GithubEmailFetcher emailFetcher) {
        this.emailFetcher = emailFetcher;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        Map<String, Object> attributes = new HashMap<>(user.getAttributes());

        // If the provider is GitHub and email is missing/invalid
        if ("github".equals(userRequest.getClientRegistration().getRegistrationId())) {
            String email = (String) attributes.get("email");
            if (email == null || !email.contains("@")) {
                String realEmail = emailFetcher.fetchEmail(userRequest);
                attributes.put("email", realEmail);
            }
        }

        return new DefaultOAuth2User(user.getAuthorities(), attributes, "email");
    }
}