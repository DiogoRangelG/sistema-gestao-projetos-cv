package com.gestao.projetos.cv.dao;

import com.gestao.projetos.cv.modelo.Recurso;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecursoDAO {
    private Connection conexao;

    public RecursoDAO() {
        this.conexao = ConexaoDB.getConexao();
    }

    public boolean salvar(Recurso recurso) {
        String sql = "INSERT INTO recursos (id, nome, descricao, categoria, custo_unitario, quantidade_disponivel, quantidade_minima, unidade_medida) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, recurso.getId());
            stmt.setString(2, recurso.getNome());
            stmt.setString(3, recurso.getDescricao());
            stmt.setString(4, recurso.getCategoria());
            stmt.setDouble(5, recurso.getCustoUnitario());
            stmt.setInt(6, recurso.getQuantidadeDisponivel());
            stmt.setInt(7, recurso.getQuantidadeMinima());
            stmt.setString(8, recurso.getUnidadeMedida());
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar recurso: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizar(Recurso recurso) {
        String sql = "UPDATE recursos SET nome = ?, descricao = ?, categoria = ?, custo_unitario = ?, " +
                     "quantidade_disponivel = ?, quantidade_minima = ?, unidade_medida = ? " +
                     "WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, recurso.getNome());
            stmt.setString(2, recurso.getDescricao());
            stmt.setString(3, recurso.getCategoria());
            stmt.setDouble(4, recurso.getCustoUnitario());
            stmt.setInt(5, recurso.getQuantidadeDisponivel());
            stmt.setInt(6, recurso.getQuantidadeMinima());
            stmt.setString(7, recurso.getUnidadeMedida());
            stmt.setString(8, recurso.getId());
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar recurso: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(String id) {
        String sql = "DELETE FROM recursos WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, id);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir recurso: " + e.getMessage());
            return false;
        }
    }

    public Recurso buscarPorId(String id) {
        String sql = "SELECT * FROM recursos WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, id);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return criarRecursoDoResultSet(rs);
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar recurso por ID: " + e.getMessage());
            return null;
        }
    }

    public List<Recurso> listarTodos() {
        List<Recurso> recursos = new ArrayList<>();
        String sql = "SELECT * FROM recursos ORDER BY nome";
        
        try (Statement stmt = conexao.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                recursos.add(criarRecursoDoResultSet(rs));
            }
            
            return recursos;
        } catch (SQLException e) {
            System.out.println("Erro ao listar recursos: " + e.getMessage());
            return recursos;
        }
    }

    public List<Recurso> buscarPorCategoria(String categoria) {
        List<Recurso> recursos = new ArrayList<>();
        String sql = "SELECT * FROM recursos WHERE categoria = ? ORDER BY nome";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, categoria);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                recursos.add(criarRecursoDoResultSet(rs));
            }
            
            return recursos;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar recursos por categoria: " + e.getMessage());
            return recursos;
        }
    }

    public List<Recurso> listarEstoqueBaixo() {
        List<Recurso> recursos = new ArrayList<>();
        String sql = "SELECT * FROM recursos WHERE quantidade_disponivel <= quantidade_minima ORDER BY nome";
        
        try (Statement stmt = conexao.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                recursos.add(criarRecursoDoResultSet(rs));
            }
            
            return recursos;
        } catch (SQLException e) {
            System.out.println("Erro ao listar recursos com estoque baixo: " + e.getMessage());
            return recursos;
        }
    }

    public boolean atualizarEstoque(String id, int novaQuantidade) {
        String sql = "UPDATE recursos SET quantidade_disponivel = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, novaQuantidade);
            stmt.setString(2, id);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar estoque: " + e.getMessage());
            return false;
        }
    }

    private Recurso criarRecursoDoResultSet(ResultSet rs) throws SQLException {
        Recurso recurso = new Recurso();
        recurso.setId(rs.getString("id"));
        recurso.setNome(rs.getString("nome"));
        recurso.setDescricao(rs.getString("descricao"));
        recurso.setCategoria(rs.getString("categoria"));
        recurso.setCustoUnitario(rs.getDouble("custo_unitario"));
        recurso.setQuantidadeDisponivel(rs.getInt("quantidade_disponivel"));
        recurso.setQuantidadeMinima(rs.getInt("quantidade_minima"));
        recurso.setUnidadeMedida(rs.getString("unidade_medida"));
        return recurso;
    }
}
