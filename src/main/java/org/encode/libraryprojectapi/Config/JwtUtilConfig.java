package org.encode.libraryprojectapi.Config;

import org.encode.libraryprojectapi.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtUtilConfig {
    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }

}
