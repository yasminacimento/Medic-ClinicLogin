<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastro de Consulta</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css?v=20261026-2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/crud.css?v=20261026-2">
    
</head>
<body>
    <%@include file="Menu.jsp"%>
    
    <div class="container-crud" id="conteudo">
        <h1>Cadastro de Consulta</h1>
        
        <div class="form-crud">
            <form class="form-grid" method="get" action="${pageContext.request.contextPath}${URL_BASE}/ConsultaControlador">
                <input type="hidden" name="opcao" value="${opcao}">
                <input type="hidden" name="id_consulta" value="${id_consulta}">
                
                <div class="form-group">
                    <label>Data:</label>
                    <input type="date" name="data" value="${data}" class="form-control" required>
                </div>
                
                <div class="form-group">
                    <label>Hora:</label>
                    <input type="time" name="hora" value="${hora}" class="form-control" required>
                </div>
                
                <div class="form-group">
                    <label>Médico:</label>
                    <select name="medicoId" class="form-control" required>
                        <option value="">Selecione um médico...</option>
                        <c:forEach var="m" items="${medicos}">
                            <option value="${m.idMedico}" ${m.idMedico == medicoId ? 'selected' : ''}>
                                ${m.nome} (${m.crm})
                            </option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-group">
                    <label>Paciente:</label>
                    <select name="pacienteId" class="form-control" required>
                        <option value="">Selecione um paciente...</option>
                        <c:forEach var="p" items="${pacientes}">
                            <option value="${p.idPaciente}" ${p.idPaciente == pacienteId ? 'selected' : ''}>
                                ${p.nome} (${p.cpf})
                            </option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-group">
                    <label>Consultório:</label>
                    <select name="consultorioId" class="form-control" required>
                        <option value="">Selecione um consultório...</option>
                        <c:forEach var="cons" items="${consultorios}">
                            <option value="${cons.id_consultorio}" ${cons.id_consultorio == consultorioId ? 'selected' : ''}>
                                ${cons.numero} - Andar ${cons.andar}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-group">
                    <label>Status:</label>
                    <select name="status" class="form-control">
                        <option value="agendada" ${status == 'agendada' ? 'selected' : ''}>Agendada</option>
                        <option value="realizada" ${status == 'realizada' ? 'selected' : ''}>Realizada</option>
                        <option value="cancelada" ${status == 'cancelada' ? 'selected' : ''}>Cancelada</option>
                    </select>
                </div>
                
                <div class="btn-group">
                        <a href="${pageContext.request.contextPath}${URL_BASE}/ConsultaControlador?opcao=cancelar" 
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
        
        <h2>Consultas Cadastradas</h2>
        <table class="table-crud">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>DATA</th>
                    <th>HORA</th>
                    <th>PACIENTE</th>
                    <th>MÉDICO</th>
                    <th>CONSULTÓRIO</th>
                    <th>STATUS</th>
                    <th colspan="2">Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="c" items="${consultas}">
                    <tr>
                        <td>${c.idConsulta}</td>
                        <td>${c.dataFormatada}</td>
                        <td>${c.horaFormatada}</td>
                        <td>${c.paciente.nome}</td>
                        <td>${c.medico.nome}</td>
                        <td>${c.consultorio.numero}</td>
                        <td><span class="badge bg-${c.status == 'agendada' ? 'primary' : c.status == 'realizada' ? 'success' : 'danger'}">${c.status}</span></td>
                        <td>
                            <form method="get" action="${pageContext.request.contextPath}${URL_BASE}/ConsultaControlador" style="display:inline;">
                                <input type="hidden" name="opcao" value="enviarAlterar">
                                <input type="hidden" name="id_consulta" value="${c.idConsulta}">
                                <input type="hidden" name="data" value="${c.data}">
                                <input type="hidden" name="hora" value="${c.hora}">
                                <input type="hidden" name="status" value="${c.status}">
                                <input type="hidden" name="medicoId" value="${c.medicoId}">
                                <input type="hidden" name="pacienteId" value="${c.pacienteId}">
                                <input type="hidden" name="consultorioId" value="${c.consultorioId}">
                                <button type="submit" class="btn btn-primary btn-sm">Alterar</button>
                            </form>
                        </td>
                        <td>
                            <form method="get" action="${pageContext.request.contextPath}${URL_BASE}/ConsultaControlador" style="display:inline;">
                                <input type="hidden" name="opcao" value="enviarExcluir">
                                <input type="hidden" name="id_consulta" value="${c.idConsulta}">
                                <input type="hidden" name="data" value="${c.data}">
                                <input type="hidden" name="hora" value="${c.hora}">
                                <input type="hidden" name="medicoId" value="${c.medicoId}">
                                <input type="hidden" name="pacienteId" value="${c.pacienteId}">
                                <input type="hidden" name="consultorioId" value="${c.consultorioId}">
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
