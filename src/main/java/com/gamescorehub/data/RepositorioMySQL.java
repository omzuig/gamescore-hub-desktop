package com.gamescorehub.data;

import com.gamescorehub.dao.AvaliacaoDAO;
import com.gamescorehub.dao.JogoDAO;
import com.gamescorehub.dao.SolicitacaoCriticoDAO;
import com.gamescorehub.dao.UsuarioDAO;
import com.gamescorehub.model.Avaliacao;
import com.gamescorehub.model.Jogo;
import com.gamescorehub.model.SolicitacaoCritico;
import com.gamescorehub.model.Usuario;
import java.util.List;

/**
 * Implementação definitiva de persistência integrada ao MySQL (Etapa 4).
 */
public class RepositorioMySQL implements RepositorioDados {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final JogoDAO jogoDAO = new JogoDAO();
    private final AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO();
    private final SolicitacaoCriticoDAO solicitacaoDAO = new SolicitacaoCriticoDAO();

    @Override
    public Usuario autenticar(String email, String senha) {
        return usuarioDAO.autenticar(email, senha);
    }

    @Override
    public String cadastrarUsuario(Usuario usuario) {
        return usuarioDAO.cadastrar(usuario);
    }

    @Override
    public List<Jogo> listarJogos(String filtro) {
        return jogoDAO.listar(filtro);
    }

    @Override
    public boolean salvarJogo(Jogo jogo) {
        return jogoDAO.salvar(jogo);
    }

    @Override
    public boolean excluirJogo(int idJogo) {
        return jogoDAO.excluir(idJogo);
    }

    @Override
    public List<Avaliacao> listarAvaliacoesPorJogo(int idJogo) {
        return avaliacaoDAO.listarPorJogo(idJogo);
    }

    @Override
    public int contarTotalAvaliacoes() {
        return avaliacaoDAO.contarTotal();
    }

    @Override
    public String registrarAvaliacao(Avaliacao avaliacao) {
        return avaliacaoDAO.registrar(avaliacao);
    }

    @Override
    public double calcularMediaCritica(int idJogo) {
        return avaliacaoDAO.calcularMediaPorPerfil(idJogo, "critico");
    }

    @Override
    public double calcularMediaJogadores(int idJogo) {
        return avaliacaoDAO.calcularMediaPorPerfil(idJogo, "jogador");
    }

    @Override
    public List<SolicitacaoCritico> listarSolicitacoes() {
        return solicitacaoDAO.listar();
    }

    @Override
    public boolean enviarSolicitacaoCritico(SolicitacaoCritico solicitacao) {
        return solicitacaoDAO.inserir(solicitacao);
    }

    @Override
    public boolean atualizarStatusSolicitacao(int idSolicitacao, String novoStatus) {
        return solicitacaoDAO.atualizarStatus(idSolicitacao, novoStatus);
    }
}
