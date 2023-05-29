package com.sicredi.desafio.service;

import com.sicredi.desafio.dto.PautaDTO;
import com.sicredi.desafio.exception.BusinessException;
import com.sicredi.desafio.model.Pauta;
import com.sicredi.desafio.repository.PautaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PautaServiceTest {

    private PautaService pautaService;

    @Mock
    private PautaRepository pautaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pautaService = new PautaService(pautaRepository);
    }

    @Test
    void cadastrarPauta_deveRetornarPautaCadastrada() {
        PautaDTO pautaDTO = PautaDTO.builder()
                .descricao("Descrição da pauta")
                .build();

        Pauta pautaSalva = Pauta.builder()
                .id(1L)
                .descricao(pautaDTO.getDescricao())
                .build();

        when(pautaRepository.save(any(Pauta.class))).thenReturn(pautaSalva);

        Pauta pautaCadastrada = pautaService.cadastrarPauta(pautaDTO);

        assertNotNull(pautaCadastrada);
        assertEquals(pautaDTO.getDescricao(), pautaCadastrada.getDescricao());
        assertEquals(pautaSalva.getId(), pautaCadastrada.getId());

        verify(pautaRepository, times(1)).save(any(Pauta.class));
    }

    @Test
    void buscarPauta_pautaExistente_deveRetornarPauta() {
        Long pautaId = 1L;
        Pauta pautaExistente = Pauta.builder()
                .id(pautaId)
                .build();

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pautaExistente));

        Pauta pautaEncontrada = pautaService.buscarPauta(pautaId);

        assertNotNull(pautaEncontrada);
        assertEquals(pautaId, pautaEncontrada.getId());

        verify(pautaRepository, times(1)).findById(pautaId);
    }

    @Test
    void buscarPauta_pautaNaoExistente_deveLancarException() {
        Long pautaId = 1L;

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> pautaService.buscarPauta(pautaId));

        verify(pautaRepository, times(1)).findById(pautaId);
    }
}
