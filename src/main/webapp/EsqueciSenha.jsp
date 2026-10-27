<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recuperar senha - Clínica Médica</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/autenticacao.css?v=20261026-2">
<script src="${pageContext.request.contextPath}/js/app.js?v=20261026-2" defer></script>
</head>
<body>
<main>
    <h1>Esqueci minha senha</h1>
    <p>Informe o e-mail cadastrado e escolha uma nova senha.</p>
    <p><strong>Demonstração escolar:</strong> a redefinição usa apenas o e-mail informado, sem verificar sua titularidade.</p>
    <c:if test="${not empty mensagem}"><p role="status"><c:out value="${mensagem}"/></p></c:if>
    <form method="post" action="${pageContext.request.contextPath}${URL_BASE}/RecuperacaoSenhaControlador">
        <input type="hidden" name="opcao" value="redefinir">
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
        <label for="email">E-mail</label>
        <input type="email" id="email" name="email" autocomplete="email" maxlength="255" required>
        <label for="password">Nova senha</label>
        <input type="password" id="password" name="password" autocomplete="new-password" minlength="8" maxlength="72" aria-describedby="regra-senha" required>
        <small id="regra-senha">Use pelo menos 8 caracteres, até 72 bytes UTF-8.</small>
        <label for="confirmacao">Confirme a nova senha</label>
        <input type="password" id="confirmacao" name="confirmacao" autocomplete="new-password" minlength="8" maxlength="72" required>
        <button type="submit">Salvar nova senha</button>
    </form>
    <p><a href="${pageContext.request.contextPath}/login.jsp">Voltar ao login</a></p>
</main>
</body>
</html>
