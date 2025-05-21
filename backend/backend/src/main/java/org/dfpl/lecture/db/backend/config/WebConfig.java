package org.dfpl.lecture.db.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 반드시 **으로
                .allowedOrigins("http://localhost:5173") // React dev server
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // OPTIONS 중요!
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
