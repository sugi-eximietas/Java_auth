package com.keycloak.keycloak.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    
    // @GetMapping("/")
    // // @PreAuthorize("hasRole('client_user')")
    // public ResponseEntity<?> home() throws Exception{
    //     try{
    //     return ResponseEntity.ok("This is the user controller or home page");
    //     }
    //     catch(OAuth2AuthorizationException e){
    //         throw new RuntimeException(e);
    //     }
    // }

    @GetMapping("/home")
    public String home(){
        return new String("this is the home page of 8081");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('client_admin')")
    // @PreAuthorize("hasAuthority('SCOPE_email')")
    public ResponseEntity<?> admin(){
        try{
            return ResponseEntity.ok("This is the admin controller");
        }
        catch(Exception e){
            return ResponseEntity.ok("badddddddddd");
        }
    }

}
