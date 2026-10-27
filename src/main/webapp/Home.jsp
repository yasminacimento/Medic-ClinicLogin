<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Início · Clínica Médica</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css?v=20261026-2">
</head>
<body>
<jsp:include page="Menu.jsp"/>
<main class="container home-content" id="conteudo">
    <div class="page-heading">
        <div><span class="eyebrow">Sua rotina, organizada</span><h1>Visão geral</h1>
            <p>Atendimentos, pacientes e prescrições em um só lugar.</p></div>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}${URL_BASE}/ConsultaControlador?opcao=cancelar">Cadastrar consulta</a>
    </div>
    <section class="home-feature" aria-labelledby="receitas-destaque">
        <div><span class="eyebrow">Prescrições</span><h2 id="receitas-destaque">Uma receita, todos os medicamentos.</h2>
            <p>Selecione a consulta, monte a prescrição e finalize o cadastro na mesma tela.</p></div>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}${URL_BASE}/ReceitaControlador?opcao=nova">Criar receita <span aria-hidden="true">→</span></a>
    </section>
    <div class="module-heading">
        <div><h2>Acesse os módulos</h2><small>Escolha por onde começar.</small></div>
        <input type="search" class="module-search" data-module-search placeholder="Buscar módulo..." aria-label="Buscar módulo">
    </div>
    <div class="module-grid">
        <a class="module-card" href="${pageContext.request.contextPath}${URL_BASE}/ConsultaControlador?opcao=cancelar"><small>Atendimento</small><h3>Consultas</h3><p>Organize horários, pacientes e médicos.</p><span class="module-open">Acessar →</span></a>
        <a class="module-card" href="${pageContext.request.contextPath}${URL_BASE}/PacienteControlador?opcao=cancelar"><small>Atendimento</small><h3>Pacientes</h3><p>Consulte e atualize os dados dos pacientes.</p><span class="module-open">Acessar →</span></a>
        <a class="module-card" href="${pageContext.request.contextPath}${URL_BASE}/ReceitaControlador"><small>Prescrições</small><h3>Receitas</h3><p>Crie e edite receitas com seus medicamentos.</p><span class="module-open">Acessar →</span></a>
        <a class="module-card" href="${pageContext.request.contextPath}${URL_BASE}/ProntuarioControlador?opcao=cancelar"><small>Atendimento</small><h3>Prontuários</h3><p>Registre sintomas e acompanhe o histórico clínico.</p><span class="module-open">Acessar →</span></a>
        <a class="module-card" href="${pageContext.request.contextPath}${URL_BASE}/SolicitacaoExameControlador?opcao=cancelar"><small>Atendimento</small><h3>Solicitações de exame</h3><p>Gerencie os exames vinculados às consultas.</p><span class="module-open">Acessar →</span></a>
        <a class="module-card" href="${pageContext.request.contextPath}${URL_BASE}/RemedioControlador?opcao=cancelar"><small>Cadastros</small><h3>Medicamentos</h3><p>Mantenha o catálogo usado nas prescrições.</p><span class="module-open">Acessar →</span></a>
        <a class="module-card" href="${pageContext.request.contextPath}${URL_BASE}/MedicoControlador?opcao=cancelar"><small>Equipe</small><h3>Médicos</h3><p>Gerencie especialidades e informações de contato.</p><span class="module-open">Acessar →</span></a>
        <a class="module-card" href="${pageContext.request.contextPath}${URL_BASE}/FuncionarioControlador?opcao=cancelar"><small>Equipe</small><h3>Funcionários</h3><p>Organize os profissionais da clínica.</p><span class="module-open">Acessar →</span></a>
        <a class="module-card" href="${pageContext.request.contextPath}${URL_BASE}/EspecialidadeControlador?opcao=cancelar"><small>Cadastros</small><h3>Especialidades</h3><p>Administre as áreas de atendimento.</p><span class="module-open">Acessar →</span></a>
        <a class="module-card" href="${pageContext.request.contextPath}${URL_BASE}/ConsultorioControlador?opcao=cancelar"><small>Estrutura</small><h3>Consultórios</h3><p>Cadastre e localize os espaços de atendimento.</p><span class="module-open">Acessar →</span></a>
        <a class="module-card" href="${pageContext.request.contextPath}${URL_BASE}/ConvenioControlador?opcao=cancelar"><small>Cadastros</small><h3>Convênios</h3><p>Consulte os convênios e contatos disponíveis.</p><span class="module-open">Acessar →</span></a>
        <a class="module-card" href="${pageContext.request.contextPath}${URL_BASE}/TipoExameControlador?opcao=cancelar"><small>Cadastros</small><h3>Tipos de exame</h3><p>Defina os exames oferecidos pela clínica.</p><span class="module-open">Acessar →</span></a>
    </div>
    <p class="empty-state" data-module-empty hidden>Nenhum módulo encontrado. Tente outro nome.</p>
</main>
</body>
</html>
