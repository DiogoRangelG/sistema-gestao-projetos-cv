package com.almoxarifado.ui;

import com.almoxarifado.dao.ConexaoDB;
import com.almoxarifado.modelo.*;
import com.almoxarifado.servico.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Terminal {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private final UsuarioServico usuarioServico;
    private final MaterialServico materialServico;
    private final ProjetoServico projetoServico;
    private final MovimentacaoServico movimentacaoServico;

    public Terminal() {
        this.usuarioServico = new UsuarioServico();
        this.materialServico = new MaterialServico();
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

        System.out.println("\nSistema de Almoxarifado encerrado. Até logo!");
        ConexaoDB.fecharConexao();
    }

    private boolean menuLogin() {
        limparTela();
        System.out.println("=== SISTEMA DE GERENCIAMENTO DE ALMOXARIFADO ===");
        System.out.println("1. Login");
        System.out.println("2. Sair");
        System.out.print("\nEscolha uma opção: ");
        String opcao = scanner.nextLine();

        switch (opcao) {
            case "1":
                fazerLogin();
                return false;
            case "2":
                return true;
            default:
                System.out.println("Opção inválida!");
                aguardarTecla();
                return false;
        }
    }

    private void fazerLogin() {
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
        aguardarTecla();
    }

    private boolean menuPrincipal() {
        limparTela();
        Usuario usuarioLogado = usuarioServico.getUsuarioLogado();

        System.out.println("=== SISTEMA DE GERENCIAMENTO DE ALMOXARIFADO ===");
        System.out.println("Usuário: " + usuarioLogado.getNome() + " | Nível: " + usuarioLogado.getNivel());
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Gerenciar Materiais");
        System.out.println("2. Gerenciar Estoque");
        System.out.println("3. Gerenciar Projetos");
        System.out.println("4. Relatórios");

        if (usuarioServico.verificarPermissao("ADMIN")) {
            System.out.println("5. Gerenciar Usuários");
        }

        System.out.println("9. Logout");
        System.out.println("0. Sair");
        System.out.print("\nEscolha uma opção: ");
        String opcao = scanner.nextLine();

        switch (opcao) {
            case "1":
                menuMateriais();
                return false;
            case "2":
                menuEstoque();
                return false;
            case "3":
                menuProjetos();
                return false;
            case "4":
                menuRelatorios();
                return false;
            case "5":
                if (usuarioServico.verificarPermissao("ADMIN")) {
                    menuUsuarios();
                } else {
                    System.out.println("Opção inválida!");
                    aguardarTecla();
                }
                return false;
            case "9":
                usuarioServico.logout();
                System.out.println("Logout realizado com sucesso!");
                aguardarTecla();
                return false;
            case "0":
                return true;
            default:
                System.out.println("Opção inválida!");
                aguardarTecla();
                return false;
        }
    }

    private void menuMateriais() {
        boolean voltar = false;

        while (!voltar) {
            limparTela();
            System.out.println("=== GERENCIAR MATERIAIS ===");
            System.out.println("1. Cadastrar Material");
            System.out.println("2. Listar Materiais");
            System.out.println("3. Buscar Material");
            System.out.println("4. Editar Material");
            System.out.println("5. Excluir Material");
            System.out.println("0. Voltar");
            System.out.print("\nEscolha uma opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    cadastrarMaterial();
                    break;
                case "2":
                    listarMateriais();
                    break;
                case "3":
                    buscarMaterial();
                    break;
                case "4":
                    editarMaterial();
                    break;
                case "5":
                    excluirMaterial();
                    break;
                case "0":
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
                    aguardarTecla();
                    break;
            }
        }
    }

    private void cadastrarMaterial() {
        limparTela();
        System.out.println("=== CADASTRAR MATERIAL ===");
        
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();
        
        System.out.print("Tipo: ");
        String tipo = scanner.nextLine();
        
        System.out.print("Preço (R$): ");
        double preco = lerDouble();
        
        System.out.print("Quantidade Atual: ");
        int quantidadeAtual = lerInteiro();
        
        System.out.print("Quantidade Mínima: ");
        int quantidadeMinima = lerInteiro();
        
        System.out.print("Unidade de Medida: ");
        String unidadeMedida = scanner.nextLine();
        
        boolean sucesso = materialServico.cadastrarMaterial(nome, descricao, tipo, preco, 
                                                        quantidadeAtual, quantidadeMinima, unidadeMedida);
        
        if (sucesso) {
            System.out.println("\nMaterial cadastrado com sucesso!");
        } else {
            System.out.println("\nErro ao cadastrar material!");
        }
        
        aguardarTecla();
    }

    private void listarMateriais() {
        limparTela();
        System.out.println("=== LISTAR MATERIAIS ===");
        
        List<Material> materiais = materialServico.listarTodosMateriais();
        
        if (materiais.isEmpty()) {
            System.out.println("Nenhum material cadastrado!");
        } else {
            System.out.println("ID | NOME | TIPO | QUANTIDADE | PREÇO (R$) | UNIDADE");
            System.out.println("-------------------------------------------------------");
            
            for (Material material : materiais) {
                System.out.printf("%s | %s | %s | %d | R$ %.2f | %s\n", 
                    material.getId().substring(0, 8), 
                    material.getNome(), 
                    material.getTipo(),
                    material.getQuantidadeAtual(),
                    material.getPreco(),
                    material.getUnidadeMedida());
            }
        }
        
        aguardarTecla();
    }

    private void buscarMaterial() {
        limparTela();
        System.out.println("=== BUSCAR MATERIAL ===");
        System.out.println("1. Por ID");
        System.out.println("2. Por Nome");
        System.out.println("3. Por Tipo");
        System.out.println("0. Voltar");
        System.out.print("\nEscolha uma opção: ");
        String opcao = scanner.nextLine();
        
        limparTela();
        List<Material> materiais = new ArrayList<>();
        
        switch (opcao) {
            case "1":
                System.out.print("Digite o ID do material: ");
                String id = scanner.nextLine();
                Material material = materialServico.buscarMaterialPorId(id);
                if (material != null) {
                    materiais.add(material);
                }
                break;
            case "2":
                System.out.print("Digite o nome do material: ");
                String nome = scanner.nextLine();
                materiais = materialServico.buscarMateriaisPorNome(nome);
                break;
            case "3":
                System.out.print("Digite o tipo do material: ");
                String tipo = scanner.nextLine();
                materiais = materialServico.listarMateriaisPorTipo(tipo);
                break;
            case "0":
                return;
            default:
                System.out.println("Opção inválida!");
                aguardarTecla();
                return;
        }
        
        if (materiais.isEmpty()) {
            System.out.println("Nenhum material encontrado!");
        } else {
            System.out.println("\n=== MATERIAIS ENCONTRADOS ===");
            System.out.println("ID | NOME | TIPO | QUANTIDADE | PREÇO (R$) | UNIDADE");
            System.out.println("-------------------------------------------------------");
            
            for (Material m : materiais) {
                System.out.printf("%s | %s | %s | %d | R$ %.2f | %s\n", 
                    m.getId().substring(0, 8), 
                    m.getNome(), 
                    m.getTipo(),
                    m.getQuantidadeAtual(),
                    m.getPreco(),
                    m.getUnidadeMedida());
            }
        }
        
        aguardarTecla();
    }

    private void editarMaterial() {
        limparTela();
        System.out.println("=== EDITAR MATERIAL ===");
        
        System.out.print("Digite o ID do material: ");
        String id = scanner.nextLine();
        
        Material material = materialServico.buscarMaterialPorId(id);
        
        if (material == null) {
            System.out.println("Material não encontrado!");
            aguardarTecla();
            return;
        }
        
        System.out.println("\nMaterial encontrado:");
        System.out.println("Nome: " + material.getNome());
        System.out.println("Descrição: " + material.getDescricao());
        System.out.println("Tipo: " + material.getTipo());
        System.out.println("Preço: R$ " + material.getPreco());
        System.out.println("Quantidade Atual: " + material.getQuantidadeAtual());
        System.out.println("Quantidade Mínima: " + material.getQuantidadeMinima());
        System.out.println("Unidade de Medida: " + material.getUnidadeMedida());
        
        System.out.println("\nPreencha os novos dados (deixe em branco para manter o atual):");
        
        System.out.print("Nome [" + material.getNome() + "]: ");
        String nome = scanner.nextLine();
        if (nome.isEmpty()) nome = material.getNome();
        
        System.out.print("Descrição [" + material.getDescricao() + "]: ");
        String descricao = scanner.nextLine();
        if (descricao.isEmpty()) descricao = material.getDescricao();
        
        System.out.print("Tipo [" + material.getTipo() + "]: ");
        String tipo = scanner.nextLine();
        if (tipo.isEmpty()) tipo = material.getTipo();
        
        System.out.print("Preço (R$) [" + material.getPreco() + "]: ");
        String precoStr = scanner.nextLine();
        double preco = precoStr.isEmpty() ? material.getPreco() : Double.parseDouble(precoStr);
        
        System.out.print("Quantidade Atual [" + material.getQuantidadeAtual() + "]: ");
        String qtdAtualStr = scanner.nextLine();
        int quantidadeAtual = qtdAtualStr.isEmpty() ? material.getQuantidadeAtual() : Integer.parseInt(qtdAtualStr);
        
        System.out.print("Quantidade Mínima [" + material.getQuantidadeMinima() + "]: ");
        String qtdMinStr = scanner.nextLine();
        int quantidadeMinima = qtdMinStr.isEmpty() ? material.getQuantidadeMinima() : Integer.parseInt(qtdMinStr);
        
        System.out.print("Unidade de Medida [" + material.getUnidadeMedida() + "]: ");
        String unidadeMedida = scanner.nextLine();
        if (unidadeMedida.isEmpty()) unidadeMedida = material.getUnidadeMedida();
        
        boolean sucesso = materialServico.atualizarMaterial(id, nome, descricao, tipo, preco, 
                                                       quantidadeAtual, quantidadeMinima, unidadeMedida);
        
        if (sucesso) {
            System.out.println("\nMaterial atualizado com sucesso!");
        } else {
            System.out.println("\nErro ao atualizar material!");
        }
        
        aguardarTecla();
    }

    private void excluirMaterial() {
        limparTela();
        System.out.println("=== EXCLUIR MATERIAL ===");
        
        System.out.print("Digite o ID do material: ");
        String id = scanner.nextLine();
        
        Material material = materialServico.buscarMaterialPorId(id);
        
        if (material == null) {
            System.out.println("Material não encontrado!");
            aguardarTecla();
            return;
        }
        
        System.out.println("\nMaterial encontrado:");
        System.out.println("Nome: " + material.getNome());
        System.out.println("Tipo: " + material.getTipo());
        System.out.println("Quantidade Atual: " + material.getQuantidadeAtual());
        
        System.out.print("\nConfirma a exclusão deste material? (S/N): ");
        String confirmacao = scanner.nextLine();
        
        if (confirmacao.equalsIgnoreCase("S")) {
            boolean sucesso = materialServico.excluirMaterial(id);
            
            if (sucesso) {
                System.out.println("\nMaterial excluído com sucesso!");
            } else {
                System.out.println("\nErro ao excluir material!");
            }
        } else {
            System.out.println("\nOperação cancelada!");
        }
        
        aguardarTecla();
    }

    private void menuEstoque() {
        boolean voltar = false;

        while (!voltar) {
            limparTela();
            System.out.println("=== GERENCIAR ESTOQUE ===");
            System.out.println("1. Registrar Entrada de Material");
            System.out.println("2. Registrar Saída de Material");
            System.out.println("3. Listar Movimentações");
            System.out.println("4. Listar Materiais com Estoque Baixo");
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
                    listarEstoqueBaixo();
                    break;
                case "0":
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
                    aguardarTecla();
                    break;
            }
        }
    }

    private void registrarEntrada() {
        limparTela();
        System.out.println("=== REGISTRAR ENTRADA DE MATERIAL ===");
        
        System.out.print("Digite o ID do material: ");
        String materialId = scanner.nextLine();
        
        Material material = materialServico.buscarMaterialPorId(materialId);
        
        if (material == null) {
            System.out.println("Material não encontrado!");
            aguardarTecla();
            return;
        }
        
        System.out.println("\nMaterial: " + material.getNome());
        System.out.println("Quantidade Atual: " + material.getQuantidadeAtual());
        
        System.out.print("\nQuantidade a adicionar: ");
        int quantidade = lerInteiro();
        
        if (quantidade <= 0) {
            System.out.println("Quantidade deve ser maior que zero!");
            aguardarTecla();
            return;
        }
        
        System.out.print("Observação: ");
        String observacao = scanner.nextLine();
        
        String responsavel = usuarioServico.getUsuarioLogado().getNome();
        
        boolean sucesso = movimentacaoServico.registrarEntrada(materialId, quantidade, responsavel, observacao);
        
        if (sucesso) {
            System.out.println("\nEntrada registrada com sucesso!");
            System.out.println("Nova quantidade: " + (material.getQuantidadeAtual() + quantidade));
        } else {
            System.out.println("\nErro ao registrar entrada!");
        }
        
        aguardarTecla();
    }

    private void registrarSaida() {
        limparTela();
        System.out.println("=== REGISTRAR SAÍDA DE MATERIAL ===");
        
        System.out.print("Digite o ID do material: ");
        String materialId = scanner.nextLine();
        
        Material material = materialServico.buscarMaterialPorId(materialId);
        
        if (material == null) {
            System.out.println("Material não encontrado!");
            aguardarTecla();
            return;
        }
        
        System.out.println("\nMaterial: " + material.getNome());
        System.out.println("Quantidade Atual: " + material.getQuantidadeAtual());
        
        if (material.getQuantidadeAtual() <= 0) {
            System.out.println("\nMaterial sem estoque disponível!");
            aguardarTecla();
            return;
        }
        
        System.out.print("\nQuantidade a retirar: ");
        int quantidade = lerInteiro();
        
        if (quantidade <= 0) {
            System.out.println("Quantidade deve ser maior que zero!");
            aguardarTecla();
            return;
        }
        
        if (quantidade > material.getQuantidadeAtual()) {
            System.out.println("Quantidade solicitada maior que a disponível!");
            aguardarTecla();
            return;
        }
        
        String projetoId = null;
        System.out.print("Associar a um projeto? (S/N): ");
        String associar = scanner.nextLine();
        
        if (associar.equalsIgnoreCase("S")) {
            List<Projeto> projetos = projetoServico.listarTodosProjetos();
            
            if (projetos.isEmpty()) {
                System.out.println("Não há projetos cadastrados!");
            } else {
                System.out.println("\n=== PROJETOS DISPONÍVEIS ===");
                for (Projeto projeto : projetos) {
                    System.out.println(projeto.getId().substring(0, 8) + " - " + projeto.getNome());
                }
                
                System.out.print("\nDigite o ID do projeto: ");
                projetoId = scanner.nextLine();
                
                Projeto projeto = projetoServico.buscarProjetoPorId(projetoId);
                if (projeto == null) {
                    System.out.println("Projeto não encontrado! A saída será registrada sem projeto.");
                    projetoId = null;
                }
            }
        }
        
        System.out.print("Observação: ");
        String observacao = scanner.nextLine();
        
        String responsavel = usuarioServico.getUsuarioLogado().getNome();
        
        boolean sucesso = movimentacaoServico.registrarSaida(materialId, projetoId, quantidade, responsavel, observacao);
        
        if (sucesso) {
            System.out.println("\nSaída registrada com sucesso!");
            System.out.println("Nova quantidade: " + (material.getQuantidadeAtual() - quantidade));
        } else {
            System.out.println("\nErro ao registrar saída!");
        }
        
        aguardarTecla();
    }

    private void listarMovimentacoes() {
        limparTela();
        System.out.println("=== LISTAR MOVIMENTAÇÕES ===");
        System.out.println("1. Todas as movimentações");
        System.out.println("2. Por material");
        System.out.println("3. Por projeto");
        System.out.println("4. Por tipo (Entrada/Saída)");
        System.out.println("0. Voltar");
        System.out.print("\nEscolha uma opção: ");
        String opcao = scanner.nextLine();
        
        limparTela();
        List<Movimentacao> movimentacoes = new ArrayList<>();
        
        switch (opcao) {
            case "1":
                System.out.println("=== TODAS AS MOVIMENTAÇÕES ===");
                movimentacoes = movimentacaoServico.listarTodasMovimentacoes();
                break;
            case "2":
                System.out.print("Digite o ID do material: ");
                String materialId = scanner.nextLine();
                System.out.println("\n=== MOVIMENTAÇÕES DO MATERIAL ===");
                movimentacoes = movimentacaoServico.listarMovimentacoesPorMaterial(materialId);
                break;
            case "3":
                System.out.print("Digite o ID do projeto: ");
                String projetoId = scanner.nextLine();
                System.out.println("\n=== MOVIMENTAÇÕES DO PROJETO ===");
                movimentacoes = movimentacaoServico.listarMovimentacoesPorProjeto(projetoId);
                break;
            case "4":
                System.out.print("Tipo (ENTRADA/SAIDA): ");
                String tipo = scanner.nextLine().toUpperCase();
                System.out.println("\n=== MOVIMENTAÇÕES DO TIPO " + tipo + " ===");
                movimentacoes = movimentacaoServico.listarEntradasOuSaidas(tipo);
                break;
            case "0":
                return;
            default:
                System.out.println("Opção inválida!");
                aguardarTecla();
                return;
        }
        
        if (movimentacoes == null || movimentacoes.isEmpty()) {
            System.out.println("Nenhuma movimentação encontrada!");
        } else {
            System.out.println("DATA | TIPO | MATERIAL | QTD | PROJETO | RESPONSÁVEL");
            System.out.println("-----------------------------------------------------");
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            for (Movimentacao m : movimentacoes) {
                String projetoNome = m.getProjeto() != null ? m.getProjeto().getNome() : "N/A";
                System.out.printf("%s | %s | %s | %d | %s | %s\n", 
                    sdf.format(m.getData()), 
                    m.getTipo(),
                    m.getMaterial().getNome(),
                    m.getQuantidade(),
                    projetoNome,
                    m.getResponsavel());
            }
        }
        
        aguardarTecla();
    }

    private void listarEstoqueBaixo() {
        limparTela();
        System.out.println("=== MATERIAIS COM ESTOQUE BAIXO ===");
        
        List<Material> materiais = materialServico.listarMateriaisComEstoqueBaixo();
        
        if (materiais.isEmpty()) {
            System.out.println("Não há materiais com estoque abaixo do mínimo!");
        } else {
            System.out.println("NOME | TIPO | QUANTIDADE ATUAL | QUANTIDADE MÍNIMA | UNIDADE");
            System.out.println("-------------------------------------------------------------");
            
            for (Material material : materiais) {
                System.out.printf("%s | %s | %d | %d | %s\n", 
                    material.getNome(), 
                    material.getTipo(),
                    material.getQuantidadeAtual(),
                    material.getQuantidadeMinima(),
                    material.getUnidadeMedida());
            }
        }
        
        aguardarTecla();
    }

    private void menuProjetos() {
        boolean voltar = false;

        while (!voltar) {
            limparTela();
            System.out.println("=== GERENCIAR PROJETOS ===");
            System.out.println("1. Cadastrar Projeto");
            System.out.println("2. Listar Projetos");
            System.out.println("3. Buscar Projeto");
            System.out.println("4. Editar Projeto");
            System.out.println("5. Excluir Projeto");
            System.out.println("0. Voltar");
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
                    editarProjeto();
                    break;
                case "5":
                    excluirProjeto();
                    break;
                case "0":
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
                    aguardarTecla();
                    break;
            }
        }
    }

    private void cadastrarProjeto() {
        limparTela();
        System.out.println("=== CADASTRAR PROJETO ===");
        
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();
        
        System.out.print("Data de Início (dd/mm/aaaa): ");
        Date dataInicio = lerData();
        
        System.out.print("Data de Término (dd/mm/aaaa) - deixe em branco se não houver: ");
        String dataFimStr = scanner.nextLine();
        Date dataFim = null;
        if (!dataFimStr.isEmpty()) {
            dataFim = lerData(dataFimStr);
        }
        
        System.out.print("Responsável: ");
        String responsavel = scanner.nextLine();
        
        System.out.print("Status (EM_ANDAMENTO, CONCLUIDO, CANCELADO): ");
        String status = scanner.nextLine();
        
        boolean sucesso = projetoServico.cadastrarProjeto(nome, descricao, dataInicio, dataFim, responsavel, status);
        
        if (sucesso) {
            System.out.println("\nProjeto cadastrado com sucesso!");
        } else {
            System.out.println("\nErro ao cadastrar projeto!");
        }
        
        aguardarTecla();
    }

    private void listarProjetos() {
        limparTela();
        System.out.println("=== LISTAR PROJETOS ===");
        
        List<Projeto> projetos = projetoServico.listarTodosProjetos();
        
        if (projetos.isEmpty()) {
            System.out.println("Nenhum projeto cadastrado!");
        } else {
            System.out.println("ID | NOME | RESPONSÁVEL | DATA INÍCIO | STATUS");
            System.out.println("--------------------------------------------------");
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            for (Projeto projeto : projetos) {
                System.out.printf("%s | %s | %s | %s | %s\n", 
                    projeto.getId().substring(0, 8), 
                    projeto.getNome(), 
                    projeto.getResponsavel(),
                    sdf.format(projeto.getDataInicio()),
                    projeto.getStatus());
            }
        }
        
        aguardarTecla();
    }

    private void buscarProjeto() {
        limparTela();
        System.out.println("=== BUSCAR PROJETO ===");
        System.out.println("1. Por ID");
        System.out.println("2. Por Nome");
        System.out.println("3. Por Status");
        System.out.println("0. Voltar");
        System.out.print("\nEscolha uma opção: ");
        String opcao = scanner.nextLine();
        
        limparTela();
        List<Projeto> projetos = new ArrayList<>();
        
        switch (opcao) {
            case "1":
                System.out.print("Digite o ID do projeto: ");
                String id = scanner.nextLine();
                Projeto projeto = projetoServico.buscarProjetoPorId(id);
                if (projeto != null) {
                    projetos.add(projeto);
                }
                break;
            case "2":
                System.out.print("Digite o nome do projeto: ");
                String nome = scanner.nextLine();
                projetos = projetoServico.buscarProjetosPorNome(nome);
                break;
            case "3":
                System.out.print("Digite o status do projeto: ");
                String status = scanner.nextLine();
                projetos = projetoServico.listarProjetosPorStatus(status);
                break;
            case "0":
                return;
            default:
                System.out.println("Opção inválida!");
                aguardarTecla();
                return;
        }
        
        if (projetos.isEmpty()) {
            System.out.println("Nenhum projeto encontrado!");
        } else {
            System.out.println("\n=== PROJETOS ENCONTRADOS ===");
            System.out.println("ID | NOME | RESPONSÁVEL | DATA INÍCIO | STATUS");
            System.out.println("--------------------------------------------------");
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            for (Projeto p : projetos) {
                System.out.printf("%s | %s | %s | %s | %s\n", 
                    p.getId().substring(0, 8), 
                    p.getNome(), 
                    p.getResponsavel(),
                    sdf.format(p.getDataInicio()),
                    p.getStatus());
            }
        }
        
        aguardarTecla();
    }

    private void editarProjeto() {
        limparTela();
        System.out.println("=== EDITAR PROJETO ===");
        
        System.out.print("Digite o ID do projeto: ");
        String id = scanner.nextLine();
        
        Projeto projeto = projetoServico.buscarProjetoPorId(id);
        
        if (projeto == null) {
            System.out.println("Projeto não encontrado!");
            aguardarTecla();
            return;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        System.out.println("\nProjeto encontrado:");
        System.out.println("Nome: " + projeto.getNome());
        System.out.println("Descrição: " + projeto.getDescricao());
        System.out.println("Data de Início: " + sdf.format(projeto.getDataInicio()));
        System.out.println("Data de Término: " + (projeto.getDataFim() != null ? sdf.format(projeto.getDataFim()) : "Não definida"));
        System.out.println("Responsável: " + projeto.getResponsavel());
        System.out.println("Status: " + projeto.getStatus());
        
        System.out.println("\nPreencha os novos dados (deixe em branco para manter o atual):");
        
        System.out.print("Nome [" + projeto.getNome() + "]: ");
        String nome = scanner.nextLine();
        if (nome.isEmpty()) nome = projeto.getNome();
        
        System.out.print("Descrição [" + projeto.getDescricao() + "]: ");
        String descricao = scanner.nextLine();
        if (descricao.isEmpty()) descricao = projeto.getDescricao();
        
        System.out.print("Data de Início [" + sdf.format(projeto.getDataInicio()) + "]: ");
        String dataInicioStr = scanner.nextLine();
        Date dataInicio = dataInicioStr.isEmpty() ? projeto.getDataInicio() : lerData(dataInicioStr);
        
        System.out.print("Data de Término [" + (projeto.getDataFim() != null ? sdf.format(projeto.getDataFim()) : "Não definida") + "]: ");
        String dataFimStr = scanner.nextLine();
        Date dataFim = null;
        if (!dataFimStr.isEmpty()) {
            dataFim = lerData(dataFimStr);
        } else if (projeto.getDataFim() != null) {
            dataFim = projeto.getDataFim();
        }
        
        System.out.print("Responsável [" + projeto.getResponsavel() + "]: ");
        String responsavel = scanner.nextLine();
        if (responsavel.isEmpty()) responsavel = projeto.getResponsavel();
        
        System.out.print("Status [" + projeto.getStatus() + "]: ");
        String status = scanner.nextLine();
        if (status.isEmpty()) status = projeto.getStatus();
        
        boolean sucesso = projetoServico.atualizarProjeto(id, nome, descricao, dataInicio, dataFim, responsavel, status);
        
        if (sucesso) {
            System.out.println("\nProjeto atualizado com sucesso!");
        } else {
            System.out.println("\nErro ao atualizar projeto!");
        }
        
        aguardarTecla();
    }

    private void excluirProjeto() {
        limparTela();
        System.out.println("=== EXCLUIR PROJETO ===");
        
        System.out.print("Digite o ID do projeto: ");
        String id = scanner.nextLine();
        
        Projeto projeto = projetoServico.buscarProjetoPorId(id);
        
        if (projeto == null) {
            System.out.println("Projeto não encontrado!");
            aguardarTecla();
            return;
        }
        
        System.out.println("\nProjeto encontrado:");
        System.out.println("Nome: " + projeto.getNome());
        System.out.println("Responsável: " + projeto.getResponsavel());
        System.out.println("Status: " + projeto.getStatus());
        
        System.out.print("\nConfirma a exclusão deste projeto? (S/N): ");
        String confirmacao = scanner.nextLine();
        
        if (confirmacao.equalsIgnoreCase("S")) {
            boolean sucesso = projetoServico.excluirProjeto(id);
            
            if (sucesso) {
                System.out.println("\nProjeto excluído com sucesso!");
            } else {
                System.out.println("\nErro ao excluir projeto!");
            }
        } else {
            System.out.println("\nOperação cancelada!");
        }
        
        aguardarTecla();
    }

    private void menuUsuarios() {
        boolean voltar = false;

        while (!voltar) {
            limparTela();
            System.out.println("=== GERENCIAR USUÁRIOS ===");
            System.out.println("1. Cadastrar Usuário");
            System.out.println("2. Listar Usuários");
            System.out.println("3. Editar Usuário");
            System.out.println("4. Excluir Usuário");
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
                case "3":
                    editarUsuario();
                    break;
                case "4":
                    excluirUsuario();
                    break;
                case "0":
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
                    aguardarTecla();
                    break;
            }
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
        
        System.out.print("Nível (ADMIN, GESTOR, OPERADOR): ");
        String nivel = scanner.nextLine().toUpperCase();
        
        boolean sucesso = usuarioServico.cadastrarUsuario(nome, login, senha, nivel);
        
        if (sucesso) {
            System.out.println("\nUsuário cadastrado com sucesso!");
        } else {
            System.out.println("\nErro ao cadastrar usuário!");
        }
        
        aguardarTecla();
    }

    private void listarUsuarios() {
        limparTela();
        System.out.println("=== LISTAR USUÁRIOS ===");
        
        List<Usuario> usuarios = usuarioServico.listarTodosUsuarios();
        
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado!");
        } else {
            System.out.println("ID | NOME | LOGIN | NÍVEL");
            System.out.println("---------------------------");
            
            for (Usuario usuario : usuarios) {
                System.out.printf("%s | %s | %s | %s\n", 
                    usuario.getId().substring(0, 8), 
                    usuario.getNome(), 
                    usuario.getLogin(),
                    usuario.getNivel());
            }
        }
        
        aguardarTecla();
    }

    private void editarUsuario() {
        limparTela();
        System.out.println("=== EDITAR USUÁRIO ===");
        
        System.out.print("Digite o ID do usuário: ");
        String id = scanner.nextLine();
        
        Usuario usuario = usuarioServico.buscarUsuarioPorId(id);
        
        if (usuario == null) {
            System.out.println("Usuário não encontrado!");
            aguardarTecla();
            return;
        }
        
        System.out.println("\nUsuário encontrado:");
        System.out.println("Nome: " + usuario.getNome());
        System.out.println("Login: " + usuario.getLogin());
        System.out.println("Nível: " + usuario.getNivel());
        
        System.out.println("\nPreencha os novos dados (deixe em branco para manter o atual):");
        
        System.out.print("Nome [" + usuario.getNome() + "]: ");
        String nome = scanner.nextLine();
        if (nome.isEmpty()) nome = usuario.getNome();
        
        System.out.print("Login [" + usuario.getLogin() + "]: ");
        String login = scanner.nextLine();
        if (login.isEmpty()) login = usuario.getLogin();
        
        System.out.print("Senha (deixe em branco para não alterar): ");
        String senha = scanner.nextLine();
        
        System.out.print("Nível [" + usuario.getNivel() + "]: ");
        String nivel = scanner.nextLine().toUpperCase();
        if (nivel.isEmpty()) nivel = usuario.getNivel();
        
        boolean sucesso = usuarioServico.atualizarUsuario(id, nome, login, senha, nivel);
        
        if (sucesso) {
            System.out.println("\nUsuário atualizado com sucesso!");
        } else {
            System.out.println("\nErro ao atualizar usuário!");
        }
        
        aguardarTecla();
    }

    private void excluirUsuario() {
        limparTela();
        System.out.println("=== EXCLUIR USUÁRIO ===");
        
        System.out.print("Digite o ID do usuário: ");
        String id = scanner.nextLine();
        
        Usuario usuario = usuarioServico.buscarUsuarioPorId(id);
        
        if (usuario == null) {
            System.out.println("Usuário não encontrado!");
            aguardarTecla();
            return;
        }
        
        if (usuarioServico.getUsuarioLogado().getId().equals(id)) {
            System.out.println("Não é possível excluir o usuário logado!");
            aguardarTecla();
            return;
        }
        
        System.out.println("\nUsuário encontrado:");
        System.out.println("Nome: " + usuario.getNome());
        System.out.println("Login: " + usuario.getLogin());
        System.out.println("Nível: " + usuario.getNivel());
        
        System.out.print("\nConfirma a exclusão deste usuário? (S/N): ");
        String confirmacao = scanner.nextLine();
        
        if (confirmacao.equalsIgnoreCase("S")) {
            boolean sucesso = usuarioServico.excluirUsuario(id);
            
            if (sucesso) {
                System.out.println("\nUsuário excluído com sucesso!");
            } else {
                System.out.println("\nErro ao excluir usuário!");
            }
        } else {
            System.out.println("\nOperação cancelada!");
        }
        
        aguardarTecla();
    }

    private void menuRelatorios() {
        boolean voltar = false;

        while (!voltar) {
            limparTela();
            System.out.println("=== RELATÓRIOS ===");
            System.out.println("1. Relatório de Estoque Baixo");
            System.out.println("2. Relatório de Movimentações por Período");
            System.out.println("3. Relatório de Consumo por Projeto");
            System.out.println("0. Voltar");
            System.out.print("\nEscolha uma opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    relatorioEstoqueBaixo();
                    break;
                case "2":
                    relatorioMovimentacoesPorPeriodo();
                    break;
                case "3":
                    relatorioConsumoPorProjeto();
                    break;
                case "0":
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
                    aguardarTecla();
                    break;
            }
        }
    }

    private void relatorioEstoqueBaixo() {
        limparTela();
        System.out.println("=== RELATÓRIO DE ESTOQUE BAIXO ===");
        
        List<Material> materiais = materialServico.listarMateriaisComEstoqueBaixo();
        
        if (materiais.isEmpty()) {
            System.out.println("Não há materiais com estoque abaixo do mínimo!");
        } else {
            System.out.println("Total de materiais com estoque baixo: " + materiais.size());
            System.out.println("\nNOME | TIPO | QUANTIDADE ATUAL | QUANTIDADE MÍNIMA | UNIDADE | PREÇO (R$)");
            System.out.println("----------------------------------------------------------------------------");
            
            for (Material material : materiais) {
                System.out.printf("%s | %s | %d | %d | %s | R$ %.2f\n", 
                    material.getNome(), 
                    material.getTipo(),
                    material.getQuantidadeAtual(),
                    material.getQuantidadeMinima(),
                    material.getUnidadeMedida(),
                    material.getPreco());
            }
        }
        
        aguardarTecla();
    }

    private void relatorioMovimentacoesPorPeriodo() {
        limparTela();
        System.out.println("=== RELATÓRIO DE MOVIMENTAÇÕES POR PERÍODO ===");
        
        System.out.print("Data Inicial (dd/mm/aaaa): ");
        Date dataInicial = lerData();
        
        System.out.print("Data Final (dd/mm/aaaa): ");
        Date dataFinal = lerData();
        
        // Adiciona 23:59:59 à data final para incluir todo o dia
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataFinal);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        dataFinal = cal.getTime();
        
        List<Movimentacao> movimentacoes = movimentacaoServico.listarMovimentacoesPorPeriodo(dataInicial, dataFinal);
        
        if (movimentacoes.isEmpty()) {
            System.out.println("Nenhuma movimentação encontrada no período especificado!");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            int totalEntradas = 0;
            int totalSaidas = 0;
            
            for (Movimentacao m : movimentacoes) {
                if (m.getTipo().equals("ENTRADA")) {
                    totalEntradas++;
                } else if (m.getTipo().equals("SAIDA")) {
                    totalSaidas++;
                }
            }
            
            System.out.println("\nResumo do período de " + sdf.format(dataInicial) + " a " + sdf.format(dataFinal));
            System.out.println("Total de movimentações: " + movimentacoes.size());
            System.out.println("Total de entradas: " + totalEntradas);
            System.out.println("Total de saídas: " + totalSaidas);
            
            System.out.println("\nDATA | TIPO | MATERIAL | QTD | PROJETO | RESPONSÁVEL");
            System.out.println("-----------------------------------------------------");
            
            for (Movimentacao m : movimentacoes) {
                String projetoNome = m.getProjeto() != null ? m.getProjeto().getNome() : "N/A";
                System.out.printf("%s | %s | %s | %d | %s | %s\n", 
                    sdf.format(m.getData()), 
                    m.getTipo(),
                    m.getMaterial().getNome(),
                    m.getQuantidade(),
                    projetoNome,
                    m.getResponsavel());
            }
        }
        
        aguardarTecla();
    }

    private void relatorioConsumoPorProjeto() {
        limparTela();
        System.out.println("=== RELATÓRIO DE CONSUMO POR PROJETO ===");
        
        System.out.print("Digite o ID do projeto: ");
        String projetoId = scanner.nextLine();
        
        Projeto projeto = projetoServico.buscarProjetoPorId(projetoId);
        
        if (projeto == null) {
            System.out.println("Projeto não encontrado!");
            aguardarTecla();
            return;
        }
        
        List<Movimentacao> movimentacoes = movimentacaoServico.listarMovimentacoesPorProjeto(projetoId);
        
        if (movimentacoes.isEmpty()) {
            System.out.println("Nenhuma movimentação encontrada para o projeto!");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            // Mapeia material -> quantidade total
            Map<String, Integer> consumoMateriais = new HashMap<>();
            Map<String, Double> valorMateriais = new HashMap<>();
            
            for (Movimentacao m : movimentacoes) {
                if (m.getTipo().equals("SAIDA")) {
                    String materialNome = m.getMaterial().getNome();
                    int quantidade = m.getQuantidade();
                    double valorTotal = m.getMaterial().getPreco() * quantidade;
                    
                    consumoMateriais.put(materialNome, consumoMateriais.getOrDefault(materialNome, 0) + quantidade);
                    valorMateriais.put(materialNome, valorMateriais.getOrDefault(materialNome, 0.0) + valorTotal);
                }
            }
            
            System.out.println("\nProjeto: " + projeto.getNome());
            System.out.println("Responsável: " + projeto.getResponsavel());
            System.out.println("Status: " + projeto.getStatus());
            System.out.println("Total de movimentações: " + movimentacoes.size());
            
            System.out.println("\nConsumo de Materiais:");
            System.out.println("MATERIAL | QUANTIDADE | VALOR TOTAL (R$)");
            System.out.println("--------------------------------------");
            
            double valorTotalProjeto = 0.0;
            
            for (String materialNome : consumoMateriais.keySet()) {
                int quantidade = consumoMateriais.get(materialNome);
                double valorTotal = valorMateriais.get(materialNome);
                valorTotalProjeto += valorTotal;
                
                System.out.printf("%s | %d | R$ %.2f\n", materialNome, quantidade, valorTotal);
            }
            
            System.out.println("\nValor Total do Projeto: R$ " + String.format("%.2f", valorTotalProjeto));
            
            System.out.println("\nDetalhes das Movimentações:");
            System.out.println("DATA | MATERIAL | QTD | RESPONSÁVEL");
            System.out.println("----------------------------------");
            
            for (Movimentacao m : movimentacoes) {
                if (m.getTipo().equals("SAIDA")) {
                    System.out.printf("%s | %s | %d | %s\n", 
                        sdf.format(m.getData()), 
                        m.getMaterial().getNome(),
                        m.getQuantidade(),
                        m.getResponsavel());
                }
            }
        }
        
        aguardarTecla();
    }

    // Métodos auxiliares
    private void limparTela() {
        // Em um terminal real, poderíamos usar o comando de limpar tela
        // Como estamos em um ambiente de console Java simples, apenas imprimimos linhas vazias
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private void aguardarTecla() {
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }

    private int lerInteiro() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double lerDouble() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private Date lerData() {
        try {
            return dateFormat.parse(scanner.nextLine());
        } catch (ParseException e) {
            System.out.println("Formato de data inválido! Usando data atual.");
            return new Date();
        }
    }

    private Date lerData(String dataStr) {
        try {
            return dateFormat.parse(dataStr);
        } catch (ParseException e) {
            System.out.println("Formato de data inválido! Usando data atual.");
            return new Date();
        }
    }
}
