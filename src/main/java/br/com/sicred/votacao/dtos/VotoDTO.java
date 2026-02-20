package br.com.sicred.votacao.dtos;

import br.com.sicred.votacao.enums.TipoVoto;

public record VotoDTO(String cpf, TipoVoto voto) {
	
}

