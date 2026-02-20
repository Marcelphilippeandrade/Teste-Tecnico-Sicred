package br.com.sicred.votacao.exception;

public class SessaoNaoEncontradaException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public SessaoNaoEncontradaException() {
        super("Sessão de votação ainda não foi aberta para esta pauta");
    }
}
