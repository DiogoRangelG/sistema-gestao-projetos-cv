package com.almoxarifado.dao;

import com.almoxarifado.modelo.Material;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {
    private Connection conexao;

    public MaterialDAO() {
        this.conexao = ConexaoDB.getConexao();
    }

    public boolean salvar(Material material) {
        String sql = "INSERT INTO materiais (id, nome, descricao, tipo, preco, quantidade_atual, quantidade_minima, unidade_medida) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, material.getId());
            stmt.setString(2, material.getNome());
            stmt.setString(3, material.getDescricao());
            stmt.setString(4, material.getTipo());
            stmt.setDouble(5, material.getPreco());
            stmt.setInt(6, material.getQuantidadeAtual());
            stmt.setInt(7, material.getQuantidadeMinima());
            stmt.setString(8, material.getUnidadeMedida());
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar material: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizar(Material material) {
        String sql = "UPDATE materiais SET nome = ?, descricao = ?, tipo = ?, preco = ?, " +
                     "quantidade_atual = ?, quantidade_minima = ?, unidade_medida = ? " +
                     "WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, material.getNome());
            stmt.setString(2, material.getDescricao());
            stmt.setString(3, material.getTipo());
            stmt.setDouble(4, material.getPreco());
            stmt.setInt(5, material.getQuantidadeAtual());
            stmt.setInt(6, material.getQuantidadeMinima());
            stmt.setString(7, material.getUnidadeMedida());
            stmt.setString(8, material.getId());
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar material: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(String id) {
        String sql = "DELETE FROM materiais WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, id);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir material: " + e.getMessage());
            return false;
        }
    }

    public Material buscarPorId(String id) {
        String sql = "SELECT * FROM materiais WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, id);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return criarMaterialDoResultSet(rs);
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar material por ID: " + e.getMessage());
            return null;
        }
    }

    public List<Material> listarTodos() {
        List<Material> materiais = new ArrayList<>();
        String sql = "SELECT * FROM materiais ORDER BY nome";
        
        try (Statement stmt = conexao.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                materiais.add(criarMaterialDoResultSet(rs));
            }
            
            return materiais;
        } catch (SQLException e) {
            System.out.println("Erro ao listar materiais: " + e.getMessage());
            return materiais;
        }
    }

    public List<Material> buscarPorTipo(String tipo) {
        List<Material> materiais = new ArrayList<>();
        String sql = "SELECT * FROM materiais WHERE tipo = ? ORDER BY nome";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, tipo);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                materiais.add(criarMaterialDoResultSet(rs));
            }
            
            return materiais;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar materiais por tipo: " + e.getMessage());
            return materiais;
        }
    }

    public List<Material> buscarPorNome(String nome) {
        List<Material> materiais = new ArrayList<>();
        String sql = "SELECT * FROM materiais WHERE nome LIKE ? ORDER BY nome";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                materiais.add(criarMaterialDoResultSet(rs));
            }
            
            return materiais;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar materiais por nome: " + e.getMessage());
            return materiais;
        }
    }

    public List<Material> listarEstoqueBaixo() {
        List<Material> materiais = new ArrayList<>();
        String sql = "SELECT * FROM materiais WHERE quantidade_atual <= quantidade_minima ORDER BY nome";
        
        try (Statement stmt = conexao.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                materiais.add(criarMaterialDoResultSet(rs));
            }
            
            return materiais;
        } catch (SQLException e) {
            System.out.println("Erro ao listar materiais com estoque baixo: " + e.getMessage());
            return materiais;
        }
    }

    public boolean atualizarEstoque(String id, int novaQuantidade) {
        String sql = "UPDATE materiais SET quantidade_atual = ? WHERE id = ?";
        
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

    private Material criarMaterialDoResultSet(ResultSet rs) throws SQLException {
        Material material = new Material();
        material.setId(rs.getString("id"));
        material.setNome(rs.getString("nome"));
        material.setDescricao(rs.getString("descricao"));
        material.setTipo(rs.getString("tipo"));
        material.setPreco(rs.getDouble("preco"));
        material.setQuantidadeAtual(rs.getInt("quantidade_atual"));
        material.setQuantidadeMinima(rs.getInt("quantidade_minima"));
        material.setUnidadeMedida(rs.getString("unidade_medida"));
        return material;
    }
}
