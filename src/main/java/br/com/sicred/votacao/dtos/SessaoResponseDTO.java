package br.com.sicred.votacao.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessaoResponseDTO(
	    UUID id,
	    LocalDateTime inicio,
	    LocalDateTime fim,
	    UUID pautaId
) {}

