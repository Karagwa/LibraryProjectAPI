package org.encode.libraryprojectapi.Config;

import org.encode.libraryprojectapi.Security.CustomUserDetailsService;
import org.encode.libraryprojectapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserDetailsServiceConfig {

    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsServiceConfig(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CustomUserDetailsService userDetailsService() {
        return new CustomUserDetailsService(userRepository, passwordEncoder);
    }
}

