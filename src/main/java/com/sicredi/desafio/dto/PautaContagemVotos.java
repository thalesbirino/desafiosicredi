package com.sicredi.desafio.dto;

public class PautaContagemVotos {
    private String pauta;
    private Long votosSim;
    private Long votosNao;

    public PautaContagemVotos(String pauta, Long votosSim, Long votosNao) {
        this.pauta = pauta;
        this.votosSim = votosSim;
        this.votosNao = votosNao;
    }

    public String getPauta() {
        return pauta;
    }

    public void setPauta(String pauta) {
        this.pauta = pauta;
    }

    public Long getVotosSim() {
        return votosSim;
    }

    public void setVotosSim(Long votosSim) {
        this.votosSim = votosSim;
    }

    public Long getVotosNao() {
        return votosNao;
    }

    public void setVotosNao(Long votosNao) {
        this.votosNao = votosNao;
    }
}
