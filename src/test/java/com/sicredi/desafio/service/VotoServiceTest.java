package com.sicredi.desafio.service;

import com.sicredi.desafio.dto.VotoDTO;
import com.sicredi.desafio.exception.BusinessException;
import com.sicredi.desafio.model.Associado;
import com.sicredi.desafio.model.Pauta;
import com.sicredi.desafio.model.SessaoVotacao;
import com.sicredi.desafio.model.Voto;
import com.sicredi.desafio.repository.VotoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VotoServiceTest {
    private VotoService votoService;

    @Mock
    private VotoRepository votoRepository;
    @Mock
    private PautaService pautaService;
    @Mock
    private AssociadoService associadoService;
    @Mock
    private SessaoVotacaoService sessaoVotacaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        votoService = new VotoService(votoRepository, pautaService, associadoService, sessaoVotacaoService);
    }

    @Test
    void votar_WhenValidVotoDTO_ShouldSaveVoto() {
        VotoDTO votoDTO = new VotoDTO();
        votoDTO.setPautaId(1L);
        votoDTO.setCpf("12345678901");
        votoDTO.setVoto(true);

        Pauta pauta = new Pauta();
        pauta.setId(1L);
        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setAbertura(LocalDateTime.now().minusMinutes(10));
        sessaoVotacao.setEncerramento(LocalDateTime.now().plusMinutes(10));

        Associado associado = new Associado();
        associado.setCpf("12345678901");

        Voto voto = new Voto();
        voto.setPauta(pauta);
        voto.setAssociado(associado);
        voto.setVoto(true);

        when(pautaService.buscarPauta(1L)).thenReturn(pauta);
        when(sessaoVotacaoService.buscarSessaoVotacao(1L)).thenReturn(sessaoVotacao);
        when(associadoService.buscarAssociadoPorCpf("12345678901")).thenReturn(associado);
        when(votoRepository.findByAssociado_CpfAndAndPauta("12345678901", pauta)).thenReturn(Optional.empty());
        when(votoRepository.save(any(Voto.class))).thenReturn(voto);

        votoService.votar(votoDTO);

        verify(pautaService).buscarPauta(1L);
        verify(sessaoVotacaoService).buscarSessaoVotacao(1L);
        verify(associadoService).buscarAssociadoPorCpf("12345678901");
        verify(votoRepository).findByAssociado_CpfAndAndPauta("12345678901", pauta);
        verify(votoRepository).save(any(Voto.class));
    }

    @Test
    void votar_WhenVotacaoParaPautaNaoEstaAberta_ShouldThrowBusinessException() {
        VotoDTO votoDTO = new VotoDTO();
        votoDTO.setPautaId(1L);
        votoDTO.setCpf("12345678901");
        votoDTO.setVoto(true);

        Pauta pauta = new Pauta();
        pauta.setId(1L);
        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setAbertura(LocalDateTime.now().plusMinutes(10));
        sessaoVotacao.setEncerramento(LocalDateTime.now().plusMinutes(20));

        when(pautaService.buscarPauta(1L)).thenReturn(pauta);
        when(sessaoVotacaoService.buscarSessaoVotacao(1L)).thenReturn(sessaoVotacao);

        Assertions.assertThrows(BusinessException.class, () -> votoService.votar(votoDTO));

        verify(pautaService).buscarPauta(1L);
        verify(sessaoVotacaoService).buscarSessaoVotacao(1L);
        verify(associadoService, never()).buscarAssociadoPorCpf(anyString());
        verify(votoRepository, never()).findByAssociado_CpfAndAndPauta(anyString(), any(Pauta.class));
        verify(votoRepository, never()).save(any(Voto.class));
    }
}
