package com.sicredi.desafio.service;

import com.sicredi.desafio.client.CpfValidatorClient;
import com.sicredi.desafio.dto.CpfValidationResponse;
import com.sicredi.desafio.service.CpfValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CpfValidationServiceTest {

    private CpfValidationService cpfValidationService;

    @Mock
    private CpfValidatorClient cpfValidatorClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cpfValidationService = new CpfValidationService(cpfValidatorClient);
    }

    @Test
    void validateCpf_shouldReturnCpfValidationResponse() {
        String cpf = "12345678900";
        CpfValidationResponse expectedResponse = new CpfValidationResponse("{\"status\": \"ABLE_TO_VOTE\"}" );

        when(cpfValidatorClient.validateCpf(cpf)).thenReturn(expectedResponse);

        CpfValidationResponse actualResponse = cpfValidationService.validateCpf(cpf);

        assertEquals(expectedResponse, actualResponse);
    }
}
