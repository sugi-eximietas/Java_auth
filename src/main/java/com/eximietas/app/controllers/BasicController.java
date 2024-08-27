package com.eximietas.app.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eximietas.libs.authentication.basic.BasicAuth;

@RestController
public class BasicController {

    @BasicAuth
    @GetMapping("/basic")
    public String getBasic(){
        return new String("This is basic Authentication Controller");
    }
}
