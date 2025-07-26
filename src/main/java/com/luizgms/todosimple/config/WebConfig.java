package com.luizgms.todosimple.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//esssa classe vai se comunicar com o front end
@Configuration//fala para o servidor iniciar isso junto com a aplicação
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    
    public void addCoreMappings(CorsRegistry registry){
        registry.addMapping("/**");//qualquer reuqisição de fora vai vir por essa rota, libera tudo, impede que a API bloqueie requisições
        
    }
}
