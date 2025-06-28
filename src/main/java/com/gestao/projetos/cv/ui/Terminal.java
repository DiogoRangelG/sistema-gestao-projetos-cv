package com.gestao.projetos.cv.ui;

import com.gestao.projetos.cv.dao.ConexaoDB;
import com.gestao.projetos.cv.modelo.*;
import com.gestao.projetos.cv.servico.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Terminal {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private final UsuarioServico usuarioServico;
    private final RecursoServico recursoServico;
    private final ProjetoServico projetoServico;
    private final MovimentacaoServico movimentacaoServico;

    public Terminal() {
        this.usuarioServico = new UsuarioServico();
        this.recursoServico = new RecursoServico();
        this.projetoServico = new ProjetoServico();
        this.movimentacaoServico = new MovimentacaoServico();
    }

    public void iniciar() {
        // Inicializa o banco de dados
        ConexaoDB.criarBancoDeDados();

        boolean sair = false;
        while (!sair) {
            if (usuarioServico.getUsuarioLogado() == null) {
                sair = menuLogin();
            } else {
                sair = menuPrincipal();
            }
        }

        System.out.println("\nSistema de Gestão de Projetos de Comunicação Visual encerrado. Até logo!");
        ConexaoDB.fecharConexao();
    }

    private boolean menuLogin() {
        limparTela();
        System.out.println("=== SISTEMA DE GESTÃO DE PROJETOS DE COMUNICAÇÃO VISUAL ===");
        System.out.println("1. Login");
        System.out.println("2. Sair");
        System.out.print("\nEscolha uma opção: ");
        String opcao = scanner.nextLine();

        switch (opcao) {
            case "1":
                realizarLogin();
                return false;
            case "2":
                return true;
            default:
                System.out.println("Opção inválida!");
                pausar();
                return false;
        }
    }

    private void realizarLogin() {
        limparTela();
        System.out.println("=== LOGIN ===");
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Usuario usuario = usuarioServico.autenticar(login, senha);
        if (usuario != null) {
            System.out.println("\nLogin realizado com sucesso!");
            System.out.println("Bem-vindo(a), " + usuario.getNome() + "!");
        } else {
            System.out.println("\nLogin ou senha incorretos!");
        }
        pausar();
    }

    private boolean menuPrincipal() {
        limparTela();
        Usuario usuarioLogado = usuarioServico.getUsuarioLogado();

        System.out.println("=== SISTEMA DE GESTÃO DE PROJETOS DE COMUNICAÇÃO VISUAL ===");
        System.out.println("Usuário: " + usuarioLogado.getNome() + " | Nível: " + usuarioLogado.getNivel());
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Gestão de Recursos");
        System.out.println("2. Gestão de Projetos");
        System.out.println("3. Movimentações");
        System.out.println("4. Relatórios");
        
        if (usuarioServico.verificarPermissao("ADMIN")) {
            System.out.println("5. Gestão de Usuários");
        }
        
        System.out.println("9. Logout");
        System.out.println("0. Sair");
        System.out.print("\nEscolha uma opção: ");
        String opcao = scanner.nextLine();

        switch (opcao) {
            case "1":
                menuRecursos();
                return false;
            case "2":
                menuProjetos();
                return false;
            case "3":
                menuMovimentacoes();
                return false;
            case "4":
                menuRelatorios();
                return false;
            case "5":
                if (usuarioServico.verificarPermissao("ADMIN")) {
                    menuUsuarios();
                } else {
                    System.out.println("Acesso negado!");
                    pausar();
                }
                return false;
            case "9":
                usuarioServico.logout();
                System.out.println("Logout realizado com sucesso!");
                pausar();
                return false;
            case "0":
                return true;
            default:
                System.out.println("Opção inválida!");
                pausar();
                return false;
        }
    }

    // ==================== MENU RECURSOS ====================
    private void menuRecursos() {
        limparTela();
        System.out.println("=== GESTÃO DE RECURSOS ===");
        System.out.println("1. Cadastrar Recurso");
        System.out.println("2. Listar Recursos");
        System.out.println("3. Buscar Recurso");
        System.out.println("4. Recursos com Estoque Baixo");
        System.out.println("5. Atualizar Estoque");
        System.out.println("6. Excluir Recurso");
        System.out.println("0. Voltar");
        System.out.println("\n💡 Dica: Digite *VOLTAR* a qualquer momento para cancelar e voltar ao menu");
        System.out.print("\nEscolha uma opção: ");
        String opcao = scanner.nextLine();

        switch (opcao) {
            case "1":
                cadastrarRecurso();
                break;
            case "2":
                listarRecursos();
                break;
            case "3":
                buscarRecurso();
                break;
            case "4":
                listarRecursosEstoqueBaixo();
                break;
            case "5":
                atualizarEstoque();
                break;
            case "6":
                excluirRecurso();
                break;
            case "0":
                return;
            default:
                System.out.println("Opção inválida!");
                pausar();
        }
    }

    private void cadastrarRecurso() {
        limparTela();
        System.out.println("=== CADASTRAR RECURSO ===");
        System.out.println("💡 Digite *VOLTAR* para cancelar a operação\n");
        
        String nome = lerEntradaComVoltar("Nome: ");
        if (nome == null) return;
        
        String descricao = lerEntradaComVoltar("Descrição: ");
        if (descricao == null) return;
        
        System.out.println("\nCategorias disponíveis:");
        System.out.println("1. Banner | 2. Placa | 3. Adesivo | 4. Impressão Digital");
        System.out.println("5. Sinalização | 6. Material Gráfico | 7. Outro");
        String categoriaInput = lerEntradaComVoltar("Escolha a categoria (ou digite personalizada): ");
        if (categoriaInput == null) return;
        
        String categoria;
        switch (categoriaInput) {
            case "1": categoria = "Banner"; break;
            case "2": categoria = "Placa"; break;
            case "3": categoria = "Adesivo"; break;
            case "4": categoria = "Impressão Digital"; break;
            case "5": categoria = "Sinalização"; break;
            case "6": categoria = "Material Gráfico"; break;
            case "7": categoria = "Outro"; break;
            default: categoria = categoriaInput; break;
        }
        
        String custoStr = lerEntradaComVoltar("Custo unitário (R$): ");
        if (custoStr == null) return;
        double custo = Double.parseDouble(custoStr);
        
        String qtdDispStr = lerEntradaComVoltar("Quantidade disponível: ");
        if (qtdDispStr == null) return;
        int quantidadeDisponivel = Integer.parseInt(qtdDispStr);
        
        String qtdMinStr = lerEntradaComVoltar("Quantidade mínima: ");
        if (qtdMinStr == null) return;
        int quantidadeMinima = Integer.parseInt(qtdMinStr);
        
        String unidadeMedida = lerEntradaComVoltar("Unidade de medida: ");
        if (unidadeMedida == null) return;

        if (recursoServico.cadastrarRecurso(nome, descricao, categoria, custo, quantidadeDisponivel, quantidadeMinima, unidadeMedida)) {
            System.out.println("\nRecurso cadastrado com sucesso!");
        } else {
            System.out.println("\nErro ao cadastrar recurso!");
        }
        pausar();
    }

    private void listarRecursos() {
        limparTela();
        System.out.println("=== LISTA DE RECURSOS ===");
        
        List<Recurso> recursos = recursoServico.listarTodosRecursos();
        
        if (recursos.isEmpty()) {
            System.out.println("Nenhum recurso cadastrado.");
        } else {
            System.out.printf("%-10s %-30s %-20s %-10s %-10s%n", "ID", "Nome", "Categoria", "Qtd Disp.", "Qtd Min.");
            System.out.println("=".repeat(80));
            
            for (Recurso recurso : recursos) {
                System.out.printf("%-10s %-30s %-20s %-10d %-10d%n",
                    recurso.getId().substring(0, Math.min(8, recurso.getId().length())),
                    recurso.getNome().substring(0, Math.min(28, recurso.getNome().length())),
                    recurso.getCategoria(),
                    recurso.getQuantidadeDisponivel(),
                    recurso.getQuantidadeMinima());
            }
        }
        pausar();
    }

    private void buscarRecurso() {
        limparTela();
        System.out.println("=== BUSCAR RECURSO ===");
        System.out.print("Digite o ID do recurso: ");
        String id = scanner.nextLine();
        
        Recurso recurso = recursoServico.buscarRecursoPorId(id);
        if (recurso != null) {
            exibirDetalhesRecurso(recurso);
        } else {
            System.out.println("Recurso não encontrado!");
        }
        pausar();
    }

    private void exibirDetalhesRecurso(Recurso recurso) {
        System.out.println("\n=== DETALHES DO RECURSO ===");
        System.out.println("ID: " + recurso.getId());
        System.out.println("Nome: " + recurso.getNome());
        System.out.println("Descrição: " + recurso.getDescricao());
        System.out.println("Categoria: " + recurso.getCategoria());
        System.out.println("Custo Unitário: R$ " + String.format("%.2f", recurso.getCustoUnitario()));
        System.out.println("Quantidade Disponível: " + recurso.getQuantidadeDisponivel());
        System.out.println("Quantidade Mínima: " + recurso.getQuantidadeMinima());
        System.out.println("Unidade de Medida: " + recurso.getUnidadeMedida());
    }

    private void listarRecursosEstoqueBaixo() {
        limparTela();
        System.out.println("=== RECURSOS COM ESTOQUE BAIXO ===");
        
        List<Recurso> recursos = recursoServico.listarRecursosComEstoqueBaixo();
        
        if (recursos.isEmpty()) {
            System.out.println("Todos os recursos estão com estoque adequado!");
        } else {
            System.out.printf("%-30s %-20s %-10s %-10s%n", "Nome", "Categoria", "Qtd Atual", "Qtd Min.");
            System.out.println("=".repeat(70));
            
            for (Recurso recurso : recursos) {
                System.out.printf("%-30s %-20s %-10d %-10d%n",
                    recurso.getNome().substring(0, Math.min(28, recurso.getNome().length())),
                    recurso.getCategoria(),
                    recurso.getQuantidadeDisponivel(),
                    recurso.getQuantidadeMinima());
            }
        }
        pausar();
    }

    private void atualizarEstoque() {
        limparTela();
        System.out.println("=== ATUALIZAR ESTOQUE ===");
        System.out.print("Digite o ID do recurso: ");
        String id = scanner.nextLine();
        
        Recurso recurso = recursoServico.buscarRecursoPorId(id);
        if (recurso != null) {
            System.out.println("Recurso: " + recurso.getNome());
            System.out.println("Quantidade atual: " + recurso.getQuantidadeDisponivel());
            System.out.print("Nova quantidade: ");
            int novaQuantidade = Integer.parseInt(scanner.nextLine());
            
            if (recursoServico.atualizarEstoque(id, novaQuantidade)) {
                System.out.println("Estoque atualizado com sucesso!");
            } else {
                System.out.println("Erro ao atualizar estoque!");
            }
        } else {
            System.out.println("Recurso não encontrado!");
        }
        pausar();
    }

    private void excluirRecurso() {
        limparTela();
        System.out.println("=== EXCLUIR RECURSO ===");
        System.out.println("💡 Digite *VOLTAR* para cancelar a operação\n");
        
        // Mostrar recursos disponíveis
        List<Recurso> recursos = recursoServico.listarTodosRecursos();
        if (recursos.isEmpty()) {
            System.out.println("Nenhum recurso cadastrado!");
            pausar();
            return;
        }
        
        System.out.println("=== RECURSOS CADASTRADOS ===");
        for (Recurso r : recursos) {
            System.out.println("ID: " + r.getId() + " - " + r.getNome() + " (Estoque: " + r.getQuantidadeDisponivel() + ")");
        }
        
        String idInput = lerEntradaComVoltar("\nID do recurso a excluir: ");
        if (idInput == null) return; // Usuário digitou *VOLTAR*
        
        Recurso recurso = buscarRecursoFlexivel(idInput);
        if (recurso == null) {
            System.out.println("Recurso não encontrado!");
            pausar();
            return;
        }
        
        System.out.println("\nRecurso selecionado: " + recurso.getNome());
        System.out.println("Descrição: " + recurso.getDescricao());
        System.out.println("Estoque atual: " + recurso.getQuantidadeDisponivel());
        
        String confirmacao = lerEntradaComVoltar("\n⚠️ ATENÇÃO: Esta operação não pode ser desfeita!\nDigite 'CONFIRMAR' para excluir o recurso: ");
        if (confirmacao == null) return; // Usuário digitou *VOLTAR*
        
        if (!"CONFIRMAR".equalsIgnoreCase(confirmacao)) {
            System.out.println("Exclusão cancelada.");
            pausar();
            return;
        }
        
        if (recursoServico.excluirRecurso(recurso.getId())) {
            System.out.println("\nRecurso excluído com sucesso!");
        } else {
            System.out.println("\nErro ao excluir recurso!");
        }
        pausar();
    }

    // ==================== MENU PROJETOS ====================
    private void menuProjetos() {
        limparTela();
        System.out.println("=== GESTÃO DE PROJETOS ===");
        System.out.println("1. Cadastrar Projeto");
        System.out.println("2. Listar Projetos");
        System.out.println("3. Buscar Projeto");
        System.out.println("4. Projetos por Status");
        System.out.println("5. Projetos por Cliente");
        System.out.println("6. Excluir Projeto");
        System.out.println("0. Voltar");
        System.out.println("\n💡 Dica: Digite *VOLTAR* a qualquer momento para cancelar e voltar ao menu");
        System.out.print("\nEscolha uma opção: ");
        String opcao = scanner.nextLine();

        switch (opcao) {
            case "1":
                cadastrarProjeto();
                break;
            case "2":
                listarProjetos();
                break;
            case "3":
                buscarProjeto();
                break;
            case "4":
                listarProjetosPorStatus();
                break;
            case "5":
                buscarProjetosPorCliente();
                break;
            case "6":
                excluirProjeto();
                break;
            case "0":
                return;
            default:
                System.out.println("Opção inválida!");
                pausar();
        }
    }

    private void cadastrarProjeto() {
        limparTela();
        System.out.println("=== CADASTRAR PROJETO ===");
        
        System.out.print("Nome do projeto: ");
        String nome = scanner.nextLine();
        
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();
        
        System.out.print("Cliente: ");
        String cliente = scanner.nextLine();
        
        System.out.print("Data de início (dd/MM/yyyy): ");
        Date dataInicio = lerData();
        
        System.out.print("Data prazo (dd/MM/yyyy) [Enter para pular]: ");
        String dataPrazoStr = scanner.nextLine();
        Date dataPrazo = null;
        if (!dataPrazoStr.trim().isEmpty()) {
            try {
                dataPrazo = dateFormat.parse(dataPrazoStr);
            } catch (ParseException e) {
                System.out.println("Data inválida, continuando sem data prazo.");
            }
        }
        
        System.out.print("Responsável: ");
        String responsavel = scanner.nextLine();
        
        System.out.println("\nStatus disponíveis:");
        System.out.println("1. Planejamento | 2. Em Andamento | 3. Finalizado | 4. Cancelado");
        System.out.print("Escolha o status: ");
        String statusInput = scanner.nextLine();
        
        String status;
        switch (statusInput) {
            case "1": status = "Planejamento"; break;
            case "2": status = "Em Andamento"; break;
            case "3": status = "Finalizado"; break;
            case "4": status = "Cancelado"; break;
            default: status = "Planejamento"; break;
        }
        
        System.out.print("Orçamento (R$): ");
        double orcamento = Double.parseDouble(scanner.nextLine());
        
        System.out.println("\nCategorias de projeto:");
        System.out.println("1. Identidade Visual | 2. Banner | 3. Sinalização | 4. Digital | 5. Outro");
        System.out.print("Escolha a categoria: ");
        String categoriaInput = scanner.nextLine();
        
        String categoria;
        switch (categoriaInput) {
            case "1": categoria = "Identidade Visual"; break;
            case "2": categoria = "Banner"; break;
            case "3": categoria = "Sinalização"; break;
            case "4": categoria = "Digital"; break;
            case "5": categoria = "Outro"; break;
            default: categoria = "Outro"; break;
        }

        if (projetoServico.cadastrarProjeto(nome, descricao, cliente, dataInicio, dataPrazo, responsavel, status, orcamento, categoria)) {
            System.out.println("\nProjeto cadastrado com sucesso!");
        } else {
            System.out.println("\nErro ao cadastrar projeto!");
        }
        pausar();
    }

    private void listarProjetos() {
        limparTela();
        System.out.println("=== LISTA DE PROJETOS ===");
        
        List<Projeto> projetos = projetoServico.listarTodosProjetos();
        
        if (projetos.isEmpty()) {
            System.out.println("Nenhum projeto cadastrado.");
        } else {
            System.out.printf("%-30s %-20s %-15s %-12s%n", "Nome", "Cliente", "Status", "Orçamento");
            System.out.println("=".repeat(77));
            
            for (Projeto projeto : projetos) {
                System.out.printf("%-30s %-20s %-15s R$ %8.2f%n",
                    projeto.getNome().substring(0, Math.min(28, projeto.getNome().length())),
                    projeto.getCliente().substring(0, Math.min(18, projeto.getCliente().length())),
                    projeto.getStatus(),
                    projeto.getOrcamento());
            }
        }
        pausar();
    }

    private void buscarProjeto() {
        limparTela();
        System.out.println("=== BUSCAR PROJETO ===");
        System.out.print("Digite o ID do projeto: ");
        String id = scanner.nextLine();
        
        Projeto projeto = projetoServico.buscarProjetoPorId(id);
        if (projeto != null) {
            exibirDetalhesProjeto(projeto);
        } else {
            System.out.println("Projeto não encontrado!");
        }
        pausar();
    }

    private void exibirDetalhesProjeto(Projeto projeto) {
        System.out.println("\n=== DETALHES DO PROJETO ===");
        System.out.println("ID: " + projeto.getId());
        System.out.println("Nome: " + projeto.getNome());
        System.out.println("Descrição: " + projeto.getDescricao());
        System.out.println("Cliente: " + projeto.getCliente());
        System.out.println("Data Início: " + (projeto.getDataInicio() != null ? dateFormat.format(projeto.getDataInicio()) : "N/A"));
        System.out.println("Data Prazo: " + (projeto.getDataPrazo() != null ? dateFormat.format(projeto.getDataPrazo()) : "N/A"));
        System.out.println("Responsável: " + projeto.getResponsavel());
        System.out.println("Status: " + projeto.getStatus());
        System.out.println("Orçamento: R$ " + String.format("%.2f", projeto.getOrcamento()));
        System.out.println("Categoria: " + projeto.getCategoriaProjeto());
    }

    private void listarProjetosPorStatus() {
        limparTela();
        System.out.println("=== PROJETOS POR STATUS ===");
        System.out.println("1. Planejamento | 2. Em Andamento | 3. Finalizado | 4. Cancelado");
        System.out.print("Escolha o status: ");
        String statusInput = scanner.nextLine();
        
        String status;
        switch (statusInput) {
            case "1": status = "Planejamento"; break;
            case "2": status = "Em Andamento"; break;
            case "3": status = "Finalizado"; break;
            case "4": status = "Cancelado"; break;
            default: 
                System.out.println("Status inválido!");
                pausar();
                return;
        }
        
        List<Projeto> projetos = projetoServico.listarProjetosPorStatus(status);
        
        if (projetos.isEmpty()) {
            System.out.println("Nenhum projeto encontrado com o status: " + status);
        } else {
            System.out.printf("%-30s %-20s %-12s%n", "Nome", "Cliente", "Orçamento");
            System.out.println("=".repeat(62));
            
            for (Projeto projeto : projetos) {
                System.out.printf("%-30s %-20s R$ %8.2f%n",
                    projeto.getNome().substring(0, Math.min(28, projeto.getNome().length())),
                    projeto.getCliente().substring(0, Math.min(18, projeto.getCliente().length())),
                    projeto.getOrcamento());
            }
        }
        pausar();
    }

    private void buscarProjetosPorCliente() {
        limparTela();
        System.out.println("=== BUSCAR PROJETOS POR CLIENTE ===");
        System.out.print("Digite o nome do cliente: ");
        String cliente = scanner.nextLine();
        
        List<Projeto> projetos = projetoServico.buscarProjetosPorCliente(cliente);
        
        if (projetos.isEmpty()) {
            System.out.println("Nenhum projeto encontrado para o cliente: " + cliente);
        } else {
            System.out.printf("%-30s %-15s %-12s%n", "Nome", "Status", "Orçamento");
            System.out.println("=".repeat(57));
            
            for (Projeto projeto : projetos) {
                System.out.printf("%-30s %-15s R$ %8.2f%n",
                    projeto.getNome().substring(0, Math.min(28, projeto.getNome().length())),
                    projeto.getStatus(),
                    projeto.getOrcamento());
            }
        }
        pausar();
    }

    // ==================== MENU MOVIMENTAÇÕES ====================
    private void menuMovimentacoes() {
        limparTela();
        System.out.println("=== MOVIMENTAÇÕES ===");
        System.out.println("1. Registrar Entrada");
        System.out.println("2. Registrar Saída");
        System.out.println("3. Listar Movimentações");
        System.out.println("4. Movimentações por Projeto");
        System.out.println("0. Voltar");
        System.out.print("\nEscolha uma opção: ");
        String opcao = scanner.nextLine();

        switch (opcao) {
            case "1":
                registrarEntrada();
                break;
            case "2":
                registrarSaida();
                break;
            case "3":
                listarMovimentacoes();
                break;
            case "4":
                listarMovimentacoesPorProjeto();
                break;
            case "0":
                return;
            default:
                System.out.println("Opção inválida!");
                pausar();
        }
    }

    private void registrarEntrada() {
        limparTela();
        System.out.println("=== REGISTRAR ENTRADA ===");
        
        // Primeiro mostrar recursos disponíveis
        System.out.println("\n=== RECURSOS DISPONÍVEIS ===");
        List<Recurso> recursos = recursoServico.listarTodosRecursos();
        if (recursos.isEmpty()) {
            System.out.println("Nenhum recurso cadastrado!");
            pausar();
            return;
        }
        
        for (Recurso r : recursos) {
            System.out.println("ID: " + r.getId() + " - " + r.getNome() + " (Estoque: " + r.getQuantidadeDisponivel() + ")");
        }
        
        System.out.print("\nID do recurso (9 dígitos completos ou apenas os últimos dígitos): ");
        String recursoIdInput = scanner.nextLine().trim();
        
        // Buscar recurso permitindo ID completo ou simplificado
        Recurso recurso = buscarRecursoFlexivel(recursoIdInput);
        if (recurso == null) {
            System.out.println("Recurso não encontrado! Verifique o ID informado.");
            pausar();
            return;
        }
        
        System.out.println("Recurso selecionado: " + recurso.getNome());
        System.out.print("Quantidade de entrada: ");
        int quantidade = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Responsável: ");
        String responsavel = scanner.nextLine();
        
        System.out.print("Observação [Enter para pular]: ");
        String observacao = scanner.nextLine();

        if (movimentacaoServico.registrarEntrada(recurso.getId(), quantidade, responsavel, observacao)) {
            System.out.println("\nEntrada registrada com sucesso!");
        } else {
            System.out.println("\nErro ao registrar entrada!");
        }
        pausar();
    }

    private void registrarSaida() {
        limparTela();
        System.out.println("=== REGISTRAR SAÍDA ===");
        
        // Primeiro mostrar recursos disponíveis
        System.out.println("\n=== RECURSOS DISPONÍVEIS ===");
        List<Recurso> recursos = recursoServico.listarTodosRecursos();
        if (recursos.isEmpty()) {
            System.out.println("Nenhum recurso cadastrado!");
            pausar();
            return;
        }
        
        for (Recurso r : recursos) {
            System.out.println("ID: " + r.getId() + " - " + r.getNome() + " (Estoque: " + r.getQuantidadeDisponivel() + ")");
        }
        
        System.out.print("\nID do recurso (9 dígitos completos ou apenas os últimos dígitos): ");
        String recursoIdInput = scanner.nextLine().trim();
        
        Recurso recurso = buscarRecursoFlexivel(recursoIdInput);
        if (recurso == null) {
            System.out.println("Recurso não encontrado! Verifique o ID informado.");
            pausar();
            return;
        }
        
        System.out.println("Recurso selecionado: " + recurso.getNome());
        System.out.println("Quantidade disponível: " + recurso.getQuantidadeDisponivel());
        
        // Mostrar projetos disponíveis
        System.out.println("\n=== PROJETOS DISPONÍVEIS ===");
        List<Projeto> projetos = projetoServico.listarTodosProjetos();
        if (projetos.isEmpty()) {
            System.out.println("Nenhum projeto cadastrado!");
            pausar();
            return;
        }
        
        for (Projeto p : projetos) {
            System.out.println("ID: " + p.getId() + " - " + p.getNome() + " (" + p.getStatus() + ")");
        }
        
        System.out.print("\nID do projeto (9 dígitos completos ou apenas os últimos dígitos): ");
        String projetoIdInput = scanner.nextLine().trim();
        
        Projeto projeto = buscarProjetoFlexivel(projetoIdInput);
        if (projeto == null) {
            System.out.println("Projeto não encontrado! Verifique o ID informado.");
            pausar();
            return;
        }
        
        System.out.println("Projeto selecionado: " + projeto.getNome());
        System.out.print("Quantidade de saída: ");
        int quantidade = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Responsável: ");
        String responsavel = scanner.nextLine();
        
        System.out.print("Observação [Enter para pular]: ");
        String observacao = scanner.nextLine();

        if (movimentacaoServico.registrarSaida(recurso.getId(), projeto.getId(), quantidade, responsavel, observacao)) {
            System.out.println("\nSaída registrada com sucesso!");
        } else {
            System.out.println("\nErro ao registrar saída!");
        }
        pausar();
    }

    private void listarMovimentacoes() {
        limparTela();
        System.out.println("=== LISTA DE MOVIMENTAÇÕES ===");
        
        List<Movimentacao> movimentacoes = movimentacaoServico.listarTodasMovimentacoes();
        
        if (movimentacoes.isEmpty()) {
            System.out.println("Nenhuma movimentação registrada.");
        } else {
            System.out.printf("%-15s %-30s %-8s %-10s %-15s%n", "Data", "Recurso", "Tipo", "Qtd", "Responsável");
            System.out.println("=".repeat(78));
            
            for (Movimentacao mov : movimentacoes) {
                System.out.printf("%-15s %-30s %-8s %-10d %-15s%n",
                    dateFormat.format(mov.getData()),
                    mov.getRecurso().getNome().substring(0, Math.min(28, mov.getRecurso().getNome().length())),
                    mov.getTipo(),
                    mov.getQuantidade(),
                    mov.getResponsavel().substring(0, Math.min(13, mov.getResponsavel().length())));
            }
        }
        pausar();
    }

    private void listarMovimentacoesPorProjeto() {
        limparTela();
        System.out.println("=== MOVIMENTAÇÕES POR PROJETO ===");
        System.out.print("Digite o ID do projeto: ");
        String projetoId = scanner.nextLine();
        
        Projeto projeto = projetoServico.buscarProjetoPorId(projetoId);
        if (projeto == null) {
            System.out.println("Projeto não encontrado!");
            pausar();
            return;
        }
        
        System.out.println("Projeto: " + projeto.getNome());
        System.out.println();
        
        List<Movimentacao> movimentacoes = movimentacaoServico.buscarMovimentacoesPorProjeto(projetoId);
        
        if (movimentacoes.isEmpty()) {
            System.out.println("Nenhuma movimentação encontrada para este projeto.");
        } else {
            System.out.printf("%-15s %-30s %-8s %-10s%n", "Data", "Recurso", "Tipo", "Qtd");
            System.out.println("=".repeat(63));
            
            for (Movimentacao mov : movimentacoes) {
                System.out.printf("%-15s %-30s %-8s %-10d%n",
                    dateFormat.format(mov.getData()),
                    mov.getRecurso().getNome().substring(0, Math.min(28, mov.getRecurso().getNome().length())),
                    mov.getTipo(),
                    mov.getQuantidade());
            }
        }
        pausar();
    }

    // ==================== MENU RELATÓRIOS ====================
    private void menuRelatorios() {
        limparTela();
        System.out.println("=== RELATÓRIOS ===");
        System.out.println("1. Relatório de Recursos");
        System.out.println("2. Relatório de Projetos");
        System.out.println("3. Recursos com Estoque Baixo");
        System.out.println("4. Projetos por Status");
        System.out.println("5. Resumo Geral");
        System.out.println("0. Voltar");
        System.out.print("\nEscolha uma opção: ");
        String opcao = scanner.nextLine();

        switch (opcao) {
            case "1":
                relatorioRecursos();
                break;
            case "2":
                relatorioProjetos();
                break;
            case "3":
                listarRecursosEstoqueBaixo();
                break;
            case "4":
                relatorioProjetosPorStatus();
                break;
            case "5":
                resumoGeral();
                break;
            case "0":
                return;
            default:
                System.out.println("Opção inválida!");
                pausar();
        }
    }

    private void relatorioRecursos() {
        limparTela();
        System.out.println("=== RELATÓRIO DE RECURSOS ===");
        
        List<Recurso> recursos = recursoServico.listarTodosRecursos();
        
        if (recursos.isEmpty()) {
            System.out.println("Nenhum recurso cadastrado.");
        } else {
            double valorTotalEstoque = 0;
            int totalRecursos = recursos.size();
            
            System.out.printf("%-30s %-20s %-10s %-12s%n", "Nome", "Categoria", "Qtd", "Valor Est.");
            System.out.println("=".repeat(72));
            
            for (Recurso recurso : recursos) {
                double valorEstoque = recurso.getQuantidadeDisponivel() * recurso.getCustoUnitario();
                valorTotalEstoque += valorEstoque;
                
                System.out.printf("%-30s %-20s %-10d R$ %8.2f%n",
                    recurso.getNome().substring(0, Math.min(28, recurso.getNome().length())),
                    recurso.getCategoria(),
                    recurso.getQuantidadeDisponivel(),
                    valorEstoque);
            }
            
            System.out.println("=".repeat(72));
            System.out.println("Total de recursos: " + totalRecursos);
            System.out.println("Valor total do estoque: R$ " + String.format("%.2f", valorTotalEstoque));
        }
        pausar();
    }

    private void relatorioProjetos() {
        limparTela();
        System.out.println("=== RELATÓRIO DE PROJETOS ===");
        
        List<Projeto> projetos = projetoServico.listarTodosProjetos();
        
        if (projetos.isEmpty()) {
            System.out.println("Nenhum projeto cadastrado.");
        } else {
            double valorTotalProjetos = 0;
            int totalProjetos = projetos.size();
            
            // Contadores por status
            int planejamento = 0, emAndamento = 0, finalizado = 0, cancelado = 0;
            
            for (Projeto projeto : projetos) {
                valorTotalProjetos += projeto.getOrcamento();
                
                switch (projeto.getStatus()) {
                    case "Planejamento": planejamento++; break;
                    case "Em Andamento": emAndamento++; break;
                    case "Finalizado": finalizado++; break;
                    case "Cancelado": cancelado++; break;
                }
            }
            
            System.out.println("=== RESUMO DOS PROJETOS ===");
            System.out.println("Total de projetos: " + totalProjetos);
            System.out.println("Valor total dos orçamentos: R$ " + String.format("%.2f", valorTotalProjetos));
            System.out.println();
            System.out.println("=== PROJETOS POR STATUS ===");
            System.out.println("Planejamento: " + planejamento);
            System.out.println("Em Andamento: " + emAndamento);
            System.out.println("Finalizado: " + finalizado);
            System.out.println("Cancelado: " + cancelado);
        }
        pausar();
    }

    private void relatorioProjetosPorStatus() {
        limparTela();
        System.out.println("=== RELATÓRIO: PROJETOS POR STATUS ===");
        
        String[] statusList = {"Planejamento", "Em Andamento", "Finalizado", "Cancelado"};
        
        for (String status : statusList) {
            List<Projeto> projetos = projetoServico.listarProjetosPorStatus(status);
            System.out.println("\n=== " + status.toUpperCase() + " ===");
            System.out.println("Quantidade: " + projetos.size());
            
            if (!projetos.isEmpty()) {
                double valorTotal = projetos.stream().mapToDouble(Projeto::getOrcamento).sum();
                System.out.println("Valor total: R$ " + String.format("%.2f", valorTotal));
            }
        }
        pausar();
    }

    private void resumoGeral() {
        limparTela();
        System.out.println("=== RESUMO GERAL DO SISTEMA ===");
        
        // Recursos
        List<Recurso> recursos = recursoServico.listarTodosRecursos();
        List<Recurso> recursosEstoqueBaixo = recursoServico.listarRecursosComEstoqueBaixo();
        
        // Projetos
        List<Projeto> projetos = projetoServico.listarTodosProjetos();
        List<Projeto> projetosEmAndamento = projetoServico.listarProjetosPorStatus("Em Andamento");
        
        // Movimentações
        List<Movimentacao> movimentacoes = movimentacaoServico.listarTodasMovimentacoes();
        
        System.out.println("=== RECURSOS ===");
        System.out.println("Total de recursos: " + recursos.size());
        System.out.println("Recursos com estoque baixo: " + recursosEstoqueBaixo.size());
        
        double valorTotalEstoque = recursos.stream()
            .mapToDouble(r -> r.getQuantidadeDisponivel() * r.getCustoUnitario())
            .sum();
        System.out.println("Valor total do estoque: R$ " + String.format("%.2f", valorTotalEstoque));
        
        System.out.println("\n=== PROJETOS ===");
        System.out.println("Total de projetos: " + projetos.size());
        System.out.println("Projetos em andamento: " + projetosEmAndamento.size());
        
        double valorTotalProjetos = projetos.stream().mapToDouble(Projeto::getOrcamento).sum();
        System.out.println("Valor total dos orçamentos: R$ " + String.format("%.2f", valorTotalProjetos));
        
        System.out.println("\n=== MOVIMENTAÇÕES ===");
        System.out.println("Total de movimentações: " + movimentacoes.size());
        
        long entradas = movimentacoes.stream().filter(m -> "ENTRADA".equals(m.getTipo())).count();
        long saidas = movimentacoes.stream().filter(m -> "SAIDA".equals(m.getTipo())).count();
        
        System.out.println("Entradas: " + entradas);
        System.out.println("Saídas: " + saidas);
        
        pausar();
    }

    // ==================== MENU USUÁRIOS ====================
    private void menuUsuarios() {
        limparTela();
        System.out.println("=== GESTÃO DE USUÁRIOS ===");
        System.out.println("1. Cadastrar Usuário");
        System.out.println("2. Listar Usuários");
        System.out.println("0. Voltar");
        System.out.print("\nEscolha uma opção: ");
        String opcao = scanner.nextLine();

        switch (opcao) {
            case "1":
                cadastrarUsuario();
                break;
            case "2":
                listarUsuarios();
                break;
            case "0":
                return;
            default:
                System.out.println("Opção inválida!");
                pausar();
        }
    }

    private void cadastrarUsuario() {
        limparTela();
        System.out.println("=== CADASTRAR USUÁRIO ===");
        
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("Login: ");
        String login = scanner.nextLine();
        
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        
        System.out.println("\nNíveis de acesso:");
        System.out.println("1. ADMIN | 2. GESTOR | 3. OPERADOR");
        System.out.print("Escolha o nível: ");
        String nivelInput = scanner.nextLine();
        
        String nivel;
        switch (nivelInput) {
            case "1": nivel = "ADMIN"; break;
            case "2": nivel = "GESTOR"; break;
            case "3": nivel = "OPERADOR"; break;
            default: nivel = "OPERADOR"; break;
        }

        if (usuarioServico.cadastrarUsuario(nome, login, senha, nivel)) {
            System.out.println("\nUsuário cadastrado com sucesso!");
        } else {
            System.out.println("\nErro ao cadastrar usuário!");
        }
        pausar();
    }

    private void listarUsuarios() {
        limparTela();
        System.out.println("=== LISTA DE USUÁRIOS ===");
        
        List<Usuario> usuarios = usuarioServico.listarTodosUsuarios();
        
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
        } else {
            System.out.printf("%-30s %-20s %-10s%n", "Nome", "Login", "Nível");
            System.out.println("=".repeat(60));
            
            for (Usuario usuario : usuarios) {
                System.out.printf("%-30s %-20s %-10s%n",
                    usuario.getNome().substring(0, Math.min(28, usuario.getNome().length())),
                    usuario.getLogin(),
                    usuario.getNivel());
            }
        }
        pausar();
    }

    // ==================== MÉTODOS AUXILIARES ====================
    private boolean verificarVoltar(String input) {
        return input != null && input.trim().equalsIgnoreCase("*VOLTAR*");
    }

    private String lerEntradaComVoltar(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        if (verificarVoltar(input)) {
            System.out.println("Operação cancelada. Voltando ao menu...");
            pausar();
            return null;
        }
        return input;
    }

    // ==================== UTILITÁRIOS ====================
    private Date lerData() {
        while (true) {
            try {
                String dataStr = scanner.nextLine();
                return dateFormat.parse(dataStr);
            } catch (ParseException e) {
                System.out.print("Data inválida! Digite novamente (dd/MM/yyyy): ");
            }
        }
    }

    private void limparTela() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private void pausar() {
        System.out.print("\nPressione Enter para continuar...");
        scanner.nextLine();
    }

    private Recurso buscarRecursoFlexivel(String idInput) {
        // Se o usuário digitou um ID de 9 dígitos, buscar diretamente
        if (idInput.length() == 9) {
            return recursoServico.buscarRecursoPorId(idInput);
        }
        
        // Se o usuário digitou menos dígitos, tentar encontrar um recurso que termine com esses dígitos
        try {
            int numeroDigitado = Integer.parseInt(idInput);
            String idFormatado = String.format("%09d", numeroDigitado);
            Recurso recurso = recursoServico.buscarRecursoPorId(idFormatado);
            
            if (recurso != null) {
                return recurso;
            }
            
            // Se não encontrou, buscar em todos os recursos
            List<Recurso> todosRecursos = recursoServico.listarTodosRecursos();
            for (Recurso r : todosRecursos) {
                if (r.getId().endsWith(idInput)) {
                    return r;
                }
            }
        } catch (NumberFormatException e) {
            // Se não for um número, buscar diretamente pelo texto
            return recursoServico.buscarRecursoPorId(idInput);
        }
        
        return null;
    }

    private Projeto buscarProjetoFlexivel(String idInput) {
        // Se o usuário digitou um ID de 9 dígitos, buscar diretamente
        if (idInput.length() == 9) {
            return projetoServico.buscarProjetoPorId(idInput);
        }
        
        // Se o usuário digitou menos dígitos, tentar encontrar um projeto que termine com esses dígitos
        try {
            int numeroDigitado = Integer.parseInt(idInput);
            String idFormatado = String.format("%09d", numeroDigitado);
            Projeto projeto = projetoServico.buscarProjetoPorId(idFormatado);
            
            if (projeto != null) {
                return projeto;
            }
            
            // Se não encontrou, buscar em todos os projetos
            List<Projeto> todosProjetos = projetoServico.listarTodosProjetos();
            for (Projeto p : todosProjetos) {
                if (p.getId().endsWith(idInput)) {
                    return p;
                }
            }
        } catch (NumberFormatException e) {
            // Se não for um número, buscar diretamente pelo texto
            return projetoServico.buscarProjetoPorId(idInput);
        }
        
        return null;
    }

    private void excluirProjeto() {
        limparTela();
        System.out.println("=== EXCLUIR PROJETO ===");
        System.out.println("💡 Digite *VOLTAR* para cancelar a operação\n");
        
        // Mostrar projetos disponíveis
        List<Projeto> projetos = projetoServico.listarTodosProjetos();
        if (projetos.isEmpty()) {
            System.out.println("Nenhum projeto cadastrado!");
            pausar();
            return;
        }
        
        System.out.println("=== PROJETOS CADASTRADOS ===");
        for (Projeto p : projetos) {
            System.out.println("ID: " + p.getId() + " - " + p.getNome() + " (" + p.getStatus() + ") - Cliente: " + p.getCliente());
        }
        
        String idInput = lerEntradaComVoltar("\nID do projeto a excluir: ");
        if (idInput == null) return; // Usuário digitou *VOLTAR*
        
        Projeto projeto = buscarProjetoFlexivel(idInput);
        if (projeto == null) {
            System.out.println("Projeto não encontrado!");
            pausar();
            return;
        }
        
        System.out.println("\nProjeto selecionado: " + projeto.getNome());
        System.out.println("Cliente: " + projeto.getCliente());
        System.out.println("Status: " + projeto.getStatus());
        System.out.println("Descrição: " + projeto.getDescricao());
        
        String confirmacao = lerEntradaComVoltar("\n⚠️ ATENÇÃO: Esta operação não pode ser desfeita!\nDigite 'CONFIRMAR' para excluir o projeto: ");
        if (confirmacao == null) return; // Usuário digitou *VOLTAR*
        
        if (!"CONFIRMAR".equalsIgnoreCase(confirmacao)) {
            System.out.println("Exclusão cancelada.");
            pausar();
            return;
        }
        
        if (projetoServico.excluirProjeto(projeto.getId())) {
            System.out.println("\nProjeto excluído com sucesso!");
        } else {
            System.out.println("\nErro ao excluir projeto!");
        }
        pausar();
    }
}
