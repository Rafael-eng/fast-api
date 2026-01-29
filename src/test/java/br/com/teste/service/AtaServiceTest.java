package br.com.teste.service;

import br.com.teste.dto.AtaRequestDTO;
import br.com.teste.dto.AtaResponseDTO;
import br.com.teste.dto.ColaboradorResponseDTO;
import br.com.teste.dto.WorkshopResponseDTO;
import br.com.teste.entity.Ata;
import br.com.teste.entity.Colaborador;
import br.com.teste.entity.Workshop;
import br.com.teste.mapper.AtaMapper;
import br.com.teste.repository.AtaRepository;
import br.com.teste.repository.WorkshopRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AtaServiceTest {

    @Mock
    private AtaRepository ataRepository;

    @Mock
    private WorkshopRepository workshopRepository;

    @Mock
    private ColaboradorService colaboradorService;

    @Mock
    private AtaMapper ataMapper;

    @InjectMocks
    private AtaService ataService;

    private Workshop workshop;
    private Colaborador colaborador;
    private Ata ata;
    private AtaRequestDTO request;
    private AtaResponseDTO response;

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
        ata.setColaboradores(new ArrayList<>(List.of(colaborador)));

        request = new AtaRequestDTO(1L, List.of(1L));
        response = new AtaResponseDTO(1L,
                new WorkshopResponseDTO(1L, "Workshop Teste", null, null, null),
                List.of(new ColaboradorResponseDTO(1L, "Rafael")));
    }

    @Test
    void criarAta_DeveCriarAtaComSucesso() {
        when(workshopRepository.findById(1L)).thenReturn(Optional.of(workshop));
        when(colaboradorService.buscarEntidadePorId(1L)).thenReturn(colaborador);
        when(ataRepository.save(any(Ata.class))).thenReturn(ata);
        when(ataMapper.toResponse(ata)).thenReturn(response);

        AtaResponseDTO result = ataService.criarAta(request);

        assertThat(result).isEqualTo(response);

        ArgumentCaptor<Ata> captor = ArgumentCaptor.forClass(Ata.class);
        verify(ataRepository).save(captor.capture());
        Ata saved = captor.getValue();
        assertThat(saved.getWorkshop()).isEqualTo(workshop);
        assertThat(saved.getColaboradores()).containsExactly(colaborador);
    }

    @Test
    void criarAta_WorkshopNaoEncontrado_DeveLancarException() {
        when(workshopRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ataService.criarAta(request));
    }

    @Test
    void removerColaboradorDaAta_DeveRemoverComSucesso() {
        when(ataRepository.findById(1L)).thenReturn(Optional.of(ata));
        when(colaboradorService.buscarEntidadePorId(1L)).thenReturn(colaborador);

        ataService.removerColaboradorDaAta(1L, 1L);

        ArgumentCaptor<Ata> captor = ArgumentCaptor.forClass(Ata.class);
        verify(ataRepository).save(captor.capture());
        Ata saved = captor.getValue();
        assertThat(saved.getColaboradores()).doesNotContain(colaborador);
    }

    @Test
    void removerColaboradorDaAta_AtaNaoEncontrada_DeveLancarException() {
        when(ataRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ataService.removerColaboradorDaAta(1L, 1L));
    }

    @Test
    void listarAtas_ComWorkshopNome_DeveRetornarLista() {
        when(ataRepository.findByWorkshopNomeContainingIgnoreCase("Workshop Teste"))
                .thenReturn(List.of(ata));
        when(ataMapper.toResponse(ata)).thenReturn(response);

        List<AtaResponseDTO> result = ataService.listarAtas("Workshop Teste", null);

        assertThat(result).containsExactly(response);
    }

    @Test
    void listarAtas_ComData_DeveRetornarLista() {
        LocalDate data = LocalDate.now();
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.plusDays(1).atStartOfDay();

        when(ataRepository.findByWorkshopData(inicio, fim)).thenReturn(List.of(ata));
        when(ataMapper.toResponse(ata)).thenReturn(response);

        List<AtaResponseDTO> result = ataService.listarAtas(null, data);

        assertThat(result).containsExactly(response);
    }

    @Test
    void listarAtas_SemFiltros_DeveRetornarTodas() {
        when(ataRepository.findAll()).thenReturn(List.of(ata));
        when(ataMapper.toResponse(ata)).thenReturn(response);

        List<AtaResponseDTO> result = ataService.listarAtas(null, null);

        assertThat(result).containsExactly(response);
    }
}
