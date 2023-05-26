package com.sicredi.desafio.service;

import com.sicredi.desafio.model.Associado;
import com.sicredi.desafio.repository.AssociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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
                .orElseGet(() -> associado(cpf));
    }

    private Associado associado(String cpf){
        Associado associado = Associado.builder()
                .cpf(cpf)
                .build();
        return associadoRepository.save(associado);
    }

}
