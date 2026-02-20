package br.com.sicred.votacao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openApiV1() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Votação - Versão 1")
                .description("API para gerenciamento de pautas, sessões e votos")
                .version("v1")
            );
    }
}
