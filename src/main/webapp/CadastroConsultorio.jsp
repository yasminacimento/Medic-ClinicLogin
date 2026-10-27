<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastro de Consultório</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css?v=20261026-2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/crud.css?v=20261026-2">
    
</head>
<body>
    
    <%@include file="Menu.jsp"%>
    
    <div class="container-crud" id="conteudo">
        <h1>Cadastro de Consultório</h1>
        
        <div class="form-crud">
            <form class="form-grid" method="get" action="${pageContext.request.contextPath}${URL_BASE}/ConsultorioControlador">
                <input type="hidden" name="opcao" value="${opcao}">
                <input type="hidden" name="id_consultorio" value="${id_consultorio}">
                
                <div class="form-group form-third">
                    <label>Número:</label>
                    <input type="number" name="numero" value="${numero}" class="form-control" required>
                </div>
                
                <div class="form-group form-third">
                    <label>Andar:</label>
                    <input type="number" name="andar" value="${andar}" class="form-control" required>
                </div>
                
                <div class="form-group form-third">
                    <label>Capacidade:</label>
                    <input type="number" name="capacidade" value="${capacidade}" class="form-control" required>
                </div>
                
                <div class="btn-group">
                        <a href="${pageContext.request.contextPath}${URL_BASE}/ConsultorioControlador?opcao=cancelar" 
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
        
        <h2>Consultórios Cadastrados</h2>
        <table class="table-crud">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>NÚMERO</th>
                    <th>ANDAR</th>
                    <th>CAPACIDADE</th>
                    <th colspan="2">Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="consultorio" items="${consultorios}">
                    <tr>
                        <td>${consultorio.id_consultorio}</td>
                        <td>${consultorio.numero}</td>
                        <td>${consultorio.andar}</td>
                        <td>${consultorio.capacidade}</td>
                        <td>
                            <form method="get" action="${pageContext.request.contextPath}${URL_BASE}/ConsultorioControlador" style="display:inline;">
                                <input type="hidden" name="opcao" value="enviarAlterar">
                                <input type="hidden" name="id_consultorio" value="${consultorio.id_consultorio}">
                                <input type="hidden" name="numero" value="${consultorio.numero}">
                                <input type="hidden" name="andar" value="${consultorio.andar}">
                                <input type="hidden" name="capacidade" value="${consultorio.capacidade}">
                                <button type="submit" class="btn btn-primary btn-sm">Alterar</button>
                            </form>
                        </td>
                        <td>
                            <form method="get" action="${pageContext.request.contextPath}${URL_BASE}/ConsultorioControlador" style="display:inline;">
                                <input type="hidden" name="opcao" value="enviarExcluir">
                                <input type="hidden" name="id_consultorio" value="${consultorio.id_consultorio}">
                                <input type="hidden" name="numero" value="${consultorio.numero}">
                                <input type="hidden" name="andar" value="${consultorio.andar}">
                                <input type="hidden" name="capacidade" value="${consultorio.capacidade}">
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
