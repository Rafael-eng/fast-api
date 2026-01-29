package br.com.teste.service;

import br.com.teste.dto.ColaboradorRequestDTO;
import br.com.teste.dto.ColaboradorResponseDTO;
import br.com.teste.entity.Colaborador;
import br.com.teste.mapper.ColaboradorMapper;
import br.com.teste.repository.ColaboradorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;
    private final ColaboradorMapper colaboradorMapper;

    public ColaboradorResponseDTO criarColaborador(ColaboradorRequestDTO request) {
        Colaborador colaborador = colaboradorMapper.toEntity(request);
        Colaborador salvo = colaboradorRepository.save(colaborador);
        return colaboradorMapper.toResponse(salvo);
    }

    public Colaborador buscarEntidadePorId(Long id) {
        return colaboradorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado: " + id));
    }

}
