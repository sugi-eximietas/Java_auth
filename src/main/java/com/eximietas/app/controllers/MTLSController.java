package com.eximietas.app.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eximietas.libs.authentication.mtls.MTLS;

@RestController
public class MTLSController {

    @MTLS
    @GetMapping("/mtls")
    public String getMtls(){
        return new String("This is MTLS controller");
    }


}
