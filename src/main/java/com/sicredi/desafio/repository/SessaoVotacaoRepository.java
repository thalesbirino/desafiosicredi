package com.sicredi.desafio.repository;

import com.sicredi.desafio.model.SessaoVotacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long> {
    Optional<SessaoVotacao> findByPautaId(Long pautaId);
}
