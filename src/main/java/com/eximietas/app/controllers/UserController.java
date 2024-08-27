package com.eximietas.app.controllers;

import com.eximietas.app.models.LoginRequest;
import com.eximietas.app.models.LoginResponse;
import com.eximietas.app.models.SignupRequest;
import com.eximietas.app.models.UserModel;
import com.eximietas.app.service.ExampleUserDetailsService;
import com.eximietas.libs.authentication.token.jwt.JwtException;
import com.eximietas.libs.authentication.token.jwt.JwtService;
import com.eximietas.libs.authorization.rbac.Roles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ExampleUserDetailsService userDetailsService;


    //JWT CONTROLLERS 
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String user() {
        return new String("This is user controller");
    }

    @GetMapping("/guest")
    @PreAuthorize("hasRole('GUEST')")
    public String guest() {
        return new String("This is guest controller");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return new String("This is admin controller");
    }

    @GetMapping("/public")
    public String pub() {
        return new String("This is the public controller");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            String jwtToken = jwtService.generateToken(loginRequest.getUsername(), loginRequest.getPassword());
            LoginResponse response = new LoginResponse(jwtToken);
            return ResponseEntity.ok(response);
        } catch (JwtException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        
        try{
            UserModel user = new UserModel();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(signupRequest.getPassword());
        userDetailsService.saveUser(user, Roles.valueOf(signupRequest.getRole()).toString());
        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException(e);
        }

        return ResponseEntity.ok("USER ADDED!!!!!");
    }

}
