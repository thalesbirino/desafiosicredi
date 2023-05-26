package com.sicredi.desafio.repository;

import com.sicredi.desafio.model.Pauta;
import com.sicredi.desafio.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    Optional<Voto> findByAssociado_CpfAndAndPauta(String associadoId, Pauta pauta);
    List<Voto> findByPautaId(Long pautaId);
}
