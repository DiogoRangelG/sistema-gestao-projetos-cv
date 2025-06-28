# Sistema de Gestão de Projetos de Comunicação Visual

## Descrição
Sistema para gerenciar projetos de comunicação visual, facilitando o controle de recursos, movimentações e projetos. O sistema permite o controle eficiente de recursos gráficos com rastreabilidade de entrada e saída por projeto de comunicação visual.

## Funcionalidades
- Cadastro e gerenciamento de recursos gráficos (materiais, insumos)
- Controle de estoque com alertas para estoque baixo
- Gestão de projetos de comunicação visual
- Registro de movimentações (entradas e saídas)
- Relatórios diversos
- Gestão de usuários com níveis de acesso

## Requisitos
- JDK 24 ou superior
- MySQL 5.7 ou superior
- Maven 3.6 ou superior

## Configuração do Banco de Dados
1. Instale o MySQL Server
2. O sistema criará automaticamente o banco de dados e as tabelas necessárias
3. Por padrão, o sistema usa as seguintes configurações:
   - URL: `jdbc:mysql://localhost:3306/gestao_projetos_cv`
   - Usuário: `root`
   - Senha: `181307`

Para alterar estas configurações, edite a classe `ConexaoDB.java` no pacote `com.gestao.projetos.cv.dao`.

## Como Executar

### Opção 1: Usando o script executar.bat
Simplesmente execute o arquivo `executar.bat` na pasta raiz do projeto.

### Opção 2: Usando Maven
1. Clone o repositório
2. Navegue até a pasta do projeto
3. Execute o comando:
   ```
   mvn clean package
   ```
4. Execute o sistema com o comando:
   ```
   java -jar target/sistema-gestao-projetos-cv-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

### Opção 3: Usando Maven exec
```
mvn exec:java -Dexec.mainClass="com.gestao.projetos.cv.Main"
```

## Usuário Padrão
O sistema cria automaticamente um usuário administrador:
- Login: `admin`
- Senha: `admin123`

## Estrutura do Projeto
- `com.gestao.projetos.cv.modelo`: Classes de modelo
- `com.gestao.projetos.cv.dao`: Classes de acesso a dados
- `com.gestao.projetos.cv.servico`: Classes de serviço (regras de negócio)
- `com.gestao.projetos.cv.ui`: Interface do usuário

## Integrantes
- Davi Henrique do Carmo - 324238198
- Diogo Rangel Gomes - 32225637
- Guilherme Braian de Souza Alcântara - 324236912
- Isabelle da Silva Gomes - 32226938
- Mateus de Oliveira Barbosa - 324222461
