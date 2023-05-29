package com.sicredi.desafio.service;

import com.sicredi.desafio.dto.SessaoVotacaoDTO;
import com.sicredi.desafio.exception.BusinessException;
import com.sicredi.desafio.model.Pauta;
import com.sicredi.desafio.model.SessaoVotacao;
import com.sicredi.desafio.repository.SessaoVotacaoRepository;
import com.sicredi.desafio.service.PautaService;
import com.sicredi.desafio.service.SessaoVotacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class SessaoVotacaoServiceTest {

    private SessaoVotacaoService sessaoVotacaoService;

    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Mock
    private PautaService pautaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sessaoVotacaoService = new SessaoVotacaoService(sessaoVotacaoRepository, pautaService);
    }

    @Test
    void abrirSessaoVotacao_shouldReturnSessaoVotacao() {
        Long pautaId = 1L;
        LocalDateTime abertura = LocalDateTime.now();
        LocalDateTime encerramento = abertura.plusMinutes(1);
        SessaoVotacaoDTO sessaoVotacaoDTO = new SessaoVotacaoDTO(pautaId, abertura, encerramento);
        Pauta pauta = new Pauta();
        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        when(pautaService.buscarPauta(pautaId)).thenReturn(pauta);
        when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenReturn(sessaoVotacao);

        SessaoVotacao result = sessaoVotacaoService.abrirSessaoVotacao(sessaoVotacaoDTO);

        assertEquals(sessaoVotacao, result);
        verify(pautaService, times(1)).buscarPauta(pautaId);
        verify(sessaoVotacaoRepository, times(1)).save(any(SessaoVotacao.class));
    }

    @Test
    void buscarSessaoVotacao_shouldReturnSessaoVotacao() {
        Long pautaId = 1L;
        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        when(sessaoVotacaoRepository.findByPautaId(pautaId)).thenReturn(Optional.of(sessaoVotacao));

        SessaoVotacao result = sessaoVotacaoService.buscarSessaoVotacao(pautaId);

        assertEquals(sessaoVotacao, result);
        verify(sessaoVotacaoRepository, times(1)).findByPautaId(pautaId);
    }

    @Test
    void buscarSessoesEncerradasComResultadosNaoEnviados_shouldReturnListOfSessaoVotacao() {
        List<SessaoVotacao> sessoesVotacao = new ArrayList<>();
        when(sessaoVotacaoRepository.findByEncerramentoBeforeAndResultadoDiulgado(any(LocalDateTime.class), eq(Boolean.FALSE))).thenReturn(sessoesVotacao);

        List<SessaoVotacao> result = sessaoVotacaoService.buscarSessoesEncerradasComResultadosNaoEnviados();

        assertEquals(sessoesVotacao, result);
        verify(sessaoVotacaoRepository, times(1)).findByEncerramentoBeforeAndResultadoDiulgado(any(LocalDateTime.class), eq(Boolean.FALSE));
    }

    @Test
    void concluirEnvio_shouldSetResultadoDivulgadoToTrue() {
        SessaoVotacao sessaoVotacao1 = new SessaoVotacao();
        SessaoVotacao sessaoVotacao2 = new SessaoVotacao();
        List<SessaoVotacao> sessoesVotacao = Arrays.asList(sessaoVotacao1, sessaoVotacao2);

        sessaoVotacaoService.concluirEnvio(sessoesVotacao);

        assertTrue(sessaoVotacao1.isResultadoDiulgado());
        assertTrue(sessaoVotacao2.isResultadoDiulgado());
        verify(sessaoVotacaoRepository, times(2)).save(any(SessaoVotacao.class));
    }
}
