<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastro de Especialidade</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css?v=20261026-2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/crud.css?v=20261026-2">
    
</head>
<body>
    
    <%@include file="Menu.jsp"%>
    
    <div class="container-crud" id="conteudo">
        <h1>Cadastro de Especialidade</h1>
        
        <div class="form-crud">
            <form class="form-grid" method="get" action="${pageContext.request.contextPath}${URL_BASE}/EspecialidadeControlador">
                <input type="hidden" name="opcao" value="${opcao}">
                <input type="hidden" name="id_especialidade" value="${id_especialidade}">
                
                <div class="form-group form-wide">
                    <label>Nome:</label>
                    <input type="text" name="nome" value="${nome}" class="form-control" required>
                </div>
                
                <div class="form-group form-wide">
                    <label>Descrição:</label>
                    <input type="text" name="descricao" value="${descricao}" class="form-control">
                </div>
                
                <div class="btn-group">
                        <a href="${pageContext.request.contextPath}${URL_BASE}/EspecialidadeControlador?opcao=cancelar" 
                       class="btn btn-secondary">Cancelar</a>
                        <button type="submit" 
                            class="btn ${opcao == 'excluir' ? 'btn-danger' : 'btn-primary'}">
                        ${opcao == 'excluir' ? 'Confirmar Exclusão' : 'Salvar'}
                    </button>
                    </div>
            </form>
        </div>
        
        <c:if test="${not empty mensagem}">
            <div class="mensagem ${mensagem.contains('sucesso') || mensagem.contains('Sucesso') ? 'sucesso' : 'erro'}">
                <c:out value="${mensagem}"/>
            </div>
        </c:if>
        
        <h2>Especialidades Cadastradas</h2>
        <table class="table-crud">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>NOME</th>
                    <th>DESCRIÇÃO</th>
                    <th colspan="2">Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="especialidade" items="${especialidades}">
                    <tr>
                        <td>${especialidade.id_especialidade}</td>
                        <td>${especialidade.nome}</td>
                        <td>${especialidade.descricao}</td>
                        <td>
                            <form method="get" action="${pageContext.request.contextPath}${URL_BASE}/EspecialidadeControlador" style="display:inline;">
                                <input type="hidden" name="opcao" value="enviarAlterar">
                                <input type="hidden" name="id_especialidade" value="${especialidade.id_especialidade}">
                                <input type="hidden" name="nome" value="${especialidade.nome}">
                                <input type="hidden" name="descricao" value="${especialidade.descricao}">
                                <button type="submit" class="btn btn-primary btn-sm">Alterar</button>
                            </form>
                        </td>
                        <td>
                            <form method="get" action="${pageContext.request.contextPath}${URL_BASE}/EspecialidadeControlador" style="display:inline;">
                                <input type="hidden" name="opcao" value="enviarExcluir">
                                <input type="hidden" name="id_especialidade" value="${especialidade.id_especialidade}">
                                <input type="hidden" name="nome" value="${especialidade.nome}">
                                <input type="hidden" name="descricao" value="${especialidade.descricao}">
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
