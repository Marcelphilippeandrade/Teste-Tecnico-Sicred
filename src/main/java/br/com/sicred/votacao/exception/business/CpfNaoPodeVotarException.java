package br.com.sicred.votacao.exception.business;

public class CpfNaoPodeVotarException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;

	public CpfNaoPodeVotarException() {
        super("Associado não pode votar");
    }
}
