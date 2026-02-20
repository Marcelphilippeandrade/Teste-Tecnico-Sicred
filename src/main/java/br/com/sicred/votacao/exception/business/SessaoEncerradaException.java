package br.com.sicred.votacao.exception.business;

public class SessaoEncerradaException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public SessaoEncerradaException() {
        super("Sessão de votação já encerrada.");
    }
}
