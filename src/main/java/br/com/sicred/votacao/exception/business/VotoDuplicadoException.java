package br.com.sicred.votacao.exception.business;

public class VotoDuplicadoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public VotoDuplicadoException() {
		super("Associado já votou nesta pauta.");
	}
}
