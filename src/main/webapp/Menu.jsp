<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ui" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="${pageContext.request.contextPath}/js/app.js?v=20261026-2" defer></script>
<a class="skip-link" href="#conteudo">Pular para o conteúdo</a>
<header class="app-header">
    <div class="app-header-inner">
        <a class="app-brand" href="${pageContext.request.contextPath}/Home.jsp">
            <span class="clinic-mark" aria-hidden="true">+</span>
            <span><strong>Clínica Médica</strong><small>Gestão de atendimentos</small></span>
        </a>
        <button class="nav-toggle" type="button" aria-controls="app-navigation" aria-expanded="false">Menu</button>
        <nav id="app-navigation" class="app-navigation" aria-label="Menu principal">
            <a class="nav-link" data-nav-link href="${pageContext.request.contextPath}/Home.jsp">Início</a>
            <details class="nav-group">
                <summary>Atendimento</summary>
                <div class="nav-dropdown">
                    <a data-nav-link href="${pageContext.request.contextPath}${URL_BASE}/ConsultaControlador?opcao=cancelar">Consultas</a>
                    <a data-nav-link href="${pageContext.request.contextPath}${URL_BASE}/PacienteControlador?opcao=cancelar">Pacientes</a>
                    <a data-nav-link href="${pageContext.request.contextPath}${URL_BASE}/ProntuarioControlador?opcao=cancelar">Prontuários</a>
                    <a data-nav-link href="${pageContext.request.contextPath}${URL_BASE}/SolicitacaoExameControlador?opcao=cancelar">Solicitações de exame</a>
                </div>
            </details>
            <a class="nav-link" data-nav-link href="${pageContext.request.contextPath}${URL_BASE}/ReceitaControlador">Receitas</a>
            <details class="nav-group">
                <summary>Cadastros</summary>
                <div class="nav-dropdown">
                    <a data-nav-link href="${pageContext.request.contextPath}${URL_BASE}/RemedioControlador?opcao=cancelar">Medicamentos</a>
                    <a data-nav-link href="${pageContext.request.contextPath}${URL_BASE}/MedicoControlador?opcao=cancelar">Médicos</a>
                    <a data-nav-link href="${pageContext.request.contextPath}${URL_BASE}/FuncionarioControlador?opcao=cancelar">Funcionários</a>
                    <a data-nav-link href="${pageContext.request.contextPath}${URL_BASE}/EspecialidadeControlador?opcao=cancelar">Especialidades</a>
                    <a data-nav-link href="${pageContext.request.contextPath}${URL_BASE}/ConsultorioControlador?opcao=cancelar">Consultórios</a>
                    <a data-nav-link href="${pageContext.request.contextPath}${URL_BASE}/ConvenioControlador?opcao=cancelar">Convênios</a>
                    <a data-nav-link href="${pageContext.request.contextPath}${URL_BASE}/TipoExameControlador?opcao=cancelar">Tipos de exame</a>
                </div>
            </details>
            <div class="user-area">
                <ui:choose>
                    <ui:when test="${not empty sessionScope.user}">
                        <span class="user-name" title="Usuário conectado"><ui:out value="${sessionScope.user}"/></span>
                        <form method="post" action="${pageContext.request.contextPath}${URL_BASE}/LoginControlador">
                            <input type="hidden" name="opcao" value="logout">
                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                            <button type="submit" class="logout-button">Sair</button>
                        </form>
                    </ui:when>
                    <ui:otherwise><a class="nav-link" href="${pageContext.request.contextPath}/login.jsp">Entrar</a></ui:otherwise>
                </ui:choose>
            </div>
        </nav>
    </div>
</header>
