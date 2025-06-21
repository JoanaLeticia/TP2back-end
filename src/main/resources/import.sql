DELETE FROM cliente_telefone;
DELETE FROM cliente_endereco;
DELETE FROM telefone;
DELETE FROM endereco;
DELETE FROM cliente;
DELETE FROM administrador;
DELETE FROM usuario;
DELETE FROM municipio;
DELETE FROM produto;
DELETE FROM estado;

ALTER SEQUENCE IF EXISTS usuario_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS estado_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS municipio_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS endereco_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS telefone_id_seq RESTART WITH 1;

INSERT INTO estado (nome, sigla, regiao) VALUES 
('Tocantins', 'TO', 2),
('São Paulo', 'SP', 4),
('Goiás', 'GO', 1),
('Rio Grande do Sul', 'RS', 5),
('Rio de Janeiro', 'RJ', 4);

INSERT INTO municipio (nome, estado_id) VALUES 
('Porto Nacional', 1),
('Goiânia', 3),
('São Paulo', 2),
('Palmas', 1),
('Porto Alegre', 4);

INSERT INTO usuario (nome, email, senha, perfil) VALUES 
('Admin Sistema', 'admin@email.com', 'SYu34Plo5KZGE9fMtUK9LRPnWC3WvVpogVg35bf5tPYMM6dxXNV6AWmPEQzOLc110uIwcv8TOigbaCB43f8KHQ==', 1), -- 123
('João Cliente', 'joao@email.com', 'SYu34Plo5KZGE9fMtUK9LRPnWC3WvVpogVg35bf5tPYMM6dxXNV6AWmPEQzOLc110uIwcv8TOigbaCB43f8KHQ==', 2), --admin123
('Maria Cliente', 'maria@email.com', 'SYu34Plo5KZGE9fMtUK9LRPnWC3WvVpogVg35bf5tPYMM6dxXNV6AWmPEQzOLc110uIwcv8TOigbaCB43f8KHQ==', 2), -- 321
('Carlos Cliente', 'carlos@email.com', 'TRwn0XU29Gwl2sagG00bvjrNJvLuYo+dbOBJ7R3xFpU4m/FAUc5q8OoGbVNwPF7F5713RaYkN4qyufNCDHm/mA==', 2),
('Ana Admin', 'ana.admin@email.com', 'TRwn0XU29Gwl2sagG00bvjrNJvLuYo+dbOBJ7R3xFpU4m/FAUc5q8OoGbVNwPF7F5713RaYkN4qyufNCDHm/mA==', 1),
('Pedro Admin', 'pedro.admin@email.com', 'TRwn0XU29Gwl2sagG00bvjrNJvLuYo+dbOBJ7R3xFpU4m/FAUc5q8OoGbVNwPF7F5713RaYkN4qyufNCDHm/mA==', 1);

INSERT INTO cliente (id, cpf, data_nascimento) VALUES 
(2, '123.456.789-00', '1990-05-15'),
(3, '987.654.321-00', '1985-10-20'),
(4, '456.789.123-99', '1978-03-25');

INSERT INTO administrador (id, cpf, data_nascimento) VALUES 
(1, '111.222.333-44', '1980-01-10'),
(5, '555.666.777-88', '1975-07-20'),
(6, '999.888.777-66', '1982-11-30');

INSERT INTO endereco (logradouro, numero, complemento, bairro, cep, municipio_id, id_cliente) VALUES 
('Rua Costa Silva Pereira', '1231', null, 'Setor Aeroporto', '77500-000', 1, 2),
('Avenida Bandeirantes', '432', null, 'Vila Mariana', '23412-444', 2, 2),
('Rua Anhenguera', '78312', null, 'Vila Nova', '49313-641', 3, 3),
('Quadra 103 Sul', '15', 'Bloco A', 'Plano Diretor Sul', '77015-020', 4, 3),
('Avenida Ipiranga', '6681', null, 'Partenon', '90619-900', 5, 4),
('Rua das Flores', '100', 'Sala 501', 'Centro', '70000-000', 1, 4),
('Avenida Paulista', '1000', null, 'Bela Vista', '01310-000', 3, 2);

INSERT INTO telefone (codArea, numero, id_cliente) VALUES 
('63', '984323854', 2),
('99', '981453843', 3),
('11', '992349812', 2),
('61', '33445566', 3),
('51', '998877665', 4);

INSERT INTO produto (nome, descricao, preco, estoque, desenvolvedora, plataforma, tipoMidia, genero, classificacao, dataLancamento, nomeImagem) VALUES
('The Last Of Us', 'Jogo de ação e aventura pós-apocalíptico', 150.0, 10, 'Naughty Dogs', 1, 1, 8, 6, '2020-03-01', 'the-last-of-us.jpg'),
('God of War', 'Aventura épica com Kratos e seu filho Atreus', 250.0, 15, 'Santa Monica Studio', 2, 2, 1, 4, '2022-05-24', 'god-of-war.jpg'),
('Red Dead Redemption 2', 'Ação no velho oeste com história profunda', 199.0, 8, 'Rockstar Games', 3, 1, 1, 6, '2018-10-26', 'red-dead-redemption-2.jpg'),
('The Last Of Us 2', 'Sequência do aclamado jogo de zumbis', 200.0, 12, 'Naughty Dogs', 2, 1, 1, 6, '2020-07-21', 'the-last-of-us-2.jpg'),
('Horizon Forbidden West', 'Aventura em mundo aberto pós-apocalíptico', 249.0, 20, 'Guerrilla Games', 2, 2, 2, 3, '2022-02-18', 'horizon-forbidden-west.jpg'),
('FIFA 23', 'Simulador de futebol mais recente', 299.0, 25, 'EA Sports', 1, 1, 5, 1, '2022-09-30', 'fifa-23.jpg'),
('Call of Duty: Modern Warfare II', 'FPS militar de última geração', 249.0, 18, 'Infinity Ward', 4, 2, 4, 6, '2022-10-28', 'call-of-duty-modern-warfare-ii.jpg'),
('The Legend of Zelda: Breath of the Wild', 'Aventura em mundo aberto da Nintendo', 299.0, 30, 'Nintendo', 5, 1, 2, 2, '2017-03-03', 'the-legend-of-zelda-breath-of-the-wild.jpg'),
('Mario Kart 8 Deluxe', 'Corrida divertida com personagens da Nintendo', 249.0, 22, 'Nintendo', 5, 1, 6, 1, '2017-04-28', 'mario-kart-8-deluxe.jpg'),
('Resident Evil Village', 'Terror em primeira pessoa', 199.0, 10, 'Capcom', 3, 2, 8, 6, '2021-05-07', 'resident-evil-village.jpg'),
('Street Fighter VI', 'Clássico jogo de luta', 249.0, 15, 'Capcom', 4, 1, 7, 3, '2023-06-02', 'street-fighter-vi.jpg'),
('Forza Horizon 5', 'Corrida em mundo aberto no México', 249.0, 12, 'Playground Games', 4, 2, 6, 1, '2021-11-09', 'forza-horizon-5.jpg'),
('Animal Crossing: New Horizons', 'Simulação de vida relaxante', 299.0, 28, 'Nintendo', 5, 1, 5, 1, '2020-03-20', 'animal-crossing-new-horizons.jpg'),
('Demon Souls', 'Remake do clássico jogo de RPG difícil', 249.0, 8, 'Bluepoint Games', 2, 1, 3, 6, '2020-11-12', 'demon-souls.jpg'),
('Halo Infinite', 'FPS icônico da Microsoft', 199.0, 15, '343 Industries', 4, 2, 4, 4, '2021-12-08', 'halo-infinite.jpg'),
('Spider-Man: Miles Morales', 'Aventura do Homem-Aranha em Nova York', 199.0, 14, 'Insomniac Games', 2, 1, 1, 3, '2020-11-12', 'spider-man-miles-morales.jpg'),
('The Witcher 3: Wild Hunt', 'RPG de mundo aberto aclamado pela crítica', 149.0, 10, 'CD Projekt Red', 1, 2, 3, 6, '2015-05-19', 'the-witcher-3-wild-hunt.jpg'),
('Cyberpunk 2077', 'RPG futurista em Night City', 149.0, 5, 'CD Projekt Red', 3, 1, 3, 6, '2020-12-10', 'cyberpunk-2077.jpg'),
('Assassin''s Creed Valhalla', 'Aventura viking em mundo aberto', 199.0, 12, 'Ubisoft', 4, 2, 1, 5, '2020-11-10', 'assassins-creed-valhalla.jpg'),
('Gran Turismo 7', 'Simulador de corridas realista', 249.0, 10, 'Polyphony Digital', 2, 1, 6, 1, '2022-03-04', 'gran-turismo-7.jpg'),
('Super Smash Bros. Ultimate', 'Luta com todos os personagens da Nintendo', 299.0, 25, 'Nintendo', 5, 1, 7, 2, '2018-12-07', 'super-smash-bros-ultimate.jpg'),
('Dead Space Remake', 'Terror espacial remake do clássico', 249.0, 8, 'EA Motive', 2, 2, 8, 6, '2023-01-27', 'dead-space-remake.jpg'),
('Star Wars Jedi: Survivor', 'Aventura de ação com sabre de luz', 299.0, 15, 'Respawn Entertainment', 4, 1, 1, 3, '2023-04-28', 'star-wars-jedi-survivor.jpg'),
('Final Fantasy XVI', 'RPG de fantasia da Square Enix', 299.0, 12, 'Square Enix', 2, 1, 3, 4, '2023-06-22', 'final-fantasy-xvi.jpg'),
('Diablo IV', 'RPG de ação sombrio', 299.0, 10, 'Blizzard', 3, 2, 3, 6, '2023-06-06', 'diablo-iv.jpg'),
('Dead Island 2', 'Zumbis em Los Angeles', 249.0, 8, 'Dambuster Studios', 1, 1, 1, 6, '2023-04-21', 'dead-island-2.jpg'),
('Hogwarts Legacy', 'Aventura no mundo de Harry Potter', 299.0, 20, 'Avalanche Software', 4, 2, 2, 3, '2023-02-10', 'hogwarts-legacy.jpg'),
('Metroid Prime Remastered', 'Aventura de tiro em primeira pessoa', 199.0, 10, 'Retro Studios', 5, 1, 4, 3, '2023-02-08', 'metroid-prime-remastered.jpg'),
('Sonic Frontiers', 'Aventura do ouriço em mundo aberto', 199.0, 12, 'Sonic Team', 5, 2, 2, 2, '2022-11-08', 'sonic-frontiers.jpg'),
('Gotham Knights', 'Ação com os heróis de Gotham', 249.0, 8, 'WB Games Montréal', 3, 1, 1, 4, '2022-10-21', 'gotham-knights.jpg'),
('Returnal', 'Tiro em terceira pessoa com elementos roguelike', 249.0, 6, 'Housemarque', 2, 2, 4, 5, '2021-04-30', 'returnal.jpg'),
('Ratchet & Clank: Rift Apart', 'Aventura de plataforma e tiro', 249.0, 10, 'Insomniac Games', 2, 1, 1, 2, '2021-06-11', 'ratchet-clank-rift-apart.jpg'),
('Death Stranding', 'Aventura única de Hideo Kojima', 149.0, 5, 'Kojima Productions', 1, 2, 2, 5, '2019-11-08', 'death-stranding.jpg'),
('Ghost of Tsushima', 'Ação em mundo aberto no Japão feudal', 199.0, 15, 'Sucker Punch', 2, 1, 1, 5, '2020-07-17', 'ghost-of-tsushima.jpg'),
('Persona 5 Royal', 'RPG japonês com elementos de simulação', 299.0, 8, 'Atlus', 1, 2, 3, 4, '2020-03-31', 'persona-5-royal.jpg'),
('Monster Hunter Rise', 'Caça a monstros em mundo aberto', 249.0, 12, 'Capcom', 5, 1, 1, 3, '2021-03-26', 'monster-hunter-rise.jpg'),
('Far Cry 6', 'Ação em mundo aberto em ilha tropical', 199.0, 10, 'Ubisoft', 4, 2, 1, 6, '2021-10-07', 'far-cry-6.jpg'),
('Battlefield 2042', 'FPS militar em cenários futuristas', 149.0, 5, 'EA DICE', 3, 1, 4, 6, '2021-11-19', 'battlefield-2042.jpg'),
('Kena: Bridge of Spirits', 'Aventura encantadora com espíritos', 199.0, 8, 'Ember Lab', 2, 2, 2, 2, '2021-09-21', 'kena-bridge-of-spirits.jpg'),
('It Takes Two', 'Aventura cooperativa premiada', 199.0, 15, 'Hazelight Studios', 1, 1, 2, 2, '2021-03-26', 'it-takes-two.jpg'),
('Psychonauts 2', 'Aventura psicodélica de plataforma', 149.0, 6, 'Double Fine', 4, 2, 2, 3, '2021-08-25', 'psychonauts-2.jpg'),
('The Medium', 'Terror psicológico com visão dividida', 149.0, 4, 'Bloober Team', 3, 1, 8, 6, '2021-01-28', 'the-medium.jpg'),
('Little Nightmares II', 'Aventura de terror em puzzle-platform', 99.0, 7, 'Tarsier Studios', 5, 2, 8, 4, '2021-02-11', 'little-nightmares-ii.jpg'),
('Outriders', 'RPG de tiro com elementos cooperativos', 149.0, 5, 'People Can Fly', 1, 1, 4, 6, '2021-04-01', 'outriders.jpg'),
('Biomutant', 'Ação-RPG em mundo aberto pós-apocalíptico', 199.0, 6, 'Experiment 101', 4, 2, 1, 3, '2021-05-25', 'biomutant.jpg'),
('Chivalry 2', 'Batalha medieval em primeira pessoa', 149.0, 8, 'Torn Banner Studios', 3, 1, 7, 6, '2021-06-08', 'chivalry-2.jpg'),
('Scarlet Nexus', 'RPG de ação com poderes psíquicos', 199.0, 5, 'Bandai Namco', 2, 2, 1, 4, '2021-06-25', 'scarlet-nexus.jpg'),
('The Ascent', 'RPG de tiro cyberpunk', 149.0, 4, 'Neon Giant', 4, 1, 4, 5, '2021-07-29', 'the-ascent.jpg'),
('Back 4 Blood', 'FPS cooperativo contra zumbis', 199.0, 10, 'Turtle Rock Studios', 1, 2, 4, 6, '2021-10-12', 'back-4-blood.jpg'),
('Age of Empires IV', 'Estratégia em tempo real histórico', 199.0, 8, 'Relic Entertainment', 4, 1, 5, 1, '2021-10-28', 'age-of-empires-iv.jpg'),
('Forza Motorsport', 'Simulador de corridas realista', 249.0, 12, 'Turn 10 Studios', 4, 2, 6, 1, '2023-10-10', 'forza-motorsport.jpg'),
('Alan Wake II', 'Terror psicológico de ação', 249.0, 7, 'Remedy Entertainment', 2, 1, 8, 6, '2023-10-27', 'alan-wake-ii.jpg'),
('Super Mario Bros. Wonder', 'Plataforma clássico da Nintendo', 299.0, 25, 'Nintendo', 5, 2, 2, 1, '2023-10-20', 'super-mario-bros-wonder.jpg');

-- Inserção de Pagamentos
INSERT INTO Pagamento (metodo, numero_cartao, data_pagamento, codigo_transacao, status) VALUES
('CARTAO_CREDITO', '4111111111111111', '2023-11-01 10:30:00', 'TRANS_20231101103000', 'APROVADO'),
('PIX', NULL, '2023-11-03 09:45:00', 'TRANS_20231103094500', 'APROVADO');

-- Inserção de Pedidos (sem especificar o ID)
INSERT INTO Pedido (data_hora, id_cliente, id_endereco, id_pagamento, valor_total, status) VALUES
('2023-11-01 10:30:00', 2, 1, 1, 450.00, 'AGUARDANDO'),
('2023-11-03 09:45:00', 4, 3, 2, 780.75, 'ENTREGUE');

-- Inserção de ItensPedido (sem especificar o ID)
INSERT INTO ItemPedido (valor, quantidade, id_produto, id_pedido) VALUES
(200.00, 2, 1, 1),
(250.00, 1, 2, 1),
(350.75, 3, 1, 2),
(180.00, 1, 2, 2),
(250.00, 1, 3, 2);