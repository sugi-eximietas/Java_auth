package com.example.oauth.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService{

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userRequest);
        logger.debug("User has Authorities {}", oAuth2User.getAuthorities());

        //to get scopes alone from token
        // Collection<String> scopes = userRequest.getAccessToken().getScopes();

        // Set<GrantedAuthority> scopeAuthorities = scopes
        //                     .stream()
        //                     .map(scope-> new SimpleGrantedAuthority("SCOPE_"+scope))
        //                     .collect(Collectors.toSet());

        //to get both roles and scopes from the user entity
        Set<GrantedAuthority> roleAuthorities = oAuth2User.getAuthorities().stream()
                                    .map(authority-> new SimpleGrantedAuthority(""+authority)).collect(Collectors.toSet());

        Set<GrantedAuthority> mappedAuthorities = roleAuthorities;
        // mappedAuthorities.addAll(scopeAuthorities);

        return new DefaultOAuth2User(mappedAuthorities, oAuth2User.getAttributes(), "login");
    }

}
