package com.gestao.projetos.cv.servico;

import com.gestao.projetos.cv.dao.ProjetoDAO;
import com.gestao.projetos.cv.modelo.Projeto;

import java.util.Date;
import java.util.List;

public class ProjetoServico {
    private ProjetoDAO projetoDAO;
    
    public ProjetoServico() {
        this.projetoDAO = new ProjetoDAO();
    }
    
    public boolean cadastrarProjeto(String nome, String descricao, String cliente, Date dataInicio, 
                                  Date dataPrazo, String responsavel, String status, double orcamento, 
                                  String categoriaProjeto) {
        // Validações básicas
        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("Nome não pode ser vazio");
            return false;
        }
        
        if (cliente == null || cliente.trim().isEmpty()) {
            System.out.println("Cliente não pode ser vazio");
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
        
        if (categoriaProjeto == null || categoriaProjeto.trim().isEmpty()) {
            System.out.println("Categoria do projeto não pode ser vazia");
            return false;
        }
        
        Projeto projeto = new Projeto(nome, descricao, cliente, dataInicio, dataPrazo, responsavel, status, orcamento, categoriaProjeto);
        return projetoDAO.salvar(projeto);
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
    
    public List<Projeto> buscarProjetosPorCliente(String cliente) {
        return projetoDAO.buscarPorCliente(cliente);
    }
    
    public boolean excluirProjeto(String id) {
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID não pode ser vazio");
            return false;
        }
        
        return projetoDAO.excluir(id);
    }
}
