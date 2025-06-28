# 🎨 Sistema de Gestão de Projetos de Comunicação Visual

[![Java](https://img.shields.io/badge/Java-24-orange)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue)](https://maven.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-5.7+-blue)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

## 📋 Descrição

Sistema completo para gerenciar projetos de comunicação visual, facilitando o controle de recursos, movimentações e projetos. O sistema permite o controle eficiente de recursos gráficos com rastreabilidade completa de entrada e saída por projeto.

## ✨ Funcionalidades

### 🔐 Sistema de Autenticação
- Login seguro com níveis de acesso (ADMIN, GESTOR, OPERADOR)
- Controle de sessão e permissões
- Usuário administrador padrão

### 📦 Gestão de Recursos
- ✅ Cadastro completo de recursos gráficos
- ✅ Controle de estoque com alertas
- ✅ Busca flexível por ID ou nome
- ✅ Atualização de estoque
- ✅ Exclusão segura com confirmação

### 🎨 Gestão de Projetos
- ✅ Cadastro de projetos de comunicação visual
- ✅ Controle de status e prazos
- ✅ Gestão por cliente
- ✅ Orçamentos e categorização
- ✅ Exclusão segura com confirmação

### 📋 Movimentações
- ✅ Registro de entradas de recursos
- ✅ Registro de saídas por projeto
- ✅ Histórico completo de movimentações
- ✅ Rastreabilidade total

### 📊 Relatórios
- ✅ Relatórios de recursos por categoria
- ✅ Relatórios de projetos por status
- ✅ Relatórios de movimentações por período
- ✅ Relatórios de usuários por nível

### 👥 Gestão de Usuários (Admin)
- ✅ Cadastro e gerenciamento de usuários
- ✅ Controle de níveis de acesso
- ✅ Relatórios de usuários

## 🆔 Sistema de IDs Inteligente

- **IDs Numéricos**: Formato 000.000.001 até 999.999.999
- **Busca Flexível**: Digite `1` ou `000000001` - ambos funcionam!
- **Gerador Centralizado**: Evita duplicação entre entidades

## ⬅️ Comando *VOLTAR* Universal

- Digite `*VOLTAR*` em qualquer campo para cancelar a operação
- Funciona em todos os cadastros e formulários
- Retorna ao menu anterior sem salvar alterações

## 🛠️ Tecnologias Utilizadas

- **Java 24**: Linguagem principal
- **Maven**: Gerenciamento de dependências
- **MySQL**: Banco de dados
- **JDBC**: Conectividade com banco de dados
- **Terminal Interface**: Interface de usuário

## 📋 Requisitos

- ☕ **JDK 24** ou superior
- 🗄️ **MySQL 5.7** ou superior  
- 🔧 **Maven 3.6** ou superior

## 🚀 Como Executar

### Opção 1: Script Automático (Recomendado)
```bash
# Execute o arquivo executar.bat na pasta raiz
./executar.bat
```

### Opção 2: Linha de Comando
```bash
# Clone o repositório
git clone https://github.com/seu-usuario/sistema-gestao-projetos-cv.git
cd sistema-gestao-projetos-cv

# Compile o projeto
mvn clean package

# Execute o sistema
java -jar target/sistema-gestao-projetos-cv-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### Opção 3: Maven Exec
```bash
mvn exec:java -Dexec.mainClass="com.gestao.projetos.cv.Main"
```

## 🗄️ Configuração do Banco de Dados

O sistema **cria automaticamente** o banco de dados e tabelas. Configuração padrão:

```properties
URL: jdbc:mysql://localhost:3306/gestao_projetos_cv
Usuário: root
Senha: 181307
```

Para alterar, edite o arquivo `ConexaoDB.java`.

## 👤 Usuário Padrão

```
Login: admin
Senha: admin123
Nível: ADMIN
```

## 📁 Estrutura do Projeto

```
src/main/java/com/gestao/projetos/cv/
├── 📄 Main.java                    # Classe principal
├── 🗄️ dao/                         # Acesso a dados
│   ├── ConexaoDB.java
│   ├── UsuarioDAO.java
│   ├── RecursoDAO.java
│   ├── ProjetoDAO.java
│   └── MovimentacaoDAO.java
├── 📊 modelo/                      # Classes de modelo
│   ├── Usuario.java
│   ├── Recurso.java
│   ├── Projeto.java
│   └── Movimentacao.java
├── ⚙️ servico/                     # Regras de negócio
│   ├── UsuarioServico.java
│   ├── RecursoServico.java
│   ├── ProjetoServico.java
│   └── MovimentacaoServico.java
├── 🖥️ ui/                          # Interface do usuário
│   └── Terminal.java
└── 🔧 util/                        # Utilitários
    └── GeradorId.java
```

## 🎯 Como Usar

### 1️⃣ Primeiro Acesso
1. Execute o sistema
2. Faça login com: `admin` / `admin123`
3. Cadastre novos usuários se necessário

### 2️⃣ Cadastrar Recursos
1. Menu → Gestão de Recursos → Cadastrar Recurso
2. Preencha as informações (digite `*VOLTAR*` para cancelar)
3. Confirme o cadastro

### 3️⃣ Registrar Movimentações
1. Menu → Movimentações → Registrar Entrada/Saída
2. Escolha o recurso pela lista (digite apenas o último dígito do ID)
3. Para saídas, escolha também o projeto
4. Confirme a movimentação

### 4️⃣ Gerar Relatórios
1. Menu → Relatórios
2. Escolha o tipo de relatório desejado
3. Visualize os dados organizados

## 🔒 Segurança

- ✅ Autenticação obrigatória
- ✅ Controle de permissões por nível
- ✅ Confirmação dupla para exclusões
- ✅ Comando *VOLTAR* para cancelar operações
- ✅ Validações de entrada em todos os campos

## 📸 Screenshots

### Tela Principal
```
=== SISTEMA DE GESTÃO DE PROJETOS DE COMUNICAÇÃO VISUAL ===
Usuário: Admin | Nível: ADMIN

=== MENU PRINCIPAL ===
1. Gestão de Recursos
2. Gestão de Projetos  
3. Movimentações
4. Relatórios
5. Gestão de Usuários
```

### Movimentações com Busca Inteligente
```
=== RECURSOS DISPONÍVEIS ===
ID: 000000001 - Banner Promocional (Estoque: 50)
ID: 000000002 - Placa Sinalização (Estoque: 25)

ID do recurso (9 dígitos completos ou apenas os últimos dígitos): 1
Recurso selecionado: Banner Promocional
```

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 👨‍💻 Integrantes

- **Davi Henrique do Carmo** - 324238198
- **Diogo Rangel Gomes** - 32225637  
- **Guilherme Braian de Souza Alcântara** - 324236912
- **Isabelle da Silva Gomes** - 32226938
- **Mateus de Oliveira Barbosa** - 324222461

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para detalhes.

## 🆘 Suporte

Se encontrar algum problema ou tiver dúvidas:

 Abra uma [issue](https://github.com/seu-usuario/sistema-gestao-projetos-cv/issues)

---

**⭐ Se este projeto foi útil para você, não esqueça de dar uma estrela!**
