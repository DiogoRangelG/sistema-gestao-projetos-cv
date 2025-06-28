package com.gestao.projetos.cv.modelo;

import com.gestao.projetos.cv.util.GeradorId;
import java.util.Date;

public class Projeto {
    private String id;
    private String nome;
    private String descricao;
    private String cliente;
    private Date dataInicio;
    private Date dataPrazo;
    private String responsavel;
    private String status;
    private double orcamento;
    private String categoriaProjeto; // Banner, Sinalização, Identidade Visual, Digital, etc.

    public Projeto(String nome, String descricao, String cliente, Date dataInicio, Date dataPrazo, String responsavel, String status, double orcamento, String categoriaProjeto) {
        this.id = GeradorId.gerarProximoId();
        this.nome = nome;
        this.descricao = descricao;
        this.cliente = cliente;
        this.dataInicio = dataInicio;
        this.dataPrazo = dataPrazo;
        this.responsavel = responsavel;
        this.status = status;
        this.orcamento = orcamento;
        this.categoriaProjeto = categoriaProjeto;
    }

    public Projeto() {
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

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataPrazo() {
        return dataPrazo;
    }

    public void setDataPrazo(Date dataPrazo) {
        this.dataPrazo = dataPrazo;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(double orcamento) {
        this.orcamento = orcamento;
    }

    public String getCategoriaProjeto() {
        return categoriaProjeto;
    }

    public void setCategoriaProjeto(String categoriaProjeto) {
        this.categoriaProjeto = categoriaProjeto;
    }

    @Override
    public String toString() {
        return "Projeto{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", cliente='" + cliente + '\'' +
                ", categoriaProjeto='" + categoriaProjeto + '\'' +
                ", dataInicio=" + dataInicio +
                ", dataPrazo=" + dataPrazo +
                ", responsavel='" + responsavel + '\'' +
                ", status='" + status + '\'' +
                ", orcamento=" + orcamento +
                '}';
    }
}
