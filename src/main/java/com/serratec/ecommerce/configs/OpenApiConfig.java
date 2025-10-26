package com.serratec.ecommerce.configs;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    //  As chaves agora estão alinhadas com o application.properties
    @Value("${grupo4ecommerce.openapi.dev-url}")
    private String devUrl;

    @Value("${grupo4ecommerce.openapi.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI ecommerceOpenAPI() {

        //  Servidor de Desenvolvimento
        Server devServer = new Server()
                .url(devUrl)
                .description("Servidor de desenvolvimento da API Serratec E-commerce Grupo 4");

        //  Servidor de Produção
        Server prodServer = new Server()
                .url(prodUrl)
                .description("Servidor de produção da API Serratec E-commerce Grupo 4");

        //  Contato
        Contact contact = new Contact()
                .name("Alisson Lima de Souza")
                .email("alisson.lima.souza@gmail.com")
                .url("https://github.com/alisson10000");

        //  Licença
        License mitLicense = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        //  Esquema de segurança (JWT Bearer)
        final String securitySchemeName = "bearerAuth";
        SecurityScheme bearerScheme = new SecurityScheme()
                .name(securitySchemeName)
                .description("Insira o token JWT no formato: **Bearer token**")
                .scheme("bearer")
                .bearerFormat("JWT")
                .type(SecurityScheme.Type.HTTP);

        //  Requisito de segurança global
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securitySchemeName);

        //  Informações gerais da API
        Info info = new Info()
                .title("Serratec E-commerce API - Grupo 4")
                .version("1.0.0")
                .description("API RESTful desenvolvida como trabalho prático do curso **Serratec FullStack**.\n\n"
                        + "🔸 Gerencia usuários, categorias e produtos.\n"
                        + "🔸 Implementa autenticação e autorização com **JWT**.\n"
                        + "🔸 Documentada via **Swagger / OpenAPI 3.0**.")
                .contact(contact)
                .license(mitLicense)
                .termsOfService("https://github.com/pedrosgleite/TrabalhoFinalAPI-Grupo4.git");

        //  Montagem final com segurança + servidores
        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer))
                .components(new Components().addSecuritySchemes(securitySchemeName, bearerScheme))
                .addSecurityItem(securityRequirement);
    }
}
