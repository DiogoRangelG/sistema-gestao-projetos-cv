package com.almoxarifado.servico;

import com.almoxarifado.dao.ProjetoDAO;
import com.almoxarifado.modelo.Projeto;

import java.util.Date;
import java.util.List;

public class ProjetoServico {
    private ProjetoDAO projetoDAO;
    
    public ProjetoServico() {
        this.projetoDAO = new ProjetoDAO();
    }
    
    public boolean cadastrarProjeto(String nome, String descricao, Date dataInicio, Date dataFim, 
                                  String responsavel, String status) {
        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("Nome do projeto não pode ser vazio");
            return false;
        }
        
        if (dataInicio == null) {
            System.out.println("Data de início não pode ser vazia");
            return false;
        }
        
        if (responsavel == null || responsavel.trim().isEmpty()) {
            System.out.println("Responsável não pode ser vazio");
            return false;
        }
        
        if (status == null || status.trim().isEmpty()) {
            System.out.println("Status não pode ser vazio");
            return false;
        }
        
        Projeto projeto = new Projeto(nome, descricao, dataInicio, dataFim, responsavel, status);
        return projetoDAO.salvar(projeto);
    }
    
    public boolean atualizarProjeto(String id, String nome, String descricao, Date dataInicio, Date dataFim, 
                                  String responsavel, String status) {
        Projeto projeto = projetoDAO.buscarPorId(id);
        
        if (projeto == null) {
            System.out.println("Projeto não encontrado");
            return false;
        }
        
        if (nome != null && !nome.trim().isEmpty()) {
            projeto.setNome(nome);
        }
        
        if (descricao != null) {
            projeto.setDescricao(descricao);
        }
        
        if (dataInicio != null) {
            projeto.setDataInicio(dataInicio);
        }
        
        projeto.setDataFim(dataFim);
        
        if (responsavel != null && !responsavel.trim().isEmpty()) {
            projeto.setResponsavel(responsavel);
        }
        
        if (status != null && !status.trim().isEmpty()) {
            projeto.setStatus(status);
        }
        
        return projetoDAO.atualizar(projeto);
    }
    
    public boolean excluirProjeto(String id) {
        Projeto projeto = projetoDAO.buscarPorId(id);
        
        if (projeto == null) {
            System.out.println("Projeto não encontrado");
            return false;
        }
        
        return projetoDAO.excluir(id);
    }
    
    public Projeto buscarProjetoPorId(String id) {
        return projetoDAO.buscarPorId(id);
    }
    
    public List<Projeto> listarTodosProjetos() {
        return projetoDAO.listarTodos();
    }
    
    public List<Projeto> listarProjetosPorStatus(String status) {
        return projetoDAO.buscarPorStatus(status);
    }
    
    public List<Projeto> buscarProjetosPorNome(String nome) {
        return projetoDAO.buscarPorNome(nome);
    }
}
