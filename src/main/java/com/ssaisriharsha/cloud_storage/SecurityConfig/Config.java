package com.ssaisriharsha.cloud_storage.SecurityConfig;

import com.ssaisriharsha.cloud_storage.Filters.JwtAuthenticationFilter;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class Config {
    @Bean
    public PasswordEncoder encoder() {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }
    @Bean
    public SecurityFilterChain httpConfig(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(
                        auth->auth
                                .requestMatchers("/api/v1/login", "/api/v1/signup").permitAll()
                                .anyRequest().authenticated()
                )
                .build();
    }
    @Bean
    public Validator esapiValidator() {
        return ESAPI.validator();
    }
}
