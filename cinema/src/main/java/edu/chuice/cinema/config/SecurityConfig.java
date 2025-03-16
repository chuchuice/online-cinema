package edu.chuice.cinema.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(request -> {
                return new CorsConfiguration() {{
                    setAllowedOrigins(List.of("*"));
                    setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    setAllowedHeaders(List.of("*"));
                }};
            }))

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/api/v1/artists").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/artists/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/v1/artists/search/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/artists").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/api/v1/artists/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/v1/artists/**").permitAll()
                .anyRequest().authenticated()
            )

            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable);
        return http.build();
    }
}