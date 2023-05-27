package com.sicredi.desafio.client;

import com.sicredi.desafio.dto.CpfValidationResponse;
import com.sicredi.desafio.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class CpfValidatorClient {

    private final RestTemplate restTemplate;
    private final String cpfValidatorUrl;

    public CpfValidatorClient(@Value("${cpf-validator.url}") String cpfValidatorUrl) {
        this.restTemplate = new RestTemplate();
        this.cpfValidatorUrl = cpfValidatorUrl;
    }

    public CpfValidationResponse validateCpf(String cpf) {
        String url = cpfValidatorUrl + "?cpf=" + cpf;
        log.info("Enviando requisição para a API validadora de CPF. URL: {}", url);

        try {
            ResponseEntity<CpfValidationResponse> response = restTemplate.getForEntity(url, CpfValidationResponse.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Falha ao validar o CPF");
            throw new BusinessException("O CPF não é válido!");
        }
    }

}
