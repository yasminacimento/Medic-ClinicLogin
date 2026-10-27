<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Solicitação de Exame</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css?v=20261026-2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/crud.css?v=20261026-2">
    
</head>
<body>
    <%@include file="Menu.jsp"%>
    
    <div class="container-crud" id="conteudo">
        <h1>Solicitação de Exame</h1>
        
        <div class="form-crud">
            <form class="form-grid" method="get" action="${pageContext.request.contextPath}${URL_BASE}/SolicitacaoExameControlador">
                <input type="hidden" name="opcao" value="${opcao}">
                <input type="hidden" name="id_solicitacaoExame" value="${id_solicitacaoExame}">
                
                <div class="form-group">
                    <label>Data:</label>
                    <input type="date" name="data" value="${data}" class="form-control" required>
                </div>
                
                <div class="form-group">
                    <label>Consulta:</label>
                    <select name="consultaId" class="form-control" required>
                        <option value="">Selecione uma consulta...</option>
                        <c:forEach var="c" items="${consultas}">
                            <option value="${c.idConsulta}" ${c.idConsulta == consultaId ? 'selected' : ''}>
                                ${c.dataFormatada} - ${c.paciente != null ? c.paciente.nome : ''}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-group">
                    <label>Tipo de Exame:</label>
                    <select name="tipoExameId" class="form-control" required>
                        <option value="">Selecione o tipo de exame...</option>
                        <c:forEach var="te" items="${tiposExame}">
                            <option value="${te.idTipoExame}" ${te.idTipoExame == tipoExameId ? 'selected' : ''}>
                                ${te.nome} (R$ ${te.valor})
                            </option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-group">
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
                
                <div class="form-group">
                    <label>Resultado / Observações:</label>
                    <textarea name="resultado" class="form-control" rows="4">${resultado}</textarea>
                </div>
                
                <div class="btn-group">
                        <a href="${pageContext.request.contextPath}${URL_BASE}/SolicitacaoExameControlador?opcao=cancelar" 
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
        
        <h2>Solicitações de Exames</h2>
        <table class="table-crud">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>DATA</th>
                    <th>PACIENTE</th>
                    <th>EXAME</th>
                    <th>FUNCIONÁRIO</th>
                    <th colspan="2">Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="s" items="${solicitacoes}">
                    <tr>
                        <td>${s.idSolicitacaoExame}</td>
                        <td>${s.dataFormatada}</td>
                        <td>${s.consulta != null && s.consulta.paciente != null ? s.consulta.paciente.nome : '—'}</td>
                        <td>${s.tipoExame != null ? s.tipoExame.nome : '—'}</td>
                        <td>${s.funcionario != null ? s.funcionario.nome : '—'}</td>
                        <td>
                            <form method="get" action="${pageContext.request.contextPath}${URL_BASE}/SolicitacaoExameControlador" style="display:inline;">
                                <input type="hidden" name="opcao" value="enviarAlterar">
                                <input type="hidden" name="id_solicitacaoExame" value="${s.idSolicitacaoExame}">
                                <input type="hidden" name="data" value="${s.data}">
                                <input type="hidden" name="resultado" value="${s.resultado}">
                                <input type="hidden" name="consultaId" value="${s.consultaId}">
                                <input type="hidden" name="tipoExameId" value="${s.tipoExameId}">
                                <input type="hidden" name="funcionarioId" value="${s.funcionarioId}">
                                <button type="submit" class="btn btn-primary btn-sm">Alterar</button>
                            </form>
                        </td>
                        <td>
                            <form method="get" action="${pageContext.request.contextPath}${URL_BASE}/SolicitacaoExameControlador" style="display:inline;">
                                <input type="hidden" name="opcao" value="enviarExcluir">
                                <input type="hidden" name="id_solicitacaoExame" value="${s.idSolicitacaoExame}">
                                <input type="hidden" name="data" value="${s.data}">
                                <input type="hidden" name="resultado" value="${s.resultado}">
                                <input type="hidden" name="consultaId" value="${s.consultaId}">
                                <input type="hidden" name="tipoExameId" value="${s.tipoExameId}">
                                <input type="hidden" name="funcionarioId" value="${s.funcionarioId}">
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
