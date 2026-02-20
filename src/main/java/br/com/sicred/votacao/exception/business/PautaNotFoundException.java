package br.com.sicred.votacao.exception.business;

public class PautaNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public PautaNotFoundException() {
		super("Pauta não encontrada.");
	}
}
