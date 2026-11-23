package com.gamescorehub.model;

/**
 * Entidade que representa um usuário do sistema GameScore Hub.
 * Perfis suportados: jogador, critico, moderador, administrador.
 */
public class Usuario {
    private int idUsuario;
    private String nome;
    private String email;
    private String senha;
    private String tipoPermissao;

    public Usuario() {
    }

    public Usuario(int idUsuario, String nome, String email, String senha, String tipoPermissao) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipoPermissao = tipoPermissao;
    }

    public boolean autenticar(String emailInformado, String senhaInformada) {
        return this.email != null && this.senha != null
                && this.email.equalsIgnoreCase(emailInformado)
                && this.senha.equals(senhaInformada);
    }

    public boolean podeEditarCatalogo() {
        return "moderador".equalsIgnoreCase(tipoPermissao)
                || "administrador".equalsIgnoreCase(tipoPermissao);
    }

    public boolean isCritico() {
        return "critico".equalsIgnoreCase(tipoPermissao);
    }

    public boolean isJogador() {
        return "jogador".equalsIgnoreCase(tipoPermissao);
    }

    public boolean isAdministrador() {
        return "administrador".equalsIgnoreCase(tipoPermissao);
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getTipoPermissao() { return tipoPermissao; }
    public void setTipoPermissao(String tipoPermissao) { this.tipoPermissao = tipoPermissao; }

    @Override
    public String toString() {
        return nome + " (" + tipoPermissao + ")";
    }
}
