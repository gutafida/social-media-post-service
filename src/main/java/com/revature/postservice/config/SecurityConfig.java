package com.revature.postservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // READ
                        .requestMatchers(HttpMethod.GET, "/posts/**")
                        .hasAuthority("SCOPE_posts.read")

                        // WRITE (create post, delete post, add comment)
                        .requestMatchers(HttpMethod.POST, "/posts/**")
                        .hasAuthority("SCOPE_posts.write")
                        .requestMatchers(HttpMethod.PUT, "/posts/**")
                        .hasAuthority("SCOPE_posts.write")
                        .requestMatchers(HttpMethod.DELETE, "/posts/**")
                        .hasAuthority("SCOPE_posts.write")

                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt());

        return http.build();
    }
}
