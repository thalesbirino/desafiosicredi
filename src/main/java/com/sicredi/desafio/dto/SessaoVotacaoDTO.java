package com.sicredi.desafio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessaoVotacaoDTO {
    private Long pautaId;
    private LocalDateTime encerramento;
    private LocalDateTime abertura;
}
