package com.sicredi.desafio.service;

import com.sicredi.desafio.dto.SessaoVotacaoDTO;
import com.sicredi.desafio.model.Pauta;
import com.sicredi.desafio.model.SessaoVotacao;
import com.sicredi.desafio.repository.SessaoVotacaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
@Slf4j
public class SessaoVotacaoService {
    private SessaoVotacaoRepository sessaoVotacaoRepository;
    private PautaService pautaService;

    @Autowired
    public SessaoVotacaoService(SessaoVotacaoRepository sessaoVotacaoRepository, PautaService pautaService) {
        this.sessaoVotacaoRepository = sessaoVotacaoRepository;
        this.pautaService = pautaService;
    }

    public SessaoVotacao abrirSessaoVotacao(SessaoVotacaoDTO sessaoVotacaoDTO) {
        LocalDateTime abertura = getAbertura(sessaoVotacaoDTO);

        log.info("Abertura de sessão de votação iniciada. Pauta ID: {}, Abertura: {}, Encerramento: {}",
                sessaoVotacaoDTO.getPautaId(), abertura, sessaoVotacaoDTO.getEncerramento());

        Pauta pauta = pautaService.buscarPauta(sessaoVotacaoDTO.getPautaId());

        LocalDateTime encerramento = getEncerramento(sessaoVotacaoDTO, abertura);

        SessaoVotacao sessaoVotacao = SessaoVotacao.builder()
                .pauta(pauta)
                .abertura(abertura)
                .encerramento(encerramento)
                .build();

        SessaoVotacao sessaoVotacaoSalva = sessaoVotacaoRepository.save(sessaoVotacao);

        log.info("Sessão de votação aberta com sucesso. ID: {}", sessaoVotacaoSalva.getId());

        return sessaoVotacaoSalva;
    }

    private LocalDateTime getEncerramento(SessaoVotacaoDTO sessaoVotacaoDTO, LocalDateTime abertura) {
        return sessaoVotacaoDTO.getEncerramento() != null ? sessaoVotacaoDTO.getEncerramento() : abertura.plusMinutes(1);
    }

    private LocalDateTime getAbertura(SessaoVotacaoDTO sessaoVotacaoDTO) {
        return sessaoVotacaoDTO.getAbertura() != null ? sessaoVotacaoDTO.getAbertura() : LocalDateTime.now();
    }

    public SessaoVotacao buscarSessaoVotacao(Long pautaId) {
        log.info("Busca de sessão de votação iniciada. Pauta ID: {}", pautaId);

        SessaoVotacao sessaoVotacao = sessaoVotacaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão de votação não encontrada"));

        log.info("Sessão de votação encontrada: {}", sessaoVotacao);

        return sessaoVotacao;
    }
}
