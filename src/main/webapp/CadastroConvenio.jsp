<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastro de Convênio</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css?v=20261026-2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/crud.css?v=20261026-2">
    
</head>
<body>
    
    <%@include file="Menu.jsp"%>
    
    <div class="container-crud" id="conteudo">
        <h1>Cadastro de Convênio</h1>
        
        <div class="form-crud">
            <form class="form-grid" method="get" action="${pageContext.request.contextPath}${URL_BASE}/ConvenioControlador">
                <input type="hidden" name="opcao" value="${opcao}">
                <input type="hidden" name="id_convenio" value="${id_convenio}">
                
                <div class="form-group form-wide">
                    <label>Nome:</label>
                    <input type="text" name="nome" value="${nome}" class="form-control" required>
                </div>
                
                <div class="form-group">
                    <label>Registro ANS:</label>
                    <input type="text" name="registro_ans" value="${registro_ans}" class="form-control">
                </div>
                
                <div class="form-group">
                    <label>Telefone de Contato:</label>
                    <input type="text" name="telefone_contato" value="${telefone_contato}" class="form-control">
                </div>
                
                <div class="btn-group">
                        <a href="${pageContext.request.contextPath}${URL_BASE}/ConvenioControlador?opcao=cancelar" 
                       class="btn btn-secondary">Cancelar</a>
                        <button type="submit" 
                            class="btn ${opcao == 'excluir' ? 'btn-danger' : 'btn-primary'}">
                        ${opcao == 'excluir' ? 'Confirmar Exclusão' : 'Salvar'}
                    </button>
                    </div>
            </form>
        </div>
        
        <!-- Mensagem -->
        <c:if test="${not empty mensagem}">
            <div class="mensagem ${mensagem.contains('sucesso') || mensagem.contains('Sucesso') ? 'sucesso' : 'erro'}">
                <c:out value="${mensagem}"/>
            </div>
        </c:if>
        
        <!-- Tabela -->
        <h2>Convênios Cadastrados</h2>
        <table class="table-crud">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>NOME</th>
                    <th>REGISTRO ANS</th>
                    <th>TELEFONE CONTATO</th>
                    <th colspan="2">Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="convenio" items="${convenios}">
                    <tr>
                        <td>${convenio.id_convenio}</td>
                        <td>${convenio.nome}</td>
                        <td>${convenio.registro_ans}</td>
                        <td>${convenio.telefone_contato}</td>
                        <td>
                            <form method="get" action="${pageContext.request.contextPath}${URL_BASE}/ConvenioControlador" style="display:inline;">
                                <input type="hidden" name="id_convenio" value="${convenio.id_convenio}">
                                <input type="hidden" name="nome" value="${convenio.nome}">
                                <input type="hidden" name="registro_ans" value="${convenio.registro_ans}">
                                <input type="hidden" name="telefone_contato" value="${convenio.telefone_contato}">
                                <input type="hidden" name="opcao" value="enviarAlterar">
                                <button type="submit" class="btn btn-primary btn-sm">Alterar</button>
                            </form>
                        </td>
                        <td>
                            <form method="get" action="${pageContext.request.contextPath}${URL_BASE}/ConvenioControlador" style="display:inline;">
                                <input type="hidden" name="id_convenio" value="${convenio.id_convenio}">
                                <input type="hidden" name="nome" value="${convenio.nome}">
                                <input type="hidden" name="registro_ans" value="${convenio.registro_ans}">
                                <input type="hidden" name="telefone_contato" value="${convenio.telefone_contato}">
                                <input type="hidden" name="opcao" value="enviarExcluir">
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
