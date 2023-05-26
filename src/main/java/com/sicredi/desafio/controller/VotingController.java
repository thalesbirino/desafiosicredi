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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
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
        Pauta pauta = pautaService.cadastrarPauta(pautaDTO);
        return ResponseEntity.ok(pauta);
    }

    @PostMapping("/sessoes-votacao")
    public ResponseEntity<SessaoVotacao> abrirSessaoVotacao(@RequestBody SessaoVotacaoDTO sessaoVotacaoDTO) {
        SessaoVotacao sessaoVotacao = sessaoVotacaoService.abrirSessaoVotacao(sessaoVotacaoDTO);
        return ResponseEntity.ok(sessaoVotacao);
    }

    @PostMapping("/votos")
    public ResponseEntity<Void> votar(@RequestBody VotoDTO votoDTO) {
        votoService.votar(votoDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pautas/{id}/votos")
    public ResponseEntity<List<Voto>> buscarVotos(@PathVariable("id") Long pautaId) {
        List<Voto> votos = votoService.buscarVotos(pautaId);
        return ResponseEntity.ok(votos);
    }
}
