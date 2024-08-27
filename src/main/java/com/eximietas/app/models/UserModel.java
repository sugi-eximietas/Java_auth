package com.eximietas.app.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserModel {
    private String username;
    private String password;
    private Set<String> roles;
}
