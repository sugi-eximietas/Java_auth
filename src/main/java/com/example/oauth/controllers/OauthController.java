package com.example.oauth.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OauthController {

    @Autowired
    private OAuth2AuthorizedClientManager authorizedClientManager;

    @GetMapping("/public")
    public String pub(){
        return new String("THIS IS NOT SECURED");
    }

    @GetMapping("/securedadmin")
    @PreAuthorize("hasAuthority('SCOPE_admin:org')")
    public String securedAdmin(){
        return new String("This is secured ADMIN controller");
    }
    
    @GetMapping("/secureduser")
    @PreAuthorize("hasAuthority('OAUTH2_USER')")
    public String securedUser(){
        return new String("THIS IS SECURED for OAUTH2_USER");
    }

    @GetMapping("/securedscope")
    @PreAuthorize("hasAuthority('SCOPE_user:email')")
    public String securedScope(){
        return new String("THIS IS SECURED with scope of SCOPE_user:email");
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal OAuth2User user){
        // var authentication = SecurityContextHolder.getContext().getAuthentication();
        // var authorities = authentication.getAuthorities().toString();
        return "hello, UserDetails: "+user.getAttributes()+"!<br><br> AUTHORITIES: "+user.getAuthorities();
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('SCOPE_admin:org')")
    public String profile(OAuth2AuthenticationToken authtoken){
        return authtoken.getAuthorities().toString(); 
    }

    //still in works to collect user data using OAUTH2 client
    @GetMapping("/repos")
    @PreAuthorize("hasAuthority('OAUTH2_USER')")
        public List<Map<String, Object>> getRepos(@AuthenticationPrincipal OAuth2User user, @RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient authorizedClient){
            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withAuthorizedClient(authorizedClient)
                                    .principal(user.getAttributes().toString())
                                    .build();

            OAuth2AuthorizedClient updatedAuth2Client = authorizedClientManager.authorize(authorizeRequest);
            OAuth2AccessToken accessToken = updatedAuth2Client.getAccessToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken.getTokenValue());
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://api.github.com/user/repos";

            ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET,entity,List.class); 
            
            return /*(List<Map<String, Object>>)*/ response.getBody();
        }
}
