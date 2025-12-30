# Sistema de Almoxarifado

## Descrição
Sistema desenvolvido em Java com interface gráfica (JFrame) para gerenciamento de almoxarifado com banco de dados MySQL.

## Funcionalidades
- **Cadastro de Produtos**: Nome, quantidade, especificação, empresa fornecedora
- **Retirada de Produtos**: Controle de quem retirou, setor, produto e quantidade
- **Listar Produtos**: Visualização de todos os produtos em ordem alfabética com estoque
- **Relatórios**: 
  - Por setor (produtos e quantidades retiradas por setor)
  - Por pessoa (histórico de retiradas)
  - Por produto (quem retirou cada produto)

## Como Executar

### Pré-requisitos
- Java JDK 25 instalado
- MySQL Server instalado e rodando
- MySQL Connector JAR (mysql-connector-j-9.5.0.jar) na pasta do projeto

### Configuração do Banco
1. Execute o script `database.sql` no MySQL Workbench
2. Configure a senha em `DatabaseConnection.java` (linha 8)

### Compilação
```bash
compilar_jdk25.bat
```

### Execução
```bash
executar_jdk25.bat
```

### Teste de Conexão
```bash
testar_conexao.bat
```

## Estrutura do Projeto
- `Produto.java` - Classe modelo para produtos
- `Retirada.java` - Classe modelo para retiradas
- `DatabaseConnection.java` - Conexão e operações com banco de dados
- `MainFrame.java` - Interface principal
- `CadastroProdutoFrame.java` - Tela de cadastro de produtos
- `RetiradaProdutoFrame.java` - Tela de retirada de produtos
- `ListarProdutosFrame.java` - Tela de listagem de produtos
- `RelatorioFrame.java` - Tela de relatórios e consultas
- `TesteConexao.java` - Teste de conexão com banco
- `database.sql` - Script de criação do banco

## Características Técnicas
- Orientação a objetos
- Persistência com MySQL
- Auto increment para código dos produtos
- Validação de estoque na retirada
- Pesquisa flexível (case insensitive)
- Interface gráfica intuitiva
- Tratamento de erros de conexão