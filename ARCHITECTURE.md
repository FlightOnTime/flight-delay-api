# Arquitetura do Projeto FlightOnTime

## 1. Introdução

Este documento descreve a arquitetura do projeto FlightOnTime, uma aplicação Spring Boot projetada para prever a probabilidade de atrasos de voos. A arquitetura foi planejada para ser modular, escalável e de fácil manutenção, seguindo as melhores práticas de desenvolvimento de software.

## 2. Arquitetura em Camadas (Layered Architecture)

O projeto segue uma arquitetura em camadas, que divide a aplicação em quatro camadas principais:

- **Camada de Apresentação (Presentation Layer)**: Responsável por expor a API REST para os clientes.
- **Camada de Serviço (Service Layer)**: Contém a lógica de negócio da aplicação.
- **Camada de Domínio (Domain Layer)**: Representa as entidades de negócio e a lógica de domínio.
- **Camada de Infraestrutura (Infrastructure Layer)**: Fornece implementações técnicas, como acesso a banco de dados e integração com serviços externos.

### 2.1. Controller

Os Controllers são responsáveis por gerenciar as requisições HTTP, receber dados dos clientes, e retornar as respostas. Eles atuam como a porta de entrada da aplicação, orquestrando a execução das operações na camada de serviço.

### 2.2. Service

A camada de serviço contém a lógica de negócio principal da aplicação. Ela é responsável por processar os dados recebidos dos controllers, aplicar as regras de negócio e interagir com a camada de domínio para obter e manipular os dados.

### 2.3. DTOs (Data Transfer Objects)

Os DTOs são utilizados para transferir dados entre as camadas, especialmente entre a camada de apresentação e a camada de serviço. Eles ajudam a desacoplar a representação dos dados da camada de apresentação da representação das entidades de domínio.

### 2.4. Repository

A camada de Repository é responsável por abstrair o acesso aos dados. As interfaces de repositório definem as operações de persistência de dados (CRUD - Create, Read, Update, Delete) para as entidades de domínio. O Spring Data JPA fornece a implementação em tempo de execução, simplificando a interação com o banco de dados.

## 3. Fluxo de Dados (Data Flow)

O fluxo de dados na aplicação segue um padrão unidirecional:

1. **Requisição HTTP**: O cliente envia uma requisição para um endpoint da API.
2. **Controller**: O Controller correspondente recebe a requisição e extrai os dados relevantes, convertendo-os em um DTO.
3. **Service**: O Controller invoca um método na camada de serviço, passando o DTO como parâmetro.
4. **Lógica de Negócio**: A camada de serviço executa a lógica de negócio, interagindo com a camada de `Repository` para acessar e persistir os dados do domínio.
5. **Resposta**: A camada de serviço retorna um resultado para o Controller.
6. **Resposta HTTP**: O Controller converte o resultado em um formato apropriado (por exemplo, JSON) e o envia como resposta para o cliente.

## 4. Padrões de Projeto (Design Patterns)

O projeto utiliza os seguintes padrões de projeto:

- **Injeção de Dependência (Dependency Injection)**: Utilizada pelo Spring Framework para gerenciar as dependências entre os componentes da aplicação, promovendo o baixo acoplamento.
- **Data Transfer Object (DTO)**: Para transferir dados entre as camadas da aplicação, evitando o acoplamento entre a camada de apresentação e o modelo de domínio.
- **Repository**: Para abstrair a lógica de acesso a dados, desacoplando a camada de serviço dos detalhes de implementação da persistência.
- **Singleton**: O Spring gerencia os beans como singletons por padrão, garantindo que haja apenas uma instância de cada componente.

## 5. Estrutura de Pacotes

A estrutura de pacotes do projeto reflete a arquitetura em camadas:

- `com.flightontime.api.controller`: Contém os controllers da API.
- `com.flightontime.api.dto`: Contém os DTOs.
- `com.flightontime.service`: Contém as classes de serviço.
- `com.flightontime.domain.model`: Contém as entidades de domínio.
- `com.flightontime.domain.repository`: Contém as interfaces de repositório.
- `com.flightontime.config`: Contém as classes de configuração do Spring.
