package com.sicredi.desafio.controller;

import com.sicredi.desafio.dto.PautaDTO;
import com.sicredi.desafio.dto.SessaoVotacaoDTO;
import com.sicredi.desafio.dto.VotoDTO;
import com.sicredi.desafio.model.Pauta;
import com.sicredi.desafio.model.SessaoVotacao;
import com.sicredi.desafio.model.Voto;
import com.sicredi.desafio.service.PautaService;
import com.sicredi.desafio.service.SessaoVotacaoService;
import com.sicredi.desafio.service.VotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class VotingController {
    private PautaService pautaService;
    private SessaoVotacaoService sessaoVotacaoService;
    private VotoService votoService;

    @Autowired
    public VotingController(PautaService pautaService, SessaoVotacaoService sessaoVotacaoService, VotoService votoService) {
        this.pautaService = pautaService;
        this.sessaoVotacaoService = sessaoVotacaoService;
        this.votoService = votoService;
    }

    @PostMapping("/pautas")
    public ResponseEntity<Pauta> cadastrarPauta(@RequestBody PautaDTO pautaDTO) {
        log.info("Endpoint '/pautas' chamado. Body: {}", pautaDTO);
        Pauta pauta = pautaService.cadastrarPauta(pautaDTO);
        log.info("Pauta cadastrada com sucesso. ID: {}", pauta.getId());
        return ResponseEntity.ok(pauta);
    }

    @PostMapping("/sessoes-votacao")
    public ResponseEntity<SessaoVotacao> abrirSessaoVotacao(@RequestBody SessaoVotacaoDTO sessaoVotacaoDTO) {
        log.info("Endpoint '/sessoes-votacao' chamado. Body: {}", sessaoVotacaoDTO);
        SessaoVotacao sessaoVotacao = sessaoVotacaoService.abrirSessaoVotacao(sessaoVotacaoDTO);
        log.info("Sessão de votação aberta com sucesso. ID: {}", sessaoVotacao.getId());
        return ResponseEntity.ok(sessaoVotacao);
    }

    @PostMapping("/votos")
    public ResponseEntity<Void> votar(@RequestBody VotoDTO votoDTO) {
        log.info("Endpoint '/votos' chamado. Body: {}", votoDTO);
        votoService.votar(votoDTO);
        log.info("Voto registrado com sucesso.");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pautas/{id}/votos")
    public ResponseEntity<List<Voto>> buscarVotos(@PathVariable("id") Long pautaId) {
        log.info("Endpoint '/pautas/{}/votos' chamado.", pautaId);
        List<Voto> votos = votoService.buscarVotos(pautaId);
        log.info("Votos encontrados: {}", votos);
        return ResponseEntity.ok(votos);
    }
}
