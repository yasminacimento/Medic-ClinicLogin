<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastro de Funcionário</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css?v=20261026-2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/crud.css?v=20261026-2">
    
</head>
<body>
    
    <%@include file="Menu.jsp"%>
    
    <div class="container-crud" id="conteudo">
        <h1>Cadastro de Funcionário</h1>
        
        <div class="form-crud">
            <form class="form-grid" method="get" action="${pageContext.request.contextPath}${URL_BASE}/FuncionarioControlador">
                <input type="hidden" name="opcao" value="${opcao}">
                <input type="hidden" name="id_funcionario" value="${id_funcionario}">
                
                <h2 class="form-section-title">Dados profissionais</h2>
                    <div class="form-group form-wide">
                    <label>Nome:</label>
                    <input type="text" name="nome" value="${nome}" class="form-control" required>
                </div>
                    <div class="form-group">
                            <label>Cargo:</label>
                            <input type="text" name="cargo" value="${cargo}" class="form-control">
                        </div>
                    <div class="form-group">
                            <label>Salário (R$):</label>
                            <input type="number" step="0.01" name="salario" value="${salario}" class="form-control">
                        </div>
                    <h2 class="form-section-title">Contato</h2>
                    <div class="form-group">
                            <label>Telefone:</label>
                            <input type="text" name="telefone" value="${telefone}" class="form-control">
                        </div>
                    <div class="form-group">
                            <label>E-mail:</label>
                            <input type="email" name="email" value="${email}" class="form-control">
                        </div>
                    <div class="form-group form-wide">
                    <label>Endereço:</label>
                    <input type="text" name="endereco" value="${endereco}" class="form-control">
                </div>
                    <div class="btn-group">
                        <a href="${pageContext.request.contextPath}${URL_BASE}/FuncionarioControlador?opcao=cancelar" 
                       class="btn btn-secondary">Cancelar</a>
                        <button type="submit" 
                            class="btn ${opcao == 'confirmarExcluir' ? 'btn-danger' : 'btn-primary'}">
                        ${opcao == 'confirmarExcluir' ? 'Confirmar Exclusão' : 'Salvar'}
                    </button>
                    </div>
            </form>
        </div>
        
        <c:if test="${not empty mensagem}">
            <div class="mensagem ${mensagem.contains('sucesso') || mensagem.contains('Sucesso') ? 'sucesso' : 'erro'}">
                <c:out value="${mensagem}"/>
            </div>
        </c:if>
        
        <h2>Funcionários Cadastrados</h2>
        <table class="table-crud">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>NOME</th>
                    <th>TELEFONE</th>
                    <th>E-MAIL</th>
                    <th>CARGO</th>
                    <th>SALÁRIO</th>
                    <th colspan="2">Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="funcionario" items="${funcionarios}">
                    <tr>
                        <td>${funcionario.id_funcionario}</td>
                        <td>${funcionario.nome}</td>
                        <td>${funcionario.telefone}</td>
                        <td>${funcionario.email}</td>
                        <td>${funcionario.cargo}</td>
                        <td>R$ ${funcionario.salario}</td>
                        <td>
                            <form method="get" action="${pageContext.request.contextPath}${URL_BASE}/FuncionarioControlador" style="display:inline;">
                                <input type="hidden" name="opcao" value="enviarAlterar">
                                <input type="hidden" name="id_funcionario" value="${funcionario.id_funcionario}">
                                <input type="hidden" name="nome" value="${funcionario.nome}">
                                <input type="hidden" name="telefone" value="${funcionario.telefone}">
                                <input type="hidden" name="email" value="${funcionario.email}">
                                <input type="hidden" name="endereco" value="${funcionario.endereco}">
                                <input type="hidden" name="cargo" value="${funcionario.cargo}">
                                <input type="hidden" name="salario" value="${funcionario.salario}">
                                <button type="submit" class="btn btn-primary btn-sm">Alterar</button>
                            </form>
                        </td>
                        <td>
                            <form method="get" action="${pageContext.request.contextPath}${URL_BASE}/FuncionarioControlador" style="display:inline;">
                                <input type="hidden" name="opcao" value="enviarExcluir">
                                <input type="hidden" name="id_funcionario" value="${funcionario.id_funcionario}">
                                <input type="hidden" name="nome" value="${funcionario.nome}">
                                <input type="hidden" name="telefone" value="${funcionario.telefone}">
                                <input type="hidden" name="email" value="${funcionario.email}">
                                <input type="hidden" name="endereco" value="${funcionario.endereco}">
                                <input type="hidden" name="cargo" value="${funcionario.cargo}">
                                <input type="hidden" name="salario" value="${funcionario.salario}">
                                <button type="submit" class="btn btn-danger btn-sm">Excluir</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    
</body>
</html>
