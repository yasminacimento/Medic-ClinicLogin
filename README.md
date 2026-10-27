# Medic-ClinicLogin
Aplicação web em Java (Jakarta EE + MySQL) que simula o sistema de uma clínica médica, com cadastro de pacientes, médicos, funcionários, consultórios, consultas e receitas. Conta com cadastro de contas e login seguro, com senhas protegidas por BCrypt, proteção CSRF e controle de sessão.

Projeto desenvolvido para a disciplina de Desenvolvimento Web — IFSULDEMINAS.

✨ Funcionalidades

Cadastros da clínica

Pacientes, médicos e funcionários
Consultórios, especialidades e convênios
Consultas e prontuários
Tipos de exame e solicitações de exame
Remédios e receitas (com vários medicamentos por receita)

Contas e segurança

Cadastro de usuário com validação e bloqueio de nome/e-mail duplicados
Login com senha protegida por BCrypt (salt aleatório, custo 12): o banco guarda só o hash, nunca a senha
Filtro de autenticação: as páginas da clínica só abrem com login
Proteção CSRF em todos os formulários de cadastro, login, recuperação e logout
Sessão renovada a cada login, cookie HttpOnly e expiração após 30 minutos sem uso
Recuperação de senha pelo e-mail cadastrado (versão simplificada para a aula, veja Limitações)

Receitas em um único fluxo

Monta a receita, adiciona e remove medicamentos e finaliza tudo de uma vez
Gravação em transação JDBC: se algum item falhar, o rollback desfaz tudo

Interface

Listagens com busca, ordenação e paginação
Formulários organizados por grupos e opção de mostrar/ocultar a senha
CSS e JavaScript locais, sem depender de CDN
🛠️ Tecnologias
Camada	Tecnologia
Linguagem	Java 11+
Plataforma	Jakarta EE 10 (Servlets + JSP)
Servidor	GlassFish
Banco de dados	MySQL 8 (JDBC)
Segurança	jBCrypt
Testes	JUnit 5 + H2 em memória
Build / IDE	Maven + NetBeans
🚀 Como executar
Pré-requisitos
JDK 11 ou superior
MySQL 8
GlassFish compatível com Jakarta EE 10
NetBeans (ou Maven pela linha de comando)
Passo a passo
Clone o repositório
bash
   git clone https://github.com/yasminacimento/Medic-ClinicLogin.git
Crie o banco de dados Execute o arquivo sql/ClinicaMedica_Atualizado.sql no MySQL (ou no HeidiSQL). Ele cria o banco clinica_medica com as 14 tabelas, as chaves estrangeiras e a view de itens de receita. O script só tem a estrutura, sem dados nem contas, e pode ser executado de novo sem apagar nada.
Configure a conexão Em src/main/java/br/edu/ifsuldeminas/clinicamedica/modelo/dao/ConnectionFactory.java, ajuste endereço, usuário e senha do seu MySQL:
java
   private static final String DB_URL = "jdbc:mysql://localhost:3306/clinica_medica?useSSL=false";
   private static final String DB_USER = "root";
   private static final String DB_PASSWORD = "sua_senha";
Rode a aplicação Abra o projeto no NetBeans e execute no GlassFish, ou gere o .war com:
bash
   mvn package

e faça o deploy de target/ClinicaMedica-1.0-SNAPSHOT.war.

Acesse Abra http://localhost:8080/ClinicaMedica/, clique em Cadastrar usuário e crie sua conta. Não existe usuário ou senha padrão.
🧪 Testes
bash
mvn test

Os testes usam um banco H2 em memória (modo MySQL), então não mexem no banco da clínica. Eles verificam:

geração e verificação de senhas com BCrypt
o filtro de acesso (AuthFilter)
a recuperação de senha
a gravação de receitas com seus itens, incluindo o rollback
📁 Estrutura do projeto
├── sql/
│   └── ClinicaMedica_Atualizado.sql    # script de criação do banco
├── src/main/java/br/edu/ifsuldeminas/clinicamedica/
│   ├── controlador/                    # servlets (Login, Paciente, Receita...)
│   ├── modelo/
│   │   ├── dao/                        # acesso ao banco (JDBC)
│   │   └── entidade/                   # classes de domínio
│   └── servico/                        # segurança, filtro de login e regras
├── src/main/webapp/                    # páginas JSP, CSS e JavaScript
├── src/test/java/                      # testes JUnit
└── pom.xml
Onde está cada parte da segurança
Arquivo	O que faz
servico/Seguranca.java	Gera e verifica o hash BCrypt das senhas
servico/AuthFilter.java	Bloqueia as páginas da clínica para quem não fez login
controlador/LoginControlador.java	Cadastro, login e logout
servico/RecuperacaoSenhaService.java	Valida e salva a nova senha
modelo/dao/UsuarioDAO.java	Consultas de usuário com parâmetros JDBC (evita SQL injection)
⚠️ Limitações

Este é um projeto acadêmico, então algumas partes foram simplificadas de propósito:

Recuperação de senha: basta informar um e-mail cadastrado e a nova senha. Não há envio de e-mail nem link de confirmação, então qualquer pessoa que saiba o e-mail de alguém pode trocar a senha dessa conta. Não use esse fluxo em produção.
Credenciais do banco: ficam escritas direto no ConnectionFactory.java. Em um sistema real, elas ficariam em variáveis de ambiente.
👥 Autores
Nome do autor — @usuario-github
Yasmin — @yasminacimento
