package br.com.sicred.votacao.exception.business;

public class SessaoEmAndamentoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public SessaoEmAndamentoException() {
        super("Sessão de votação ainda está em andamento");
    }
}
