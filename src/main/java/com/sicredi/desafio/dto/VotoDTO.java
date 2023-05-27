package com.sicredi.desafio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VotoDTO {
    @NotNull(message = "O ID da pauta é obrigatório!")
    private Long pautaId;
    @NotBlank(message = "O CPF é obrigatório!")
    private String cpf;
    @NotNull(message = "O voto é obrigatório!")
    private Boolean voto;
}
