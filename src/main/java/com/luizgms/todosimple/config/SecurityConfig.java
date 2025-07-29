package com.luizgms.todosimple.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // <-- Atualizado
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource; // <-- Atualizado
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // <-- Atualizado: substitui @EnableGlobalMethodSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_MATCHERS = {
            "/" // Endpoint raiz
    };

    private static final String[] PUBLIC_MATCHERS_POST = {
            "/user", // Endpoint para criar usuário
            "/login" // Endpoint para login
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF (bom para APIs REST stateless)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configura CORS usando o Bean
                                                                                   // abaixo
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sessões
                                                                                                              // stateless
                                                                                                              // (não
                                                                                                              // guarda
                                                                                                              // estado
                                                                                                              // no
                                                                                                              // servidor)
                .authorizeHttpRequests(authorize -> authorize // <-- Nova API de autorização (lambda)
                        .requestMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll() // Permite POST para /user e
                                                                                            // /login
                        .requestMatchers(HttpMethod.GET, PUBLIC_MATCHERS).permitAll() // Permite GET para / (endpoint
                                                                                      // raiz)
                        .anyRequest().authenticated() // Qualquer outra requisição precisa de autenticação
                );

        // Se você estiver usando o H2 Console, adicione estas linhas para permitir o
        // frame:
        // http.headers(headers -> headers.frameOptions(frameOptions ->
        // frameOptions.disable()));

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite todas as origens, métodos e cabeçalhos por padrão (bom para
        // desenvolvimento)
        configuration.setAllowedOrigins(List.of("*")); // Ou defina origens específicas:
                                                       // List.of("http://localhost:3000")
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(false); // Geralmente true para cookies, mas false para tokens JWT se você não
                                                  // envia credenciais com requests cross-origin.

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica a configuração a todos os caminhos
        return source;
    }
}