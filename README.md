# Orders Microservices

Repositório com três microserviços Java/Spring Boot que compõem um sistema de pedidos, pagamentos e usuários. A orquestração para execução local em containers é feita via Docker Compose, incluindo MySQL e RabbitMQ.

Este README descreve a pilha tecnológica, como executar localmente e via Docker, scripts úteis, variáveis de ambiente, estrutura do projeto, testes e itens pendentes (TODOs) quando a informação não está explícita no código.

## Visão Geral da Arquitetura
- Banco de dados: MySQL (container `orders-mysql`)
- Mensageria: RabbitMQ (com console de administração)
- Microserviços:
  - users-service (porta 8080)
  - orders-service (porta 8081)
  - payment-service (porta 8082)

Relações (conforme docker-compose.yml):
- users-service depende do RabbitMQ e do MySQL para persistência.
- orders-service depende do MySQL e do RabbitMQ.
- payment-service depende do RabbitMQ.

## Stack 
- Linguagem: Java 17
- Framework: Spring Boot 3.5.5
- Build/Package Manager: Maven
- Conteinerização: Docker + Docker Compose
- Banco de dados: MySQL
- Mensageria: RabbitMQ

Dependências principais (por serviço):
- users-service
  - spring-boot-starter-web, data-jpa, security, validation, actuator
  - mysql-connector-j (runtime)
  - com.auth0:java-jwt
- orders-service
  - spring-boot-starter-web, data-jpa, security, validation, actuator
  - spring-boot-starter-amqp
  - mysql-connector-j (runtime)
  - com.auth0:java-jwt
- payment-service
  - spring-boot-starter-web
  - spring-boot-starter-amqp

## Requisitos
- Java 17 (JDK)
- Maven 3.9+
- Docker 20+ e Docker Compose

## Variáveis de Ambiente (importantes)
Definidas no docker-compose.yml (usadas pelo Spring Boot via `SPRING_*`):
- Comuns ao MySQL (users-service e orders-service):
  - `SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/orders`
  - `SPRING_DATASOURCE_USERNAME=admin`
  - `SPRING_DATASOURCE_PASSWORD=admin`
- Comuns ao RabbitMQ (todos que usam AMQP):
  - `SPRING_RABBITMQ_HOST=rabbitmq`
  - `SPRING_RABBITMQ_PORT=5672`

Outras propriedades relevantes dos serviços (application.properties):
- `spring.jpa.hibernate.ddl-auto=update`
- `spring.jpa.show-sql=true` (em users e orders)
- Driver do MySQL configurado em users e orders.

Observação sobre portas:
- As portas externas são mapeadas no docker-compose:
  - users-service: 8080 -> 8080
  - orders-service: 8081 -> 8081
  - payment-service: 8082 -> 8082
- Internamente, cada serviço usa a porta padrão do Spring (8080) se não houver `server.port` definido. Como o compose mapeia portas distintas, cada imagem deve expor seu serviço na porta que o jar iniciar. Caso algum serviço precise alterar a porta interna, defina `server.port` no application.properties ou via env `SERVER_PORT`.

## Como Executar

### 1) Usando Docker Compose (recomendado)
Pré-requisito: gerar os jars em `target/` antes do build das imagens (os Dockerfiles copiam os jars já construídos; não há estágio de build do Maven dentro das imagens).

- Build dos jars localmente (na raiz do repo):
  - Linux/macOS:
    - `mvn -q -DskipTests -f users-service/pom.xml clean package`
    - `mvn -q -DskipTests -f orders-service/pom.xml clean package`
    - `mvn -q -DskipTests -f payment-service/pom.xml clean package`
  - Ou em um comando para todos, respeitando dependências:
    - `mvn -q -DskipTests -pl users-service,orders-service,payment-service -am clean package`

- Subir toda a stack:
  - `docker compose up --build`

Serviços e consoles:
- MySQL: porta 3306
- RabbitMQ: broker 5672; console de administração em http://localhost:15672 (usuário/senha padrão do container: guest/guest)
- users-service: http://localhost:8080
- orders-service: http://localhost:8081
- payment-service: http://localhost:8082

Parar e remover containers:
- `docker compose down`

### 2) Executar localmente sem Docker
Pré-requisitos: um MySQL acessível e (se necessário) RabbitMQ local.
- Ajuste as variáveis de ambiente ou `application.properties` para apontar para seu MySQL/RabbitMQ local.
- Rodar cada serviço via Maven:
  - users-service: `mvn -f users-service/pom.xml spring-boot:run`
  - orders-service: `mvn -f orders-service/pom.xml spring-boot:run`
  - payment-service: `mvn -f payment-service/pom.xml spring-boot:run`

## Scripts e Comandos Úteis
- Build com testes: `mvn -f <serviço>/pom.xml clean package`
- Build sem testes: `mvn -f <serviço>/pom.xml -DskipTests clean package`
- Rodar serviço: `mvn -f <serviço>/pom.xml spring-boot:run`
- Rodar todos (multi-módulos seletivos): `mvn -pl users-service,orders-service,payment-service -am clean package`
- Testes: `mvn -f <serviço>/pom.xml test`

## Estrutura do Projeto
- docker-compose.yml
- users-service
  - Dockerfile
  - pom.xml
  - src/main/java/br/com/joao/users_service/UsersServiceApplication.java
  - src/main/resources/application.properties
  - src/test/java
- orders-service
  - Dockerfile
  - pom.xml
  - src/main/java/br/com/joao/orders_service/OrdersServiceApplication.java
  - src/main/resources/application.properties
  - src/test/java
- payment-service
  - Dockerfile
  - pom.xml
  - src/main/java/br/com/joao/payment_service/PaymentServiceApplication.java
  - src/main/resources/application.properties
  - src/test/java

## Segurança/Autenticação
- Os serviços `users-service` e `orders-service` incluem `spring-boot-starter-security` e a biblioteca `com.auth0:java-jwt`.

## Migrações de Banco de Dados
- `spring.jpa.hibernate.ddl-auto=update` está habilitado em users/orders.

