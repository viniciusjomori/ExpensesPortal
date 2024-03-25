# Expenses Portal

## Resumo
Expenses Portal é uma plataforma onde funcionários podem registrar gastos feitos com recursos próprios pela empresa. Após o registro, essas despesas serão enviadas ao gerente responsável, o qual irá aprova-las ou recusa-las.

Se a despesa for aprovada, será enviada automaticamente para o sistema ERP da empresa por meio de uma requisição HTTP. Assim, o setor financeiro terá acesso à despesa para realizar o pagamento. Por fim, o ERP fará uma requisição ao Portal informando que o reembolso foi realizado.

## Instalação

### Pré-Requisitos
* Instalar o Jar em [*releases*](https://github.com/viniciusjomori/ExpensesPortal/releases/tag/jar)
* Java 17

### Iniciar API

No terminal, acesse a pasta onde está o Jar e execute o comando

`java -jar ExpensesPortal-0.0.1-SNAPSHOT.jar`

A API usará o endereço `localhost:8080`

## Relação Cargo x Acesso

Um cargo (como por exemplo, Administrador ou Solicitante) estão associados a uma lista de acessos (como "Registrar despesa" ou "Gerir usuários"). Um acesso está associado a uma lista de endpoints.

### Acesso:
* Registrar Despesa: cadastro de despesa em seu nome
* Aprovar Despesa: aprovar ou recusar despesas sob sua gestão
* Gerir Usuários: visualização, cadastro e edição de usuários
* Notificar Pagamento: notificar à API que a despesa foi paga

### Cargos
* Solicitante: registrar despesa
* Aprovador: registrar despesa, aprovar despesa
* Administrador: registrar despesa, aprovar despesa, gerir usuários
* ERP: notificar pagamento

## Relação Solicitante x Aprovador

Um solicitante possui um aprovador, enquanto um aprovador pode possuir vários solicitantes. Uma despesa só pode ser aprovada ou recusada pelo aprovador do solicitante. Uma despesa é somente paga após ser aprovada

## H2 Console

A API utiliza H2, um banco de dados para testes em memória local. É possível fazer operações SQL através do console acessando o link `http://localhost/h2-console`

Você será direcionado uma pagina de login. Para obter acesso, insira as seguintes informações:

```
JDBC URL: jdbc:h2:mem:expenses-portal
Username: sa
passoword: 
```

## Usuários de Teste

Quando a API é iniciada, ela automaticamente cadastra 4 usuários de teste. Seus dados de acesso estão listados abaixo:

### Solicitante
```
{
  "email":"orderer.test@email.com",
  "password":"password"
}
```

### Aprovador
```
{
  "email":"approver.test@email.com",
  "password":"password"
}
```

### Administrador
```
{
  "email":"admin.test@email.com",
  "password":"password"
}
```
### ERP
```
{
  "email":"erp.test@email.com",
  "password":"password"
}
```

## Token JWT
Essa aplicacação usa Token JWT para autenticação. Após realizar login com sucesso, a API retornará um token, o qual deve ser inserido no header, como um Bearer Token, das próximas requisições para validar o usuário.

## Endpoints:

### Permitido a todos:

#### /auth/login
* Metodo: POST
* Requisição:
```
{
  "email":"orderer.test@email.com",
  "password":"password"
}
```
* Descrição: enviar login com usuário e senha. A API irá validar as credenciais e, se estiverem corretas, enviará um token JWT de autenticação.
* Resposta:
```
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvcmRlcmVyLnRlc3RAZW1haWwuY29tIiwiaWF0IjoxNzExMzk0MzQ5LCJleHAiOjE3MTE0ODA3NDksImlzcyI6ImV4cGVuc2UtcG9ydGFsIn0.1SKdWr_7je03vo1LbO1pd_o5bGSK6ClMTfGoGWeApfs"
}
```

### Somente a usuários autenticados

Com o token em mãos, coloque-o no header da requisição para que a API saiba quem é você

#### user/current
* Método: GET
* Descrição: estando o token no header, a API consegue descripta-lo e identificar o usuário que realizou a requisição. A resposta desse endpoint são os seus dados cadastrais
* Resposta:

```
{
  "id": "77df057c-0f18-4ee6-bde5-057f8e4f6543",
  "email": "orderer.test@email.com",
  "cpf": "904601943",
  "firstname": "orderer",
  "lastname": "Test",
  "createDate": "2024-03-25T16:17:10.413148",
  "active": true,
  "role": {
    "id": "cc929b54-3117-4af6-8fe9-48d6631d0aad",
    "roleName": "ROLE_ORDERER"
  },
  "approver": {
    "id": "a6ac0fd7-82b0-483a-bc19-5975026f5884",
    "firstname": "approver",
    "lastname": "Test"
  }
}
```

#### /auth/logout
* Método: POST
* Descrição: quando a API gera um token, ele é salvo no bando de dados. Isso garante que, mesmo que um token gerado externamente esteja estruturado da forma correta, somente usuários que realizaram login tenham acesso ao sistema. Esse endpoint faz com que todos os tokens do usuário autenticado sejam inativados.

### Registrar Despesa

#### /expense/unit
* Método: GET
* Descrição: retorna todos os tipos de unidade de medida de uma despesa
* Resposta:
```
[
  "GRAMS",
  "KM",
  "KG",
  "UNITY"
]
```

#### /expense
* Método: POST
* Requisição:
```
{
  "productDesc": "Descricao do Produto",
  "unitType": "KG",
  "unitQnt": 2,
  "valuePerUnit": 30
}
```
* Descrição: cadastra uma despesa em nome do usuário autenticado. Ela estará pendente pela aprovação do gestor.
* Resposta:
```
{
  "id": "9d877134-7efa-40fc-b52c-22de217343f5",
  "orderer": {
    "id": "77df057c-0f18-4ee6-bde5-057f8e4f6543",
    "firstname": "orderer",
    "lastname": "Test"
  },
  "approver": {
    "id": "a6ac0fd7-82b0-483a-bc19-5975026f5884",
    "firstname": "approver",
    "lastname": "Test"
  },
  "productDesc": "Descricao do Produto",
  "unitType": "KG",
  "unitQnt": 2.0,
  "valuePerUnit": 30.0,
  "createDate": "2024-03-25T16:22:42.2501656",
  "approvalDate": null,
  "expenseStatus": "PENDING"
}
```

#### /expense/my
* Método: GET
* Descrição: retorna as despesas do usuário autenticado.

#### /expense/{ID DA DESPESA}/cancel
* Método: PUT
* Descrição: cancela uma despesa cadastrada pelo usuário autenticado. Uma despesa cancelada não poderá ser aprovada.

### Aprovação de Despesa

#### /expense/{ID DA DESPESA}/approve
* Método: PUT
* Descrição: aprova uma despesa. Em seguida, ela será enviada ao ERP para que seja paga. Somente permitido caso o usuário autenticado seja o aprovador da despesa.

#### /expense/{ID DA DESPESA}/refuse
* Método: PUT
* Descrição: reprova uma despesa. Somente permitido caso o usuário autenticado seja o aprovador da despesa.

#### /expense/below
* Método: GET
* Descrição: : retorna uma lista de despesas cuja aprovação esteja sob sua aprovação

> É importante destacar que esse método não exibe todas as despesas de todos os solicitantes geridos pelo usuário autenticado. Isso porque, após a aprovação ou recusa de uma despesa, o aprovador designado se mantém o mesmo permanentemente. Entretanto, se o aprovador de um solicitante for alterado, as despesas pendentes desse solicitante serão redirecionadas ao novo aprovador.

#### /user/current/orderers
* Método: GET
* Descrição: Retorna uma lista de usuários cujo aprovador é o usuário autenticado

### Gerir Usuários

#### /role
* Método: GET
* Descrição: retorna todos os cargos. Necessário para cadastro de usuário
* Resposta:
```
[
  {
    "id": "7919187e-1c2a-4203-bd85-a5741cf50a09",
    "roleName": "ROLE_ERP"
  },
  {
    "id": "cc929b54-3117-4af6-8fe9-48d6631d0aad",
    "roleName": "ROLE_ORDERER"
  },
  {
    "id": "49cc673d-1784-49e2-92ef-70148c6d80d1",
    "roleName": "ROLE_APPROVER"
  },
  {
    "id": "3fc947f9-7271-4306-bf42-39ed5fb7becf",
    "roleName": "ROLE_ADMIN"
  }
]
```

#### user/approvers
* Método: GET
* Descrição: retorna todos os usuários com acesso a aprovar despesas. Necessário para cadastro de usuários

#### user
* Método: POST
* Requisição:
```
{
  "email": "newuser.test@email.com",
  "password": "passoword",
  "cpf": "123456789",
  "firstname": "New User",
  "lastname": "Test",
  "roleId": "64c40e2a-3526-4107-9851-1ac5f464d52a",
  "approverId": "6fe801cf-a44c-47f4-b0ed-8c4d4988cb39"
}
```
* Descrição: cadastra um usuário, associando-o tambem a um cargo e um aprovador

#### user
* Método: GET
* Descrição: retorna uma lista de todos os usuários cadastrados

#### user/{ID DO USUARIO}
* Método: PUT
* Requisição:
```
{
  "email": "olduser.test@email.com",
  "password": "newpassword",
  "cpf": "987654321",
  "firstname": "old User",
  "lastname": "Test",
  "roleId": "64c40e2a-3526-4107-9851-1ac5f464d52a",
  "approverId": "6fe801cf-a44c-47f4-b0ed-8c4d4988cb39"
}

```

* Descrição: modifica dados de um usuário cadastrado, podendo também ser seu cargo, aprovador e/ou se esta ativo ou não.

### ERP

#### /expense/{ID DA DESPESA}/notify_payment
* Método: PUT
* Descrição: ERP informa ao Portal que a despesa foi paga

## Integração com o ERP

Após obter sucesso em cadastrar usuário, editar usuário e cadastrar despesa, o Portal irá fazer uma requisição HTTP para o ERP. Essa versão do Expenses Portal possui um Mock para simular essa integração.

### Fila de Requisições

Todas as requisições feitas pelo Portal são salvas em uma tabela, chamada "REQUESTS". Ela possui a url, metodo HTTP e corpo da requisição como colunas. Quando um registro é salvo nessa tabela, seu status é definido como "PENDING"

Requests salvas como pendentes são automaticamente processadas. A requisição é feita utilizando a url, metodo e corpo salvos no registro. Se ela for bem sucedida, o status da request é salvo como "SUCCESS". Caso obtenha erro (como por exemplo, um problema de conexão), é salvo como "ERROR".

A cada um minuto, o Portal altera todas as requisições com erro e a definem como pendente, para que elas sejam reprocessadas.

### Mock ERP

Mock é uma classe ou conjunto de classes que simulam o comportamento de um sistema complexo para realizar testes. Expenses Portal possui um Mock que simula o comportamento de um ERP. Ele recebe o cadastro de usuário, cadastro de despesa e notifica o Portal de que o reembolso foi realizado.

Todo endpoint possui uma probabilidade simulada de obter erro de conexão de 50%. Se isso ocorrer, o Mock retornará uma mensagem de erro para o Portal e o registro da requisição será salvo como "ERROR".

Para uma aplicação real, será necessário alterar os endpoints de envio (presentes em application.yaml) e os atributos nos DTOs de envio.

### Funcionalidades Simuladas

#### Cadastrar usuário

Quando um usuário é cadastrado, o Portal envia seus dados para o ERP. Se bem sucedida, ele será cadastrado na tabela de pessoas.

##### Para o Mock
* URL: erp/person
* Método: POST
```
{
  "idPortal": "713f702c-1446-4d0e-bf56-0cafdb6a243d",
  "email": "erp.test@email.com",
  "cpf": "1262207754",
  "firstname": "erp",
  "lastname": "Test",
  "active": true
}
```
* tabela: MOCK_ERP_PEOPLE

#### Atualizar usuário

Quando um usuário tem seus dados editados, o Portal envia seus novos dados para o ERP. Se bem sucedido, o registro na tabela de pessoas do ERP será atualizado.

##### Para o Mock
* URL: erp/person/{ID DO USUÁRIO}
* Método: POST
* Tabela: MOCK_ERP_PEOPLE

> Suponha que um administrador cadastre um usuário e logo em seguida, edite seu registro. O Portal fará duas requisições quase que simultaneamente. Caso a requisição de cadastro obtenha um erro de conexão, a requisição de atualização obterá erro de ID não encontrado

#### Cadastrar Despesa

Após uma despesa ser aprovada, o Portal enviará seus dados ao ERP para que o setor financeiro da empresa possa paga-la

##### Para o Mock
* URL: erp/expense
* Método: POST
* Requisição:
```
{
  "idPortal": "9d877134-7efa-40fc-b52c-22de217343f5",
  "ordererPortalId": "77df057c-0f18-4ee6-bde5-057f8e4f6543",
  "approverPortalId": "a6ac0fd7-82b0-483a-bc19-5975026f5884",
  "productDesc": "Descricao do Produto",
  "unitType": "KG",
  "unitQnt": 2.0,
  "valuePerUnit": 30.0,
  "approvalDate": "2024-03-25T16:33:31.717368",
  "expenseStatus": "WAITING_PAYMENT"
}
```
* Tabela: MOCK_ERP_EXPENSE

> Caso a requisição de cadastro do solicitante ou aprovador obtenha erro, esse método retornará erro de ID não encontrado

#### Realizar Login

Para realizar requisições ao Portal, é necessário que o ERP obtenha o token de autenticação de através de uma requisição de login de um usuário com cargo ERP no Portal.

##### Para o Mock
* A cada 10 minutos, o Mock fará uma requisição de login ao Portal. Se bem sucedida, irá salvar o token em uma variável.

#### Notificar que a Despesa foi Paga

Após o financeiro realizar o reembolso, o ERP irá notificar o Portal para atualizar o status da despesa para "PAYED".

Para isso, é necessário que no cabeçalho da requisição, seja incluído o token de autenticação.

##### Para o Mock
* O Mock simula que o pagamento foi pago mudando o status das despesas com status "WAITING_PAYMENT" para "PAYED" a cada 2 minutos. Em seguida, executa a requisição HTTP de notificar que a despesa foi paga (*expense/{ID DA DESPESA}/notify_payment*)