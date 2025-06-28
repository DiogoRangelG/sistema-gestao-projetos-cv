package com.gestao.projetos.cv.servico;

import com.gestao.projetos.cv.dao.UsuarioDAO;
import com.gestao.projetos.cv.modelo.Usuario;

import java.util.List;

public class UsuarioServico {
    private UsuarioDAO usuarioDAO;
    private static Usuario usuarioLogado;
    
    public UsuarioServico() {
        this.usuarioDAO = new UsuarioDAO();
    }
    
    public boolean cadastrarUsuario(String nome, String login, String senha, String nivel) {
        // Verificar se o login já existe
        Usuario usuarioExistente = usuarioDAO.buscarPorLogin(login);
        if (usuarioExistente != null) {
            System.out.println("Já existe um usuário com este login");
            return false;
        }
        
        // Validações básicas
        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("Nome não pode ser vazio");
            return false;
        }
        
        if (login == null || login.trim().isEmpty()) {
            System.out.println("Login não pode ser vazio");
            return false;
        }
        
        if (senha == null || senha.trim().isEmpty()) {
            System.out.println("Senha não pode ser vazia");
            return false;
        }
        
        Usuario usuario = new Usuario(nome, login, senha, nivel);
        return usuarioDAO.salvar(usuario);
    }
    
    public Usuario autenticar(String login, String senha) {
        Usuario usuario = usuarioDAO.autenticar(login, senha);
        if (usuario != null) {
            usuarioLogado = usuario;
        }
        return usuario;
    }
    
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
    
    public void logout() {
        usuarioLogado = null;
    }
    
    public List<Usuario> listarTodosUsuarios() {
        return usuarioDAO.listarTodos();
    }
    
    public boolean verificarPermissao(String nivelMinimo) {
        if (usuarioLogado == null) {
            return false;
        }
        
        switch (nivelMinimo) {
            case "ADMIN":
                return usuarioLogado.getNivel().equals("ADMIN");
            case "GESTOR":
                return usuarioLogado.getNivel().equals("ADMIN") || usuarioLogado.getNivel().equals("GESTOR");
            case "OPERADOR":
                return true;
            default:
                return false;
        }
    }
}
