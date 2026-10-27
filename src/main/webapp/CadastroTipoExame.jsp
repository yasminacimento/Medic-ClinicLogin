<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastro de Tipo de Exame</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css?v=20261026-2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/crud.css?v=20261026-2">
    
</head>
<body>
    <%@include file="Menu.jsp"%>
    
    <div class="container-crud" id="conteudo">
        <h1>Cadastro de Tipo de Exame</h1>
        
        <div class="form-crud">
            <form class="form-grid" method="get" action="${pageContext.request.contextPath}${URL_BASE}/TipoExameControlador">
                <input type="hidden" name="opcao" value="${opcao}">
                <input type="hidden" name="id_tipoExame" value="${id_tipoExame}">
                
                <div class="form-group form-wide">
                    <label>Nome do Exame:</label>
                    <input type="text" name="nome" value="${nome}" class="form-control" required>
                </div>
                
                <div class="form-group form-wide">
                    <label>Descrição:</label>
                    <textarea name="descricao" class="form-control" rows="3">${descricao}</textarea>
                </div>
                
                <div class="form-group form-third">
                    <label>Valor (R$):</label>
                    <input type="number" step="0.01" name="valor" value="${valor}" class="form-control" required>
                </div>
                
                <div class="form-group form-third">
                    <label>Funcionário Responsável:</label>
                    <select name="funcionarioId" class="form-control" required>
                        <option value="">Selecione um funcionário...</option>
                        <c:forEach var="f" items="${funcionarios}">
                            <option value="${f.id_funcionario}" ${f.id_funcionario == funcionarioId ? 'selected' : ''}>
                                ${f.nome}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-group form-third">
                    <label>Consultório (opcional):</label>
                    <select name="consultorioId" class="form-control">
                        <option value="">Sem consultório específico</option>
                        <c:forEach var="cons" items="${consultorios}">
                            <option value="${cons.id_consultorio}" ${cons.id_consultorio == consultorioId ? 'selected' : ''}>
                                ${cons.numero} - Andar ${cons.andar}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="btn-group">
                        <a href="${pageContext.request.contextPath}${URL_BASE}/TipoExameControlador?opcao=cancelar" 
                       class="btn btn-secondary">Cancelar</a>
                        <button type="submit" class="btn ${opcao == 'confirmarExcluir' ? 'btn-danger' : 'btn-primary'}">
                        ${opcao == 'confirmarExcluir' ? 'Confirmar Exclusão' : 'Salvar'}
                    </button>
                    </div>
            </form>
        </div>
        
        <c:if test="${not empty mensagem}">
            <div class="mensagem ${mensagem.contains('sucesso') ? 'sucesso' : 'erro'}">
                <c:out value="${mensagem}"/>
            </div>
        </c:if>
        
        <h2>Tipos de Exame Cadastrados</h2>
        <table class="table-crud">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>NOME</th>
                    <th>VALOR</th>
                    <th>FUNCIONÁRIO</th>
                    <th>CONSULTÓRIO</th>
                    <th colspan="2">Ações</th>
                </tr>
            </thead>
            <tbody>
    <c:forEach var="te" items="${tiposExame}">
        <tr>
            <td>${te.idTipoExame}</td>
            <td>${te.nome}</td>
            <td>R$ ${te.valor}</td>
            <td>${te.funcionario != null ? te.funcionario.nome : '—'}</td>
            <td>${te.consultorio != null ? te.consultorio.numero : '—'}</td>
            
            <!-- ALTERAR -->
            <td>
                <form method="get" action="${pageContext.request.contextPath}${URL_BASE}/TipoExameControlador" style="display:inline;">
                    <input type="hidden" name="opcao" value="enviarAlterar">
                    <input type="hidden" name="id_tipoExame" value="${te.idTipoExame}">
                    <input type="hidden" name="nome" value="${te.nome}">
                    <input type="hidden" name="descricao" value="${te.descricao}">
                    <input type="hidden" name="valor" value="${te.valor}">
                    <input type="hidden" name="funcionarioId" value="${te.funcionarioId}">
                    <input type="hidden" name="consultorioId" value="${te.consultorioId}">
                    <button type="submit" class="btn btn-primary btn-sm">Alterar</button>
                </form>
            </td>
            
            <!-- EXCLUIR - COM TODOS OS CAMPOS -->
            <td>
                <form method="get" action="${pageContext.request.contextPath}${URL_BASE}/TipoExameControlador" style="display:inline;">
                    <input type="hidden" name="opcao" value="enviarExcluir">
                    <input type="hidden" name="id_tipoExame" value="${te.idTipoExame}">
                    <input type="hidden" name="nome" value="${te.nome}">
                    <input type="hidden" name="descricao" value="${te.descricao}">
                    <input type="hidden" name="valor" value="${te.valor}">
                    <input type="hidden" name="funcionarioId" value="${te.funcionarioId}">
                    <input type="hidden" name="consultorioId" value="${te.consultorioId}">
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
