DELETE FROM cliente_telefone;
DELETE FROM cliente_endereco;
DELETE FROM telefone;
DELETE FROM endereco;
DELETE FROM cliente;
DELETE FROM administrador;
DELETE FROM usuario;
DELETE FROM municipio;
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

INSERT INTO municipio (id, nome, estado_id) VALUES 
(1, 'Porto Nacional', 1),
(2, 'Goiânia', 3),
(3, 'São Paulo', 2),
(4, 'Palmas', 1),
(5, 'Porto Alegre', 4);

INSERT INTO endereco (logradouro, numero, complemento, bairro, cep, municipio_id) VALUES 
('Rua C S P', '1071', null, 'Centro', '77500-000', 1),
('Avenida Bandeirantes', '432', null, 'Vila Mariana', '23412-444', 2),
('Rua Anhenguera', '78312', null, 'Vila Nova', '49313-641', 3),
('Quadra 103 Sul', '15', 'Bloco A', 'Plano Diretor Sul', '77015-020', 4),
('Avenida Ipiranga', '6681', null, 'Partenon', '90619-900', 5),
('Rua das Flores', '100', 'Sala 501', 'Centro', '70000-000', 1),
('Avenida Paulista', '1000', null, 'Bela Vista', '01310-000', 3);

INSERT INTO telefone (codArea, numero) VALUES 
('63', '984323854'),
('99', '981453843'),
('11', '992349812'),
('61', '33445566'),
('51', '998877665');

INSERT INTO usuario (nome, email, senha, perfil) VALUES 
('Admin Sistema', 'admin@email.com', 'TRwn0XU29Gwl2sagG00bvjrNJvLuYo+dbOBJ7R3xFpU4m/FAUc5q8OoGbVNwPF7F5713RaYkN4qyufNCDHm/mA==', 1),
('João Cliente', 'joao@email.com', 'TRwn0XU29Gwl2sagG00bvjrNJvLuYo+dbOBJ7R3xFpU4m/FAUc5q8OoGbVNwPF7F5713RaYkN4qyufNCDHm/mA==', 2), --123
('Maria Cliente', 'maria@email.com', '2jqHB2Uf9imuz2oRVlzQCEMTCOoHPgbnPCwXCm100JmUzMNhlZFMjcXoeWp9T91TTCruG2sL5JNYRvt6wtw2Ew==', 2), -- 321
('Carlos Gerente', 'carlos@email.com', 'TRwn0XU29Gwl2sagG00bvjrNJvLuYo+dbOBJ7R3xFpU4m/FAUc5q8OoGbVNwPF7F5713RaYkN4qyufNCDHm/mA==', 1),
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

INSERT INTO cliente_telefone (id_cliente, id_telefone) VALUES
(2, 1),
(2, 3),
(3, 2),
(4, 4),
(4, 5);

INSERT INTO cliente_endereco (id_cliente, id_endereco) VALUES
(2, 1),
(2, 2),
(3, 3),
(4, 4),
(4, 5);

INSERT INTO produto (nome, descricao, preco, estoque, desenvolvedora, tipoMidia, genero, classificacao, dataLancamento, capaUrl) VALUES
('The Last Of Us', 'Descrição teste 1', 150.0, 10, 'Naughty Dog', 1, 8, 6, '2020-03-01', null),
('God of War', 'Descrição teste 2', 250.0, 10, 'teste2', 2, 4, 3, '2022-05-24', null),
('Red Dead Redemption 2', 'Descrição teste 3', 85.0, 3, 'Rockstar Games', 1, 1, 6, '2018-07-21', null);

-- Inserção de Pedidos (sem especificar o ID)
INSERT INTO Pedido (data_hora, id_cliente, id_endereco, valor_total) VALUES
('2023-11-01 10:30:00', 2, 1, 450.00),
('2023-11-02 14:15:00', 3, 2, 320.50),
('2023-11-03 09:45:00', 4, 3, 780.75);

-- Inserção de ItensPedido (sem especificar o ID)
INSERT INTO ItemPedido (valor, quantidade, id_produto, id_pedido) VALUES
(200.00, 2, 1, 1),
(250.00, 1, 2, 1),
(120.50, 1, 3, 2),
(100.00, 2, 1, 2),
(350.75, 3, 1, 3),
(180.00, 1, 2, 3),
(250.00, 1, 3, 3);