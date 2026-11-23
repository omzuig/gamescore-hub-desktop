package com.gamescorehub.model;

/**
 * Entidade que representa um jogo eletrônico no catálogo do GameScore Hub.
 */
public class Jogo {
    private int idJogo;
    private String titulo;
    private String sinopse;
    private String capaUrl;
    private String trailerUrl;

    public Jogo() {
    }

    public Jogo(int idJogo, String titulo, String sinopse, String capaUrl, String trailerUrl) {
        this.idJogo = idJogo;
        this.titulo = titulo;
        this.sinopse = sinopse;
        this.capaUrl = capaUrl;
        this.trailerUrl = trailerUrl;
    }

    public int getIdJogo() { return idJogo; }
    public void setIdJogo(int idJogo) { this.idJogo = idJogo; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getSinopse() { return sinopse; }
    public void setSinopse(String sinopse) { this.sinopse = sinopse; }

    public String getCapaUrl() { return capaUrl; }
    public void setCapaUrl(String capaUrl) { this.capaUrl = capaUrl; }

    public String getTrailerUrl() { return trailerUrl; }
    public void setTrailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; }

    @Override
    public String toString() {
        return "[" + idJogo + "] " + titulo;
    }
}
