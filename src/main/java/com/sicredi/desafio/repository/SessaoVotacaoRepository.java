package com.sicredi.desafio.repository;

import com.sicredi.desafio.model.SessaoVotacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long> {
    Optional<SessaoVotacao> findByPautaId(Long pautaId);
    List<SessaoVotacao> findByEncerramentoBeforeAndResultadoDiulgado(LocalDateTime dataLimite, Boolean isDivulgado);
}
