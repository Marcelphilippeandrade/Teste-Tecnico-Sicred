package br.com.sicred.votacao.exception.handler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TestExceptionController.class)
@Import(ApiExceptionHandler.class)
class ApiExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveRetornar409QuandoSessaoJaAberta() throws Exception {
        mockMvc.perform(get("/test/sessao-ja-aberta"))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(409))
            .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Já existe uma sessão aberta para esta pauta.")));
    }

    @Test
    void deveRetornar404QuandoPautaNaoEncontrada() throws Exception {
        mockMvc.perform(get("/test/pauta-nao-encontrada"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Pauta não encontrada.")));
    }

    @Test
    void deveRetornar422QuandoSessaoEncerrada() throws Exception {
        mockMvc.perform(get("/test/sessao-encerrada"))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.status").value(422))
            .andExpect(jsonPath("$.message")
                .value(org.hamcrest.Matchers.containsString("Sessão de votação já encerrada")));
    }

    @Test
    void deveRetornar409QuandoVotoDuplicado() throws Exception {
        mockMvc.perform(get("/test/voto-duplicado"))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(409))
            .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Associado já votou nesta pauta.")));
    }

    @Test
    void deveRetornar422QuandoSessaoEmAndamento() throws Exception {
        mockMvc.perform(get("/test/sessao-em-andamento"))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.status").value(422))
            .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Sessão de votação ainda está em andamento")));
    }

    @Test
    void deveRetornar400QuandoUuidInvalido() throws Exception {
        mockMvc.perform(get("/test/uuid-invalido/abc"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message")
                .value(org.hamcrest.Matchers.containsString("Parâmetro 'id' inválido. Informe um UUID válido.")));
    }
}
