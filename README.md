# Pagamentos API

Este é um projeto que implementa um sistema de transações financeiras com suporte a depósitos, pagamentos com cartão, pagamentos com saldo, e integração com autenticação de cartões de crédito.

## Funcionalidades

- **Depósitos**: Permite que o usuário deposite um valor em sua conta.
- **Pagamento com Cartão**: Permite que o usuário faça pagamentos usando um cartão de crédito.
- **Pagamento com Saldo**: Permite que o usuário pague uma cobrança utilizando o saldo da sua conta.
- **Cobranças**: O sistema suporta o gerenciamento de cobranças associadas a usuários.

## Estrutura do Projeto

### Diretórios e Arquivos Importantes

- **`/src/main/java/com/pagamentos/jpa/`**: Diretório principal com o código fonte da aplicação.
  - **`services/`**: Contém os serviços que lidam com a lógica de negócios.
  - **`repositories/`**: Contém os repositórios responsáveis pela comunicação com o banco de dados.
  - **`models/`**: Contém as entidades de domínio, que representam as tabelas do banco de dados.
  - **`dtos/`**: Contém os Data Transfer Objects (DTOs), que são utilizados para transferir dados entre camadas.

## Fluxo de Execução

### 1. **Depósito**
   - O usuário solicita um depósito, informando os dados do cartão e o valor.
   - O sistema valida o cartão de crédito utilizando o serviço de validação.
   - Caso a validação seja bem-sucedida, o saldo do usuário é atualizado com o valor do depósito.

### 2. **Pagamento com Cartão**
   - O usuário solicita um pagamento com cartão, informando os dados do cartão e o ID da cobrança.
   - O sistema valida o cartão de crédito.
   - Caso a validação seja bem-sucedida, o sistema atualiza o saldo do usuário de destino com o valor da cobrança e marca a cobrança como realizada.

### 3. **Pagamento com Saldo**
   - O usuário solicita um pagamento utilizando seu saldo.
   - O sistema verifica se o usuário tem saldo suficiente para cobrir o valor da cobrança.
   - Caso tenha saldo suficiente, o sistema realiza a transação e atualiza os saldos dos usuários.

### 4. **Cobrança**
   - A cobrança é associada a um usuário de origem e um usuário de destino.
   - Quando o pagamento é realizado (seja por cartão ou saldo), o status da cobrança é alterado para `REALIZADO`.

### 5. **Registro de Usuário**
   - O usuário informa os dados necessários para criação de um registro.
   - O sistema verifica se os dados necessários foram preenchidos, valida o CPF e a não-existência do registro no banco de dados.
   - A senha informada é hasheada e os dados são armazenados no banco de dados.

### 6. **Login de Usuário**
   - O usuário informa login (CPF ou email) e senha.
   - O sistema valida as informações e retorna um token de autenticação caso sucedido.

## Endpoints da API

### **POST /pagamentos/transactions**

Este endpoint é responsável por criar uma nova transação, que pode ser um **depósito**, um **pagamento com cartão** ou um **pagamento com saldo**.

#### Exemplo de Corpo da Requisição:

```json
{
  "valor": 100.0,
  "tipo": "DEPOSITO",
  "cobranca_id": null,
  "user_origem_id": 1,
  "numero_cartao": "1234567890123456",
  "cvv_cartao": "123",
  "validade_cartao": "12/25"
}
```

### **POST /pagamentos/cobrancas**

Este endpoint é responsável por criar uma nova cobrança, que deve possuir obrigatoriamente o **CPF do cobrador**, o **CPF do cobrado** e um **valor**. Além disso pode ou não possuir uma **descrição** da cobrança.

#### Exemplo de Corpo da Requisição:

```json
{
  "valor": 50.0,
  "cpf_origem": "12345678910",
  "cpf_destino": "10987654321",
  "descrição": "Cobrança referente ao dia 12/12/2012"
}
```

### **POST /auth/register**

Este endpoint é responsável por registrar um novo usuário, que deve informar **nome**, **CPF**, **email** e **senha**.

#### Exemplo de Corpo da Requisição:

```json
{
  "cpf": "12345678910",
  "email": "email@email.com",
  "password": "password123",
  "nome": "João"
}
```

### **POST /verifica-cartao**

Este endpoint é responsável por acessar a API de verificação externa para autorização de transações com cartão de crédito. Utiliza como parâmetro um objeto do tipo CardModel, com **número, CVV, data de validade e ID do usuário**. É acessado pelo endpoint **/pagamentos/transactions**, que faz as validações necessárias do cartão.

#### Exemplo de Corpo da Requisição:

```json
{
    "numero": "1111222233334444",
    "cvv": "123",
    "validade": "01/30",
    "user_origem_id": "e37d9d3c-1532-425a-89fa-e21769a3d530"
}
```

### **POST /auth/login**

Este endpoint é responsável pelo login de um usuário, que deve informar uma forma de login, que pode ser por **email ou CPF** e **senha**.

#### Exemplo de Corpo da Requisição:

```json
{
  "login": "12345678910",
  "senha": "password123",
}
```

### **GET /pagamentos/cobrancas**

Este endpoint traz todas as cobranças existentes no banco de dados e não utiliza parâmetros.

### **GET /pagamentos/transactions**

Este endpoint traz todas as transações existentes no banco de dados e não utiliza parâmetros.

### **GET /pagamentos/cartoes**

Este endpoint traz todos os cartões existentes no banco de dados e não utiliza parâmetros.

## Dependências

- **Spring Boot**: Framework para desenvolvimento da aplicação Java.
- **Spring Data JPA**: Facilita a comunicação com o banco de dados utilizando JPA.
- **Jakarta Transaction**: Suporte a transações.
- **Banco de Dados PostgreSQL**: Para persistência de dados.

## Futuras Implementações

- Acesso aos endpoints sensíveis somente com access token gerado na autenticação do usuário.
- Atribuição de roles aos usuários.
- Implementação de testes unitários.
- Configuração para execução em contêineres.