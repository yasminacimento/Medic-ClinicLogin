<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Clínica Médica</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/autenticacao.css?v=20261026-2">
<script src="${pageContext.request.contextPath}/js/app.js?v=20261026-2" defer></script>
</head>
<body>
<main>
    <h1>Clínica Médica</h1>
    <h2>Entre para acessar os atendimentos.</h2>
    <c:choose>
        <c:when test="${param.estado == 'cadastrado'}"><p role="status">Usuário cadastrado. Faça login para continuar.</p></c:when>
        <c:when test="${param.estado == 'saiu'}"><p role="status">Sessão encerrada.</p></c:when>
        <c:when test="${param.estado == 'redefinida'}"><p role="status">Senha alterada. Entre com a nova senha.</p></c:when>
    </c:choose>
    <c:if test="${not empty mensagem}"><p role="alert"><c:out value="${mensagem}"/></p></c:if>
    <form method="post" action="${pageContext.request.contextPath}${URL_BASE}/LoginControlador">
        <input type="hidden" name="opcao" value="login">
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
        <label for="username">Nome de usuário</label>
        <input id="username" name="username" autocomplete="username" maxlength="255" required autofocus>
        <label for="password">Senha</label>
        <input type="password" id="password" name="password" autocomplete="current-password" required>
        <button type="submit">Entrar</button>
    </form>
    <p><a href="${pageContext.request.contextPath}/CadastrarUsuario.jsp">Cadastrar usuário</a></p>
    <p><a href="${pageContext.request.contextPath}/EsqueciSenha.jsp">Esqueci minha senha</a></p>
</main>
</body>
</html>
