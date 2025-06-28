package com.almoxarifado.servico;

import com.almoxarifado.dao.MaterialDAO;
import com.almoxarifado.modelo.Material;

import java.util.List;

public class MaterialServico {
    private MaterialDAO materialDAO;
    
    public MaterialServico() {
        this.materialDAO = new MaterialDAO();
    }
    
    public boolean cadastrarMaterial(String nome, String descricao, String tipo, double preco, 
                                   int quantidadeAtual, int quantidadeMinima, String unidadeMedida) {
        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("Nome do material não pode ser vazio");
            return false;
        }
        
        if (tipo == null || tipo.trim().isEmpty()) {
            System.out.println("Tipo do material não pode ser vazio");
            return false;
        }
        
        if (preco < 0) {
            System.out.println("Preço do material não pode ser negativo");
            return false;
        }
        
        if (quantidadeAtual < 0) {
            System.out.println("Quantidade atual não pode ser negativa");
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
        
        Material material = new Material(nome, descricao, tipo, preco, quantidadeAtual, quantidadeMinima, unidadeMedida);
        return materialDAO.salvar(material);
    }
    
    public boolean atualizarMaterial(String id, String nome, String descricao, String tipo, double preco, 
                                   int quantidadeAtual, int quantidadeMinima, String unidadeMedida) {
        Material material = materialDAO.buscarPorId(id);
        
        if (material == null) {
            System.out.println("Material não encontrado");
            return false;
        }
        
        if (nome != null && !nome.trim().isEmpty()) {
            material.setNome(nome);
        }
        
        if (descricao != null) {
            material.setDescricao(descricao);
        }
        
        if (tipo != null && !tipo.trim().isEmpty()) {
            material.setTipo(tipo);
        }
        
        if (preco >= 0) {
            material.setPreco(preco);
        }
        
        if (quantidadeAtual >= 0) {
            material.setQuantidadeAtual(quantidadeAtual);
        }
        
        if (quantidadeMinima >= 0) {
            material.setQuantidadeMinima(quantidadeMinima);
        }
        
        if (unidadeMedida != null && !unidadeMedida.trim().isEmpty()) {
            material.setUnidadeMedida(unidadeMedida);
        }
        
        return materialDAO.atualizar(material);
    }
    
    public boolean excluirMaterial(String id) {
        Material material = materialDAO.buscarPorId(id);
        
        if (material == null) {
            System.out.println("Material não encontrado");
            return false;
        }
        
        return materialDAO.excluir(id);
    }
    
    public Material buscarMaterialPorId(String id) {
        return materialDAO.buscarPorId(id);
    }
    
    public List<Material> listarTodosMateriais() {
        return materialDAO.listarTodos();
    }
    
    public List<Material> listarMateriaisPorTipo(String tipo) {
        return materialDAO.buscarPorTipo(tipo);
    }
    
    public List<Material> buscarMateriaisPorNome(String nome) {
        return materialDAO.buscarPorNome(nome);
    }
    
    public List<Material> listarMateriaisComEstoqueBaixo() {
        return materialDAO.listarEstoqueBaixo();
    }
    
    public boolean atualizarEstoque(String id, int novaQuantidade) {
        Material material = materialDAO.buscarPorId(id);
        
        if (material == null) {
            System.out.println("Material não encontrado");
            return false;
        }
        
        if (novaQuantidade < 0) {
            System.out.println("Quantidade não pode ser negativa");
            return false;
        }
        
        return materialDAO.atualizarEstoque(id, novaQuantidade);
    }
}
