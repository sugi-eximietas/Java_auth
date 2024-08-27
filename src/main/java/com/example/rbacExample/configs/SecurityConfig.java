package com.example.rbacExample.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

// import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
// @EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig{

    @Bean
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
            .anyRequest()
            .fullyAuthenticated()
            .and()
            .httpBasic();

            // return http.build();
        
    }

}
