package com.sicredi.desafio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessaoVotacaoDTO {
    @NotNull(message = "O ID da pauta é obrigatório")
    private Long pautaId;
    @NotNull(message = "A data de encerramento é obrigatória")
    private LocalDateTime encerramento;
    private LocalDateTime abertura;
}
