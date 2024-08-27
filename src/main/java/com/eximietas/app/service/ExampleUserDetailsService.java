package com.eximietas.app.service;

import com.eximietas.app.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class ExampleUserDetailsService implements UserDetailsService {

    private final Map<String, UserModel> users = new HashMap<>();

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(UserModel userModel, String role) {
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        Set<String> roles = new HashSet<>();
        roles.add(role);
        userModel.setRoles(roles);
        users.put(userModel.getUsername(), userModel);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(SimpleGrantedAuthority::new).toList()
        );
    }
}
