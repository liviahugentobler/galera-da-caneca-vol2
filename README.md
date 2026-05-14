# ☕ Galera da Caneca — Sistema de Gestão de Vendas

> **Projeto Integrador ll **  
> Curso Técnico em Desenvolvimento de Sistemas — SENAC EAD
> Responsável: Lívia Hugentobler  
> Refatoração do sistema desktop Java Swing com aplicação dos princípios SOLID

---

## 📋 Sobre o Projeto

O **Galera da Caneca** é um sistema de gestão de vendas desenvolvido como Projeto Integrador do curso técnico. Esta versão (v2.0) representa a **Etapa 6**, na qual o código do sistema desktop original (Java Swing) foi **refatorado e reorganizado** para separar corretamente as responsabilidades, eliminando code smells e aplicando os princípios SOLID — preparando a base para futura migração para uma camada web.

### O que mudou da v1.0 para a v2.0?

| Aspecto | v1.0 (Desktop) | v2.0 (Refatorado) |
|---|---|---|
| Arquitetura | 2 camadas (View + DAO) | 3 camadas (Model / DAO / Service) |
| Interfaces DAO | Não existiam | `GenericDAO<T,ID>` + interfaces específicas |
| Retorno de null | Frequente nos DAOs | Substituído por `Optional<T>` |
| Validações | Dentro das Views Swing | Centralizadas nos Services |
| JPAUtil | No pacote `dao` | Movido para `util` (Singleton thread-safe) |
| Dependências | Concretas (acoplamento alto) | Via interfaces (Injeção de Dependência) |

---

## 🏗️ Arquitetura

```
br.com.galeradacaneca
├── model/              ← Entidades JPA (dados puros, sem lógica)
├── dao/                ← Interfaces de acesso a dados
│   └── impl/           ← Implementações JPA das interfaces
├── service/            ← Interfaces de regras de negócio
│   └── impl/           ← Implementações com validações
├── util/               ← JPAUtil (Singleton)
└── main/               ← Principal.java (testes e ponto de entrada)
```

### Fluxo entre camadas

```
[ main / futura View Web ]
          │
          ▼
   [ Service Interface ]   ← regras de negócio e validações
          │
          ▼
   [ DAO Interface ]        ← contrato de persistência
          │
          ▼
   [ DAO Impl (JPA) ]       ← queries Hibernate/MySQL
          │
          ▼
      [ Banco MySQL ]
```

---

## 📁 Estrutura de Arquivos

```
GaleraDaCaneca_Refatorado/
├── pom.xml
└── src/main/java/br/com/galeradacaneca/
    ├── model/
    │   ├── Cargo.java
    │   ├── Cliente.java
    │   ├── Pagamento.java
    │   ├── Produto.java
    │   ├── Venda.java
    │   └── Vendedor.java
    ├── dao/
    │   ├── GenericDAO.java          ← interface CRUD genérica
    │   ├── CargoDAO.java
    │   ├── ClienteDAO.java
    │   ├── ProdutoDAO.java
    │   ├── VendaDAO.java
    │   ├── VendedorDAO.java
    │   └── impl/
    │       ├── CargoDAOImpl.java
    │       ├── ClienteDAOImpl.java
    │       ├── ProdutoDAOImpl.java
    │       ├── VendaDAOImpl.java
    │       └── VendedorDAOImpl.java
    ├── service/
    │   ├── ClienteService.java
    │   ├── ProdutoService.java
    │   ├── VendaService.java
    │   ├── VendedorService.java
    │   └── impl/
    │       ├── ClienteServiceImpl.java
    │       ├── ProdutoServiceImpl.java
    │       ├── VendaServiceImpl.java
    │       └── VendedorServiceImpl.java
    ├── util/
    │   └── JPAUtil.java
    └── main/
        └── Principal.java
```

---

## ✅ Princípios SOLID Aplicados

### S — Single Responsibility Principle
Cada classe tem uma única responsabilidade. As validações que estavam nas Views Swing foram extraídas para os Services. O `JPAUtil` foi movido para o pacote `util`, deixando claro que é infraestrutura.

### O — Open/Closed Principle
A interface `GenericDAO<T, ID>` e as interfaces de serviço permitem extensão (novas implementações) sem modificação do contrato existente.

### L — Liskov Substitution Principle
Todas as implementações (`ClienteDAOImpl`, `ProdutoServiceImpl`, etc.) podem substituir suas interfaces sem alterar o comportamento esperado.

### I — Interface Segregation Principle
Interfaces enxutas e específicas por domínio: `ClienteDAO`, `ProdutoDAO`, `VendedorDAO` e `VendaDAO` estendem `GenericDAO` acrescentando apenas o que é relevante para cada entidade.

### D — Dependency Inversion Principle
Os Services recebem suas dependências (DAOs) via construtor, dependendo sempre das interfaces, nunca das implementações concretas.

---

## 🔨 Tecnologias

- **Java 17**
- **Maven** (gerenciador de dependências)
- **Hibernate 5.6** (ORM / JPA)
- **MySQL 8**
- **NetBeans 13**

---

## ⚙️ Como Configurar e Executar

### Pré-requisitos

- Java JDK 17+
- Maven 3.6+
- MySQL 8 rodando localmente
- NetBeans 13 (ou IntelliJ / VS Code com suporte a Maven)

### 1. Clonar o repositório

```bash
git clone https://github.com/SEU_USUARIO/GaleraDaCaneca-Refatorado.git
cd GaleraDaCaneca-Refatorado
```

### 2. Criar o banco de dados

Certifique-se de que o banco `galera_da_caneca` existe no seu MySQL. Você pode usar o script SQL do projeto desktop (Etapa 5) para criar as tabelas.

### 3. Configurar o persistence.xml

Crie o arquivo em `src/main/resources/META-INF/persistence.xml` com suas credenciais:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.2"
             xmlns="http://xmlns.jcp.org/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence
             http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd">
    <persistence-unit name="GaleraDaCanecaPU" transaction-type="RESOURCE_LOCAL">
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>

        <class>br.com.galeradacaneca.model.Cargo</class>
        <class>br.com.galeradacaneca.model.Cliente</class>
        <class>br.com.galeradacaneca.model.Produto</class>
        <class>br.com.galeradacaneca.model.Vendedor</class>
        <class>br.com.galeradacaneca.model.Venda</class>
        <class>br.com.galeradacaneca.model.Pagamento</class>

        <properties>
            <property name="javax.persistence.jdbc.driver"   value="com.mysql.cj.jdbc.Driver"/>
            <property name="javax.persistence.jdbc.url"      value="jdbc:mysql://localhost/galera_da_caneca"/>
            <property name="javax.persistence.jdbc.user"     value="root"/>
            <property name="javax.persistence.jdbc.password" value="SUA_SENHA"/>
            <property name="hibernate.dialect"               value="org.hibernate.dialect.MySQL8Dialect"/>
            <property name="hibernate.show_sql"              value="true"/>
            <property name="hibernate.hbm2ddl.auto"         value="validate"/>
        </properties>
    </persistence-unit>
</persistence>
```

> ⚠️ **Não suba o `persistence.xml` com sua senha real para o GitHub!**  
> Adicione `src/main/resources/META-INF/persistence.xml` ao `.gitignore`.

### 4. Compilar o projeto

```bash
mvn clean install
```

### 5. Executar os testes do main()

```bash
mvn exec:java -Dexec.mainClass="br.com.galeradacaneca.main.Principal"
```

Ou pelo NetBeans: abra `Principal.java` → botão direito → **Run File**.

---

## 🧪 Resultado dos Testes

Ao rodar `Principal.java` com o banco populado, todos os 8 testes devem passar:

```
=== Galera da Caneca — Projeto Refatorado v2.0 ===

[OK] Conexão com banco de dados estabelecida.
[OK] Camadas instanciadas via Injeção de Dependência.

[TESTE 1] listarTodos (Cliente): 11 registro(s) encontrado(s). [OK]
[TESTE 2] listarTodos (Produto): 19 registro(s) encontrado(s). [OK]
[TESTE 3] listarTodos (Vendedor): 6 registro(s) encontrado(s). [OK]
[TESTE 4] listarTodas (Venda): 49 venda(s). Total geral: R$ 1491.70 [OK]
[TESTE 5] Validação de CPF inválido capturada corretamente: "CPF deve conter 11 dígitos numéricos." [OK]
[TESTE 6] Validação de preço negativo capturada corretamente: "Preço inválido." [OK]
[TESTE 7] autenticar com credenciais vazias retornou Optional.empty(). [OK]
[TESTE 8] Validação de senha curta capturada corretamente: "A nova senha deve ter pelo menos 4 caracteres." [OK]

=== Resultado: TODOS OS TESTES PASSARAM ===
```

---

## 📄 Licença

Projeto acadêmico — SENAC EAD. 
