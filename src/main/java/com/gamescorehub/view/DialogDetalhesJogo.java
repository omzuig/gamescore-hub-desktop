package com.gamescorehub.view;

import com.gamescorehub.data.RepositorioDados;
import com.gamescorehub.model.Avaliacao;
import com.gamescorehub.model.Jogo;
import com.gamescorehub.model.Usuario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URI;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
 * Tela 04 — Detalhes do Jogo, Trailer, Médias Separadas (RF005) e Registro de Avaliações (RF002/RF003/RN001).
 */
public class DialogDetalhesJogo extends JDialog {
    private final RepositorioDados repositorio;
    private final Usuario usuarioLogado;
    private final Jogo jogo;

    private JLabel lblMediaCritica;
    private JLabel lblMediaJogadores;
    private JPanel painelListaAvaliacoes;
    private JComboBox<String> cmbNota;
    private JTextArea txtComentario;
    private JTextField txtMidia;

    public DialogDetalhesJogo(Frame owner, RepositorioDados repositorio, Usuario usuarioLogado, Jogo jogo) {
        super(owner, "Detalhes e Avaliações — " + jogo.getTitulo(), true);
        this.repositorio = repositorio;
        this.usuarioLogado = usuarioLogado;
        this.jogo = jogo;
        inicializarComponentes();
        atualizarDados();
    }

    private void inicializarComponentes() {
        setSize(960, 620);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(TemaUI.FUNDO_CANVAS);

        // Cabeçalho do Jogo
        JPanel topo = new JPanel(new BorderLayout(16, 8));
        topo.setBackground(TemaUI.SUPERFICIE_BRANCA);
        topo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, TemaUI.BORDA_SUAVE),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));

        JPanel infoJogo = new JPanel();
        infoJogo.setOpaque(false);
        infoJogo.setLayout(new BoxLayout(infoJogo, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel(jogo.getTitulo());
        lblTitulo.setFont(TemaUI.FONTE_TITULO);
        lblTitulo.setForeground(TemaUI.TEXTO_ESCURO);

        JLabel lblSinopse = new JLabel("<html><body style='width:460px'>"
                + (jogo.getSinopse() != null ? jogo.getSinopse() : "Sem sinopse cadastrada.")
                + "</body></html>");
        lblSinopse.setFont(TemaUI.FONTE_CORPO);
        lblSinopse.setForeground(TemaUI.TEXTO_SECUNDARIO);

        JButton btnTrailer = TemaUI.criarBotao("Abrir Trailer no YouTube", TemaUI.ALERTA_EXCLUIR, KeyEvent.VK_T);
        btnTrailer.setEnabled(jogo.getTrailerUrl() != null && !jogo.getTrailerUrl().trim().isEmpty());
        btnTrailer.addActionListener(e -> abrirTrailer());

        infoJogo.add(lblTitulo);
        infoJogo.add(Box.createVerticalStrut(6));
        infoJogo.add(lblSinopse);
        infoJogo.add(Box.createVerticalStrut(10));
        infoJogo.add(btnTrailer);

        JPanel painelNotas = new JPanel(new GridLayout(1, 2, 10, 0));
        painelNotas.setOpaque(false);

        lblMediaCritica = criarCardNota("NOTA DA CRÍTICA", "--", TemaUI.FUNDO_CRITICA, TemaUI.NOTA_CRITICA);
        lblMediaJogadores = criarCardNota("NOTA JOGADORES", "--", TemaUI.FUNDO_JOGADOR, TemaUI.NOTA_JOGADORES);
        painelNotas.add(lblMediaCritica);
        painelNotas.add(lblMediaJogadores);

        topo.add(infoJogo, BorderLayout.CENTER);
        topo.add(painelNotas, BorderLayout.EAST);
        add(topo, BorderLayout.NORTH);

        // Corpo dividido: Esquerda = Avaliações | Direita = Nova Avaliação
        JPanel corpo = new JPanel(new GridLayout(1, 2, 14, 0));
        corpo.setBackground(TemaUI.FUNDO_CANVAS);
        corpo.setBorder(BorderFactory.createEmptyBorder(4, 18, 14, 18));

        // Esquerda
        JPanel boxLista = new JPanel(new BorderLayout(0, 8));
        boxLista.setBackground(TemaUI.SUPERFICIE_BRANCA);
        boxLista.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaUI.BORDA_SUAVE),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        JLabel lblAv = new JLabel("Avaliações Registradas");
        lblAv.setFont(TemaUI.FONTE_SUBTITULO);
        boxLista.add(lblAv, BorderLayout.NORTH);

        painelListaAvaliacoes = new JPanel();
        painelListaAvaliacoes.setBackground(TemaUI.SUPERFICIE_BRANCA);
        painelListaAvaliacoes.setLayout(new BoxLayout(painelListaAvaliacoes, BoxLayout.Y_AXIS));
        JScrollPane scrollAv = new JScrollPane(painelListaAvaliacoes);
        scrollAv.setBorder(BorderFactory.createEmptyBorder());
        boxLista.add(scrollAv, BorderLayout.CENTER);

        // Direita
        JPanel boxForm = new JPanel();
        boxForm.setBackground(TemaUI.SUPERFICIE_BRANCA);
        boxForm.setLayout(new BoxLayout(boxForm, BoxLayout.Y_AXIS));
        boxForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaUI.BORDA_SUAVE),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));

        JLabel lblFormTit = new JLabel("Publicar Avaliação (RF002 / RF003)");
        lblFormTit.setFont(TemaUI.FONTE_SUBTITULO);
        lblFormTit.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblRn = new JLabel("RN001: Jogador comum pode avaliar cada jogo apenas 1 vez.");
        lblRn.setFont(TemaUI.FONTE_LEGENDA);
        lblRn.setForeground(TemaUI.ALERTA_EXCLUIR);
        lblRn.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblNota = new JLabel("Nota (1 a 5 estrelas): *");
        lblNota.setFont(TemaUI.FONTE_BOTAO);
        lblNota.setAlignmentX(LEFT_ALIGNMENT);

        cmbNota = new JComboBox<>(new String[]{
            "5 - Excelente", "4 - Muito Bom", "3 - Bom", "2 - Regular", "1 - Ruim"
        });
        cmbNota.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cmbNota.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblCom = new JLabel("Comentário descritivo: *");
        lblCom.setFont(TemaUI.FONTE_BOTAO);
        lblCom.setAlignmentX(LEFT_ALIGNMENT);

        txtComentario = new JTextArea(5, 25);
        txtComentario.setFont(TemaUI.FONTE_CORPO);
        txtComentario.setLineWrap(true);
        txtComentario.setWrapStyleWord(true);
        JScrollPane scrollCom = new JScrollPane(txtComentario);
        scrollCom.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblMidia = new JLabel("Anexar imagem de gameplay/conquista (opcional):");
        lblMidia.setFont(TemaUI.FONTE_BOTAO);
        lblMidia.setAlignmentX(LEFT_ALIGNMENT);

        JPanel linhaMidia = new JPanel(new BorderLayout(6, 0));
        linhaMidia.setOpaque(false);
        linhaMidia.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        linhaMidia.setAlignmentX(LEFT_ALIGNMENT);
        txtMidia = new JTextField();
        txtMidia.setFont(TemaUI.FONTE_LEGENDA);
        JButton btnAnexar = TemaUI.criarBotao("Anexar...", TemaUI.BARRA_TOPO, KeyEvent.VK_A);
        btnAnexar.addActionListener(e -> escolherMidia());
        linhaMidia.add(txtMidia, BorderLayout.CENTER);
        linhaMidia.add(btnAnexar, BorderLayout.EAST);

        JButton btnPublicar = TemaUI.criarBotao("Publicar Avaliação (Alt+P)", TemaUI.ACAO_PRIMARIA, KeyEvent.VK_P);
        btnPublicar.setAlignmentX(LEFT_ALIGNMENT);
        btnPublicar.addActionListener(e -> publicarAvaliacao());

        boxForm.add(lblFormTit);
        boxForm.add(Box.createVerticalStrut(4));
        boxForm.add(lblRn);
        boxForm.add(Box.createVerticalStrut(12));
        boxForm.add(lblNota);
        boxForm.add(Box.createVerticalStrut(4));
        boxForm.add(cmbNota);
        boxForm.add(Box.createVerticalStrut(10));
        boxForm.add(lblCom);
        boxForm.add(Box.createVerticalStrut(4));
        boxForm.add(scrollCom);
        boxForm.add(Box.createVerticalStrut(10));
        boxForm.add(lblMidia);
        boxForm.add(Box.createVerticalStrut(4));
        boxForm.add(linhaMidia);
        boxForm.add(Box.createVerticalStrut(14));
        boxForm.add(btnPublicar);

        corpo.add(boxLista);
        corpo.add(boxForm);
        add(corpo, BorderLayout.CENTER);
    }

    private JLabel criarCardNota(String titulo, String valor, Color fundo, Color corTexto) {
        JLabel lbl = new JLabel("<html><center><span style='font-size:9px'>" + titulo
                + "</span><br><b style='font-size:16px'>" + valor + "</b></center></html>", JLabel.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(fundo);
        lbl.setForeground(corTexto);
        lbl.setPreferredSize(new Dimension(145, 72));
        lbl.setBorder(BorderFactory.createLineBorder(corTexto, 2));
        return lbl;
    }

    private void atualizarDados() {
        double mc = repositorio.calcularMediaCritica(jogo.getIdJogo());
        double mj = repositorio.calcularMediaJogadores(jogo.getIdJogo());
        lblMediaCritica.setText("<html><center><span style='font-size:9px'>NOTA DA CRÍTICA</span><br><b style='font-size:16px'>"
                + (mc > 0 ? String.format("%.1f ★", mc) : "Sem nota") + "</b></center></html>");
        lblMediaJogadores.setText("<html><center><span style='font-size:9px'>NOTA JOGADORES</span><br><b style='font-size:16px'>"
                + (mj > 0 ? String.format("%.1f ★", mj) : "Sem nota") + "</b></center></html>");

        painelListaAvaliacoes.removeAll();
        List<Avaliacao> lista = repositorio.listarAvaliacoesPorJogo(jogo.getIdJogo());
        if (lista.isEmpty()) {
            JLabel vazio = new JLabel("Nenhuma avaliação registrada ainda. Seja o primeiro!");
            vazio.setFont(TemaUI.FONTE_CORPO);
            vazio.setForeground(TemaUI.TEXTO_SECUNDARIO);
            painelListaAvaliacoes.add(vazio);
        } else {
            for (Avaliacao a : lista) {
                boolean isCrit = a.getUsuario().isCritico();
                JPanel card = new JPanel(new BorderLayout(4, 4));
                card.setBackground(isCrit ? TemaUI.FUNDO_CRITICA : TemaUI.FUNDO_JOGADOR);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(isCrit ? TemaUI.NOTA_CRITICA : TemaUI.NOTA_JOGADORES),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 105));

                JLabel lblTopo = new JLabel(a.getUsuario().getNome() + " ["
                        + a.getUsuario().getTipoPermissao().toUpperCase() + "]   —   " + a.getNota() + " / 5 ★");
                lblTopo.setFont(TemaUI.FONTE_BOTAO);
                lblTopo.setForeground(isCrit ? TemaUI.NOTA_CRITICA : TemaUI.NOTA_JOGADORES);

                String extra = (a.getMidiaUrl() != null && !a.getMidiaUrl().trim().isEmpty())
                        ? "<br><i>Anexo: " + a.getMidiaUrl() + "</i>" : "";
                JLabel lblTxt = new JLabel("<html>" + a.getComentario() + extra + "</html>");
                lblTxt.setFont(TemaUI.FONTE_LEGENDA);
                lblTxt.setForeground(TemaUI.TEXTO_ESCURO);

                card.add(lblTopo, BorderLayout.NORTH);
                card.add(lblTxt, BorderLayout.CENTER);
                painelListaAvaliacoes.add(card);
                painelListaAvaliacoes.add(Box.createVerticalStrut(8));
            }
        }
        painelListaAvaliacoes.revalidate();
        painelListaAvaliacoes.repaint();
    }

    private void escolherMidia() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Imagens (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            txtMidia.setText(f.getName());
        }
    }

    private void publicarAvaliacao() {
        String comentario = txtComentario.getText().trim();
        if (comentario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escreva um comentário para sua avaliação.",
                    "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int nota = 5 - cmbNota.getSelectedIndex();
        String midia = txtMidia.getText().trim();

        Avaliacao nova = new Avaliacao(0, nota, comentario, midia, usuarioLogado, jogo);
        String res = repositorio.registrarAvaliacao(nova);
        if ("OK".equals(res)) {
            txtComentario.setText("");
            txtMidia.setText("");
            atualizarDados();
            JOptionPane.showMessageDialog(this, "Avaliação publicada com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, res, "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void abrirTrailer() {
        try {
            if (Desktop.isDesktopSupported() && jogo.getTrailerUrl() != null) {
                Desktop.getDesktop().browse(new URI(jogo.getTrailerUrl()));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Link do trailer: " + jogo.getTrailerUrl(),
                    "Trailer do Jogo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
