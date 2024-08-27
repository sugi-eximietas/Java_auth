package com.eximietas.libs.authentication.token.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterchain) throws ServletException, IOException {
        logger.debug("AUTHTOKENFILTER called for URI {}", request.getRequestURI());
        try {
            String token = jwtUtils.getTokenFromHeader(request);
            logger.debug("AUTH TOKEN FILTER token {}", token);

            if (token != null && jwtUtils.isTokenValid(token)) {
                String user = jwtUtils.getUserFromToken(token);

                UserDetails userDetails = userDetailsService.loadUserByUsername(user);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                logger.debug("ROLES from token {}", userDetails.getAuthorities());

                //enhancing authtoken with extra details form request
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //setting up the context for the duration of the request
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication {}", e);
        }

        //to continue the filterchain with the req & res
        filterchain.doFilter(request, response);
    }

}
