package com.gamescorehub.dao;

import com.gamescorehub.model.Avaliacao;
import com.gamescorehub.model.Jogo;
import com.gamescorehub.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pelas avaliações, regra RN001 e cálculo separado de médias (RF005) via SQL.
 */
public class AvaliacaoDAO {

    public List<Avaliacao> listarPorJogo(int idJogo) {
        List<Avaliacao> lista = new ArrayList<>();
        String sql = "SELECT a.id_avaliacao, a.nota, a.comentario, a.midia_url, "
                + "u.id_usuario, u.nome, u.email, u.tipo_permissao, "
                + "j.id_jogo, j.titulo, j.sinopse, j.capa_url, j.trailer_url "
                + "FROM avaliacoes a "
                + "INNER JOIN usuarios u ON a.usuarios_id_usuarios = u.id_usuario "
                + "INNER JOIN jogos j ON a.jogos_id_jogo = j.id_jogo "
                + "WHERE a.jogos_id_jogo = ? ORDER BY a.id_avaliacao DESC";

        try (Connection conn = ConexaoBD.obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idJogo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario u = new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            "",
                            rs.getString("tipo_permissao")
                    );
                    Jogo j = new Jogo(
                            rs.getInt("id_jogo"),
                            rs.getString("titulo"),
                            rs.getString("sinopse"),
                            rs.getString("capa_url"),
                            rs.getString("trailer_url")
                    );
                    lista.add(new Avaliacao(
                            rs.getInt("id_avaliacao"),
                            rs.getInt("nota"),
                            rs.getString("comentario"),
                            rs.getString("midia_url"),
                            u, j
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar avaliações: " + e.getMessage());
        }
        return lista;
    }

    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM avaliacoes";
        try (Connection conn = ConexaoBD.obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao contar avaliações: " + e.getMessage());
        }
        return 0;
    }

    public String registrar(Avaliacao avaliacao) {
        if (!avaliacao.validarNota()) {
            return "ERRO: A nota deve estar entre 1 e 5 estrelas.";
        }
        String sqlCheck = "SELECT COUNT(*) FROM avaliacoes WHERE usuarios_id_usuarios = ? AND jogos_id_jogo = ?";
        String sqlInsert = "INSERT INTO avaliacoes (nota, comentario, midia_url, usuarios_id_usuarios, jogos_id_jogo) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.obterConexao()) {
            // RN001: Jogador comum só pode avaliar cada jogo uma única vez
            if (avaliacao.getUsuario().isJogador()) {
                try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                    psCheck.setInt(1, avaliacao.getUsuario().getIdUsuario());
                    psCheck.setInt(2, avaliacao.getJogo().getIdJogo());
                    try (ResultSet rs = psCheck.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            return "REGRA DE NEGÓCIO (RN001): Jogador comum só pode avaliar cada jogo uma única vez.";
                        }
                    }
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, avaliacao.getNota());
                ps.setString(2, avaliacao.getComentario());
                ps.setString(3, avaliacao.getMidiaUrl());
                ps.setInt(4, avaliacao.getUsuario().getIdUsuario());
                ps.setInt(5, avaliacao.getJogo().getIdJogo());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        avaliacao.setIdAvaliacao(keys.getInt(1));
                    }
                }
                return "OK";
            }
        } catch (SQLException e) {
            return "Erro ao salvar avaliação no banco: " + e.getMessage();
        }
    }

    public double calcularMediaPorPerfil(int idJogo, String tipoPermissao) {
        String sql = "SELECT AVG(a.nota) AS media FROM avaliacoes a "
                + "INNER JOIN usuarios u ON a.usuarios_id_usuarios = u.id_usuario "
                + "WHERE a.jogos_id_jogo = ? AND u.tipo_permissao = ?";
        try (Connection conn = ConexaoBD.obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idJogo);
            ps.setString(2, tipoPermissao);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("media");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao calcular média: " + e.getMessage());
        }
        return 0.0;
    }
}
