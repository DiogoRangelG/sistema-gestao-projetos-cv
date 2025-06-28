package com.gestao.projetos.cv.dao;

import com.gestao.projetos.cv.modelo.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private Connection conexao;

    public UsuarioDAO() {
        this.conexao = ConexaoDB.getConexao();
    }
    
    public boolean salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (id, nome, login, senha, nivel) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        // Verifica se a conexão é nula e tenta obter uma nova
        if (this.conexao == null) {
            this.conexao = ConexaoDB.getConexao();
        }
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getId());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getLogin());
            stmt.setString(4, usuario.getSenha());
            stmt.setString(5, usuario.getNivel());
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar usuário: " + e.getMessage());
            return false;
        }
    }
    
    public boolean atualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome = ?, login = ?, senha = ?, nivel = ? " +
                     "WHERE id = ?";
        
        // Verifica se a conexão é nula e tenta obter uma nova
        if (this.conexao == null) {
            this.conexao = ConexaoDB.getConexao();
        }
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getLogin());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getNivel());
            stmt.setString(5, usuario.getId());
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar usuário: " + e.getMessage());
            return false;
        }
    }
    
    public boolean excluir(String id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        
        // Verifica se a conexão é nula e tenta obter uma nova
        if (this.conexao == null) {
            this.conexao = ConexaoDB.getConexao();
        }
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, id);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir usuário: " + e.getMessage());
            return false;
        }
    }
    
    public Usuario buscarPorId(String id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        
        // Verifica se a conexão é nula e tenta obter uma nova
        if (this.conexao == null) {
            this.conexao = ConexaoDB.getConexao();
        }
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, id);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return criarUsuarioDoResultSet(rs);
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário por ID: " + e.getMessage());
            return null;
        }
    }
    
    public Usuario buscarPorLogin(String login) {
        String sql = "SELECT * FROM usuarios WHERE login = ?";
        
        // Verifica se a conexão é nula e tenta obter uma nova
        if (this.conexao == null) {
            this.conexao = ConexaoDB.getConexao();
        }
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, login);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return criarUsuarioDoResultSet(rs);
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário por login: " + e.getMessage());
            return null;
        }
    }
    
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY nome";
        
        // Verifica se a conexão é nula e tenta obter uma nova
        if (this.conexao == null) {
            this.conexao = ConexaoDB.getConexao();
        }
        
        try (Statement stmt = conexao.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                usuarios.add(criarUsuarioDoResultSet(rs));
            }
            
            return usuarios;
        } catch (SQLException e) {
            System.out.println("Erro ao listar usuários: " + e.getMessage());
            return usuarios;
        }
    }
    
    public List<Usuario> buscarPorNivel(String nivel) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE nivel = ? ORDER BY nome";
        
        // Verifica se a conexão é nula e tenta obter uma nova
        if (this.conexao == null) {
            this.conexao = ConexaoDB.getConexao();
        }
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, nivel);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                usuarios.add(criarUsuarioDoResultSet(rs));
            }
            
            return usuarios;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuários por nível: " + e.getMessage());
            return usuarios;
        }
    }
    
    public Usuario autenticar(String login, String senha) {
        String sql = "SELECT * FROM usuarios WHERE login = ? AND senha = ?";
        
        // Verifica se a conexão é nula e tenta obter uma nova
        if (this.conexao == null) {
            this.conexao = ConexaoDB.getConexao();
        }
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, senha);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return criarUsuarioDoResultSet(rs);
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Erro ao autenticar usuário: " + e.getMessage());
            return null;
        }
    }

    private Usuario criarUsuarioDoResultSet(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getString("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setLogin(rs.getString("login"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setNivel(rs.getString("nivel"));
        return usuario;
    }
}
