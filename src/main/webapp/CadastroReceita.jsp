<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Receitas · Clínica Médica</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css?v=20261026-2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/crud.css?v=20261026-2">
</head>
<body>
<%@include file="Menu.jsp"%>
<main class="container-crud" id="conteudo">
    <div class="page-heading">
        <div><span class="eyebrow">Prescrições</span><h1>Receitas</h1>
            <p>Monte a prescrição com os medicamentos da consulta e finalize em um único cadastro.</p></div>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}${URL_BASE}/RemedioControlador?opcao=cancelar">Catálogo de medicamentos</a>
    </div>
    <c:if test="${not empty mensagem}"><div class="mensagem ${tipoMensagem == 'sucesso' ? 'sucesso' : (tipoMensagem == 'info' ? 'info' : 'erro')}" role="status"><c:out value="${mensagem}"/></div></c:if>
    <c:if test="${not empty receitaExcluir}">
        <section class="panel delete-panel" aria-labelledby="titulo-excluir">
            <h2 id="titulo-excluir">Excluir receita #${receitaExcluir.idReceita}?</h2>
            <p>A receita de <strong><c:out value="${receitaExcluir.pacienteNome}"/></strong> e seus ${receitaExcluir.quantidadeItens} medicamento(s) serão removidos.</p>
            <form method="post" class="actions" action="${pageContext.request.contextPath}${URL_BASE}/ReceitaControlador">
                <input type="hidden" name="opcao" value="excluir"><input type="hidden" name="id_receita" value="${receitaExcluir.idReceita}">
                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                <button class="btn btn-danger" type="submit">Confirmar exclusão</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}${URL_BASE}/ReceitaControlador">Manter receita</a>
            </form>
        </section>
    </c:if>
    <section class="panel recipe-editor" aria-labelledby="titulo-editor">
        <div class="section-title">
            <h2 id="titulo-editor">${empty rascunho ? 'Nova receita' : (empty receitaEdicao.idReceita ? 'Montar receita' : 'Editar receita')}<c:if test="${not empty receitaEdicao.idReceita}"> #${receitaEdicao.idReceita}</c:if></h2>
            <span class="step-label"><span class="step-number">${empty rascunho ? '1' : '2'}</span>${empty rascunho ? 'Dados da consulta' : 'Medicamentos e revisão'}</span>
        </div>
        <form method="post" data-no-grid action="${pageContext.request.contextPath}${URL_BASE}/ReceitaControlador">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
            <input type="hidden" name="rascunhoId" value="${rascunho.identificador}">
            <div class="recipe-data">
                <div class="form-group"><label for="consulta-receita">Consulta e paciente</label>
                    <select id="consulta-receita" name="consultaId" required>
                        <option value="">Selecione uma consulta...</option>
                        <c:forEach var="consulta" items="${consultas}">
                            <option value="${consulta.idConsulta}" ${consulta.idConsulta == receitaEdicao.consultaId ? 'selected' : ''}><c:out value="${consulta.paciente.nome}"/> · ${consulta.dataFormatada} às ${consulta.horaFormatada} · <c:out value="${consulta.medico.nome}"/> (#${consulta.idConsulta})</option>
                        </c:forEach>
                    </select>
                    <c:if test="${empty consultas}"><small>Cadastre uma consulta antes de emitir uma receita.</small></c:if>
                </div>
                <div class="form-group"><label for="data-receita">Data de emissão</label>
                    <input type="date" id="data-receita" name="dataEmissao" value="${receitaEdicao.dataEmissao}" required>
                </div>
                <div class="form-group form-wide"><label for="observacoes-receita">Observações <span class="muted">(opcional)</span></label>
                    <textarea id="observacoes-receita" name="observacoes" rows="2" maxlength="300" placeholder="Orientações gerais da prescrição"><c:out value="${receitaEdicao.observacoes}"/></textarea>
                </div>
            </div>
            <c:choose>
                <c:when test="${empty rascunho}">
                    <div class="form-actions"><button type="submit" name="opcao" value="iniciar" class="btn btn-primary" ${empty consultas ? 'disabled' : ''}>Continuar para medicamentos <span aria-hidden="true">→</span></button></div>
                </c:when>
                <c:otherwise>
                    <div class="recipe-data-actions"><button type="submit" name="opcao" value="atualizar" class="btn btn-secondary btn-sm">Atualizar dados da consulta</button></div>
                    <div class="recipe-medication">
                        <div class="section-title">
                            <div><h2>Adicionar medicamento</h2><p class="section-description">Selecione o medicamento e preencha as orientações de uso. Até 50 itens.</p></div>
                            <input type="search" class="catalog-search" data-catalog-search="medicamento-receita" aria-label="Buscar medicamento no catálogo" placeholder="Buscar no catálogo...">
                        </div>
                        <div class="medication-fields">
                            <div class="form-group">
                                <label for="medicamento-receita">Medicamento</label>
                                    <select id="medicamento-receita" name="medicamentoId">
                                        <option value="">Selecione um medicamento...</option>
                                        <c:forEach var="medicamento" items="${remedios}"><option value="${medicamento.idRemedio}" ${param.medicamentoId == medicamento.idRemedio ? 'selected' : ''}><c:out value="${medicamento.nome}"/></option></c:forEach>
                                    </select>
                                <c:if test="${empty remedios}"><small>O catálogo está vazio. Cadastre um medicamento para continuar.</small></c:if>
                            </div>
                            <div class="form-group"><label for="dosagem-receita">Dosagem</label><input id="dosagem-receita" name="dosagem" maxlength="100" value="<c:out value='${param.dosagem}'/>" placeholder="Dose e apresentação"></div>
                            <div class="form-group"><label for="posologia-receita">Posologia</label><input id="posologia-receita" name="posologia" maxlength="255" value="<c:out value='${param.posologia}'/>" placeholder="Frequência e duração do uso"></div>
                        </div>
                        <div class="actions medication-actions">
                            <button type="submit" name="opcao" value="adicionar" class="btn btn-secondary" ${empty remedios ? 'disabled' : ''}>+ Adicionar à receita</button>
                        </div>
                    </div>
                    <div class="recipe-items">
                        <div class="section-title"><h2>Medicamentos da receita</h2><span class="recipe-status">${rascunho.itens.size()} item(ns)</span></div>
                        <c:choose><c:when test="${empty rascunho.itens}"><div class="empty-state">A lista está vazia. Escolha um medicamento e informe a dosagem e a posologia.</div></c:when>
                        <c:otherwise>
                            <div class="table-responsive"><table class="table-crud" data-static>
                                <thead><tr><th>Medicamento</th><th>Dosagem</th><th>Posologia</th><th>Ação</th></tr></thead>
                                <tbody><c:forEach var="item" items="${rascunho.itens}" varStatus="linha">
                                    <tr><td><strong><c:out value="${item.medicamentoNome}"/></strong></td><td><c:out value="${item.dosagem}"/></td><td><c:out value="${item.posologia}"/></td>
                                        <td><button type="submit" name="opcao" value="remover:${linha.index}" class="btn btn-danger btn-sm" formnovalidate aria-label="Remover <c:out value='${item.medicamentoNome}'/>">Remover</button></td></tr>
                                </c:forEach></tbody>
                            </table></div>
                        </c:otherwise></c:choose>
                    </div>
                    <div class="recipe-finalize">
                        <p>Confira os itens antes de finalizar.<br>As alterações serão salvas juntas.</p>
                        <div class="actions">
                            <button type="submit" name="opcao" value="cancelar" class="btn btn-secondary" formnovalidate data-confirm="Descartar esta edição? As alterações ainda não foram salvas.">Cancelar edição</button>
                            <button type="submit" name="opcao" value="finalizar" class="btn btn-primary" ${empty rascunho.itens ? 'disabled' : ''}>${empty receitaEdicao.idReceita ? 'Finalizar receita' : 'Salvar alterações'}</button>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </form>
    </section>
    <h2>Receitas cadastradas</h2>
    <div class="table-responsive"><table class="table-crud">
        <thead><tr><th>Receita</th><th>Emissão</th><th>Paciente / médico</th><th>Medicamentos</th><th>Ações</th></tr></thead>
        <tbody><c:forEach var="r" items="${receitas}"><tr>
            <td><strong>#${r.idReceita}</strong><small class="muted" style="display:block">Consulta #${r.consultaId}</small></td>
            <td data-sort="${r.dataEmissao}">${r.dataFormatada}</td>
            <td><c:out value="${r.pacienteNome}"/><small class="muted" style="display:block"><c:out value="${r.medicoNome}"/></small></td>
            <td>${r.quantidadeItens} item(ns)</td>
            <td><div class="actions">
                <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}${URL_BASE}/ReceitaControlador?opcao=editar&amp;id_receita=${r.idReceita}" <c:if test="${not empty rascunho}">data-confirm="Abrir esta receita descartará a edição atual. Continuar?"</c:if>>Abrir / editar</a>
                <a class="btn btn-danger btn-sm" href="${pageContext.request.contextPath}${URL_BASE}/ReceitaControlador?opcao=excluir&amp;id_receita=${r.idReceita}">Excluir</a>
            </div></td>
        </tr></c:forEach></tbody>
    </table></div>
</main>
</body>
</html>
