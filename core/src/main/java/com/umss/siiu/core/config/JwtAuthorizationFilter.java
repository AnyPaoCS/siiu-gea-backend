package com.umss.siiu.core.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umss.siiu.core.service.TokenService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String token = tokenService.extractToken(req);
        if (token != null) {
            try {
                var principal = tokenService.getTokenInformationAsString(token);
                var jsonNode = objectMapper.readTree(principal);
                var json = jsonNode.get("authorities");
                if (json != null) {
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    json.forEach(authority -> authorities.add(
                            new SimpleGrantedAuthority(authority.get("authority").textValue())));
                            var authentication = new UsernamePasswordAuthenticationToken(principal,null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JwtException e) {
                logger.error("Error validating the token", e);
            }
        }
        chain.doFilter(req, res);
    }
}
