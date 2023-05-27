package com.sicredi.desafio.service;

import com.sicredi.desafio.client.CpfValidatorClient;
import com.sicredi.desafio.dto.CpfValidationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CpfValidationService {

    private final CpfValidatorClient cpfValidatorClient;

    @Autowired
    public CpfValidationService(CpfValidatorClient cpfValidatorClient) {
        this.cpfValidatorClient = cpfValidatorClient;
    }

    public CpfValidationResponse validateCpf(String cpf) {
        return cpfValidatorClient.validateCpf(cpf);
    }

}
