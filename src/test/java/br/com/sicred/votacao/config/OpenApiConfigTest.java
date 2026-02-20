package br.com.sicred.votacao.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

class OpenApiConfigTest {

    @Test
    void deveCriarBeanOpenApiComInformacoesCorretas() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(OpenApiConfig.class);

        OpenAPI openAPI = context.getBean(OpenAPI.class);

        assertNotNull(openAPI);

        Info info = openAPI.getInfo();
        assertNotNull(info);

        assertEquals("API de Votação - Versão 1", info.getTitle());
        assertEquals("API para gerenciamento de pautas, sessões e votos", info.getDescription());
        assertEquals("v1", info.getVersion());

        context.close();
    }
}
