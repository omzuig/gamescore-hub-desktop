package com.gamescorehub.view;

import com.gamescorehub.data.RepositorioDados;
import com.gamescorehub.model.SolicitacaoCritico;
import com.gamescorehub.model.Usuario;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Tela 05 — Formulário de Solicitação de Crítico e Painel de Aprovação do Administrador (RF004).
 */
public class DialogCriticos extends JDialog {
    private final RepositorioDados repositorio;
    private final Usuario usuarioLogado;
    private JTextField txtVeiculo;
    private JTextField txtPortfolio;
    private JTextArea txtJustificativa;
    private JTable tabelaSolicitacoes;
    private DefaultTableModel modeloSol;
    private List<SolicitacaoCritico> listaSolicitacoes;

    public DialogCriticos(Frame owner, RepositorioDados repositorio, Usuario usuarioLogado) {
        super(owner, "Programa de Críticos Especializados (RF004)", true);
        this.repositorio = repositorio;
        this.usuarioLogado = usuarioLogado;
        inicializarComponentes();
        carregarSolicitacoes();
    }

    private void inicializarComponentes() {
        setSize(760, 500);
        setLocationRelativeTo(getOwner());

        JTabbedPane abas = new JTabbedPane();
        abas.setFont(TemaUI.FONTE_BOTAO);

        // Aba 1: Formulário de Solicitação
        JPanel abaForm = new JPanel(new GridBagLayout());
        abaForm.setBackground(TemaUI.SUPERFICIE_BRANCA);
        abaForm.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0;
        g.weightx = 1.0;

        JLabel lblIntro = new JLabel("Solicite o selo de Crítico Especializado para compor a Nota da Crítica:");
        lblIntro.setFont(TemaUI.FONTE_SUBTITULO);
        g.gridy = 0;
        abaForm.add(lblIntro, g);

        g.gridy = 1;
        abaForm.add(new JLabel("Veículo de Imprensa / Canal: *"), g);
        txtVeiculo = new JTextField(30);
        txtVeiculo.setPreferredSize(new Dimension(360, 32));
        g.gridy = 2;
        abaForm.add(txtVeiculo, g);

        g.gridy = 3;
        abaForm.add(new JLabel("Link do Portfólio / Publicações: *"), g);
        txtPortfolio = new JTextField(30);
        txtPortfolio.setPreferredSize(new Dimension(360, 32));
        g.gridy = 4;
        abaForm.add(txtPortfolio, g);

        g.gridy = 5;
        abaForm.add(new JLabel("Justificativa / Experiência: *"), g);
        txtJustificativa = new JTextArea(4, 30);
        txtJustificativa.setLineWrap(true);
        txtJustificativa.setWrapStyleWord(true);
        g.gridy = 6;
        g.fill = GridBagConstraints.BOTH;
        g.weighty = 1.0;
        abaForm.add(new JScrollPane(txtJustificativa), g);

        JButton btnEnviar = TemaUI.criarBotao("Enviar Solicitação (Alt+E)", TemaUI.NOTA_CRITICA, KeyEvent.VK_E);
        btnEnviar.addActionListener(e -> enviarSolicitacao());
        g.gridy = 7;
        g.weighty = 0;
        g.fill = GridBagConstraints.NONE;
        g.anchor = GridBagConstraints.EAST;
        abaForm.add(btnEnviar, g);

        abas.addTab("1. Solicitar Selo de Crítico", abaForm);

        // Aba 2: Fila de Aprovação (Administrador)
        JPanel abaAdmin = new JPanel(new BorderLayout(10, 10));
        abaAdmin.setBackground(TemaUI.SUPERFICIE_BRANCA);
        abaAdmin.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        JLabel lblAdmin = new JLabel(usuarioLogado.isAdministrador()
                ? "Fila de solicitações pendentes — aprove para promover o usuário ao perfil 'critico'."
                : "Visualização restrita: apenas Administradores podem aprovar ou recusar solicitações.");
        lblAdmin.setFont(TemaUI.FONTE_CORPO);
        abaAdmin.add(lblAdmin, BorderLayout.NORTH);

        modeloSol = new DefaultTableModel(new String[]{"ID", "Usuário", "Veículo", "Portfólio", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaSolicitacoes = new JTable(modeloSol);
        tabelaSolicitacoes.setRowHeight(28);
        tabelaSolicitacoes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        abaAdmin.add(new JScrollPane(tabelaSolicitacoes), BorderLayout.CENTER);

        JPanel botoesAdmin = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botoesAdmin.setOpaque(false);
        JButton btnAprovar = TemaUI.criarBotao("Aprovar Crítico", TemaUI.NOTA_JOGADORES, KeyEvent.VK_A);
        JButton btnRecusar = TemaUI.criarBotao("Recusar", TemaUI.ALERTA_EXCLUIR, KeyEvent.VK_R);
        btnAprovar.setEnabled(usuarioLogado.isAdministrador());
        btnRecusar.setEnabled(usuarioLogado.isAdministrador());

        btnAprovar.addActionListener(e -> processarSolicitacao("APROVADO"));
        btnRecusar.addActionListener(e -> processarSolicitacao("RECUSADO"));
        botoesAdmin.add(btnAprovar);
        botoesAdmin.add(btnRecusar);
        abaAdmin.add(botoesAdmin, BorderLayout.SOUTH);

        abas.addTab("2. Gestão de Solicitações (Admin)", abaAdmin);
        if (usuarioLogado.isAdministrador()) {
            abas.setSelectedIndex(1);
        }
        add(abas);
    }

    private void enviarSolicitacao() {
        String veiculo = txtVeiculo.getText().trim();
        String port = txtPortfolio.getText().trim();
        String just = txtJustificativa.getText().trim();
        if (veiculo.isEmpty() || port.isEmpty() || just.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos do formulário.",
                    "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }
        SolicitacaoCritico sol = new SolicitacaoCritico(0, usuarioLogado, veiculo, port, just);
        if (repositorio.enviarSolicitacaoCritico(sol)) {
            txtVeiculo.setText("");
            txtPortfolio.setText("");
            txtJustificativa.setText("");
            carregarSolicitacoes();
            JOptionPane.showMessageDialog(this,
                    "Solicitação enviada com sucesso! Aguarde análise do Administrador.",
                    "Enviado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void carregarSolicitacoes() {
        modeloSol.setRowCount(0);
        listaSolicitacoes = repositorio.listarSolicitacoes();
        for (SolicitacaoCritico s : listaSolicitacoes) {
            modeloSol.addRow(new Object[]{
                s.getIdSolicitacao(),
                s.getUsuario().getNome(),
                s.getVeiculoImprensa(),
                s.getPortfolioUrl(),
                s.getStatus()
            });
        }
    }

    private void processarSolicitacao(String status) {
        int row = tabelaSolicitacoes.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma solicitação na tabela.",
                    "Seleção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        SolicitacaoCritico sel = listaSolicitacoes.get(row);
        if (repositorio.atualizarStatusSolicitacao(sel.getIdSolicitacao(), status)) {
            carregarSolicitacoes();
            JOptionPane.showMessageDialog(this, "Solicitação marcada como " + status + ".",
                    "Atualizado", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
