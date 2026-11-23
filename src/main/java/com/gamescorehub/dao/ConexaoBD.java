package com.gamescorehub.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gerenciador de conexão JDBC com o banco de dados MySQL (gamescore_db).
 */
public final class ConexaoBD {
    private static String url = "jdbc:mysql://localhost:3306/gamescore_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo";
    private static String user = "root";
    private static String password = "";

    static {
        carregarConfiguracoes();
    }

    private ConexaoBD() {}

    private static void carregarConfiguracoes() {
        try (InputStream is = ConexaoBD.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                url = props.getProperty("db.url", url).trim();
                user = props.getProperty("db.user", user).trim();
                password = props.getProperty("db.password", password);
            }
        } catch (Exception e) {
            System.err.println("Aviso: usando configurações padrão de conexão MySQL: " + e.getMessage());
        }
    }

    public static Connection obterConexao() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static String testarConexao() {
        try (Connection conn = obterConexao()) {
            return conn != null && !conn.isClosed() ? "OK" : "Falha ao abrir conexão.";
        } catch (SQLException e) {
            return e.getMessage();
        }
    }
}
