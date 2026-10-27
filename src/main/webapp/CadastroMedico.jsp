<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Cadastro de Médico</title>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css?v=20261026-2">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/crud.css?v=20261026-2">
        
    </head>
    <body>

        <%@include file="Menu.jsp"%>

        <div class="container-crud" id="conteudo">
            <h1>Cadastro de Médico</h1>

            <div class="form-crud">
                <form class="form-grid" method="get" action="${pageContext.request.contextPath}${URL_BASE}/MedicoControlador">
                    <input type="hidden" name="opcao" value="${opcao}">
                    <input type="hidden" name="id_medico" value="${id_medico}">

                    <h2 class="form-section-title">Dados profissionais</h2>
                    <div class="form-group form-wide">
                        <label>Nome:</label>
                        <input type="text" name="nome" value="${nome}" class="form-control" ${opcao == 'confirmarExcluir' ? 'readonly' : 'required'}>
                    </div>
                    <div class="form-group">
                        <label>CRM:</label>
                        <input type="text" name="crm" value="${crm}" class="form-control" ${opcao == 'confirmarExcluir' ? 'readonly' : 'required'}>
                    </div>
                    <div class="form-group">
                        <label>Especialidade:</label>
                        <select name="especialidadeId" class="form-control" ${opcao == 'confirmarExcluir' ? 'disabled' : 'required'}>
                            <option value="">Selecione uma especialidade...</option>
                            <c:forEach var="esp" items="${especialidades}">
                                <option value="${esp.id_especialidade}" 
                                        ${esp.id_especialidade == especialidadeId ? 'selected' : ''}>
                                    ${esp.nome}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <h2 class="form-section-title">Contato</h2>
                    <div class="form-group">
                        <label>Telefone:</label>
                        <input type="text" name="telefone" value="${telefone}" class="form-control" ${opcao == 'confirmarExcluir' ? 'readonly' : ''}>
                    </div>
                    <div class="form-group">
                        <label>Email:</label>
                        <input type="email" name="email" value="${email}" class="form-control" ${opcao == 'confirmarExcluir' ? 'readonly' : ''}>
                    </div>
                    <div class="form-group form-wide">
                        <label>Endereço:</label>
                        <input type="text" name="endereco" value="${endereco}" class="form-control" ${opcao == 'confirmarExcluir' ? 'readonly' : ''}>
                    </div>
                    <div class="btn-group">
                        <a href="${pageContext.request.contextPath}${URL_BASE}/MedicoControlador?opcao=cancelar" 
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

            <h2>Médicos Cadastrados</h2>
            <table class="table-crud">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>NOME</th>
                        <th>CRM</th>
                        <th>ESPECIALIDADE</th>
                        <th>TELEFONE</th>
                        <th>EMAIL</th>
                        <th colspan="2">Ações</th>
                    </tr>
                </thead>
                <tbody>
    <c:forEach var="medico" items="${medicos}">
        <tr>
            <td>${medico.idMedico}</td>
            <td>${medico.nome}</td>
            <td>${medico.crm}</td>
            <td>${medico.especialidade != null ? medico.especialidade.nome : '—'}</td>
            <td>${medico.telefone}</td>
            <td>${medico.email}</td>
            
            <!-- Botão Alterar -->
            <td>
                <form method="get" action="${pageContext.request.contextPath}${URL_BASE}/MedicoControlador" style="display:inline;">
                    <input type="hidden" name="opcao" value="enviarAlterar">
                    <input type="hidden" name="id_medico" value="${medico.idMedico}">
                    <input type="hidden" name="nome" value="${medico.nome}">
                    <input type="hidden" name="telefone" value="${medico.telefone}">
                    <input type="hidden" name="crm" value="${medico.crm}">
                    <input type="hidden" name="especialidadeId" value="${medico.especialidadeId}">
                    <input type="hidden" name="email" value="${medico.email}">
                    <input type="hidden" name="endereco" value="${medico.endereco}">
                    <button type="submit" class="btn btn-primary btn-sm">Alterar</button>
                </form>
            </td>
            
            <!-- Botão Excluir - AGORA COM TODOS OS CAMPOS -->
            <td>
                <form method="get" action="${pageContext.request.contextPath}${URL_BASE}/MedicoControlador" style="display:inline;">
                    <input type="hidden" name="opcao" value="enviarExcluir">
                    <input type="hidden" name="id_medico" value="${medico.idMedico}">
                    <input type="hidden" name="nome" value="${medico.nome}">
                    <input type="hidden" name="telefone" value="${medico.telefone}">
                    <input type="hidden" name="crm" value="${medico.crm}">
                    <input type="hidden" name="especialidadeId" value="${medico.especialidadeId}">
                    <input type="hidden" name="email" value="${medico.email}">
                    <input type="hidden" name="endereco" value="${medico.endereco}">
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
