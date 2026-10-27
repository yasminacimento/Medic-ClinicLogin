<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Cadastro de Paciente</title>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css?v=20261026-2">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/crud.css?v=20261026-2">
        
    </head>
    <body>
        <%@include file="Menu.jsp"%>

        <div class="container-crud" id="conteudo">
            <h1>Cadastro de Paciente</h1>

            <div class="form-crud">
                <form class="form-grid" method="get" action="${pageContext.request.contextPath}${URL_BASE}/PacienteControlador">
                    <input type="hidden" name="opcao" value="${opcao}">
                    <input type="hidden" name="id_paciente" value="${id_paciente}">

                    <h2 class="form-section-title">Dados do paciente</h2>
                    <div class="form-group form-wide">
                        <label>Nome:</label>
                        <input type="text" name="nome" value="${nome}" class="form-control" required>
                    </div>
                    <div class="form-group form-third">
                        <label>Data de Nascimento:</label>
                        <input type="date" name="dataNascimento" value="${dataNascimento}" class="form-control" required>
                    </div>
                    <div class="form-group form-third">
                        <label>CPF:</label>
                        <input type="text" name="cpf" value="${cpf}" class="form-control" required>
                    </div>
                    <div class="form-group form-third">
                        <label>Convênio:</label>
                        <select name="convenioId" class="form-control">
                            <option value="">Sem Convênio</option>
                            <c:forEach var="conv" items="${convenios}">
                                <option value="${conv.id_convenio}" 
                                        ${conv.id_convenio == convenioId ? 'selected="selected"' : ''}>
                                    ${conv.nome}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <h2 class="form-section-title">Contato</h2>
                    <div class="form-group">
                        <label>Telefone:</label>
                        <input type="text" name="telefone" value="${telefone}" class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Email:</label>
                        <input type="email" name="email" value="${email}" class="form-control">
                    </div>
                    <div class="form-group form-wide">
                        <label>Endereço:</label>
                        <input type="text" name="endereco" value="${endereco}" class="form-control">
                    </div>
                    <div class="btn-group">
                        <a href="${pageContext.request.contextPath}${URL_BASE}/PacienteControlador?opcao=cancelar" 
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

            <h2>Pacientes Cadastrados</h2>
            <table class="table-crud">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>NOME</th>
                        <th>DATA NASC.</th>
                        <th>CPF</th>
                        <th>CONVÊNIO</th>
                        <th>TELEFONE</th>
                        <th colspan="2">Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${pacientes}">
                        <tr>
                            <td>${p.idPaciente}</td>
                            <td>${p.nome}</td>
                            <td>${p.dataNascimentoFormatado}</td>
                            <td>${p.cpf}</td>
                            <td>${p.convenio != null ? p.convenio.nome : 'Particular'}</td>
                            <td>${p.telefone}</td>

                            <!-- ALTERAR -->
                            <td>
                                <form method="get" action="${pageContext.request.contextPath}${URL_BASE}/PacienteControlador" style="display:inline;">
                                    <input type="hidden" name="opcao" value="enviarAlterar">
                                    <input type="hidden" name="id_paciente" value="${p.idPaciente}">
                                    <input type="hidden" name="nome" value="${p.nome}">
                                    <input type="hidden" name="dataNascimento" value="${p.dataNascimento}">
                                    <input type="hidden" name="cpf" value="${p.cpf}">
                                    <input type="hidden" name="convenioId" value="${p.convenioId}">
                                    <input type="hidden" name="telefone" value="${p.telefone}">
                                    <input type="hidden" name="email" value="${p.email}">
                                    <input type="hidden" name="endereco" value="${p.endereco}">
                                    <button type="submit" class="btn btn-primary btn-sm">Alterar</button>
                                </form>
                            </td>
                            <td>
                                <form method="get" action="${pageContext.request.contextPath}${URL_BASE}/PacienteControlador" style="display:inline;">
                                    <input type="hidden" name="opcao" value="enviarExcluir">
                                    <input type="hidden" name="id_paciente" value="${p.idPaciente}">
                                    <input type="hidden" name="nome" value="${p.nome}">
                                    <input type="hidden" name="dataNascimento" value="${p.dataNascimento}">
                                    <input type="hidden" name="cpf" value="${p.cpf}">
                                    <input type="hidden" name="convenioId" value="${p.convenioId}">
                                    <input type="hidden" name="telefone" value="${p.telefone}">
                                    <input type="hidden" name="email" value="${p.email}">
                                    <input type="hidden" name="endereco" value="${p.endereco}">
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
