package com.example.SpringSecurity.config;

import jakarta.annotation.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authentication");
        if (header == null || !header.startsWith("Bearer ")) {
            if (filterChain != null) {
                filterChain.doFilter(request, response);
            }
            return;
        }

        String token = header.substring(7);
        String username = jwtService.decodeToken(token);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, null);

        SecurityContextHolder.getContext().setAuthentication(auth);
        if (filterChain != null) {
            filterChain.doFilter(request, response);
        }
    }
}
