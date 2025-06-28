package com.gestao.projetos.cv.modelo;

import com.gestao.projetos.cv.util.GeradorId;

public class Recurso {
    private String id;
    private String nome;
    private String descricao;
    private String categoria; // Banner, Placa, Adesivo, Digital, Impressão, etc.
    private double custoUnitario;
    private int quantidadeDisponivel;
    private int quantidadeMinima;
    private String unidadeMedida;

    public Recurso(String nome, String descricao, String categoria, double custoUnitario, int quantidadeDisponivel, int quantidadeMinima, String unidadeMedida) {
        this.id = GeradorId.gerarProximoId();
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.custoUnitario = custoUnitario;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.quantidadeMinima = quantidadeMinima;
        this.unidadeMedida = unidadeMedida;
    }

    public Recurso() {
        this.id = GeradorId.gerarProximoId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getCustoUnitario() {
        return custoUnitario;
    }

    public void setCustoUnitario(double custoUnitario) {
        this.custoUnitario = custoUnitario;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(int quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public int getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public void setQuantidadeMinima(int quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    @Override
    public String toString() {
        return "Recurso{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", categoria='" + categoria + '\'' +
                ", quantidadeDisponivel=" + quantidadeDisponivel +
                ", quantidadeMinima=" + quantidadeMinima +
                ", unidadeMedida='" + unidadeMedida + '\'' +
                ", custoUnitario=" + custoUnitario +
                '}';
    }
}
