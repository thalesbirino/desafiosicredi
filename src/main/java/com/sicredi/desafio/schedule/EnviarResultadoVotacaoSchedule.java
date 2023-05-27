package com.sicredi.desafio.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sicredi.desafio.dto.PautaContagemVotos;
import com.sicredi.desafio.exception.BusinessException;
import com.sicredi.desafio.model.SessaoVotacao;
import com.sicredi.desafio.model.Voto;
import com.sicredi.desafio.service.SessaoVotacaoService;
import com.sicredi.desafio.service.SqsService;
import com.sicredi.desafio.service.VotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EnviarResultadoVotacaoSchedule {

    private SessaoVotacaoService sessaoVotacaoService;
    private SqsService sqsService;
    private VotoService votoService;

    @Autowired
    public EnviarResultadoVotacaoSchedule(SessaoVotacaoService sessaoVotacaoService, SqsService sqsService, VotoService votoService) {
        this.sessaoVotacaoService = sessaoVotacaoService;
        this.sqsService = sqsService;
        this.votoService = votoService;
    }

    @Scheduled(fixedDelay = 10000)
    public void enviarResultados() {
        log.info("Iniciando envio de resultados...");

        List<SessaoVotacao> sessaoVotacaos = sessaoVotacaoService.buscarSessoesEncerradasComResultadosNaoEnviados();
        Map<String, Map<Boolean, Long>> contagemVotosPorPautaETipo = getContagemVotosPorPautaETipo(sessaoVotacaos);
        List<PautaContagemVotos> listaPautaContagemVotos = mapToPautaContagemVotos(contagemVotosPorPautaETipo);

        listaPautaContagemVotos.forEach(pautaContagemVotos -> {
            String json = converterParaJson(pautaContagemVotos);
            sqsService.sendMessage(json);
            log.info("Mensagem enviada para a fila: {}", json);
        });

        sessaoVotacaoService.concluirEnvio(sessaoVotacaos);
        log.info("Envio de resultados concluído. Número de votos por tipo: {}", listaPautaContagemVotos);
    }

    private String converterParaJson(Object o) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Erro ao transformar objeto em json!");
        }
    }

    private List<PautaContagemVotos> mapToPautaContagemVotos(Map<String, Map<Boolean, Long>> contagemVotosPorPautaETipo) {
        return contagemVotosPorPautaETipo.entrySet().stream()
                .map(entry -> {
                    String pauta = entry.getKey();
                    Long votosSim = entry.getValue().getOrDefault(true, 0L);
                    Long votosNao = entry.getValue().getOrDefault(false, 0L);
                    return new PautaContagemVotos(pauta, votosSim, votosNao);
                })
                .collect(Collectors.toList());
    }

    private Map<String, Map<Boolean, Long>> getContagemVotosPorPautaETipo(List<SessaoVotacao> sessaoVotacaos) {
        Map<String, Map<Boolean, Long>> contagemVotosPorPautaETipo = sessaoVotacaos.stream()
                .flatMap(sessaoVotacao -> votoService.buscarVotos(sessaoVotacao.getPauta().getId()).stream())
                .collect(Collectors.groupingBy(
                        voto -> voto.getPauta().getDescricao() + " - " +voto.getPauta().getId(),
                        Collectors.groupingBy(Voto::isVoto, Collectors.counting())
                ));
        return contagemVotosPorPautaETipo;
    }
}
