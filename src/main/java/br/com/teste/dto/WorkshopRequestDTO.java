package br.com.teste.dto;

import java.time.LocalDateTime;

public record WorkshopRequestDTO(
        String nome,
        LocalDateTime dataRealizacao,
        String descricao
) {}