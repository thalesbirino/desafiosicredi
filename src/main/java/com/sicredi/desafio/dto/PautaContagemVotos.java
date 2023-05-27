package com.sicredi.desafio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PautaContagemVotos {
    private String pauta;
    private Long votosSim;
    private Long votosNao;
}
