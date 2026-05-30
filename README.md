# 📬 Notification API

Sistema de notificações distribuído construído com Java + Spring Boot, RabbitMQ e integrações reais com serviços externos de envio.

---

## 🧩 Arquitetura

```
Cliente
  → POST /notifications (com API Key)
    → API valida, autentica e salva no banco
      → Publica mensagem na fila RabbitMQ (com prioridade)
        → Worker consome da fila
          → Resolve template (se aplicável)
            → Envia via serviço externo (Resend / Twilio / Firebase)
              → Atualiza status no banco (SENT)
              → Em caso de falha: retry até 3x → DLQ → status FAILED
```

### Componentes

| Camada | Tecnologia |
|---|---|
| API REST | Java 21 + Spring Boot 4 |
| Fila de mensagens | RabbitMQ |
| Banco de dados | PostgreSQL |
| E-mail | Resend |
| SMS | Twilio |
| Push Notification | Firebase Cloud Messaging |
| Containerização | Docker + Docker Compose |

---

## ✅ Funcionalidades

- Envio de notificações via **e-mail**, **SMS** e **push notification**
- Processamento **assíncrono** com RabbitMQ
- **Prioridade de mensagens** (HIGH=10, MEDIUM=5, LOW=1)
- **Retry automático** com backoff exponencial (3 tentativas)
- **Dead Letter Queue** — mensagens que falharam ficam arquivadas com status `FAILED`
- **Templates com placeholders** — `"Olá {{nome}}, seu pedido {{pedido}} foi confirmado!"`
- **Autenticação via API Key** por cliente, armazenada no banco
- **Validação de recipient** por canal (e-mail, telefone, device token)
- **GlobalExceptionHandler** com respostas de erro controladas

---

## 🚀 Como rodar localmente

### Pré-requisitos

- Java 21+
- Maven
- Docker + Docker Compose

### 1. Clone o repositório

```bash
git clone https://github.com/luanferro/notification-api.git
cd notification-api
```

### 2. Configure as variáveis de ambiente

Cria um arquivo `.env` na raiz do projeto baseado no `.env.example`:

```bash
cp .env.example .env
```

Preenche os valores no `.env`:

```env
DB_USER=admin
DB_PASS=admin
RABBITMQ_USER=admin
RABBITMQ_PASS=admin
RESEND_API_KEY=sua_chave_resend
TWILIO_ACCOUNT_SID=seu_account_sid
TWILIO_AUTH_TOKEN=seu_auth_token
TWILIO_PHONE_NUMBER=seu_numero_twilio
```

### 3. Sobe os serviços de infraestrutura

```bash
docker compose up -d
```

Isso sobe o **PostgreSQL** na porta `5433` e o **RabbitMQ** na porta `5672` (painel em `http://localhost:15672`).

### 4. Roda a aplicação

```bash
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

---

## 🔑 Autenticação

Todas as requisições precisam do header `X-API-KEY` com uma chave válida.

### Gerando uma API Key

```http
POST /keys
Content-Type: application/json

{
  "clientName": "meu-sistema"
}
```

Resposta:
```json
{
  "id": "uuid",
  "clientName": "meu-sistema",
  "apiKey": "chave-gerada-automaticamente",
  "active": true,
  "createdAt": "2026-01-01T00:00:00"
}
```

Use o valor de `apiKey` no header `X-API-KEY` das próximas requisições.

---

## 📡 Endpoints

### Notificações

#### Criar notificação com mensagem direta

```http
POST /notifications
X-API-KEY: sua-chave
Content-Type: application/json

{
  "channel": "EMAIL",
  "recipient": "usuario@email.com",
  "message": "Olá! Sua conta foi criada com sucesso.",
  "priority": "HIGH"
}
```

#### Criar notificação via template

```http
POST /notifications
X-API-KEY: sua-chave
Content-Type: application/json

{
  "channel": "EMAIL",
  "recipient": "usuario@email.com",
  "templateName": "order-confirmation",
  "data": {
    "nome": "João",
    "pedido": "123"
  },
  "priority": "MEDIUM"
}
```

#### Consultar status de uma notificação

```http
GET /notifications/{id}
X-API-KEY: sua-chave
```

Resposta:
```json
{
  "id": "uuid",
  "channel": "EMAIL",
  "recipient": "usuario@email.com",
  "message": "Olá João, seu pedido 123 foi confirmado!",
  "priority": "MEDIUM",
  "status": "SENT",
  "createdAt": "2026-01-01T00:00:00",
  "updatedAt": "2026-01-01T00:00:01"
}
```

**Status possíveis:** `PENDING`, `SENT`, `FAILED`

---

### Templates

#### Criar template

```http
POST /templates
X-API-KEY: sua-chave
Content-Type: application/json

{
  "name": "order-confirmation",
  "content": "Olá {{nome}}, seu pedido {{pedido}} foi confirmado!",
  "channel": "EMAIL"
}
```

#### Buscar template por nome

```http
GET /templates/{name}
X-API-KEY: sua-chave
```

---

## 📋 Canais e validações de recipient

| Canal | Formato esperado | Exemplo |
|---|---|---|
| `EMAIL` | E-mail válido | `usuario@email.com` |
| `SMS` | Telefone com DDI | `+5511999999999` |
| `PUSH` | Device token (string) | `token_do_dispositivo` |

---

## 🏗️ Estrutura do projeto

```
src/main/java/com/luanferro/notification_api/
├── config/          → Configurações (RabbitMQ, Firebase, ApiKeyFilter)
├── controller/      → Endpoints REST
├── dto/             → Objetos de entrada/saída da API
├── entity/          → Entidades JPA
│   └── enums/       → Enums (canal, prioridade, status)
├── messaging/       → Producer, Consumer e Senders
│   └── sender/      → Interface e implementações por canal
│       └── impl/    → EmailSender, SmsSender, PushSender
├── repository/      → Interfaces JPA
└── service/         → Lógica de negócio
```

---

## 🔁 Fluxo de retry e DLQ

| Tentativa | Intervalo |
|---|---|
| 1ª (inicial) | imediato |
| 2ª | 5 segundos |
| 3ª | 10 segundos |
| 4ª | 20 segundos |
| Após 4 tentativas | Mensagem vai para a DLQ com status `FAILED` |

---

## 🐳 Serviços Docker

| Serviço | Porta | Credenciais |
|---|---|---|
| PostgreSQL | 5433 | admin / admin |
| RabbitMQ | 5672 | admin / admin |
| RabbitMQ Management | 15672 | admin / admin |

---

## 🛠️ Tecnologias utilizadas

- **Java 21**
- **Spring Boot 4** (Web, AMQP, Data JPA, Validation)
- **RabbitMQ** com prioridade nativa e Dead Letter Queue
- **PostgreSQL**
- **Resend** — envio de e-mails
- **Twilio** — envio de SMS
- **Firebase Cloud Messaging** — push notifications
- **Lombok**
- **Docker + Docker Compose**