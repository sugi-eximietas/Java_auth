package com.eximietas.libs.authentication.token.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
// import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
// import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private SecretKey key;

    @PostConstruct
    void init() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        logger.debug("AuthToken", bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateToken(UserDetails userDetails) {
        String username = userDetails.getUsername();
        /*Collection<? extends GrantedAuthority>*/String role = userDetails.getAuthorities().toString();
        Map<String, Object> claims = new HashMap<>();
        // claims.put("username", username);
        claims.put("roles", role);
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key)
                .claims(claims)
                .compact();
    }

    public String getUserFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Boolean isTokenValid(String authToken) {
        try {
            logger.debug("validating", authToken);
            Jwts.parser().verifyWith(key).build().parseSignedClaims(authToken);
            return true;
        } catch (JwtException e) {
            logger.error("JWT Exception {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT Claim String is missing {}", e.getMessage());
        }
        return false;
    }
}
