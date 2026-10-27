<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Cadastro de Prontuário</title>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css?v=20261026-2">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/crud.css?v=20261026-2">
        
    </head>
    <body>
        <%@include file="Menu.jsp"%>

        <div class="container-crud" id="conteudo">
            <h1>Cadastro de Prontuário</h1>

            <div class="form-crud">
                <form class="form-grid" method="get" action="${pageContext.request.contextPath}${URL_BASE}/ProntuarioControlador">
                    <input type="hidden" name="opcao" value="${opcao}">
                    <input type="hidden" name="id_prontuario" value="${id_prontuario}">

                    <div class="form-group">
                        <label>Consulta:</label>
                        <select name="consultaId" class="form-control" required>
                            <option value="">Selecione uma consulta...</option>
                            <c:forEach var="cons" items="${consultas}">
                                <option value="${cons.idConsulta}" ${cons.idConsulta == consultaId ? 'selected' : ''}>
                                    ${cons.dataFormatada} ${cons.horaFormatada} - ${cons.paciente.nome}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Data do Registro:</label>
                        <input type="date" name="dataRegistro" value="${dataRegistro}" class="form-control" required>
                    </div>

                    <div class="form-group form-wide">
                        <label>Diagnóstico:</label>
                        <input type="text" name="diagnostico" value="${diagnostico}" class="form-control" required maxlength="120">
                    </div>

                    <div class="form-group">
                        <label>Descrição dos Sintomas:</label>
                        <textarea name="descricaoSintomas" class="form-control" rows="4" maxlength="300">${descricaoSintomas}</textarea>
                    </div>

                    <div class="btn-group">
                        <a href="${pageContext.request.contextPath}${URL_BASE}/ProntuarioControlador?opcao=cancelar" 
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

            <h2>Prontuários Cadastrados</h2>
            <table class="table-crud">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>DATA</th>
                        <th>PACIENTE</th>
                        <th>DIAGNÓSTICO</th>
                        <th colspan="2">Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${prontuarios}">
                        <tr>
                            <td>${p.idProntuario}</td>
                            <td>${p.dataRegistroFormatado}</td>
                            <td>${p.consulta != null && p.consulta.paciente != null ? p.consulta.paciente.nome : '—'}</td>
                            <td>${p.diagnostico}</td>

                            <!-- Botão ALTERAR -->
                            <td>
                                <form method="get" action="${pageContext.request.contextPath}${URL_BASE}/ProntuarioControlador" style="display:inline;">
                                    <input type="hidden" name="opcao" value="enviarAlterar">
                                    <input type="hidden" name="id_prontuario" value="${p.idProntuario}">
                                    <input type="hidden" name="diagnostico" value="${p.diagnostico}">
                                    <input type="hidden" name="descricaoSintomas" value="${p.descricaoSintomas}">
                                    <input type="hidden" name="dataRegistro" value="${p.dataRegistro}">
                                    <input type="hidden" name="consultaId" value="${p.consultaId}">
                                    <button type="submit" class="btn btn-primary btn-sm">Alterar</button>
                                </form>
                            </td>

                            <!-- Botão EXCLUIR - AGORA COM TODOS OS CAMPOS -->
                            <td>
                                <form method="get" action="${pageContext.request.contextPath}${URL_BASE}/ProntuarioControlador" style="display:inline;">
                                    <input type="hidden" name="opcao" value="enviarExcluir">
                                    <input type="hidden" name="id_prontuario" value="${p.idProntuario}">
                                    <input type="hidden" name="diagnostico" value="${p.diagnostico}">
                                    <input type="hidden" name="descricaoSintomas" value="${p.descricaoSintomas}">
                                    <input type="hidden" name="dataRegistro" value="${p.dataRegistro}">
                                    <input type="hidden" name="consultaId" value="${p.consultaId}">
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
