package com.sicredi.desafio.service;

import com.sicredi.desafio.dto.VotoDTO;
import com.sicredi.desafio.exception.BusinessException;
import com.sicredi.desafio.model.Associado;
import com.sicredi.desafio.model.Pauta;
import com.sicredi.desafio.model.SessaoVotacao;
import com.sicredi.desafio.model.Voto;
import com.sicredi.desafio.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class VotoService {
    private static final Logger LOGGER = Logger.getLogger(VotoService.class.getName());

    private VotoRepository votoRepository;
    private PautaService pautaService;
    private AssociadoService associadoService;
    private SessaoVotacaoService sessaoVotacaoService;

    @Autowired
    public VotoService(VotoRepository votoRepository, PautaService pautaService, AssociadoService associadoService, SessaoVotacaoService sessaoVotacaoService) {
        this.votoRepository = votoRepository;
        this.pautaService = pautaService;
        this.associadoService = associadoService;
        this.sessaoVotacaoService = sessaoVotacaoService;
    }

    @Transactional
    public void votar(VotoDTO votoDTO) {
        Pauta pauta = pautaService.buscarPauta(votoDTO.getPautaId());
        SessaoVotacao sessaoVotacao = sessaoVotacaoService.buscarSessaoVotacao(pauta.getId());

        if (!verifificarSeVotacaoParaPautaEstaAberta(LocalDateTime.now(), sessaoVotacao.getAbertura(), sessaoVotacao.getEncerramento())) {
            throw new BusinessException("A sessão de votação para esta pauta não está aberta!");
        }
        Associado associado = associadoService.buscarAssociadoPorCpf(votoDTO.getCpf());
        Optional<Voto> votoExistente = votoRepository.findByAssociado_CpfAndAndPauta(associado.getCpf(), pauta);

        if (votoExistente.isPresent()) {
            throw new BusinessException("Associado já votou nesta pauta");
        }

        Voto voto = Voto.builder()
                .pauta(pauta)
                .associado(associado)
                .voto(votoDTO.getVoto())
                .build();

        votoRepository.save(voto);
        LOGGER.log(Level.INFO, "Voto registrado com sucesso para a pauta: " + pauta.getId());
    }

    public List<Voto> buscarVotos(Long pautaId) {
        return votoRepository.findByPautaId(pautaId);
    }

    public boolean verifificarSeVotacaoParaPautaEstaAberta(LocalDateTime dateTimeToCheck, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return dateTimeToCheck.isAfter(startDateTime) && dateTimeToCheck.isBefore(endDateTime);
    }
}
