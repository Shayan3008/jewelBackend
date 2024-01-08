package com.jewelbackend.backend.security;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.Level;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.config.HelperUtils;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAccessFilter extends OncePerRequestFilter {


    private final JwtAuthConfig jwtAuthConfig;

    public JwtAccessFilter(JwtAuthConfig jwtAuthConfig){
        this.jwtAuthConfig = jwtAuthConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = jwtAuthConfig.resolveToken(request);
        HelperUtils.logMessage(Level.INFO, accessToken);
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }
        Claims claims = jwtAuthConfig.resolveClaims(request);

        if (claims != null && jwtAuthConfig.validateClaims(claims)) {
            String email = claims.getSubject();
            HelperUtils.logMessage(Level.INFO ,"email : " + email);
            
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, "", new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);

    }
}