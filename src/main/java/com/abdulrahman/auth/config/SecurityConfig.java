package com.abdulrahman.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception{
        http
                .csrf()
                .disable()
                //whitelisting
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/demo-controller/roleUser").hasRole("USER")
                        .requestMatchers("/api/v1/demo-controller/roleAdmin").hasRole("ADMIN")
                        .requestMatchers("/api/v1/demo-controller/roleAdminOrUser").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                //authenticatedRequests
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //authentication provider
                .authenticationProvider(authenticationProvider)
                //since we created the username password authentication token in the jwtAuthFilter we need to add the jwtFilter before the usernamePasswordAuthFilter before it.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
