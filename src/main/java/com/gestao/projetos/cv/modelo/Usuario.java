package com.gestao.projetos.cv.modelo;

import com.gestao.projetos.cv.util.GeradorId;

public class Usuario {
    private String id;
    private String nome;
    private String login;
    private String senha;
    private String nivel; // ADMIN, GESTOR, OPERADOR

    public Usuario(String nome, String login, String senha, String nivel) {
        this.id = GeradorId.gerarProximoId();
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.nivel = nivel;
    }

    public Usuario() {
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", login='" + login + '\'' +
                ", nivel='" + nivel + '\'' +
                '}';
    }
}
