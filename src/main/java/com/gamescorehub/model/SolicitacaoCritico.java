package com.gamescorehub.model;

/**
 * Entidade para o formulário de solicitação de selo de Crítico Especializado (RF004).
 */
public class SolicitacaoCritico {
    private int idSolicitacao;
    private Usuario usuario;
    private String veiculoImprensa;
    private String portfolioUrl;
    private String justificativa;
    private String status; // PENDENTE, APROVADO, RECUSADO

    public SolicitacaoCritico() {
        this.status = "PENDENTE";
    }

    public SolicitacaoCritico(int idSolicitacao, Usuario usuario, String veiculoImprensa,
                              String portfolioUrl, String justificativa) {
        this.idSolicitacao = idSolicitacao;
        this.usuario = usuario;
        this.veiculoImprensa = veiculoImprensa;
        this.portfolioUrl = portfolioUrl;
        this.justificativa = justificativa;
        this.status = "PENDENTE";
    }

    public void aprovar() {
        this.status = "APROVADO";
        if (this.usuario != null) {
            this.usuario.setTipoPermissao("critico");
        }
    }

    public void recusar() {
        this.status = "RECUSADO";
    }

    public int getIdSolicitacao() { return idSolicitacao; }
    public void setIdSolicitacao(int idSolicitacao) { this.idSolicitacao = idSolicitacao; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getVeiculoImprensa() { return veiculoImprensa; }
    public void setVeiculoImprensa(String veiculoImprensa) { this.veiculoImprensa = veiculoImprensa; }

    public String getPortfolioUrl() { return portfolioUrl; }
    public void setPortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }

    public String getJustificativa() { return justificativa; }
    public void setJustificativa(String justificativa) { this.justificativa = justificativa; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
