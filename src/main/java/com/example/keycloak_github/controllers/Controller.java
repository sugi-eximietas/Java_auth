package com.example.keycloak_github.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    
    @GetMapping("/home")
    public String home(){
        return new String("This is home controller");
    }
    
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('client_admin')")
    public String admin(){
        return new String("This is the admin controller");
    }

}
