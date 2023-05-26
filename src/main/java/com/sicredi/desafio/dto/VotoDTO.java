package com.sicredi.desafio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VotoDTO {
    private Long pautaId;
    private String cpf;
    private Boolean voto;
}
