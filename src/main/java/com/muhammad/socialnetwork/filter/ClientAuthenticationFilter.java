package com.muhammad.socialnetwork.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ClientAuthenticationFilter extends OncePerRequestFilter {
    @Value("${env.client_api_key}")
    private String clientApiKey;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    String authHeader = request.getHeader("X-CLIENT-API-KEY");
    String apiKey=null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            apiKey = authHeader.substring(7);
        }
        if (apiKey == null || !apiKey.equals(clientApiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid or missing Client API Key\"}");
            return; // CRITICAL: Stop the chain here
        }

        // 2. CONTINUE if fine
        filterChain.doFilter(request, response);
    }

}
