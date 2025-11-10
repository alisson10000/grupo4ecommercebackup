package com.serratec.ecommerce.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UploadConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Caminho real da pasta onde estão as imagens
        String uploadDir = System.getProperty("user.dir") + "/uploads/produtos/";

        // Expõe a URL http://localhost:8080/imagens/1.jpg
        registry.addResourceHandler("/imagens/**")
                .addResourceLocations("file:" + uploadDir);

        System.out.println("🟢 Servindo imagens de: " + uploadDir);
    }
}
