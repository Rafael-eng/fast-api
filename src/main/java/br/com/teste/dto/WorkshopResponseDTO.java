package br.com.teste.dto;

import java.time.LocalDateTime;
import java.util.List;

public record WorkshopResponseDTO(
        Long id,
        String nome,
        LocalDateTime dataRealizacao,
        String descricao,
        List<ColaboradorResponseDTO> colaboradores
) {}