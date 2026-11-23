package com.gamescorehub.view;

import com.gamescorehub.data.RepositorioDados;
import com.gamescorehub.model.Jogo;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Tela 03 — Cadastro e Edição de Jogo no Catálogo (RF001).
 */
public class DialogCadastroJogo extends JDialog {
    private final RepositorioDados repositorio;
    private final Jogo jogoEdicao;
    private JTextField txtTitulo;
    private JTextArea txtSinopse;
    private JTextField txtCapa;
    private JTextField txtTrailer;
    private boolean salvo = false;

    public DialogCadastroJogo(Frame owner, RepositorioDados repositorio, Jogo jogoEdicao) {
        super(owner, jogoEdicao == null ? "Cadastrar Novo Jogo (RF001)" : "Editar Jogo", true);
        this.repositorio = repositorio;
        this.jogoEdicao = jogoEdicao;
        inicializarComponentes();
        if (jogoEdicao != null) {
            preencherCampos();
        }
    }

    private void inicializarComponentes() {
        setSize(620, 480);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(TemaUI.FUNDO_CANVAS);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(TemaUI.SUPERFICIE_BRANCA);
        form.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0;
        g.weightx = 1.0;

        JLabel lblCab = new JLabel(jogoEdicao == null ? "Novo Jogo no Catálogo" : "Editar Jogo #" + jogoEdicao.getIdJogo());
        lblCab.setFont(TemaUI.FONTE_TITULO);
        g.gridy = 0;
        g.gridwidth = 2;
        form.add(lblCab, g);

        g.gridwidth = 2;
        g.gridy = 1;
        JLabel lblTit = new JLabel("Título do Jogo: *");
        lblTit.setFont(TemaUI.FONTE_BOTAO);
        form.add(lblTit, g);

        txtTitulo = new JTextField(30);
        txtTitulo.setFont(TemaUI.FONTE_CORPO);
        txtTitulo.setPreferredSize(new Dimension(400, 32));
        lblTit.setLabelFor(txtTitulo);
        g.gridy = 2;
        form.add(txtTitulo, g);

        g.gridy = 3;
        JLabel lblSin = new JLabel("Sinopse Descritiva: *");
        lblSin.setFont(TemaUI.FONTE_BOTAO);
        form.add(lblSin, g);

        txtSinopse = new JTextArea(4, 30);
        txtSinopse.setFont(TemaUI.FONTE_CORPO);
        txtSinopse.setLineWrap(true);
        txtSinopse.setWrapStyleWord(true);
        lblSin.setLabelFor(txtSinopse);
        g.gridy = 4;
        g.fill = GridBagConstraints.BOTH;
        g.weighty = 1.0;
        form.add(new JScrollPane(txtSinopse), g);

        g.weighty = 0;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridy = 5;
        JLabel lblCapa = new JLabel("Capa do Jogo (Caminho local ou URL):");
        lblCapa.setFont(TemaUI.FONTE_BOTAO);
        form.add(lblCapa, g);

        txtCapa = new JTextField(24);
        txtCapa.setFont(TemaUI.FONTE_CORPO);
        txtCapa.setPreferredSize(new Dimension(320, 32));
        g.gridy = 6;
        g.gridwidth = 1;
        g.weightx = 0.8;
        form.add(txtCapa, g);

        JButton btnEscolherCapa = TemaUI.criarBotao("Selecionar... (Alt+I)", TemaUI.BARRA_TOPO, KeyEvent.VK_I);
        btnEscolherCapa.addActionListener(e -> selecionarImagemCapa());
        g.gridx = 1;
        g.weightx = 0.2;
        form.add(btnEscolherCapa, g);

        g.gridx = 0;
        g.gridwidth = 2;
        g.weightx = 1.0;
        g.gridy = 7;
        JLabel lblTr = new JLabel("Link / ID do Trailer no YouTube:");
        lblTr.setFont(TemaUI.FONTE_BOTAO);
        form.add(lblTr, g);

        txtTrailer = new JTextField(30);
        txtTrailer.setFont(TemaUI.FONTE_CORPO);
        txtTrailer.setPreferredSize(new Dimension(400, 32));
        g.gridy = 8;
        form.add(txtTrailer, g);

        add(form, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rodape.setBackground(TemaUI.FUNDO_CANVAS);
        JButton btnCancelar = TemaUI.criarBotaoSecundario("Cancelar", 0);
        btnCancelar.addActionListener(e -> dispose());

        JButton btnSalvar = TemaUI.criarBotao("Salvar Jogo (Alt+S)", TemaUI.ACAO_PRIMARIA, KeyEvent.VK_S);
        btnSalvar.addActionListener(e -> salvar());

        rodape.add(btnCancelar);
        rodape.add(btnSalvar);
        add(rodape, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(btnSalvar);
    }

    private void preencherCampos() {
        txtTitulo.setText(jogoEdicao.getTitulo());
        txtSinopse.setText(jogoEdicao.getSinopse());
        txtCapa.setText(jogoEdicao.getCapaUrl() != null ? jogoEdicao.getCapaUrl() : "");
        txtTrailer.setText(jogoEdicao.getTrailerUrl() != null ? jogoEdicao.getTrailerUrl() : "");
    }

    private void selecionarImagemCapa() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Imagens (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File arq = fc.getSelectedFile();
            txtCapa.setText(arq.getAbsolutePath());
        }
    }

    private void salvar() {
        String titulo = txtTitulo.getText().trim();
        String sinopse = txtSinopse.getText().trim();
        String capa = txtCapa.getText().trim();
        String trailer = txtTrailer.getText().trim();

        if (titulo.isEmpty() || sinopse.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o título e a sinopse do jogo.",
                    "Campos Obrigatórios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Jogo jogo = (jogoEdicao != null) ? jogoEdicao : new Jogo();
        jogo.setTitulo(titulo);
        jogo.setSinopse(sinopse);
        jogo.setCapaUrl(capa);
        jogo.setTrailerUrl(trailer);

        if (repositorio.salvarJogo(jogo)) {
            salvo = true;
            JOptionPane.showMessageDialog(this, "Jogo salvo com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Não foi possível salvar o jogo.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSalvo() {
        return salvo;
    }
}
