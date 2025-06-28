package com.gestao.projetos.cv.servico;

import com.gestao.projetos.cv.dao.MovimentacaoDAO;
import com.gestao.projetos.cv.modelo.Movimentacao;
import com.gestao.projetos.cv.modelo.Recurso;
import com.gestao.projetos.cv.modelo.Projeto;

import java.util.Date;
import java.util.List;

public class MovimentacaoServico {
    private MovimentacaoDAO movimentacaoDAO;
    private RecursoServico recursoServico;
    private ProjetoServico projetoServico;
    
    public MovimentacaoServico() {
        this.movimentacaoDAO = new MovimentacaoDAO();
        this.recursoServico = new RecursoServico();
        this.projetoServico = new ProjetoServico();
    }
    
    public boolean registrarEntrada(String recursoId, int quantidade, String responsavel, String observacao) {
        // Buscar o recurso
        Recurso recurso = recursoServico.buscarRecursoPorId(recursoId);
        if (recurso == null) {
            System.out.println("Recurso não encontrado");
            return false;
        }
        
        // Validações
        if (quantidade <= 0) {
            System.out.println("Quantidade deve ser maior que zero");
            return false;
        }
        
        if (responsavel == null || responsavel.trim().isEmpty()) {
            System.out.println("Responsável não pode ser vazio");
            return false;
        }
        
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setRecurso(recurso);
        movimentacao.setTipo("ENTRADA");
        movimentacao.setQuantidade(quantidade);
        movimentacao.setData(new Date());
        movimentacao.setResponsavel(responsavel);
        movimentacao.setObservacao(observacao);
        
        return movimentacaoDAO.salvar(movimentacao);
    }
    
    public boolean registrarSaida(String recursoId, String projetoId, int quantidade, String responsavel, String observacao) {
        // Buscar o recurso
        Recurso recurso = recursoServico.buscarRecursoPorId(recursoId);
        if (recurso == null) {
            System.out.println("Recurso não encontrado");
            return false;
        }
        
        // Buscar o projeto
        Projeto projeto = projetoServico.buscarProjetoPorId(projetoId);
        if (projeto == null) {
            System.out.println("Projeto não encontrado");
            return false;
        }
        
        // Validações
        if (quantidade <= 0) {
            System.out.println("Quantidade deve ser maior que zero");
            return false;
        }
        
        if (quantidade > recurso.getQuantidadeDisponivel()) {
            System.out.println("Quantidade insuficiente em estoque");
            return false;
        }
        
        if (responsavel == null || responsavel.trim().isEmpty()) {
            System.out.println("Responsável não pode ser vazio");
            return false;
        }
        
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setRecurso(recurso);
        movimentacao.setProjeto(projeto);
        movimentacao.setTipo("SAIDA");
        movimentacao.setQuantidade(quantidade);
        movimentacao.setData(new Date());
        movimentacao.setResponsavel(responsavel);
        movimentacao.setObservacao(observacao);
        
        return movimentacaoDAO.salvar(movimentacao);
    }
    
    public List<Movimentacao> listarTodasMovimentacoes() {
        return movimentacaoDAO.listarTodas();
    }
    
    public List<Movimentacao> buscarMovimentacoesPorProjeto(String projetoId) {
        return movimentacaoDAO.buscarPorProjeto(projetoId);
    }
    
    public Movimentacao buscarMovimentacaoPorId(String id) {
        return movimentacaoDAO.buscarPorId(id);
    }
}
