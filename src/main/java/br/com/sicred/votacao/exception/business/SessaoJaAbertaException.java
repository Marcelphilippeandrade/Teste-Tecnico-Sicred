package br.com.sicred.votacao.exception.business;

public class SessaoJaAbertaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SessaoJaAbertaException() {
		super("Já existe uma sessão aberta para esta pauta.");
	}
}
