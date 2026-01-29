package br.com.teste.service;

import br.com.teste.dto.AtaResponseDTO;
import br.com.teste.dto.ColaboradorResponseDTO;
import br.com.teste.dto.WorkshopRequestDTO;
import br.com.teste.dto.WorkshopResponseDTO;
import br.com.teste.entity.Ata;
import br.com.teste.entity.Colaborador;
import br.com.teste.entity.Workshop;
import br.com.teste.mapper.AtaMapper;
import br.com.teste.mapper.WorkshopMapper;
import br.com.teste.repository.AtaRepository;
import br.com.teste.repository.WorkshopRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class WorkshopServiceTest {

    @Mock
    private WorkshopRepository workshopRepository;

    @Mock
    private AtaRepository ataRepository;

    @Mock
    private ColaboradorService colaboradorService;

    @Mock
    private WorkshopMapper workshopMapper;

    @Mock
    private AtaMapper ataMapper;

    @InjectMocks
    private WorkshopService workshopService;

    private Workshop workshop;
    private Ata ata;
    private Colaborador colaborador;
    private WorkshopRequestDTO workshopRequestDTO;
    private WorkshopResponseDTO workshopResponseDTO;
    private AtaResponseDTO ataResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        workshop = new Workshop();
        workshop.setId(1L);
        workshop.setNome("Workshop Teste");

        colaborador = new Colaborador();
        colaborador.setId(1L);
        colaborador.setNome("Rafael");

        ata = new Ata();
        ata.setId(1L);
        ata.setWorkshop(workshop);
        ata.setColaboradores(new ArrayList<>()); // lista mutável

        workshopRequestDTO = new WorkshopRequestDTO("Workshop Teste", null, null);
        workshopResponseDTO = new WorkshopResponseDTO(1L, "Workshop Teste", null, null, null);
        ataResponseDTO = new AtaResponseDTO(1L,
                new WorkshopResponseDTO(1L, "Workshop Teste", null, null, null),
                List.of(new ColaboradorResponseDTO(1L, "Rafael")));
    }

    @Test
    void criarWorkshop_DeveRetornarWorkshopResponseDTO() {
        when(workshopMapper.toEntity(workshopRequestDTO)).thenReturn(workshop);
        when(workshopRepository.save(workshop)).thenReturn(workshop);
        when(workshopMapper.toResponse(workshop)).thenReturn(workshopResponseDTO);

        WorkshopResponseDTO response = workshopService.criarWorkshop(workshopRequestDTO);

        assertThat(response).isNotNull();
        assertThat(response.nome()).isEqualTo("Workshop Teste");

        verify(workshopRepository).save(workshop);
        verify(workshopMapper).toResponse(workshop);
    }

    @Test
    void adicionarColaboradorNaAta_DeveAdicionarComSucesso() {
        when(workshopRepository.findById(1L)).thenReturn(Optional.of(workshop));
        when(ataRepository.findById(1L)).thenReturn(Optional.of(ata));
        when(colaboradorService.buscarEntidadePorId(1L)).thenReturn(colaborador);
        when(ataRepository.save(any(Ata.class))).thenReturn(ata);
        when(ataMapper.toResponse(ata)).thenReturn(ataResponseDTO);

        AtaResponseDTO response = workshopService.adicionarColaboradorNaAta(1L, 1L, 1L);

        assertThat(response).isNotNull();
        assertThat(response.colaboradores().getFirst().nome()).isEqualTo("Rafael");
        assertThat(ata.getColaboradores()).contains(colaborador);

        verify(ataRepository).save(ata);
        verify(ataMapper).toResponse(ata);
    }

    @Test
    void adicionarColaboradorNaAta_WorkshopNaoEncontrado_DeveLancarExcecao() {
        when(workshopRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> workshopService.adicionarColaboradorNaAta(1L, 1L, 1L));
    }

    @Test
    void adicionarColaboradorNaAta_AtaNaoEncontrada_DeveLancarExcecao() {
        when(workshopRepository.findById(1L)).thenReturn(Optional.of(workshop));
        when(ataRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> workshopService.adicionarColaboradorNaAta(1L, 1L, 1L));
    }
}
