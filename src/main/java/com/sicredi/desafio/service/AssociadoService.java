package com.sicredi.desafio.service;

import com.sicredi.desafio.model.Associado;
import com.sicredi.desafio.repository.AssociadoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AssociadoService {
    private AssociadoRepository associadoRepository;

    @Autowired
    public AssociadoService(AssociadoRepository associadoRepository) {
        this.associadoRepository = associadoRepository;
    }

    public Associado buscarAssociadoPorCpf(String cpf) {
        return associadoRepository.findByCpf(cpf)
                .stream()
                .findAny()
                .orElseGet(() -> criarNovoAssociado(cpf));
    }

    private Associado criarNovoAssociado(String cpf) {
        log.info("Criando novo associado com CPF: {}", cpf);
        Associado associado = Associado.builder()
                .cpf(cpf)
                .build();
        return associadoRepository.save(associado);
    }
}

