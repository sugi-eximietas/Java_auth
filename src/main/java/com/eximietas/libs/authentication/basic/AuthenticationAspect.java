package com.eximietas.libs.authentication.basic;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Aspect
@Component
public class AuthenticationAspect {

    @Value("${auth.username}")
    private String authUsername;

    @Value("${auth.password}")
    private String authPassword;

    @Value("${auth.ztoken}")
    private String authZToken;

    @Around("@annotation(com.eximietas.libs.authentication.basic.BasicAuth)")
    public Object authenticateBasic(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getResponse();

        try {
            // Extract ztoken from headers
            String ztoken = request.getHeader("ztoken");
            if (ztoken == null || !ztoken.equals(authZToken)) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid or missing ztoken");
                return null;
            }

            // Extract Basic Authentication credentials
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Basic ")) {
                String base64Credentials = authHeader.substring(6);
                byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
                String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
                String[] credentials = decodedString.split(":", 2);
                String username = credentials[0];
                String password = credentials[1];

                // Validate the credentials
                if (username.equals(authUsername) && password.equals(authPassword)) {
                    return joinPoint.proceed();
                } else {
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials");
                    return null;
                }
            } else {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Basic authentication required");
                return null;
            }
        } catch (Throwable t) {
            // Log or handle other exceptions
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred during authentication");
            return null;
        }
    }
}
