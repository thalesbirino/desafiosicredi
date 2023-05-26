package com.sicredi.desafio.service;

import com.sicredi.desafio.dto.PautaDTO;
import com.sicredi.desafio.exception.BusinessException;
import com.sicredi.desafio.model.Pauta;
import com.sicredi.desafio.repository.PautaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PautaService {
    private PautaRepository pautaRepository;

    @Autowired
    public PautaService(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    public Pauta cadastrarPauta(PautaDTO pautaDTO) {
        log.info("Cadastro de pauta iniciado. Descrição: {}", pautaDTO.getDescricao());

        Pauta pauta = Pauta.builder()
                .descricao(pautaDTO.getDescricao())
                .build();

        Pauta pautaSalva = pautaRepository.save(pauta);

        log.info("Pauta cadastrada com sucesso. ID: {}", pautaSalva.getId());

        return pautaSalva;
    }

    public Pauta buscarPauta(Long id) {
        log.info("Busca de pauta iniciada. ID: {}", id);

        Pauta pauta = pautaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Pauta não encontrada"));

        log.info("Pauta encontrada: {}", pauta);

        return pauta;
    }
}

