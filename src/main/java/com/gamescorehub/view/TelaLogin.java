package com.gamescorehub.view;

import com.gamescorehub.data.RepositorioDados;
import com.gamescorehub.model.Usuario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Tela 01 — Autenticação de Usuário e Cadastro de Novo Jogador.
 */
public class TelaLogin extends JFrame {
    private final RepositorioDados repositorio;
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JCheckBox chkMostrarSenha;

    public TelaLogin(RepositorioDados repositorio) {
        this.repositorio = repositorio;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("GameScore Hub — Autenticação de Usuário");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 540);
        setMinimumSize(new Dimension(820, 480));
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // Painel Esquerdo (Identidade Visual)
        JPanel painelMarca = new JPanel();
        painelMarca.setBackground(TemaUI.PRIMARIA_ESCURA);
        painelMarca.setLayout(new BoxLayout(painelMarca, BoxLayout.Y_AXIS));
        painelMarca.setBorder(BorderFactory.createEmptyBorder(60, 40, 40, 40));

        JLabel lblLogo = new JLabel("GAMESCORE HUB");
        lblLogo.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 28));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Catálogo & Avaliações de Jogos");
        lblSub.setFont(TemaUI.FONTE_SUBTITULO);
        lblSub.setForeground(new Color(147, 197, 253));
        lblSub.setAlignmentX(CENTER_ALIGNMENT);

        JPanel boxInfo = new JPanel();
        boxInfo.setBackground(TemaUI.BARRA_TOPO);
        boxInfo.setLayout(new BoxLayout(boxInfo, BoxLayout.Y_AXIS));
        boxInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85)),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)
        ));
        boxInfo.setMaximumSize(new Dimension(340, 210));

        String[] linhasInfo = {
            "Contas de Teste (Base PI I):",
            "• Jogador: alice@email.com / senha123",
            "• Crítico: bruno@critica.com / critico456",
            "• Moderador: carlos@sistema.com / admin789",
            "• Admin: admin@gamescorehub.com / admin123"
        };
        for (int i = 0; i < linhasInfo.length; i++) {
            JLabel l = new JLabel(linhasInfo[i]);
            l.setFont(i == 0 ? TemaUI.FONTE_BOTAO : TemaUI.FONTE_LEGENDA);
            l.setForeground(i == 0 ? Color.WHITE : new Color(203, 213, 225));
            boxInfo.add(l);
            boxInfo.add(Box.createVerticalStrut(6));
        }

        JLabel lblRodape = new JLabel("Versão Desktop 1.0  |  Java Swing + MySQL");
        lblRodape.setFont(TemaUI.FONTE_LEGENDA);
        lblRodape.setForeground(new Color(148, 163, 184));
        lblRodape.setAlignmentX(CENTER_ALIGNMENT);

        painelMarca.add(Box.createVerticalGlue());
        painelMarca.add(lblLogo);
        painelMarca.add(Box.createVerticalStrut(8));
        painelMarca.add(lblSub);
        painelMarca.add(Box.createVerticalStrut(30));
        painelMarca.add(boxInfo);
        painelMarca.add(Box.createVerticalGlue());
        painelMarca.add(lblRodape);

        // Painel Direito (Formulário de Login)
        JPanel painelDireito = new JPanel(new GridBagLayout());
        painelDireito.setBackground(TemaUI.FUNDO_CANVAS);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(TemaUI.SUPERFICIE_BRANCA);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaUI.BORDA_SUAVE),
                BorderFactory.createEmptyBorder(28, 30, 28, 30)
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0;
        g.weightx = 1.0;

        JLabel lblTitulo = new JLabel("Acessar Sistema");
        lblTitulo.setFont(TemaUI.FONTE_TITULO);
        lblTitulo.setForeground(TemaUI.TEXTO_ESCURO);
        g.gridy = 0;
        card.add(lblTitulo, g);

        JLabel lblDesc = new JLabel("Informe suas credenciais de acesso:");
        lblDesc.setFont(TemaUI.FONTE_LEGENDA);
        lblDesc.setForeground(TemaUI.TEXTO_SECUNDARIO);
        g.gridy = 1;
        card.add(lblDesc, g);

        JLabel lblEmail = new JLabel("E-mail (Alt+E): *");
        lblEmail.setFont(TemaUI.FONTE_BOTAO);
        lblEmail.setDisplayedMnemonic(KeyEvent.VK_E);
        g.gridy = 2;
        g.insets = new Insets(14, 4, 2, 4);
        card.add(lblEmail, g);

        txtEmail = new JTextField("carlos@sistema.com", 24);
        txtEmail.setFont(TemaUI.FONTE_CORPO);
        txtEmail.setPreferredSize(new Dimension(280, 34));
        lblEmail.setLabelFor(txtEmail);
        TemaUI.configurarAcessibilidade(txtEmail, "Campo E-mail", "Digite o e-mail cadastrado para login");
        g.gridy = 3;
        g.insets = new Insets(2, 4, 8, 4);
        card.add(txtEmail, g);

        JLabel lblSenha = new JLabel("Senha (Alt+S): *");
        lblSenha.setFont(TemaUI.FONTE_BOTAO);
        lblSenha.setDisplayedMnemonic(KeyEvent.VK_S);
        g.gridy = 4;
        g.insets = new Insets(6, 4, 2, 4);
        card.add(lblSenha, g);

        txtSenha = new JPasswordField("admin789", 24);
        txtSenha.setFont(TemaUI.FONTE_CORPO);
        txtSenha.setPreferredSize(new Dimension(280, 34));
        lblSenha.setLabelFor(txtSenha);
        TemaUI.configurarAcessibilidade(txtSenha, "Campo Senha", "Digite sua senha de acesso");
        g.gridy = 5;
        g.insets = new Insets(2, 4, 4, 4);
        card.add(txtSenha, g);

        chkMostrarSenha = new JCheckBox("Mostrar senha (acessibilidade visual)");
        chkMostrarSenha.setBackground(TemaUI.SUPERFICIE_BRANCA);
        chkMostrarSenha.setFont(TemaUI.FONTE_LEGENDA);
        char echoPadrao = txtSenha.getEchoChar();
        chkMostrarSenha.addActionListener(e -> {
            txtSenha.setEchoChar(chkMostrarSenha.isSelected() ? (char) 0 : echoPadrao);
        });
        g.gridy = 6;
        g.insets = new Insets(2, 2, 12, 4);
        card.add(chkMostrarSenha, g);

        JButton btnEntrar = TemaUI.criarBotao("ENTRAR NO SISTEMA", TemaUI.ACAO_PRIMARIA, 0);
        TemaUI.configurarAcessibilidade(btnEntrar, "Botão Entrar", "Autentica o usuário e abre o catálogo de jogos");
        btnEntrar.addActionListener(e -> realizarLogin());
        g.gridy = 7;
        g.insets = new Insets(4, 4, 8, 4);
        card.add(btnEntrar, g);

        JButton btnCadastrar = TemaUI.criarBotaoSecundario("Criar Nova Conta de Jogador (Alt+C)", KeyEvent.VK_C);
        TemaUI.configurarAcessibilidade(btnCadastrar, "Criar Conta", "Abre formulário para cadastrar novo jogador");
        btnCadastrar.addActionListener(e -> abrirCadastroUsuario());
        g.gridy = 8;
        card.add(btnCadastrar, g);

        getRootPane().setDefaultButton(btnEntrar);
        painelDireito.add(card);

        add(painelMarca);
        add(painelDireito);
    }

    private void realizarLogin() {
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha o e-mail e a senha para entrar.",
                    "Campos obrigatórios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuario = repositorio.autenticar(email, senha);
        if (usuario == null) {
            JOptionPane.showMessageDialog(this,
                    "E-mail ou senha inválidos. Verifique suas credenciais.",
                    "Falha na Autenticação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TelaCatalogo catalogo = new TelaCatalogo(repositorio, usuario);
        catalogo.setVisible(true);
        dispose();
    }

    private void abrirCadastroUsuario() {
        JTextField campoNome = new JTextField(22);
        JTextField campoEmail = new JTextField(22);
        JPasswordField campoSenha = new JPasswordField(22);

        JPanel form = new JPanel(new GridLayout(0, 1, 4, 4));
        form.add(new JLabel("Nome completo:"));
        form.add(campoNome);
        form.add(new JLabel("E-mail:"));
        form.add(campoEmail);
        form.add(new JLabel("Senha:"));
        form.add(campoSenha);

        int opt = JOptionPane.showConfirmDialog(this, form,
                "Cadastro de Novo Jogador", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opt == JOptionPane.OK_OPTION) {
            String nome = campoNome.getText().trim();
            String email = campoEmail.getText().trim();
            String senha = new String(campoSenha.getPassword()).trim();
            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios.",
                        "Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Usuario novo = new Usuario(0, nome, email, senha, "jogador");
            String ret = repositorio.cadastrarUsuario(novo);
            if ("OK".equals(ret)) {
                JOptionPane.showMessageDialog(this,
                        "Conta criada com sucesso! Você já pode fazer login.",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                txtEmail.setText(email);
                txtSenha.setText(senha);
            } else {
                JOptionPane.showMessageDialog(this, ret, "Erro no Cadastro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
