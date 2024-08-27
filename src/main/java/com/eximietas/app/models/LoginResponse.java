package com.eximietas.app.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String jwtToken;

    public LoginResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

}
