# ✈️ FlightOnTime



## 🎯 Objetivo do Projeto



O principal objetivo deste projeto é fornecer uma API robusta e escalável para prever atrasos de voos. Utilizando um modelo de aprendizado de máquina treinado, a API pode fornecer previsões em tempo real, ajudando companhias aéreas e passageiros a gerenciar melhor seus planos de viagem.



## 🏆 Contexto do Hackathon



Este projeto foi desenvolvido como parte do \*\*Hackathon\*\*, uma competição de programação de 48 horas focada em criar soluções inovadoras para a indústria aérea. Nossa equipe buscou enfrentar o desafio dos atrasos de voos construindo uma API preditiva que pudesse ser facilmente integrada aos sistemas existentes das companhias aéreas.



## 💻 Tecnologias Utilizadas



- **Java 17:** A linguagem de programação principal da aplicação.

- **Spring Boot:** Framework para criar aplicações Spring independentes e prontas para produção.

- **Spring Web MVC:** Fornece arquitetura Model-View-Controller e componentes prontos para desenvolver aplicações web flexíveis e desacopladas.

- **Lombok:** Uma biblioteca Java que se conecta automaticamente ao seu editor e ferramentas de build, ajudando a escrever menos código repetitivo (boilerplate).

- **SpringDoc OpenAPI:** Biblioteca que gera documentação OpenAPI 3.0 para projetos Spring Boot.

- **Maven:** Uma poderosa ferramenta de gerenciamento de projetos baseada no conceito de Modelo de Objeto de Projeto (POM).



## 🚀 Como Executar Localmente



Para rodar o projeto localmente, certifique-se de ter o seguinte instalado:



- **Java 17:** Certifique-se de ter o JDK do Java 17 instalado e configurado no seu sistema.

- **Maven:** Este projeto usa o Maven para gerenciamento de dependências.



Uma vez que você tenha os pré-requisitos, pode executar a aplicação com o seguinte comando:



```bash

mvn spring-boot:run

```



A API estará disponível em: [http://localhost:8080](http://localhost:8080)



## 📚 Documentação da API



A documentação da API é gerada automaticamente usando o SpringDoc OpenAPI e pode ser acessada em:



👉 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)



## 🔗 Principais Endpoints da API



A seguir estão os principais endpoints da API disponíveis na aplicação:



### Endpoint de Teste



- `GET /predict/hello`

&nbsp; - **Descrição:** Um endpoint de teste simples para verificar se a API está rodando corretamente.

&nbsp; - **Resposta:** Retorna uma mensagem "Hello World!".



## 🤝 Fluxo de Trabalho da Equipe



Para garantir um processo de desenvolvimento fluido e eficiente, nossa equipe segue um fluxo de trabalho simples:



1.  **Branching:** Todas as novas funcionalidades e correções de bugs devem ser desenvolvidas em branches separadas, seguindo a convenção de nomenclatura `feature/` ou `fix/`.

2.  **Code Review:** Assim que uma funcionalidade estiver completa, um pull request deve ser aberto para revisão de código. Pelo menos um membro da equipe deve aprovar as alterações antes que possam ser mescladas (merged).

3.  **Testes:** Todo novo código deve ser acompanhado por testes unitários para garantir que está funcionando corretamente e não introduz regressões.

