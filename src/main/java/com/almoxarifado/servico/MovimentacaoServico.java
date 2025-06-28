package com.almoxarifado.servico;

import com.almoxarifado.dao.MovimentacaoDAO;
import com.almoxarifado.modelo.Movimentacao;
import com.almoxarifado.modelo.Material;
import com.almoxarifado.modelo.Projeto;

import java.util.Date;
import java.util.List;

public class MovimentacaoServico {
    private MovimentacaoDAO movimentacaoDAO;
    private MaterialServico materialServico;
    private ProjetoServico projetoServico;
    
    public MovimentacaoServico() {
        this.movimentacaoDAO = new MovimentacaoDAO();
        this.materialServico = new MaterialServico();
        this.projetoServico = new ProjetoServico();
    }
    
    public boolean registrarEntrada(String materialId, int quantidade, String responsavel, String observacao) {
        if (quantidade <= 0) {
            System.out.println("Quantidade deve ser maior que zero");
            return false;
        }
        
        Material material = materialServico.buscarMaterialPorId(materialId);
        if (material == null) {
            System.out.println("Material não encontrado");
            return false;
        }
        
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setMaterial(material);
        movimentacao.setTipo("ENTRADA");
        movimentacao.setQuantidade(quantidade);
        movimentacao.setData(new Date());
        movimentacao.setResponsavel(responsavel);
        movimentacao.setObservacao(observacao);
        
        return movimentacaoDAO.salvar(movimentacao);
    }
    
    public boolean registrarSaida(String materialId, String projetoId, int quantidade, String responsavel, String observacao) {
        if (quantidade <= 0) {
            System.out.println("Quantidade deve ser maior que zero");
            return false;
        }
        
        Material material = materialServico.buscarMaterialPorId(materialId);
        if (material == null) {
            System.out.println("Material não encontrado");
            return false;
        }
        
        if (material.getQuantidadeAtual() < quantidade) {
            System.out.println("Quantidade insuficiente em estoque");
            return false;
        }
        
        Projeto projeto = null;
        if (projetoId != null && !projetoId.trim().isEmpty()) {
            projeto = projetoServico.buscarProjetoPorId(projetoId);
            if (projeto == null) {
                System.out.println("Projeto não encontrado");
                return false;
            }
        }
        
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setMaterial(material);
        movimentacao.setProjeto(projeto);
        movimentacao.setTipo("SAIDA");
        movimentacao.setQuantidade(quantidade);
        movimentacao.setData(new Date());
        movimentacao.setResponsavel(responsavel);
        movimentacao.setObservacao(observacao);
        
        return movimentacaoDAO.salvar(movimentacao);
    }
    
    public boolean cancelarMovimentacao(String id) {
        return movimentacaoDAO.excluir(id);
    }
    
    public Movimentacao buscarMovimentacaoPorId(String id) {
        return movimentacaoDAO.buscarPorId(id);
    }
    
    public List<Movimentacao> listarTodasMovimentacoes() {
        return movimentacaoDAO.listarTodas();
    }
    
    public List<Movimentacao> listarMovimentacoesPorPeriodo(Date dataInicio, Date dataFim) {
        return movimentacaoDAO.buscarPorPeriodo(dataInicio, dataFim);
    }
    
    public List<Movimentacao> listarMovimentacoesPorMaterial(String materialId) {
        return movimentacaoDAO.buscarPorMaterial(materialId);
    }
    
    public List<Movimentacao> listarMovimentacoesPorProjeto(String projetoId) {
        return movimentacaoDAO.buscarPorProjeto(projetoId);
    }
    
    public List<Movimentacao> listarEntradasOuSaidas(String tipo) {
        if (!tipo.equals("ENTRADA") && !tipo.equals("SAIDA")) {
            System.out.println("Tipo inválido. Utilize ENTRADA ou SAIDA");
            return null;
        }
        return movimentacaoDAO.buscarPorTipo(tipo);
    }
}
