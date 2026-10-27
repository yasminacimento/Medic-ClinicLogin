<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Medicamentos · Clínica Médica</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css?v=20261026-2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/crud.css?v=20261026-2">
</head>
<body>
<%@include file="Menu.jsp"%>
<main class="container-crud" id="conteudo">
    <div class="page-heading"><div><span class="eyebrow">Cadastros</span><h1>Medicamentos</h1><p>Organize o catálogo utilizado nas prescrições da clínica.</p></div>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}${URL_BASE}/ReceitaControlador">${empty sessionScope.rascunhoReceita ? 'Ir para receitas' : 'Voltar à receita em edição'}</a></div>
    <c:if test="${not empty mensagem}"><div class="mensagem ${mensagem.contains('sucesso') ? 'sucesso' : 'erro'}"><c:out value="${mensagem}"/></div></c:if>
    <div class="form-crud">
        <h2>${opcao == 'confirmarExcluir' ? 'Excluir medicamento' : (opcao == 'confirmarAlterar' ? 'Editar medicamento' : 'Cadastrar medicamento')}</h2>
        <form class="form-grid" method="post" action="${pageContext.request.contextPath}${URL_BASE}/RemedioControlador">
            <input type="hidden" name="opcao" value="${empty opcao ? 'cadastrar' : opcao}">
            <input type="hidden" name="id_remedio" value="${id_remedio}">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
            <div class="form-group form-wide"><label for="nome-medicamento">Nome do medicamento</label>
                <input type="text" id="nome-medicamento" name="nome" maxlength="150" value="<c:out value='${nome}'/>" required ${opcao == 'confirmarExcluir' ? 'readonly' : ''} placeholder="Informe o nome para o catálogo">
            </div>
            <div class="btn-group">
                        <a href="${pageContext.request.contextPath}${URL_BASE}/RemedioControlador?opcao=cancelar" class="btn btn-secondary">Limpar / cancelar</a>
                        <button type="submit" class="btn ${opcao == 'confirmarExcluir' ? 'btn-danger' : 'btn-primary'}">${opcao == 'confirmarExcluir' ? 'Confirmar exclusão' : (opcao == 'confirmarAlterar' ? 'Salvar alterações' : 'Cadastrar medicamento')}</button>
                    </div>
        </form>
    </div>
    <h2>Catálogo de medicamentos</h2>
    <div class="table-responsive"><table class="table-crud">
        <thead><tr><th>Código</th><th>Nome</th><th>Ações</th></tr></thead>
        <tbody><c:forEach var="medicamento" items="${remedios}"><tr>
            <td>#${medicamento.idRemedio}</td><td><c:out value="${medicamento.nome}"/></td>
            <td><div class="actions">
                <a href="${pageContext.request.contextPath}${URL_BASE}/RemedioControlador?opcao=enviarAlterar&amp;id_remedio=${medicamento.idRemedio}" class="btn btn-primary btn-sm">Editar</a>
                <a href="${pageContext.request.contextPath}${URL_BASE}/RemedioControlador?opcao=enviarExcluir&amp;id_remedio=${medicamento.idRemedio}" class="btn btn-danger btn-sm">Excluir</a>
            </div></td>
        </tr></c:forEach></tbody>
    </table></div>
</main>
</body>
</html>
