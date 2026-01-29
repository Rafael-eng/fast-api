package br.com.teste.mapper;

import br.com.teste.dto.AtaRequestDTO;
import br.com.teste.dto.AtaResponseDTO;
import br.com.teste.entity.Ata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {WorkshopMapper.class, ColaboradorMapper.class})
public interface AtaMapper {
    AtaResponseDTO toResponse(Ata entity);
}