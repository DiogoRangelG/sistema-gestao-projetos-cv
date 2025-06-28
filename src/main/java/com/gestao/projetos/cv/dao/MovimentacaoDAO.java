package com.gestao.projetos.cv.dao;

import com.gestao.projetos.cv.modelo.Movimentacao;
import com.gestao.projetos.cv.modelo.Recurso;
import com.gestao.projetos.cv.modelo.Projeto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoDAO {
    private Connection conexao;
    private RecursoDAO recursoDAO;
    private ProjetoDAO projetoDAO;

    public MovimentacaoDAO() {
        this.conexao = ConexaoDB.getConexao();
        this.recursoDAO = new RecursoDAO();
        this.projetoDAO = new ProjetoDAO();
    }

    public boolean salvar(Movimentacao movimentacao) {
        String sql = "INSERT INTO movimentacoes (id, recurso_id, projeto_id, tipo, quantidade, data, responsavel, observacao) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, movimentacao.getId());
            stmt.setString(2, movimentacao.getRecurso().getId());
            
            if (movimentacao.getProjeto() != null) {
                stmt.setString(3, movimentacao.getProjeto().getId());
            } else {
                stmt.setNull(3, Types.VARCHAR);
            }
            
            stmt.setString(4, movimentacao.getTipo());
            stmt.setInt(5, movimentacao.getQuantidade());
            stmt.setTimestamp(6, new java.sql.Timestamp(movimentacao.getData().getTime()));
            stmt.setString(7, movimentacao.getResponsavel());
            stmt.setString(8, movimentacao.getObservacao());
            
            stmt.executeUpdate();
            
            // Atualizar o estoque do recurso
            Recurso recurso = movimentacao.getRecurso();
            int novaQuantidade = recurso.getQuantidadeDisponivel();
            
            if (movimentacao.getTipo().equals("ENTRADA")) {
                novaQuantidade += movimentacao.getQuantidade();
            } else if (movimentacao.getTipo().equals("SAIDA")) {
                novaQuantidade -= movimentacao.getQuantidade();
            }
            
            recursoDAO.atualizarEstoque(recurso.getId(), novaQuantidade);
            
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar movimentação: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(String id) {
        // Primeiro buscar a movimentação para poder restaurar o estoque
        Movimentacao movimentacao = buscarPorId(id);
        if (movimentacao == null) {
            return false;
        }
        
        // Restaurar o estoque
        Recurso recurso = movimentacao.getRecurso();
        int quantidadeAtual = recurso.getQuantidadeDisponivel();
        
        if (movimentacao.getTipo().equals("ENTRADA")) {
            quantidadeAtual -= movimentacao.getQuantidade();
        } else if (movimentacao.getTipo().equals("SAIDA")) {
            quantidadeAtual += movimentacao.getQuantidade();
        }
        
        boolean atualizouEstoque = recursoDAO.atualizarEstoque(recurso.getId(), quantidadeAtual);
        if (!atualizouEstoque) {
            return false;
        }
        
        // Agora excluir a movimentação
        String sql = "DELETE FROM movimentacoes WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, id);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir movimentação: " + e.getMessage());
            return false;
        }
    }

    public Movimentacao buscarPorId(String id) {
        String sql = "SELECT * FROM movimentacoes WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, id);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return criarMovimentacaoDoResultSet(rs);
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar movimentação por ID: " + e.getMessage());
            return null;
        }
    }

    public List<Movimentacao> listarTodas() {
        List<Movimentacao> movimentacoes = new ArrayList<>();
        String sql = "SELECT * FROM movimentacoes ORDER BY data DESC";
        
        try (Statement stmt = conexao.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                movimentacoes.add(criarMovimentacaoDoResultSet(rs));
            }
            
            return movimentacoes;
        } catch (SQLException e) {
            System.out.println("Erro ao listar movimentações: " + e.getMessage());
            return movimentacoes;
        }
    }

    public List<Movimentacao> buscarPorProjeto(String projetoId) {
        List<Movimentacao> movimentacoes = new ArrayList<>();
        String sql = "SELECT * FROM movimentacoes WHERE projeto_id = ? ORDER BY data DESC";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, projetoId);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                movimentacoes.add(criarMovimentacaoDoResultSet(rs));
            }
            
            return movimentacoes;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar movimentações por projeto: " + e.getMessage());
            return movimentacoes;
        }
    }

    private Movimentacao criarMovimentacaoDoResultSet(ResultSet rs) throws SQLException {
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setId(rs.getString("id"));
        
        String recursoId = rs.getString("recurso_id");
        Recurso recurso = recursoDAO.buscarPorId(recursoId);
        movimentacao.setRecurso(recurso);
        
        String projetoId = rs.getString("projeto_id");
        if (projetoId != null) {
            Projeto projeto = projetoDAO.buscarPorId(projetoId);
            movimentacao.setProjeto(projeto);
        }
        
        movimentacao.setTipo(rs.getString("tipo"));
        movimentacao.setQuantidade(rs.getInt("quantidade"));
        movimentacao.setData(rs.getTimestamp("data"));
        movimentacao.setResponsavel(rs.getString("responsavel"));
        movimentacao.setObservacao(rs.getString("observacao"));
        
        return movimentacao;
    }
}
