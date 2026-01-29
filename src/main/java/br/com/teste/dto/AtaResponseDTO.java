package br.com.teste.dto;


import java.util.List;

public record AtaResponseDTO(
        Long id,
        WorkshopResponseDTO workshop,
        List<ColaboradorResponseDTO> colaboradores
) {}
