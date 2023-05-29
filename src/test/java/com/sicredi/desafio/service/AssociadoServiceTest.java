package com.sicredi.desafio.service;

import com.sicredi.desafio.dto.CpfValidationResponse;
import com.sicredi.desafio.model.Associado;
import com.sicredi.desafio.repository.AssociadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssociadoServiceTest {

    private AssociadoService associadoService;

    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private CpfValidationService cpfValidationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        associadoService = new AssociadoService(associadoRepository, cpfValidationService);
    }

    @Test
    void buscarAssociadoPorCpf_associadoExistente_deveRetornarAssociado() {
        // Arrange
        String cpf = "12345678900";
        CpfValidationResponse cpfValidationResponse = new CpfValidationResponse("{\"status\": \"ABLE_TO_VOTE\"}");

        when(cpfValidationService.validateCpf(cpf)).thenReturn(cpfValidationResponse);
        when(associadoRepository.findByCpf(cpf)).thenReturn(Optional.of(new Associado()));

        // Act
        Associado associadoEncontrado = associadoService.buscarAssociadoPorCpf(cpf);

        // Assert
        assertNotNull(associadoEncontrado);

        verify(cpfValidationService, times(1)).validateCpf(cpf);
        verify(associadoRepository, times(1)).findByCpf(cpf);
    }

    @Test
    void buscarAssociadoPorCpf_associadoNaoExistente_deveCriarENovoAssociado() {
        // Arrange
        String cpf = "12345678900";
        CpfValidationResponse cpfValidationResponse = new CpfValidationResponse("{\"status\": \"ABLE_TO_VOTE\"}");

        when(cpfValidationService.validateCpf(cpf)).thenReturn(cpfValidationResponse);
        when(associadoRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        // Act
        Associado novoAssociado = new Associado();
        when(associadoRepository.save(any(Associado.class))).thenReturn(novoAssociado);

        Associado associadoCriado = associadoService.buscarAssociadoPorCpf(cpf);

        // Assert
        assertNotNull(associadoCriado);

        verify(cpfValidationService, times(1)).validateCpf(cpf);
        verify(associadoRepository, times(1)).findByCpf(cpf);
        verify(associadoRepository, times(1)).save(any(Associado.class));
    }
}
