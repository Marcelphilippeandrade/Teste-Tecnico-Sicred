package br.com.sicred.votacao.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.sicred.votacao.dtos.PautaDTO;
import br.com.sicred.votacao.dtos.ResultadoDTO;
import br.com.sicred.votacao.dtos.VotoDTO;
import br.com.sicred.votacao.modelo.Pauta;
import br.com.sicred.votacao.modelo.Sessao;
import br.com.sicred.votacao.service.VotacaoService;

@RestController
@RequestMapping("/api/v1/pautas")
public class VotacaoController {

    private final VotacaoService service;

    public VotacaoController(VotacaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Pauta> criar(@RequestBody PautaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(service.criarPauta(dto));
    }

    @PostMapping("/{id}/sessao")
    public ResponseEntity<Sessao> abrirSessao(
        @PathVariable UUID id,
        @RequestParam(required = false) Long minutos) {
        return ResponseEntity.ok(service.abrirSessao(id, minutos));
    }

    @PostMapping("/{id}/votos")
    public ResponseEntity<Void> votar(
        @PathVariable UUID id,
        @RequestBody VotoDTO dto) {
        service.votar(id, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/resultado")
    public ResultadoDTO resultado(@PathVariable UUID id) {
        return service.resultado(id);
    }
}

