package com.luizgms.todosimple.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica a configuração a todas as rotas da sua API
                .allowedOrigins("*") // Permite requisições de qualquer origem (ex: seu frontend)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite estes métodos HTTP
                .allowedHeaders("*"); // Permite todos os cabeçalhos na requisição
    }
}