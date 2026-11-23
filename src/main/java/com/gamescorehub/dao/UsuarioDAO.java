package com.gamescorehub.dao;

import com.gamescorehub.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DAO responsável pelas operações SQL na tabela usuarios.
 */
public class UsuarioDAO {

    public Usuario autenticar(String email, String senha) {
        String sql = "SELECT id_usuario, nome, email, senha, tipo_permissao FROM usuarios WHERE email = ? AND senha = ?";
        try (Connection conn = ConexaoBD.obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, senha);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao autenticar usuário: " + e.getMessage());
        }
        return null;
    }

    public String cadastrar(Usuario usuario) {
        String sqlCheck = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        String sqlInsert = "INSERT INTO usuarios (nome, email, senha, tipo_permissao) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.obterConexao()) {
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                psCheck.setString(1, usuario.getEmail());
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return "ERRO: Já existe um usuário cadastrado com este e-mail.";
                    }
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, usuario.getNome());
                ps.setString(2, usuario.getEmail());
                ps.setString(3, usuario.getSenha());
                String perm = (usuario.getTipoPermissao() == null || usuario.getTipoPermissao().trim().isEmpty())
                        ? "jogador" : usuario.getTipoPermissao();
                ps.setString(4, perm);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        usuario.setIdUsuario(keys.getInt(1));
                    }
                }
                return "OK";
            }
        } catch (SQLException e) {
            return "Erro no banco de dados: " + e.getMessage();
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id_usuario"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("senha"),
                rs.getString("tipo_permissao")
        );
    }
}
