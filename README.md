# Clínica Médica

Projeto Maven WAR com Java 11+, Jakarta EE 10, MySQL 8 e GlassFish, utilizado pelo NetBeans IDE 30.

## Execução e entrega

1. Para uma instalação nova, execute o arquivo completo `sql/ClinicaMedica_Atualizado.sql` no MySQL 8 ou no HeidiSQL. Ele cria o banco `clinica_medica`, as 14 tabelas atuais, as chaves estrangeiras e a view de itens de receita.
2. Esse é o único SQL necessário: inclui a estrutura da clínica e a tabela `usuario`, usada na autenticação e recuperação de senha. Contém somente a estrutura, sem cadastros, contas, senhas nem dados clínicos. O antigo script de autenticação foi incorporado e removido para evitar duplicidade.
3. Confira a conexão local em `ConnectionFactory.java` e ajuste endereço, porta, usuário e senha do MySQL para a máquina de destino.
4. Abra o projeto no NetBeans, compile e execute no GlassFish compatível com Jakarta EE 10.
5. Acesse `http://localhost:8080/ClinicaMedica/`, clique em **Cadastrar usuário** e crie sua conta.

Nenhuma conta padrão ou senha inicial é incluída. SMTP, conta de Google, senha de aplicativo e arquivos de e-mail não são necessários.
A máquina do professor precisa apenas do ambiente Java/GlassFish e do banco configurado.

O SQL atualizado não contém comandos de exclusão e pode ser executado novamente sem apagar os dados.
Como usa `CREATE TABLE IF NOT EXISTS`, ele não altera a estrutura de tabelas que já existam; sua finalidade é construir um banco novo.
As tabelas são criadas na ordem dos relacionamentos, sem desativar a validação das chaves estrangeiras.
Depois da criação, cadastre os dados básicos e uma consulta para poder emitir uma receita com medicamentos.

## Recuperação simplificada para aula

Esta versão permite redefinir a senha diretamente com o e-mail cadastrado, por solicitação do autor do projeto.
Não há envio de e-mail, link de recuperação ou token de recuperação.

1. Na tela de login, clique em **Esqueci minha senha**.
2. Informe o e-mail cadastrado, a nova senha e a confirmação.
3. Clique em **Salvar nova senha**.
4. Faça login com seu nome de usuário e a nova senha.

A nova senha continua sendo armazenada com BCrypt. Um endereço não cadastrado não cria usuário nem altera outras contas.
A troca exige pelo menos 8 caracteres, no máximo 72 bytes UTF-8 e confirmação correspondente.

**Limitação intencional da demonstração:** qualquer pessoa que conheça um e-mail cadastrado pode redefinir sua senha.
Esse fluxo não comprova a posse do e-mail e não deve ser utilizado em produção.
A simplificação substitui os requisitos de envio de e-mail, validade e uso único de tokens previstos na atividade original.

Se a versão anterior já tiver criado a tabela `recuperacao_senha`, ela pode permanecer no banco: a aplicação atual não a utiliza.
Não é necessário apagar tabelas para executar a atualização.

## Receitas e medicamentos em um unico cadastro

O menu **Receitas** concentra o registro principal e seus itens. O antigo atalho de itens redireciona para esse fluxo.

1. Selecione a consulta pelo paciente, médico e horário, informe a data e as observações e clique em **Continuar para medicamentos**.
2. Escolha um medicamento do catálogo, informe dosagem e posologia e clique em **Adicionar à receita**. Repita para os outros medicamentos.
3. Revise a lista e remova um item se precisar corrigi-lo. Os campos da consulta continuam editáveis.
4. Clique em **Finalizar receita**. O sistema insere a receita, obtém o ID gerado e insere seus itens usando a mesma conexão JDBC.
5. A transação só é confirmada quando todos os registros são gravados. Se qualquer item falhar, o rollback desfaz toda a operação.

Antes da finalização, os dados ficam em um rascunho na sessão. **Cancelar edição** descarta esse rascunho sem criar uma receita vazia.
Ao editar uma receita existente, o cancelamento mantém os dados já salvos; a confirmação atualiza o principal e seus itens juntos.
A exclusão também pede confirmação e remove os itens e a receita na mesma transação.
O cadastro não exige informar manualmente um ID de receita nem acessar uma tela separada para seus medicamentos.
Cada receita aceita de 1 a 50 medicamentos, sem duplicidade, com dosagem e posologia preenchidas.

Para explicar ao professor, acompanhe `ReceitaControlador` (montagem do cadastro), `RascunhoReceita` (estado na sessão),
`ReceitaService` (validação) e `ReceitaDao.salvarComItens` (principal, chave gerada, itens, commit e rollback).
São utilizadas as tabelas `receita`, `item_receita` e o catálogo `remedio` do banco existente, sem mudança de esquema.

## Navegação e apresentação

O visual utiliza fundo claro, verde discreto, formulários alinhados e tabelas com ações consistentes.
O menu agrupa atendimento e cadastros, com acesso direto a receitas. A página inicial permite buscar módulos.
As listagens têm busca por texto, ordenação e paginação; tabelas extensas permitem rolagem em telas pequenas.
Os formulários de autenticação permitem mostrar ou ocultar a senha. CSS e JavaScript são locais, sem dependência de CDN.
O espaçamento segue uma escala compartilhada, com campos alinhados, ações ao final dos formulários e listas separadas dos cadastros.
Pacientes, médicos e funcionários têm grupos próprios para dados pessoais/profissionais e contato.
As grades ficam declaradas nas páginas JSP, de modo que a organização dos formulários funciona mesmo sem JavaScript.
Os endereços de CSS e JavaScript incluem uma versão para evitar a reutilização de recursos antigos pelo navegador após esta atualização.

## Como explicar a implementação

- **Senha:** `Seguranca` gera BCrypt com salt aleatório e custo 12. O banco armazena somente o hash de 60 caracteres; `checkpw` verifica a senha informada.
- **Cadastro:** `LoginControlador` valida os campos e `UsuarioDAO` usa parâmetros JDBC. Nome e e-mail duplicados são rejeitados.
- **Sessão:** um login válido invalida a sessão anterior e cria outra com `user` e `usuarioId`. A senha e seu hash não ficam na sessão. A sessão expira após 30 minutos de inatividade.
- **Acesso:** `AuthFilter` verifica a sessão existente e o atributo `user` antes de liberar páginas e controladores da clínica.
- **Logout:** um formulário POST chama `session.invalidate()` e redireciona ao login.
- **Recuperação:** `RecuperacaoSenhaService` normaliza o e-mail, valida a senha e a confirmação, gera BCrypt e chama `UsuarioDAO.atualizarSenhaPorEmail`.
- **Formulários:** cadastro, login, recuperação e logout usam POST com token CSRF. Esse token protege os formulários e não é um token de recuperação de senha.
- **Respostas:** não são armazenadas em cache; o cookie de sessão é HttpOnly e o ID não é propagado na URL.

A recuperação encerra a sessão do navegador que fez a troca. Outras sessões já abertas permanecem até logout ou timeout.
Login, cadastro, recuperação e arquivos estáticos são públicos; os módulos da clínica exigem login.

## Verificação

O arquivo `sql/ClinicaMedica_Atualizado.sql` foi executado em um banco temporário no MySQL 8:
foram conferidas 14 tabelas, 14 chaves estrangeiras e uma view. Uma segunda execução preservou um registro de teste.
O banco temporário foi removido após a validação, sem modificar os dados da clínica.

Execute `mvn test` para validar BCrypt, cadastro, duplicidade, filtro de acesso, recuperação direta por e-mail
e a integridade da gravação de receitas e itens, incluindo rollback na criação e na edição.
Os testes de dados usam H2 em memória no modo MySQL, sem alterar o banco da clínica.
Use `mvn package` para gerar `target/ClinicaMedica-1.0-SNAPSHOT.war`.

Com GlassFish e MySQL ativos e a aplicação atualizada, execute
`mvn -Dtest=AutenticacaoGlassFishIT test` para o teste completo por HTTP e JDBC.
Ele cria uma conta aleatória, testa cadastro, login, logout, recuperação sem autenticação, validação de confirmação
e rejeição da senha antiga, e remove apenas a conta que criou.
Se a URL for diferente, informe `-Dclinica.it.url=...`.

Execute `mvn -Dtest=ReceitaGlassFishIT test` para conferir o processo completo de receitas no servidor e no MySQL:
montagem, cancelamento, inclusão e remoção de medicamentos, finalização, edição, prevenção de duplicação e exclusão.
Esse teste também verifica a abertura de todos os módulos e remove somente a conta e as receitas temporárias que criou.

Para a demonstração manual, tente abrir uma página ou controlador da clínica sem login e confirme o redirecionamento.
Cadastre, entre, navegue e saia. Depois redefina a senha pelo e-mail cadastrado e confirme que apenas a nova senha permite entrar.
