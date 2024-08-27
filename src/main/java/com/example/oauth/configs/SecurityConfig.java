package com.example.oauth.configs;

import com.example.oauth.services.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
// import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
// import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
// import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
// import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
// import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.HttpStatusEntryPoint;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Bean
    public CustomOAuth2UserService customOAuth2UserService(){
        return new CustomOAuth2UserService();
    }

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        
        http.authorizeHttpRequests(req->req.anyRequest().permitAll());

        http.oauth2Login(oauth2->oauth2.userInfoEndpoint()
                                .userService(customOAuth2UserService())); //for user login
        
        http.oauth2Client(Customizer.withDefaults()); // for accessing user data inplace of user 
        // http.oauth2ResourceServer(jwt->jwt.jwt(Customizer.withDefaults())); //can only be used with auth server it only provides jwt


        http.csrf(csrf->csrf.disable());

        return http.build();
    }


    //to instantiate the OAUTH2CLIENT 
    // @Bean
    // public OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository authorizedClientRepository){
    //     OAuth2AuthorizedClientProvider authorizedClientProvider = 
    //                         OAuth2AuthorizedClientProviderBuilder.builder()
    //                         .authorizationCode()
    //                         .refreshToken()
    //                         .clientCredentials()
    //                         .build();
        
    //     DefaultOAuth2AuthorizedClientManager authorizedClientManager = 
    //                         new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);

    //     authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

    //     return authorizedClientManager;
    // }

}
