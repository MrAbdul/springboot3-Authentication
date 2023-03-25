package com.abdulrahman.auth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // This method is called for every incoming request and is responsible for authenticating the user.
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Extract the JWT token from the request header.
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        jwt=authHeader.substring(7);

        // Extract the email address from the JWT token using the JwtService class.
        userEmail=jwtService.extractUsername(jwt);

        // Check if a user is not already authenticated and if the extracted email is not null.
        if(userEmail !=null && SecurityContextHolder.getContext().getAuthentication()==null){
            // Load the UserDetails object for the extracted email using the UserDetailsService class.
            UserDetails userDetails=this.userDetailsService.loadUserByUsername(userEmail);
            // Check if the JWT token is valid for the UserDetails object using the JwtService class.
            if(jwtService.isTokenValid(jwt, userDetails)){
                // Create an authenticated authentication token with the UserDetails object and set it in the SecurityContext.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Pass the request and response to the next filter in the filter chain.
        filterChain.doFilter(request,response);
    }
}
