package br.com.teste.service;

import br.com.teste.dto.ColaboradorRequestDTO;
import br.com.teste.dto.ColaboradorResponseDTO;
import br.com.teste.entity.Colaborador;
import br.com.teste.mapper.ColaboradorMapper;
import br.com.teste.repository.ColaboradorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ColaboradorServiceTest {

    @Mock
    private ColaboradorRepository colaboradorRepository;

    @Mock
    private ColaboradorMapper colaboradorMapper;

    @InjectMocks
    private ColaboradorService colaboradorService;

    private Colaborador colaborador;
    private ColaboradorRequestDTO requestDTO;
    private ColaboradorResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        colaborador = new Colaborador();
        colaborador.setId(1L);
        colaborador.setNome("Rafael");

        requestDTO = new ColaboradorRequestDTO("Rafael");
        responseDTO = new ColaboradorResponseDTO(1L, "Rafael");
    }

    @Test
    void criarColaborador_DeveRetornarColaboradorResponseDTO() {
        when(colaboradorMapper.toEntity(requestDTO)).thenReturn(colaborador);
        when(colaboradorRepository.save(colaborador)).thenReturn(colaborador);
        when(colaboradorMapper.toResponse(colaborador)).thenReturn(responseDTO);

        ColaboradorResponseDTO response = colaboradorService.criarColaborador(requestDTO);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.nome()).isEqualTo("Rafael");

        verify(colaboradorMapper).toEntity(requestDTO);
        verify(colaboradorRepository).save(colaborador);
        verify(colaboradorMapper).toResponse(colaborador);
    }

    @Test
    void buscarEntidadePorId_DeveRetornarColaborador() {
        when(colaboradorRepository.findById(1L)).thenReturn(Optional.of(colaborador));

        Colaborador found = colaboradorService.buscarEntidadePorId(1L);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
        assertThat(found.getNome()).isEqualTo("Rafael");

        verify(colaboradorRepository).findById(1L);
    }

    @Test
    void buscarEntidadePorId_ColaboradorNaoEncontrado_DeveLancarExcecao() {
        when(colaboradorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> colaboradorService.buscarEntidadePorId(1L));

        verify(colaboradorRepository).findById(1L);
    }
}
