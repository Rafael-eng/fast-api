package br.com.teste.service;

import br.com.teste.dto.AtaRequestDTO;
import br.com.teste.dto.AtaResponseDTO;
import br.com.teste.entity.Ata;
import br.com.teste.entity.Colaborador;
import br.com.teste.entity.Workshop;
import br.com.teste.mapper.AtaMapper;
import br.com.teste.repository.AtaRepository;
import br.com.teste.repository.WorkshopRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AtaService {

    private final AtaRepository ataRepository;
    private final WorkshopRepository workshopRepository;
    private final ColaboradorService colaboradorService;
    private final AtaMapper ataMapper;

    public AtaResponseDTO criarAta(AtaRequestDTO request) {
        Workshop workshop = workshopRepository.findById(request.workshopId())
                .orElseThrow(() -> new EntityNotFoundException("Workshop não encontrado: " + request.workshopId()));

        List<Colaborador> colaboradores = request.colaboradorIds().stream()
                .map(colaboradorService::buscarEntidadePorId)
                .toList();

        Ata ata = new Ata();
        ata.setWorkshop(workshop);
        ata.setColaboradores(colaboradores);

        return ataMapper.toResponse(ataRepository.save(ata));
    }

    public void removerColaboradorDaAta(Long ataId, Long colaboradorId) {
        Ata ata = ataRepository.findById(ataId)
                .orElseThrow(() -> new EntityNotFoundException("Ata não encontrada: " + ataId));

        Colaborador colaborador = colaboradorService.buscarEntidadePorId(colaboradorId);
        ata.getColaboradores().remove(colaborador);

        ataRepository.save(ata);
    }

    public List<AtaResponseDTO> listarAtas(String workshopNome, LocalDate data) {
        List<Ata> atas;

        if (workshopNome != null) {
            atas = ataRepository.findByWorkshopNomeContainingIgnoreCase(workshopNome);
        } else if (data != null) {
            LocalDateTime inicio = data.atStartOfDay();
            LocalDateTime fim = data.plusDays(1).atStartOfDay();
            atas = ataRepository.findByWorkshopData(inicio, fim);
        } else {
            atas = ataRepository.findAll();
        }

        return atas.stream().map(ataMapper::toResponse).toList();
    }

}
