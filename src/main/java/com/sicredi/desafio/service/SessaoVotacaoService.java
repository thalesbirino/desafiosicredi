package com.sicredi.desafio.service;

import com.sicredi.desafio.dto.SessaoVotacaoDTO;
import com.sicredi.desafio.model.SessaoVotacao;
import com.sicredi.desafio.repository.SessaoVotacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
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
        SessaoVotacao sessaoVotacao = SessaoVotacao.builder()
                .pauta(pautaService.buscarPauta(sessaoVotacaoDTO.getPautaId()))
                .abertura(abertura)
                .encerramento(getEncerramento(sessaoVotacaoDTO, abertura))
                .build();
        return sessaoVotacaoRepository.save(sessaoVotacao);
    }

    private LocalDateTime getEncerramento(SessaoVotacaoDTO sessaoVotacaoDTO, LocalDateTime abertura) {
        return sessaoVotacaoDTO.getEncerramento() != null ? sessaoVotacaoDTO.getEncerramento() : abertura.plusMinutes(1);
    }

    private LocalDateTime getAbertura(SessaoVotacaoDTO sessaoVotacaoDTO) {
        return sessaoVotacaoDTO.getAbertura() != null ? sessaoVotacaoDTO.getAbertura() : LocalDateTime.now();
    }

    public SessaoVotacao buscarSessaoVotacao(Long pautaId) {
        return sessaoVotacaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão de votação não encontrada"));
    }
}
