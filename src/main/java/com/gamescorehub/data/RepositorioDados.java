package com.gamescorehub.data;

import com.gamescorehub.model.Avaliacao;
import com.gamescorehub.model.Jogo;
import com.gamescorehub.model.SolicitacaoCritico;
import com.gamescorehub.model.Usuario;
import java.util.List;

/**
 * Contrato de persistência utilizado pelas telas do GameScore Hub.
 */
public interface RepositorioDados {
    Usuario autenticar(String email, String senha);
    String cadastrarUsuario(Usuario usuario);
    List<Jogo> listarJogos(String filtro);
    boolean salvarJogo(Jogo jogo);
    boolean excluirJogo(int idJogo);
    List<Avaliacao> listarAvaliacoesPorJogo(int idJogo);
    int contarTotalAvaliacoes();
    String registrarAvaliacao(Avaliacao avaliacao);
    double calcularMediaCritica(int idJogo);
    double calcularMediaJogadores(int idJogo);
    List<SolicitacaoCritico> listarSolicitacoes();
    boolean enviarSolicitacaoCritico(SolicitacaoCritico solicitacao);
    boolean atualizarStatusSolicitacao(int idSolicitacao, String novoStatus);
}
