package com.services.dm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests((authz) -> authz.anyRequest().permitAll());
        return http.csrf().disable().build();
    }
}
