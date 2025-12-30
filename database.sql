-- Script de criação do banco de dados para Sistema de Almoxarifado

CREATE DATABASE almoxarifado;
USE almoxarifado;

-- Tabela de produtos
CREATE TABLE produtos (
    codigo INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    quantidade INT NOT NULL DEFAULT 0,
    especificacao TEXT,
    empresa VARCHAR(100) NOT NULL
);

-- Tabela de retiradas
CREATE TABLE retiradas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_pessoa VARCHAR(100) NOT NULL,
    setor VARCHAR(100) NOT NULL,
    codigo_produto INT NOT NULL,
    nome_produto VARCHAR(100) NOT NULL,
    quantidade INT NOT NULL,
    data_retirada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (codigo_produto) REFERENCES produtos(codigo)
);

-- Índices para melhor performance
CREATE INDEX idx_produto_nome ON produtos(nome);
CREATE INDEX idx_retirada_pessoa ON retiradas(nome_pessoa);
CREATE INDEX idx_retirada_setor ON retiradas(setor);
CREATE INDEX idx_retirada_produto ON retiradas(nome_produto);