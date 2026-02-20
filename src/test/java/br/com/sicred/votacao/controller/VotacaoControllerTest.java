package br.com.sicred.votacao.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.sicred.votacao.dtos.PautaDTO;
import br.com.sicred.votacao.dtos.ResultadoDTO;
import br.com.sicred.votacao.dtos.VotoDTO;
import br.com.sicred.votacao.enums.TipoVoto;
import br.com.sicred.votacao.modelo.Pauta;
import br.com.sicred.votacao.modelo.Sessao;
import br.com.sicred.votacao.service.VotacaoService;

@WebMvcTest(VotacaoController.class)
class VotacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VotacaoService service;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------- POST /pautas ----------

    @Test
    void deveCriarPauta() throws Exception {
        Pauta pauta = new Pauta();
        pauta.setId(UUID.randomUUID());
        pauta.setTitulo("Pauta teste");

        when(service.criarPauta(any(PautaDTO.class))).thenReturn(pauta);

        PautaDTO dto = new PautaDTO("Pauta teste", "Descricao");

        mockMvc.perform(post("/api/v1/pautas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.titulo").value("Pauta teste"));
    }

    // ---------- POST /pautas/{id}/sessao ----------

    @Test
    void deveAbrirSessao() throws Exception {
        UUID pautaId = UUID.randomUUID();

        Pauta pauta = new Pauta();
        pauta.setId(pautaId);

        Sessao sessao = new Sessao();
        sessao.setId(UUID.randomUUID());
        sessao.setInicio(LocalDateTime.now());
        sessao.setFim(LocalDateTime.now().plusMinutes(1));
        sessao.setPauta(pauta);

        when(service.abrirSessao(eq(pautaId), any()))
            .thenReturn(sessao);

        mockMvc.perform(post("/api/v1/pautas/{id}/sessao", pautaId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.pautaId").value(pautaId.toString()));
    }

    // ---------- POST /pautas/{id}/votos ----------

    @Test
    void deveRegistrarVoto() throws Exception {
        UUID pautaId = UUID.randomUUID();

        VotoDTO dto = new VotoDTO("12345678901", TipoVoto.SIM);

        mockMvc.perform(post("/api/v1/pautas/{id}/votos", pautaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
    }

    // ---------- GET /pautas/{id}/resultado ----------

    @Test
    void deveRetornarResultado() throws Exception {
        UUID pautaId = UUID.randomUUID();

        ResultadoDTO resultado = new ResultadoDTO(3, 2);

        when(service.resultado(pautaId)).thenReturn(resultado);

        mockMvc.perform(get("/api/v1/pautas/{id}/resultado", pautaId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.votosSim").value(3))
            .andExpect(jsonPath("$.votosNao").value(2));
    }
}

