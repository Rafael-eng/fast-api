package br.com.teste.dto;

import java.util.List;

public record AtaRequestDTO(
        Long workshopId,
        List<Long> colaboradorIds
) {}