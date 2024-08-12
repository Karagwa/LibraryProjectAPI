package org.encode.libraryprojectapi.Config;

import org.encode.libraryprojectapi.Security.CustomUserDetailsService;
import org.encode.libraryprojectapi.Security.JwtRequestFilter;
import org.encode.libraryprojectapi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity

public class SecurityConfig {


    private CustomUserDetailsService userDetailsService;


    private JwtUtil jwtUtil;

    private AuthenticationManager authenticationManager;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtRequestFilter jwtRequestFilter) throws Exception{
        http.csrf(csrf->csrf.disable())
                .authorizeHttpRequests((requests)-> requests
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/signup/librarian").permitAll()
                        .requestMatchers("/authenticate","/register/**","/api/member/signup").permitAll()
                        .requestMatchers("/librarian/**").hasRole("LIBRARIAN")
                        .requestMatchers("/member/**").hasRole("MEMBER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }








}




