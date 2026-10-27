<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastrar usuário - Clínica Médica</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/autenticacao.css?v=20261026-2">
<script src="${pageContext.request.contextPath}/js/app.js?v=20261026-2" defer></script>
</head>
<body>
<main>
    <h1>Cadastrar usuário</h1>
    <c:if test="${not empty mensagem}"><p role="alert"><c:out value="${mensagem}"/></p></c:if>
    <form method="post" action="${pageContext.request.contextPath}${URL_BASE}/LoginControlador">
        <input type="hidden" name="opcao" value="cadastrar">
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
        <label for="username">Nome de usuário</label>
        <input id="username" name="username" autocomplete="username" minlength="3" maxlength="255" required>
        <label for="email">E-mail</label>
        <input type="email" id="email" name="email" autocomplete="email" maxlength="255" required>
        <label for="password">Senha</label>
        <input type="password" id="password" name="password" autocomplete="new-password" minlength="8" maxlength="72" aria-describedby="regra-senha" required>
        <small id="regra-senha">Use pelo menos 8 caracteres, até 72 bytes UTF-8 (acentos podem ocupar mais de um byte).</small>
        <label for="confirmacao">Confirme a senha</label>
        <input type="password" id="confirmacao" name="confirmacao" autocomplete="new-password" minlength="8" maxlength="72" required>
        <button type="submit">Cadastrar</button>
    </form>
    <p><a href="${pageContext.request.contextPath}/login.jsp">Voltar ao login</a></p>
</main>
</body>
</html>
