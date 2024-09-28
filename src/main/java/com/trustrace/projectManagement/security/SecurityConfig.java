package com.trustrace.projectManagement.security;

import com.trustrace.projectManagement.user.User;
import com.trustrace.projectManagement.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserService userService;
    @Autowired
    private AppUserConfig appUserConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .anyRequest().authenticated() // Require authentication for all requests
                )
                .httpBasic(withDefaults()) // Use HTTP Basic Authentication
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(username -> {
            User user = userService.findByName(username);
            if (user != null) {
                System.out.println("User found: " + user.getName());
                return org.springframework.security.core.userdetails.User
                        .withUsername(user.getName())
                        .password(user.getPassword())
                        .roles(user.getRole()) // Assign roles as needed
                        .build();
            } else {
                throw new UsernameNotFoundException("User not found");
            }
        }).passwordEncoder(appUserConfig.passwordEncoder());
        // Ensuring BCryptPasswordEncoder is used

        return authenticationManagerBuilder.build();
    }

}
