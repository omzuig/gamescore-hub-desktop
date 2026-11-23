package com.gamescorehub.view;

import com.gamescorehub.data.RepositorioDados;
import com.gamescorehub.model.Jogo;
import com.gamescorehub.model.Usuario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Tela 02 — Catálogo Principal de Jogos com busca, KPIs e médias separadas (Crítica vs Jogadores).
 */
public class TelaCatalogo extends JFrame {
    private final RepositorioDados repositorio;
    private final Usuario usuarioLogado;
    private JTextField txtBusca;
    private JTable tabelaJogos;
    private DefaultTableModel modeloTabela;
    private JLabel lblTotalJogos;
    private JLabel lblTotalAvaliacoes;
    private List<Jogo> jogosExibidos = new ArrayList<>();

    public TelaCatalogo(RepositorioDados repositorio, Usuario usuarioLogado) {
        this.repositorio = repositorio;
        this.usuarioLogado = usuarioLogado;
        inicializarComponentes();
        carregarTabela("");
    }

    private void inicializarComponentes() {
        setTitle("GameScore Hub — Catálogo Principal de Jogos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 660);
        setMinimumSize(new Dimension(920, 560));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 10));
        getContentPane().setBackground(TemaUI.FUNDO_CANVAS);

        // Barra Superior
        JPanel barraTopo = new JPanel(new BorderLayout(12, 0));
        barraTopo.setBackground(TemaUI.BARRA_TOPO);
        barraTopo.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JPanel esqTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        esqTopo.setOpaque(false);
        JLabel lblMarca = new JLabel("GAMESCORE HUB");
        lblMarca.setFont(new Font("SansSerif", Font.BOLD, 17));
        lblMarca.setForeground(Color.WHITE);

        JLabel lblUsuario = new JLabel("Usuário: " + usuarioLogado.getNome()
                + " [" + usuarioLogado.getTipoPermissao().toUpperCase() + "]");
        lblUsuario.setFont(TemaUI.FONTE_LEGENDA);
        lblUsuario.setForeground(new Color(226, 232, 240));
        lblUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(71, 85, 105)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        esqTopo.add(lblMarca);
        esqTopo.add(lblUsuario);

        JPanel dirTopo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        dirTopo.setOpaque(false);

        JButton btnNovoJogo = TemaUI.criarBotao("+ Novo Jogo (Alt+N)", TemaUI.ACAO_PRIMARIA, KeyEvent.VK_N);
        TemaUI.configurarAcessibilidade(btnNovoJogo, "Novo Jogo", "Cadastra um novo jogo no catálogo (Moderador/Admin)");
        btnNovoJogo.setEnabled(usuarioLogado.podeEditarCatalogo());
        btnNovoJogo.addActionListener(e -> abrirCadastroJogo(null));

        JButton btnCriticos = TemaUI.criarBotao("Críticos (Alt+C)", TemaUI.NOTA_CRITICA, KeyEvent.VK_C);
        TemaUI.configurarAcessibilidade(btnCriticos, "Programa de Críticos", "Solicitar selo de crítico ou aprovar solicitações");
        btnCriticos.addActionListener(e -> {
            DialogCriticos dlg = new DialogCriticos(this, repositorio, usuarioLogado);
            dlg.setVisible(true);
            carregarTabela(txtBusca.getText());
        });

        JButton btnAtualizar = TemaUI.criarBotao("Atualizar", TemaUI.NOTA_JOGADORES, KeyEvent.VK_R);
        btnAtualizar.addActionListener(e -> carregarTabela(txtBusca.getText()));

        JButton btnSair = TemaUI.criarBotao("Sair", TemaUI.ALERTA_EXCLUIR, KeyEvent.VK_Q);
        btnSair.addActionListener(e -> {
            new TelaLogin(repositorio).setVisible(true);
            dispose();
        });

        dirTopo.add(btnNovoJogo);
        dirTopo.add(btnCriticos);
        dirTopo.add(btnAtualizar);
        dirTopo.add(btnSair);

        barraTopo.add(esqTopo, BorderLayout.WEST);
        barraTopo.add(dirTopo, BorderLayout.EAST);
        add(barraTopo, BorderLayout.NORTH);

        // Conteúdo Central
        JPanel painelCentro = new JPanel(new BorderLayout(10, 12));
        painelCentro.setBackground(TemaUI.FUNDO_CANVAS);
        painelCentro.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));

        // Linha de Busca + KPIs
        JPanel linhaBusca = new JPanel(new BorderLayout(12, 0));
        linhaBusca.setOpaque(false);

        JPanel boxBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        boxBusca.setBackground(TemaUI.SUPERFICIE_BRANCA);
        boxBusca.setBorder(BorderFactory.createLineBorder(TemaUI.BORDA_SUAVE));

        JLabel lblBusca = new JLabel("Buscar jogo (Alt+B):");
        lblBusca.setFont(TemaUI.FONTE_BOTAO);
        lblBusca.setDisplayedMnemonic(KeyEvent.VK_B);
        txtBusca = new JTextField(26);
        txtBusca.setFont(TemaUI.FONTE_CORPO);
        txtBusca.setPreferredSize(new Dimension(280, 32));
        lblBusca.setLabelFor(txtBusca);
        TemaUI.configurarAcessibilidade(txtBusca, "Busca de Jogos", "Digite título ou palavras da sinopse para filtrar");
        txtBusca.addActionListener(e -> carregarTabela(txtBusca.getText()));

        JButton btnFiltrar = TemaUI.criarBotao("Filtrar", TemaUI.ACAO_PRIMARIA, KeyEvent.VK_F);
        btnFiltrar.addActionListener(e -> carregarTabela(txtBusca.getText()));

        JButton btnLimpar = TemaUI.criarBotaoSecundario("Limpar", KeyEvent.VK_L);
        btnLimpar.addActionListener(e -> {
            txtBusca.setText("");
            carregarTabela("");
        });

        boxBusca.add(lblBusca);
        boxBusca.add(txtBusca);
        boxBusca.add(btnFiltrar);
        boxBusca.add(btnLimpar);

        JPanel boxKpis = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        boxKpis.setOpaque(false);

        lblTotalJogos = criarKpiLabel("Jogos: 0", TemaUI.FUNDO_CRITICA, TemaUI.NOTA_CRITICA);
        lblTotalAvaliacoes = criarKpiLabel("Avaliações: 0", TemaUI.FUNDO_JOGADOR, TemaUI.NOTA_JOGADORES);
        boxKpis.add(lblTotalJogos);
        boxKpis.add(lblTotalAvaliacoes);

        linhaBusca.add(boxBusca, BorderLayout.CENTER);
        linhaBusca.add(boxKpis, BorderLayout.EAST);
        painelCentro.add(linhaBusca, BorderLayout.NORTH);

        // Tabela de Jogos
        String[] colunas = {"ID", "Título do Jogo", "Sinopse", "Nota Crítica ★", "Nota Jogadores ★"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaJogos = new JTable(modeloTabela);
        tabelaJogos.setFont(TemaUI.FONTE_CORPO);
        tabelaJogos.setRowHeight(32);
        tabelaJogos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaJogos.getTableHeader().setFont(TemaUI.FONTE_BOTAO);
        tabelaJogos.getTableHeader().setBackground(TemaUI.PRIMARIA_ESCURA);
        tabelaJogos.getTableHeader().setForeground(Color.WHITE);
        tabelaJogos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaJogos.getColumnModel().getColumn(1).setPreferredWidth(240);
        tabelaJogos.getColumnModel().getColumn(2).setPreferredWidth(380);
        tabelaJogos.getColumnModel().getColumn(3).setPreferredWidth(140);
        tabelaJogos.getColumnModel().getColumn(4).setPreferredWidth(140);
        TemaUI.configurarAcessibilidade(tabelaJogos, "Tabela do Catálogo de Jogos",
                "Lista de jogos com médias da crítica e dos jogadores. Dê duplo clique para abrir detalhes.");

        tabelaJogos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    abrirDetalhesSelecionado();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabelaJogos);
        scroll.setBorder(BorderFactory.createLineBorder(TemaUI.BORDA_SUAVE));
        painelCentro.add(scroll, BorderLayout.CENTER);
        add(painelCentro, BorderLayout.CENTER);

        // Barra Inferior de Ações
        JPanel barraAcoes = new JPanel(new BorderLayout());
        barraAcoes.setBackground(new Color(241, 245, 249));
        barraAcoes.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, TemaUI.BORDA_SUAVE),
                BorderFactory.createEmptyBorder(10, 18, 10, 18)
        ));

        JLabel lblDica = new JLabel("Selecione um jogo na tabela (ou clique duas vezes) para visualizar detalhes e avaliar.");
        lblDica.setFont(TemaUI.FONTE_LEGENDA);
        lblDica.setForeground(TemaUI.TEXTO_SECUNDARIO);

        JPanel botoesAcao = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botoesAcao.setOpaque(false);

        JButton btnDetalhes = TemaUI.criarBotao("Ver Detalhes e Avaliar (Alt+D)", TemaUI.ACAO_PRIMARIA, KeyEvent.VK_D);
        btnDetalhes.addActionListener(e -> abrirDetalhesSelecionado());

        JButton btnEditar = TemaUI.criarBotao("Editar Jogo (Alt+E)", TemaUI.NOTA_JOGADORES, KeyEvent.VK_E);
        btnEditar.setEnabled(usuarioLogado.podeEditarCatalogo());
        btnEditar.addActionListener(e -> editarSelecionado());

        JButton btnExcluir = TemaUI.criarBotao("Excluir Jogo", TemaUI.ALERTA_EXCLUIR, KeyEvent.VK_X);
        btnExcluir.setEnabled(usuarioLogado.podeEditarCatalogo());
        btnExcluir.addActionListener(e -> excluirSelecionado());

        botoesAcao.add(btnDetalhes);
        botoesAcao.add(btnEditar);
        botoesAcao.add(btnExcluir);

        barraAcoes.add(lblDica, BorderLayout.WEST);
        barraAcoes.add(botoesAcao, BorderLayout.EAST);
        add(barraAcoes, BorderLayout.SOUTH);
    }

    private JLabel criarKpiLabel(String texto, Color fundo, Color corTexto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(TemaUI.FONTE_BOTAO);
        lbl.setForeground(corTexto);
        lbl.setOpaque(true);
        lbl.setBackground(fundo);
        lbl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(corTexto),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        return lbl;
    }

    public void carregarTabela(String filtro) {
        modeloTabela.setRowCount(0);
        jogosExibidos = repositorio.listarJogos(filtro);
        for (Jogo j : jogosExibidos) {
            double mc = repositorio.calcularMediaCritica(j.getIdJogo());
            double mj = repositorio.calcularMediaJogadores(j.getIdJogo());
            String txtCritica = mc > 0 ? String.format("%.1f / 5.0", mc) : "Sem nota";
            String txtJogadores = mj > 0 ? String.format("%.1f / 5.0", mj) : "Sem nota";
            modeloTabela.addRow(new Object[]{
                j.getIdJogo(), j.getTitulo(), j.getSinopse(), txtCritica, txtJogadores
            });
        }
        lblTotalJogos.setText("Total Jogos: " + jogosExibidos.size());
        lblTotalAvaliacoes.setText("Avaliações: " + repositorio.contarTotalAvaliacoes());
    }

    private Jogo getJogoSelecionado() {
        int row = tabelaJogos.getSelectedRow();
        if (row < 0 || row >= jogosExibidos.size()) {
            JOptionPane.showMessageDialog(this, "Selecione um jogo na tabela primeiro.",
                    "Nenhum Jogo Selecionado", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return jogosExibidos.get(row);
    }

    private void abrirCadastroJogo(Jogo jogoParaEditar) {
        if (!usuarioLogado.podeEditarCatalogo()) {
            JOptionPane.showMessageDialog(this,
                    "Acesso negado (RNF001): apenas Moderadores e Administradores podem alterar o catálogo.",
                    "Permissão Insuficiente", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DialogCadastroJogo dlg = new DialogCadastroJogo(this, repositorio, jogoParaEditar);
        dlg.setVisible(true);
        if (dlg.isSalvo()) {
            carregarTabela(txtBusca.getText());
        }
    }

    private void editarSelecionado() {
        Jogo j = getJogoSelecionado();
        if (j != null) {
            abrirCadastroJogo(j);
        }
    }

    private void excluirSelecionado() {
        Jogo j = getJogoSelecionado();
        if (j == null) return;
        int resp = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir \"" + j.getTitulo() + "\" e suas avaliações?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resp == JOptionPane.YES_OPTION) {
            if (repositorio.excluirJogo(j.getIdJogo())) {
                carregarTabela(txtBusca.getText());
                JOptionPane.showMessageDialog(this, "Jogo excluído com sucesso.",
                        "Exclusão", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void abrirDetalhesSelecionado() {
        Jogo j = getJogoSelecionado();
        if (j != null) {
            DialogDetalhesJogo dlg = new DialogDetalhesJogo(this, repositorio, usuarioLogado, j);
            dlg.setVisible(true);
            carregarTabela(txtBusca.getText());
        }
    }
}
