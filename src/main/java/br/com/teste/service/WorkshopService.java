package br.com.teste.service;

import br.com.teste.dto.AtaResponseDTO;
import br.com.teste.dto.ColaboradorRequestDTO;
import br.com.teste.dto.WorkshopRequestDTO;
import br.com.teste.dto.WorkshopResponseDTO;
import br.com.teste.entity.Ata;
import br.com.teste.entity.Colaborador;
import br.com.teste.entity.Workshop;
import br.com.teste.mapper.AtaMapper;
import br.com.teste.mapper.ColaboradorMapper;
import br.com.teste.mapper.WorkshopMapper;
import br.com.teste.repository.AtaRepository;
import br.com.teste.repository.WorkshopRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkshopService {

    private final WorkshopRepository workshopRepository;
    private final AtaRepository ataRepository;
    private final ColaboradorService colaboradorService;

    private final WorkshopMapper workshopMapper;
    private final AtaMapper ataMapper;

    public WorkshopResponseDTO criarWorkshop(WorkshopRequestDTO dto) {
        Workshop workshop = workshopMapper.toEntity(dto);
        Workshop salvo = workshopRepository.save(workshop);
        return workshopMapper.toResponse(salvo);
    }

    public AtaResponseDTO adicionarColaboradorNaAta(Long workshopId, Long ataId, Long colaboradorId) {
        Workshop workshop = workshopRepository.findById(workshopId)
                .orElseThrow(() -> new EntityNotFoundException("Workshop não encontrado: " + workshopId));

        Ata ata = ataRepository.findById(ataId)
                .orElseThrow(() -> new EntityNotFoundException("Ata não encontrada: " + ataId));

        Colaborador colaborador = colaboradorService.buscarEntidadePorId(colaboradorId);

        if (!ata.getColaboradores().contains(colaborador)) {
            ata.getColaboradores().add(colaborador);
        }

        Ata salvo = ataRepository.save(ata);
        return ataMapper.toResponse(salvo);
    }

}
