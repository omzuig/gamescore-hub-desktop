package com.gamescorehub;

import com.gamescorehub.dao.ConexaoBD;
import com.gamescorehub.data.RepositorioDados;
import com.gamescorehub.data.RepositorioMySQL;
import com.gamescorehub.view.TelaLogin;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Ponto de entrada da aplicação Desktop GameScore Hub — Etapa 4 (Integrado ao MySQL via JDBC).
 */
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> {
            String status = ConexaoBD.testarConexao();
            if (!"OK".equals(status)) {
                JOptionPane.showMessageDialog(null,
                        "Não foi possível conectar ao banco de dados MySQL (gamescore_db).\n\n"
                        + "Verifique:\n"
                        + "1. Se o serviço MySQL está iniciado;\n"
                        + "2. Se o script sql/gamescore_db_completo.sql foi executado no MySQL Workbench;\n"
                        + "3. Se o usuário e senha em src/main/resources/db.properties estão corretos.\n\n"
                        + "Detalhe técnico: " + status,
                        "Erro de Conexão MySQL", JOptionPane.ERROR_MESSAGE);
                return;
            }
            RepositorioDados repo = new RepositorioMySQL();
            new TelaLogin(repo).setVisible(true);
        });
    }
}
