package com.eximietas.app.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
public class SignupResponse {
    private String username;
    private Collection<? extends GrantedAuthority> roles;

    public SignupResponse(String username, Collection<? extends GrantedAuthority> roles) {
        this.username = username;
        this.roles = roles;
    }
}
