### Dependências do Projeto

O projeto possui as seguintes dependências:

- **Spring Boot Starter Web**
- **Spring Boot Starter Data JPA**
- **WireMock JRE8**
- **AWS SDK for Java BOM**
- **AWS SDK for Java - SQS**
- **Validation API**
- **Spring Boot Starter Test**
- **Lombok**
- **MySQL Connector/J**

### Configuração do banco de dados - Se quiser executar apontando para banco local

Para configurar o banco de dados MySQL, você pode executar o seguinte comando Docker:

```
docker run -p 3306:3306 --name mysql -e MYSQL_ROOT_PASSWORD=root -d -v MySqlData:/var/lib/mysql mysql
```

Esse comando irá iniciar um contêiner MySQL com as seguintes configurações:
- Nome do contêiner: mysql
- Porta mapeada: 3306
- Senha do root: root
- Volume montado para persistir os dados: MySqlData

Após executar esse comando, o banco de dados estará disponível para uso.

Se desejar usar o banco local, fazer o apontamento no application.properties.

---

### Criação do banco de dados

Antes de utilizar o sistema de votação em assembleia, é necessário criar o banco de dados `votacao_assembleia`. Você pode fazer isso executando o seguinte comando no cliente MySQL:

```
CREATE DATABASE votacao_assembleia;
```

Isso criará um novo banco de dados chamado `votacao_assembleia`, que será usado para armazenar os dados relacionados à votação em assembleia.

---

### Criação de uma nova pauta

Cria uma nova pauta.

```
curl -X POST -H "Content-Type: application/json" -d '{
  "descricao": "Descrição da pauta"
}' http://localhost:8080/api/v1/pautas
```
**Resposta:**

```json
{
  "id": 1,
  "descricao": "Primeira pauta para assembleia"
}
```

---

### Criação de uma nova sessão de votação

Cria uma nova sessão de votação para uma pauta específica.

```
curl -X POST -H "Content-Type: application/json" -d '{
  "pautaId": 1,
  "encerramento": "2023-05-30T10:00:00",
  "abertura": "2023-05-30T09:00:00"
}' http://localhost:8080/api/v1/sessoes-votacao
```

**Resposta:**

```json
{
  "id": 1,
  "pauta": {
    "id": 1,
    "descricao": "Primeira pauta para assembleia"
  },
  "abertura": "27/05/2023 09:00:00",
  "encerramento": "27/05/2023 18:00:00",
  "resultadoDiulgado": false
}
```

---

### Registro de um novo voto

Registra um novo voto para uma determinada pauta.

```
curl -X POST -H "Content-Type: application/json" -d '{
  "pautaId": 1,
  "cpf": "123456789",
  "voto": true
}' http://localhost:8080/api/v1/votos
```

---

### Obter votos de uma pauta

Obtém todos os votos registrados para uma determinada pauta.

```
curl -X GET http://localhost:8080/api/v1/pautas/1/votos
```

**Resposta:**

```json
[
  {
    "id": 1,
    "pauta": {
      "id": 1,
      "descricao": "Primeira pauta para assembleia"
    },
    "associado": {
      "id": 1,
      "cpf": "75416650028"
    },
    "voto": true
  }
]
```

---

## `EnviarResultadoVotacaoSchedule`

Esta classe representa um agendador de tarefas que é responsável por enviar os resultados das votações encerradas para uma fila.
Ela implementa a lógica para obter os resultados das votações, convertê-los em formato JSON e enviá-los para a fila por meio do serviço SQS.


# Configuração de Stubs para Validação de CPF no servidor WireMock

O método `configureCpfValidation()` é responsável por configurar os stubs para validação de CPF no servidor WireMock. Ele define dois stubs diferentes:

## 1. Stub para CPFs válidos

Este stub corresponde a requisições feitas para a rota "/cpf-validator" com um parâmetro de consulta "cpf" contendo exatamente 11 dígitos. A resposta retornada por esse stub possui as seguintes características:

- Código de status: 200 (OK)
- Tipo de conteúdo: "application/json"
- Corpo da resposta (JSON): `{"status": "ABLE_TO_VOTE"}`

Esse stub simula a resposta de um serviço de validação de CPF para um CPF válido, indicando que o usuário é capaz de votar.

## 2. Stub para CPFs inválidos

Este stub corresponde a requisições feitas para a rota "/cpf-validator" com um parâmetro de consulta "cpf" que contenha caracteres não numéricos ou tenha mais de 11 caracteres. A resposta retornada por esse stub possui as seguintes características:

- Código de status: 404 (Not Found)
- Tipo de conteúdo: "application/json"
- Corpo da resposta (JSON): `{"status": "UNABLE_TO_VOTE"}`

Esse stub simula a resposta de um serviço de validação de CPF para um CPF inválido, indicando que o usuário não é capaz de votar.

Esses stubs são utilizados para simular o comportamento de um serviço de validação de CPF.
