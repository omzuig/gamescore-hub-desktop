package com.gamescorehub.dao;

import com.gamescorehub.model.Jogo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pelas operações CRUD na tabela jogos.
 */
public class JogoDAO {

    public List<Jogo> listar(String filtro) {
        List<Jogo> lista = new ArrayList<>();
        boolean temFiltro = filtro != null && !filtro.trim().isEmpty();
        String sql = temFiltro
                ? "SELECT id_jogo, titulo, sinopse, capa_url, trailer_url FROM jogos "
                  + "WHERE LOWER(titulo) LIKE ? OR LOWER(sinopse) LIKE ? ORDER BY id_jogo ASC"
                : "SELECT id_jogo, titulo, sinopse, capa_url, trailer_url FROM jogos ORDER BY id_jogo ASC";

        try (Connection conn = ConexaoBD.obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (temFiltro) {
                String termo = "%" + filtro.trim().toLowerCase() + "%";
                ps.setString(1, termo);
                ps.setString(2, termo);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Jogo(
                            rs.getInt("id_jogo"),
                            rs.getString("titulo"),
                            rs.getString("sinopse"),
                            rs.getString("capa_url"),
                            rs.getString("trailer_url")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar jogos: " + e.getMessage());
        }
        return lista;
    }

    public boolean salvar(Jogo jogo) {
        if (jogo.getIdJogo() <= 0) {
            String sql = "INSERT INTO jogos (titulo, sinopse, capa_url, trailer_url) VALUES (?, ?, ?, ?)";
            try (Connection conn = ConexaoBD.obterConexao();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, jogo.getTitulo());
                ps.setString(2, jogo.getSinopse());
                ps.setString(3, jogo.getCapaUrl());
                ps.setString(4, jogo.getTrailerUrl());
                int linhas = ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        jogo.setIdJogo(keys.getInt(1));
                    }
                }
                return linhas > 0;
            } catch (SQLException e) {
                System.err.println("Erro ao inserir jogo: " + e.getMessage());
                return false;
            }
        } else {
            String sql = "UPDATE jogos SET titulo = ?, sinopse = ?, capa_url = ?, trailer_url = ? WHERE id_jogo = ?";
            try (Connection conn = ConexaoBD.obterConexao();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, jogo.getTitulo());
                ps.setString(2, jogo.getSinopse());
                ps.setString(3, jogo.getCapaUrl());
                ps.setString(4, jogo.getTrailerUrl());
                ps.setInt(5, jogo.getIdJogo());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                System.err.println("Erro ao atualizar jogo: " + e.getMessage());
                return false;
            }
        }
    }

    public boolean excluir(int idJogo) {
        String sql = "DELETE FROM jogos WHERE id_jogo = ?";
        try (Connection conn = ConexaoBD.obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idJogo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir jogo: " + e.getMessage());
            return false;
        }
    }
}
