package br.com.sicred.votacao.dtos;

import java.time.LocalDateTime;

public record ApiErrorResponse(
    int status,
    String message,
    LocalDateTime timestamp
) {}
