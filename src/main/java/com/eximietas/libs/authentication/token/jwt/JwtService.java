package com.eximietas.libs.authentication.token.jwt;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    // private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;


    public String generateToken(String loginUsername, String loginPassword) throws JwtException {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUsername, loginPassword);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            return jwtUtils.generateToken(userDetails);
        } catch (AuthenticationException authException) {
            throw new JwtException(authException.getMessage());
        }


    }
}
