package com.almoxarifado.modelo;

import java.util.Date;
import java.util.UUID;

public class Movimentacao {
    private String id;
    private Material material;
    private Projeto projeto;
    private String tipo; // ENTRADA ou SAIDA
    private int quantidade;
    private Date data;
    private String responsavel;
    private String observacao;

    public Movimentacao(Material material, Projeto projeto, String tipo, int quantidade, Date data, String responsavel, String observacao) {
        this.id = UUID.randomUUID().toString();
        this.material = material;
        this.projeto = projeto;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.data = data;
        this.responsavel = responsavel;
        this.observacao = observacao;
    }

    public Movimentacao() {
        this.id = UUID.randomUUID().toString();
        this.data = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public String toString() {
        return "Movimentacao{" +
                "id='" + id + '\'' +
                ", material=" + material.getNome() +
                ", projeto=" + (projeto != null ? projeto.getNome() : "N/A") +
                ", tipo='" + tipo + '\'' +
                ", quantidade=" + quantidade +
                ", data=" + data +
                ", responsavel='" + responsavel + '\'' +
                '}';
    }
}
