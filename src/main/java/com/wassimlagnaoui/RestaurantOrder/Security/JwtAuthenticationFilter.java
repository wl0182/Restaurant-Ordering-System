package com.wassimlagnaoui.RestaurantOrder.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // ✅ Skip JWT validation for auth endpoints
        if (request.getServletPath().startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }


        // 1. Get the Authorization header from the request
        final String authHeader = request.getHeader("Authorization");

        // 2. Check if the header is missing or doesn't start with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // If no valid token is found, just continue the filter chain
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract the actual token (removing "Bearer " prefix)
        final String jwtToken = authHeader.substring(7);

        // 4. Extract username (email) from the token
        final String userEmail = jwtService.extractUsername(jwtToken);

        //check if the rquest was already authenticated
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            // Load user details
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

            // validate the token
            if (jwtService.isTokenValid(jwtToken,userDetails)) {
                // 8. Create an Authentication object to represent the verified user
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                // 9. Set the authentication in the SecurityContext — tells Spring "this user is now authenticated"
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }

        filterChain.doFilter(request, response);

    }
}
