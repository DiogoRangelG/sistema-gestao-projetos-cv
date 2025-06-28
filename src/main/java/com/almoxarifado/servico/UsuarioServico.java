package com.almoxarifado.servico;

import com.almoxarifado.dao.UsuarioDAO;
import com.almoxarifado.modelo.Usuario;

import java.util.List;

public class UsuarioServico {
    private UsuarioDAO usuarioDAO;
    private static Usuario usuarioLogado;
    
    public UsuarioServico() {
        this.usuarioDAO = new UsuarioDAO();
    }
    
    public boolean cadastrarUsuario(String nome, String login, String senha, String nivel) {
        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("Nome do usuário não pode ser vazio");
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
        
        if (nivel == null || nivel.trim().isEmpty()) {
            System.out.println("Nível não pode ser vazio");
            return false;
        }
        
        // Verifica se já existe um usuário com o mesmo login
        Usuario usuarioExistente = usuarioDAO.buscarPorLogin(login);
        if (usuarioExistente != null) {
            System.out.println("Já existe um usuário com este login");
            return false;
        }
        
        Usuario usuario = new Usuario(nome, login, senha, nivel);
        return usuarioDAO.salvar(usuario);
    }
    
    public boolean atualizarUsuario(String id, String nome, String login, String senha, String nivel) {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        
        if (usuario == null) {
            System.out.println("Usuário não encontrado");
            return false;
        }
        
        // Se estiver alterando o login, verifica se já existe um usuário com o novo login
        if (login != null && !login.trim().isEmpty() && !login.equals(usuario.getLogin())) {
            Usuario usuarioExistente = usuarioDAO.buscarPorLogin(login);
            if (usuarioExistente != null) {
                System.out.println("Já existe um usuário com este login");
                return false;
            }
            usuario.setLogin(login);
        }
        
        if (nome != null && !nome.trim().isEmpty()) {
            usuario.setNome(nome);
        }
        
        if (senha != null && !senha.trim().isEmpty()) {
            usuario.setSenha(senha);
        }
        
        if (nivel != null && !nivel.trim().isEmpty()) {
            usuario.setNivel(nivel);
        }
        
        return usuarioDAO.atualizar(usuario);
    }
    
    public boolean excluirUsuario(String id) {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        
        if (usuario == null) {
            System.out.println("Usuário não encontrado");
            return false;
        }
        
        if (usuarioLogado != null && usuarioLogado.getId().equals(id)) {
            System.out.println("Não é possível excluir o usuário logado");
            return false;
        }
        
        return usuarioDAO.excluir(id);
    }
    
    public Usuario buscarUsuarioPorId(String id) {
        return usuarioDAO.buscarPorId(id);
    }
    
    public List<Usuario> listarTodosUsuarios() {
        return usuarioDAO.listarTodos();
    }
    
    public List<Usuario> listarUsuariosPorNivel(String nivel) {
        return usuarioDAO.buscarPorNivel(nivel);
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
