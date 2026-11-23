package com.gamescorehub.model;

/**
 * Entidade que representa uma avaliação (nota de 1 a 5 e comentário) feita por um usuário para um jogo.
 */
public class Avaliacao {
    private int idAvaliacao;
    private int nota;
    private String comentario;
    private String midiaUrl;
    private Usuario usuario;
    private Jogo jogo;

    public Avaliacao() {
    }

    public Avaliacao(int idAvaliacao, int nota, String comentario, String midiaUrl, Usuario usuario, Jogo jogo) {
        this.idAvaliacao = idAvaliacao;
        this.nota = nota;
        this.comentario = comentario;
        this.midiaUrl = midiaUrl;
        this.usuario = usuario;
        this.jogo = jogo;
    }

    public boolean validarNota() {
        return nota >= 1 && nota <= 5;
    }

    public int getIdAvaliacao() { return idAvaliacao; }
    public void setIdAvaliacao(int idAvaliacao) { this.idAvaliacao = idAvaliacao; }

    public int getNota() { return nota; }
    public void setNota(int nota) { this.nota = nota; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public String getMidiaUrl() { return midiaUrl; }
    public void setMidiaUrl(String midiaUrl) { this.midiaUrl = midiaUrl; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Jogo getJogo() { return jogo; }
    public void setJogo(Jogo jogo) { this.jogo = jogo; }
}
