package com.gestao.projetos.cv.dao;

import com.gestao.projetos.cv.modelo.Projeto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjetoDAO {
    private Connection conexao;

    public ProjetoDAO() {
        this.conexao = ConexaoDB.getConexao();
    }

    public boolean salvar(Projeto projeto) {
        String sql = "INSERT INTO projetos (id, nome, descricao, cliente, data_inicio, data_prazo, responsavel, status, orcamento, categoria_projeto) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, projeto.getId());
            stmt.setString(2, projeto.getNome());
            stmt.setString(3, projeto.getDescricao());
            stmt.setString(4, projeto.getCliente());
            stmt.setDate(5, new java.sql.Date(projeto.getDataInicio().getTime()));
            
            if (projeto.getDataPrazo() != null) {
                stmt.setDate(6, new java.sql.Date(projeto.getDataPrazo().getTime()));
            } else {
                stmt.setNull(6, Types.DATE);
            }
            
            stmt.setString(7, projeto.getResponsavel());
            stmt.setString(8, projeto.getStatus());
            stmt.setDouble(9, projeto.getOrcamento());
            stmt.setString(10, projeto.getCategoriaProjeto());
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar projeto: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizar(Projeto projeto) {
        String sql = "UPDATE projetos SET nome = ?, descricao = ?, cliente = ?, data_inicio = ?, " +
                     "data_prazo = ?, responsavel = ?, status = ?, orcamento = ?, categoria_projeto = ? " +
                     "WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getDescricao());
            stmt.setString(3, projeto.getCliente());
            stmt.setDate(4, new java.sql.Date(projeto.getDataInicio().getTime()));
            
            if (projeto.getDataPrazo() != null) {
                stmt.setDate(5, new java.sql.Date(projeto.getDataPrazo().getTime()));
            } else {
                stmt.setNull(5, Types.DATE);
            }
            
            stmt.setString(6, projeto.getResponsavel());
            stmt.setString(7, projeto.getStatus());
            stmt.setDouble(8, projeto.getOrcamento());
            stmt.setString(9, projeto.getCategoriaProjeto());
            stmt.setString(10, projeto.getId());
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar projeto: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(String id) {
        String sql = "DELETE FROM projetos WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, id);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir projeto: " + e.getMessage());
            return false;
        }
    }

    public Projeto buscarPorId(String id) {
        String sql = "SELECT * FROM projetos WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, id);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return criarProjetoDoResultSet(rs);
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar projeto por ID: " + e.getMessage());
            return null;
        }
    }

    public List<Projeto> listarTodos() {
        List<Projeto> projetos = new ArrayList<>();
        String sql = "SELECT * FROM projetos ORDER BY nome";
        
        try (Statement stmt = conexao.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                projetos.add(criarProjetoDoResultSet(rs));
            }
            
            return projetos;
        } catch (SQLException e) {
            System.out.println("Erro ao listar projetos: " + e.getMessage());
            return projetos;
        }
    }

    public List<Projeto> buscarPorStatus(String status) {
        List<Projeto> projetos = new ArrayList<>();
        String sql = "SELECT * FROM projetos WHERE status = ? ORDER BY nome";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, status);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                projetos.add(criarProjetoDoResultSet(rs));
            }
            
            return projetos;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar projetos por status: " + e.getMessage());
            return projetos;
        }
    }

    public List<Projeto> buscarPorCliente(String cliente) {
        List<Projeto> projetos = new ArrayList<>();
        String sql = "SELECT * FROM projetos WHERE cliente LIKE ? ORDER BY nome";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, "%" + cliente + "%");
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                projetos.add(criarProjetoDoResultSet(rs));
            }
            
            return projetos;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar projetos por cliente: " + e.getMessage());
            return projetos;
        }
    }

    private Projeto criarProjetoDoResultSet(ResultSet rs) throws SQLException {
        Projeto projeto = new Projeto();
        projeto.setId(rs.getString("id"));
        projeto.setNome(rs.getString("nome"));
        projeto.setDescricao(rs.getString("descricao"));
        projeto.setCliente(rs.getString("cliente"));
        projeto.setDataInicio(rs.getDate("data_inicio"));
        projeto.setDataPrazo(rs.getDate("data_prazo"));
        projeto.setResponsavel(rs.getString("responsavel"));
        projeto.setStatus(rs.getString("status"));
        projeto.setOrcamento(rs.getDouble("orcamento"));
        projeto.setCategoriaProjeto(rs.getString("categoria_projeto"));
        return projeto;
    }
}
