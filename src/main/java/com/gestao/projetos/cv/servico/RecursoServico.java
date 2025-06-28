package com.gestao.projetos.cv.servico;

import com.gestao.projetos.cv.dao.RecursoDAO;
import com.gestao.projetos.cv.modelo.Recurso;

import java.util.List;

public class RecursoServico {
    private RecursoDAO recursoDAO;
    
    public RecursoServico() {
        this.recursoDAO = new RecursoDAO();
    }
    
    public boolean cadastrarRecurso(String nome, String descricao, String categoria, double custoUnitario, 
                                   int quantidadeDisponivel, int quantidadeMinima, String unidadeMedida) {
        // Validações básicas
        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("Nome não pode ser vazio");
            return false;
        }
        
        if (categoria == null || categoria.trim().isEmpty()) {
            System.out.println("Categoria não pode ser vazia");
            return false;
        }
        
        if (custoUnitario < 0) {
            System.out.println("Custo unitário não pode ser negativo");
            return false;
        }
        
        if (quantidadeDisponivel < 0) {
            System.out.println("Quantidade disponível não pode ser negativa");
            return false;
        }
        
        if (quantidadeMinima < 0) {
            System.out.println("Quantidade mínima não pode ser negativa");
            return false;
        }
        
        if (unidadeMedida == null || unidadeMedida.trim().isEmpty()) {
            System.out.println("Unidade de medida não pode ser vazia");
            return false;
        }
        
        Recurso recurso = new Recurso(nome, descricao, categoria, custoUnitario, quantidadeDisponivel, quantidadeMinima, unidadeMedida);
        return recursoDAO.salvar(recurso);
    }
    
    public Recurso buscarRecursoPorId(String id) {
        return recursoDAO.buscarPorId(id);
    }
    
    public List<Recurso> listarTodosRecursos() {
        return recursoDAO.listarTodos();
    }
    
    public List<Recurso> listarRecursosPorCategoria(String categoria) {
        return recursoDAO.buscarPorCategoria(categoria);
    }
    
    public List<Recurso> listarRecursosComEstoqueBaixo() {
        return recursoDAO.listarEstoqueBaixo();
    }
    
    public boolean atualizarEstoque(String id, int novaQuantidade) {
        Recurso recurso = recursoDAO.buscarPorId(id);
        
        if (recurso == null) {
            System.out.println("Recurso não encontrado");
            return false;
        }
        
        if (novaQuantidade < 0) {
            System.out.println("Quantidade não pode ser negativa");
            return false;
        }
        
        return recursoDAO.atualizarEstoque(id, novaQuantidade);
    }
    
    public boolean excluirRecurso(String id) {
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID não pode ser vazio");
            return false;
        }
        
        return recursoDAO.excluir(id);
    }
}
