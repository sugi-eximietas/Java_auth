package com.example.rbacExample.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
// import src\main\java\com\libs\rbac\DetailsConfig.java

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/user")
public class UserController {
    @GetMapping("/home")
    @ResponseBody
    public String user(){
        return "Welcome user";
    }
}
