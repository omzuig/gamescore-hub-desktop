-- =====================================================================
-- GameScore Hub — Script Completo do Banco de Dados (Etapa 4 - UC15)
-- Baseado no banco gamescore_db desenvolvido no Módulo I (etapa3.sql)
-- Responsável: Guilherme Aguiar dos Santos
-- =====================================================================

DROP DATABASE IF EXISTS gamescore_db;
CREATE DATABASE gamescore_db
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE gamescore_db;

-- Tabela de Usuários (perfis: jogador, critico, moderador, administrador)
CREATE TABLE usuarios (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    tipo_permissao VARCHAR(20) NOT NULL DEFAULT 'jogador'
);

-- Tabela de Jogos (ampliada com trailer_url para atender RF001)
CREATE TABLE jogos (
    id_jogo INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    sinopse TEXT,
    capa_url VARCHAR(255),
    trailer_url VARCHAR(255)
);

-- Tabela de Avaliações (ampliada com midia_url para atender RF003)
CREATE TABLE avaliacoes (
    id_avaliacao INT PRIMARY KEY AUTO_INCREMENT,
    nota INT NOT NULL,
    comentario TEXT,
    midia_url VARCHAR(255),
    usuarios_id_usuarios INT NOT NULL,
    jogos_id_jogo INT NOT NULL,
    CONSTRAINT chk_nota CHECK (nota BETWEEN 1 AND 5),
    CONSTRAINT fk_usu_aval FOREIGN KEY (usuarios_id_usuarios)
        REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    CONSTRAINT fk_jog_aval FOREIGN KEY (jogos_id_jogo)
        REFERENCES jogos(id_jogo) ON DELETE CASCADE
);

-- Tabela de Solicitações de Selo de Crítico Especializado (RF004)
CREATE TABLE solicitacoes_critico (
    id_solicitacao INT PRIMARY KEY AUTO_INCREMENT,
    usuarios_id_usuario INT NOT NULL,
    veiculo_imprensa VARCHAR(120) NOT NULL,
    portfolio_url VARCHAR(255) NOT NULL,
    justificativa TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    CONSTRAINT fk_usu_sol FOREIGN KEY (usuarios_id_usuario)
        REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

-- =====================================================================
-- DADOS INICIAIS PARA TESTE (preservando registros do etapa3.sql)
-- =====================================================================

INSERT INTO usuarios (nome, email, senha, tipo_permissao) VALUES
('Alice Silva', 'alice@email.com', 'senha123', 'jogador'),
('Bruno Games', 'bruno@critica.com', 'critico456', 'critico'),
('Carlos Adm', 'carlos@sistema.com', 'admin789', 'moderador'),
('Diego Player', 'diego@email.com', 'd12345', 'jogador'),
('Elena Review', 'elena@pauta.com', 'elena@2024', 'critico'),
('Guilherme Admin', 'admin@gamescorehub.com', 'admin123', 'administrador');

INSERT INTO jogos (titulo, sinopse, capa_url, trailer_url) VALUES
('Zelda: Tears of the Kingdom', 'Aventura épica em Hyrule', '', 'https://www.youtube.com/watch?v=uHGShqcAHlQ'),
('Elden Ring', 'Desafio em um mundo aberto sombrio', '', 'https://www.youtube.com/watch?v=E3Huy2cdih0'),
('God of War', 'Kratos em terras nórdicas', '', 'https://www.youtube.com/watch?v=K0u_kAWLJOA'),
('Mario Odyssey', 'Aventura 3D do encanador', '', 'https://www.youtube.com/watch?v=wGQHQc_3ycE'),
('Cyberpunk 2077', 'RPG futurista em Night City', '', 'https://www.youtube.com/watch?v=8X2kIfS6fb8');

INSERT INTO avaliacoes (nota, comentario, midia_url, usuarios_id_usuarios, jogos_id_jogo) VALUES
(5, 'Obra prima!', '', 1, 1),
(4, 'Muito difícil, mas incrível', '', 2, 2),
(5, 'Gráficos lindos', '', 4, 3),
(3, 'Bom, mas esperava mais', '', 5, 5),
(5, 'Divertido demais', '', 1, 4);

INSERT INTO solicitacoes_critico (usuarios_id_usuario, veiculo_imprensa, portfolio_url, justificativa, status) VALUES
(4, 'Canal IndieSul', 'https://youtube.com/@indiesul', 'Produzo análises técnicas de jogos indie e AAA há 3 anos.', 'PENDENTE');
