package com.gamescorehub.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;

/**
 * Paleta de cores WCAG 2.1 AA/AAA, tipografia e utilitários de acessibilidade definidos na Etapa 2.
 */
public final class TemaUI {
    public static final Color PRIMARIA_ESCURA = new Color(15, 23, 42);     // #0F172A
    public static final Color BARRA_TOPO = new Color(30, 41, 59);          // #1E293B
    public static final Color ACAO_PRIMARIA = new Color(29, 78, 216);      // #1D4ED8
    public static final Color NOTA_JOGADORES = new Color(15, 118, 110);    // #0F766E
    public static final Color NOTA_CRITICA = new Color(180, 83, 9);        // #B45309
    public static final Color ALERTA_EXCLUIR = new Color(220, 38, 38);     // #DC2626
    public static final Color FUNDO_CANVAS = new Color(248, 250, 252);     // #F8FAFC
    public static final Color SUPERFICIE_BRANCA = Color.WHITE;
    public static final Color TEXTO_ESCURO = new Color(15, 23, 42);
    public static final Color TEXTO_SECUNDARIO = new Color(71, 85, 105);   // #475569
    public static final Color BORDA_SUAVE = new Color(203, 213, 225);      // #CBD5E1
    public static final Color FUNDO_CRITICA = new Color(255, 251, 235);    // #FFFBEB
    public static final Color FUNDO_JOGADOR = new Color(236, 253, 245);    // #ECFDF5

    public static final Font FONTE_TITULO = new Font("SansSerif", Font.BOLD, 20);
    public static final Font FONTE_SUBTITULO = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FONTE_CORPO = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONTE_BOTAO = new Font("SansSerif", Font.BOLD, 12);
    public static final Font FONTE_LEGENDA = new Font("SansSerif", Font.PLAIN, 11);

    private TemaUI() {}

    public static JButton criarBotao(String texto, Color fundo, int mnemonic) {
        JButton btn = new JButton(texto);
        btn.setFont(FONTE_BOTAO);
        btn.setBackground(fundo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(true);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        btn.setMinimumSize(new Dimension(90, 34));
        if (mnemonic > 0) {
            btn.setMnemonic(mnemonic);
        }
        return btn;
    }

    public static JButton criarBotaoSecundario(String texto, int mnemonic) {
        JButton btn = new JButton(texto);
        btn.setFont(FONTE_BOTAO);
        btn.setBackground(new Color(241, 245, 249));
        btn.setForeground(TEXTO_ESCURO);
        btn.setFocusPainted(true);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA_SUAVE, 1),
                BorderFactory.createEmptyBorder(7, 13, 7, 13)
        ));
        if (mnemonic > 0) {
            btn.setMnemonic(mnemonic);
        }
        return btn;
    }

    public static void configurarAcessibilidade(JComponent comp, String nome, String descricao) {
        comp.getAccessibleContext().setAccessibleName(nome);
        comp.getAccessibleContext().setAccessibleDescription(descricao);
        comp.setToolTipText(descricao);
    }
}
