package com.gamescorehub.dao;

import com.gamescorehub.model.SolicitacaoCritico;
import com.gamescorehub.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestão de solicitações de selo de Crítico Especializado (RF004).
 */
public class SolicitacaoCriticoDAO {

    public List<SolicitacaoCritico> listar() {
        List<SolicitacaoCritico> lista = new ArrayList<>();
        String sql = "SELECT s.id_solicitacao, s.veiculo_imprensa, s.portfolio_url, s.justificativa, s.status, "
                + "u.id_usuario, u.nome, u.email, u.tipo_permissao "
                + "FROM solicitacoes_critico s "
                + "INNER JOIN usuarios u ON s.usuarios_id_usuario = u.id_usuario "
                + "ORDER BY s.id_solicitacao DESC";
        try (Connection conn = ConexaoBD.obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        "",
                        rs.getString("tipo_permissao")
                );
                SolicitacaoCritico s = new SolicitacaoCritico(
                        rs.getInt("id_solicitacao"),
                        u,
                        rs.getString("veiculo_imprensa"),
                        rs.getString("portfolio_url"),
                        rs.getString("justificativa")
                );
                s.setStatus(rs.getString("status"));
                lista.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar solicitações: " + e.getMessage());
        }
        return lista;
    }

    public boolean inserir(SolicitacaoCritico sol) {
        String sql = "INSERT INTO solicitacoes_critico (usuarios_id_usuario, veiculo_imprensa, portfolio_url, justificativa, status) "
                + "VALUES (?, ?, ?, ?, 'PENDENTE')";
        try (Connection conn = ConexaoBD.obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, sol.getUsuario().getIdUsuario());
            ps.setString(2, sol.getVeiculoImprensa());
            ps.setString(3, sol.getPortfolioUrl());
            ps.setString(4, sol.getJustificativa());
            int linhas = ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    sol.setIdSolicitacao(keys.getInt(1));
                }
            }
            return linhas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir solicitação: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizarStatus(int idSolicitacao, String novoStatus) {
        String sqlUpdateSol = "UPDATE solicitacoes_critico SET status = ? WHERE id_solicitacao = ?";
        String sqlPromover = "UPDATE usuarios u INNER JOIN solicitacoes_critico s ON s.usuarios_id_usuario = u.id_usuario "
                + "SET u.tipo_permissao = 'critico' WHERE s.id_solicitacao = ?";
        try (Connection conn = ConexaoBD.obterConexao()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sqlUpdateSol)) {
                ps1.setString(1, novoStatus);
                ps1.setInt(2, idSolicitacao);
                ps1.executeUpdate();
            }
            if ("APROVADO".equalsIgnoreCase(novoStatus)) {
                try (PreparedStatement ps2 = conn.prepareStatement(sqlPromover)) {
                    ps2.setInt(1, idSolicitacao);
                    ps2.executeUpdate();
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status da solicitação: " + e.getMessage());
            return false;
        }
    }
}
