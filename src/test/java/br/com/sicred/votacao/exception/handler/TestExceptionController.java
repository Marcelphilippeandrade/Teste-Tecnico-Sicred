package br.com.sicred.votacao.exception.handler;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.sicred.votacao.exception.business.*;

@RestController
class TestExceptionController {

    @GetMapping("/test/sessao-ja-aberta")
    void sessaoJaAberta() {
        throw new SessaoJaAbertaException();
    }

    @GetMapping("/test/pauta-nao-encontrada")
    void pautaNaoEncontrada() {
        throw new PautaNotFoundException();
    }

    @GetMapping("/test/sessao-encerrada")
    void sessaoEncerrada() {
        throw new SessaoEncerradaException();
    }

    @GetMapping("/test/voto-duplicado")
    void votoDuplicado() {
        throw new VotoDuplicadoException();
    }

    @GetMapping("/test/sessao-em-andamento")
    void sessaoEmAndamento() {
        throw new SessaoEmAndamentoException();
    }

    @GetMapping("/test/uuid-invalido/{id}")
    void uuidInvalido(@PathVariable UUID id) {
    }
}

