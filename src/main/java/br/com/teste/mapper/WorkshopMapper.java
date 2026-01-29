package br.com.teste.mapper;

import br.com.teste.dto.WorkshopRequestDTO;
import br.com.teste.dto.WorkshopResponseDTO;
import br.com.teste.entity.Workshop;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ColaboradorMapper.class})
public interface WorkshopMapper {
    WorkshopResponseDTO toResponse(Workshop entity);

    Workshop toEntity(WorkshopRequestDTO request);
}