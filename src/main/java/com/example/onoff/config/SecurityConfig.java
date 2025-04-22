package com.example.onoff.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Value("${spring.profiles.active:}")
    private String activeProfiles;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            if (activeProfiles.contains("dev")) {
                auth.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll();
            }
            auth.anyRequest().authenticated();
        });
        if (activeProfiles.contains("dev")) {
            http
                .csrf(csrf ->
                    csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                )
                .headers(headers ->
                    headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );
        }
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
