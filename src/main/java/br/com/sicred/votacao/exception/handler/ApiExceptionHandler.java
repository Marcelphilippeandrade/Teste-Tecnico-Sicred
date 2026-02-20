package br.com.sicred.votacao.exception.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import br.com.sicred.votacao.dtos.ApiErrorResponse;
import br.com.sicred.votacao.exception.business.CpfInvalidoException;
import br.com.sicred.votacao.exception.business.CpfNaoPodeVotarException;
import br.com.sicred.votacao.exception.business.PautaNotFoundException;
import br.com.sicred.votacao.exception.business.SessaoEmAndamentoException;
import br.com.sicred.votacao.exception.business.SessaoEncerradaException;
import br.com.sicred.votacao.exception.business.SessaoJaAbertaException;
import br.com.sicred.votacao.exception.business.VotoDuplicadoException;

@RestControllerAdvice
public class ApiExceptionHandler {
	
	@ExceptionHandler(SessaoJaAbertaException.class)
    public ResponseEntity<ApiErrorResponse> handleSessaoJaAberta(
            SessaoJaAbertaException ex) {

        ApiErrorResponse error = new ApiErrorResponse(
            HttpStatus.CONFLICT.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );

        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(error);
    }

	@ExceptionHandler(PautaNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handlePautaNotFound(PautaNotFoundException ex) {

        ApiErrorResponse error = new ApiErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error);
    }

	@ExceptionHandler(SessaoEncerradaException.class)
	public ResponseEntity<ApiErrorResponse> handleSessaoEncerrada(
	        SessaoEncerradaException ex) {

	    ApiErrorResponse error = new ApiErrorResponse(
	        HttpStatus.UNPROCESSABLE_ENTITY.value(),
	        ex.getMessage(),
	        LocalDateTime.now()
	    );

	    return ResponseEntity
	        .status(HttpStatus.UNPROCESSABLE_ENTITY)
	        .body(error);
	}
	
	@ExceptionHandler(VotoDuplicadoException.class)
	public ResponseEntity<ApiErrorResponse> handleVotoDuplicado(
	        VotoDuplicadoException ex) {

	    ApiErrorResponse error = new ApiErrorResponse(
	        HttpStatus.CONFLICT.value(),
	        ex.getMessage(),
	        LocalDateTime.now()
	    );

	    return ResponseEntity
	        .status(HttpStatus.CONFLICT)
	        .body(error);
	}
	
	@ExceptionHandler(SessaoEmAndamentoException.class)
	public ResponseEntity<ApiErrorResponse> handleSessaoEmAndamento(
	        SessaoEmAndamentoException ex) {

	    ApiErrorResponse error = new ApiErrorResponse(
	        HttpStatus.UNPROCESSABLE_ENTITY.value(),
	        ex.getMessage(),
	        LocalDateTime.now()
	    );

	    return ResponseEntity
	        .status(HttpStatus.UNPROCESSABLE_ENTITY)
	        .body(error);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ApiErrorResponse> handleMethodArgumentTypeMismatch(
	        MethodArgumentTypeMismatchException ex) {

	    String parametro = ex.getName();
	    String tipoEsperado = ex.getRequiredType() != null
	            ? ex.getRequiredType().getSimpleName()
	            : "valor válido";

	    String mensagem = String.format(
	            "Parâmetro '%s' inválido. Informe um %s válido.",
	            parametro,
	            tipoEsperado
	    );

	    ApiErrorResponse error = new ApiErrorResponse(
	            HttpStatus.BAD_REQUEST.value(),
	            mensagem,
	            LocalDateTime.now()
	    );

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(error);
	}
	
	@ExceptionHandler(CpfInvalidoException.class)
	public ResponseEntity<ApiErrorResponse> handleCpfInvalido(
	        CpfInvalidoException ex) {

	    ApiErrorResponse error = new ApiErrorResponse(
	        HttpStatus.NOT_FOUND.value(),
	        ex.getMessage(),
	        LocalDateTime.now()
	    );

	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(CpfNaoPodeVotarException.class)
	public ResponseEntity<ApiErrorResponse> handleCpfNaoPodeVotar(
	        CpfNaoPodeVotarException ex) {

	    ApiErrorResponse error = new ApiErrorResponse(
	        HttpStatus.UNPROCESSABLE_ENTITY.value(),
	        ex.getMessage(),
	        LocalDateTime.now()
	    );

	    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
	}
}
