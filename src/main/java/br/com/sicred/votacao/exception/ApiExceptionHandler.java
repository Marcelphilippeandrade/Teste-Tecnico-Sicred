package br.com.sicred.votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(PautaNotFoundException.class)
	public ResponseEntity<String> pautaNaoEncontrada() {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pauta não encontrada");
	}

	@ExceptionHandler(SessaoEncerradaException.class)
	public ResponseEntity<String> sessaoEncerrada() {
		return ResponseEntity.badRequest().body("Sessão encerrada");
	}
}
