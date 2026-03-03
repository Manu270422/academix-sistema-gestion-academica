package com.academix.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Definimos el encriptador como un Bean para usarlo en el Service de Usuario
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Configuramos la seguridad para que no bloquee tu Frontend actual
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitamos CSRF para poder usar Postman y Fetch fácilmente
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Por ahora permitimos todo para no romper tu flujo de desarrollo
            )
            .headers(headers -> headers.frameOptions(frame -> frame.disable())); // Por si usas consola H2
        
        return http.build();
    }
}