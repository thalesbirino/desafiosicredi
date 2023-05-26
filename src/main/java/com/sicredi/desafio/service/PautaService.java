package com.sicredi.desafio.service;

import com.sicredi.desafio.dto.PautaDTO;
import com.sicredi.desafio.model.Pauta;
import com.sicredi.desafio.repository.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class PautaService {
    private PautaRepository pautaRepository;

    @Autowired
    public PautaService(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    public Pauta cadastrarPauta(PautaDTO pautaDTO) {
        Pauta pauta = Pauta.builder()
                .descricao(pautaDTO.getDescricao())
                .build();
        return pautaRepository.save(pauta);
    }

    public Pauta buscarPauta(Long id) {
        return pautaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pauta não encontrada"));
    }
}
