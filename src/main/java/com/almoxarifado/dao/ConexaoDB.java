package com.almoxarifado.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {
    private static final String URL = "jdbc:mysql://localhost:3306/almoxarifado";
    private static final String USUARIO = "root";
    private static final String SENHA = "181307"; // Senha modificada
    
    private static Connection conexao = null;
    
    public static Connection getConexao() {
        if (conexao == null) {
            try {
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
            } catch (SQLException e) {
                System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return conexao;
    }
    
    public static void fecharConexao() {
        if (conexao != null) {
            try {
                conexao.close();
                conexao = null;
                System.out.println("Conexão com o banco de dados fechada com sucesso!");
            } catch (SQLException e) {
                System.out.println("Erro ao fechar conexão com o banco de dados: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    public static void criarBancoDeDados() {
        try {
            // Conectar ao MySQL sem especificar um banco de dados
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/", 
                    USUARIO, 
                    SENHA);
            
            // Criar o banco de dados se não existir
            java.sql.Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS almoxarifado");
            
            // Fechar a conexão temporária
            conn.close();
            
            // Agora conectar ao banco de dados almoxarifado e criar as tabelas
            conn = getConexao();
            stmt = conn.createStatement();
            
            // Criar tabela de Usuarios
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS usuarios (" +
                    "id VARCHAR(36) PRIMARY KEY, " +
                    "nome VARCHAR(100) NOT NULL, " +
                    "login VARCHAR(50) UNIQUE NOT NULL, " +
                    "senha VARCHAR(100) NOT NULL, " +
                    "nivel VARCHAR(20) NOT NULL" +
                    ")");
            
            // Criar tabela de Materiais
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS materiais (" +
                    "id VARCHAR(36) PRIMARY KEY, " +
                    "nome VARCHAR(100) NOT NULL, " +
                    "descricao TEXT, " +
                    "tipo VARCHAR(50) NOT NULL, " +
                    "preco DECIMAL(10,2) NOT NULL, " +
                    "quantidade_atual INT NOT NULL, " +
                    "quantidade_minima INT NOT NULL, " +
                    "unidade_medida VARCHAR(20) NOT NULL" +
                    ")");
            
            // Criar tabela de Projetos
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS projetos (" +
                    "id VARCHAR(36) PRIMARY KEY, " +
                    "nome VARCHAR(100) NOT NULL, " +
                    "descricao TEXT, " +
                    "data_inicio DATE NOT NULL, " +
                    "data_fim DATE, " +
                    "responsavel VARCHAR(100) NOT NULL, " +
                    "status VARCHAR(20) NOT NULL" +
                    ")");
            
            // Criar tabela de Movimentações
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS movimentacoes (" +
                    "id VARCHAR(36) PRIMARY KEY, " +
                    "material_id VARCHAR(36) NOT NULL, " +
                    "projeto_id VARCHAR(36), " +
                    "tipo VARCHAR(10) NOT NULL, " +
                    "quantidade INT NOT NULL, " +
                    "data DATETIME NOT NULL, " +
                    "responsavel VARCHAR(100) NOT NULL, " +
                    "observacao TEXT, " +
                    "FOREIGN KEY (material_id) REFERENCES materiais(id), " +
                    "FOREIGN KEY (projeto_id) REFERENCES projetos(id)" +
                    ")");
            
            // Inserir um usuário administrador padrão se não existir nenhum
            String checkAdmin = "SELECT COUNT(*) FROM usuarios WHERE nivel = 'ADMIN'";
            java.sql.ResultSet rs = stmt.executeQuery(checkAdmin);
            rs.next();
            if (rs.getInt(1) == 0) {
                String insertAdmin = "INSERT INTO usuarios (id, nome, login, senha, nivel) VALUES " +
                        "('admin', 'Administrador', 'admin', 'admin123', 'ADMIN')";
                stmt.executeUpdate(insertAdmin);
                System.out.println("Usuário admin criado com sucesso! Login: admin, Senha: admin123");
            }
            
            System.out.println("Banco de dados e tabelas criados com sucesso!");
            
        } catch (SQLException e) {
            System.out.println("Erro ao criar banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
