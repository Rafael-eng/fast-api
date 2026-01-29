package br.com.teste.mapper;


import br.com.teste.dto.ColaboradorRequestDTO;
import br.com.teste.dto.ColaboradorResponseDTO;
import br.com.teste.entity.Colaborador;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ColaboradorMapper {
    ColaboradorResponseDTO toResponse(Colaborador entity);

    Colaborador toEntity(ColaboradorRequestDTO request);
}