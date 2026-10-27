package br.edu.ifsuldeminas.clinicamedica.controlador;

import br.edu.ifsuldeminas.clinicamedica.modelo.dao.ConsultaDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.dao.ReceitaDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.dao.RemedioDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.ItemReceita;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.RascunhoReceita;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Receita;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Remedio;
import br.edu.ifsuldeminas.clinicamedica.servico.ReceitaService;
import br.edu.ifsuldeminas.clinicamedica.servico.WebConstante;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet(WebConstante.BASE_PATH + "/ReceitaControlador")
public class ReceitaControlador extends HttpServlet {
    private final ReceitaDao receitaDao = new ReceitaDao();
    private final ConsultaDao consultaDao = new ConsultaDao();
    private final RemedioDao remedioDao = new RemedioDao();
    private final ReceitaService service = new ReceitaService(receitaDao);

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            String opcao = request.getParameter("opcao");
            if ("nova".equals(opcao)) {
                if (session.getAttribute("rascunhoReceita") != null) {
                    request.setAttribute("mensagem", "Há uma receita em edição. Finalize ou cancele antes de iniciar outra.");
                    request.setAttribute("tipoMensagem", "info");
                }
            } else if ("editar".equals(opcao) || "enviarAlterar".equals(opcao)) {
                Receita receita = receitaDao.buscarPorId(id(request, "id_receita"));
                if (receita == null) { throw new IllegalArgumentException("Receita não encontrada."); }
                RascunhoReceita rascunho = new RascunhoReceita(receita);
                rascunho.getItens().addAll(receitaDao.buscarItensPorReceita(receita.getIdReceita()));
                session.setAttribute("rascunhoReceita", rascunho);
            } else if ("excluir".equals(opcao) || "enviarExcluir".equals(opcao)) {
                Receita receita = receitaDao.buscarPorId(id(request, "id_receita"));
                if (receita == null) { throw new IllegalArgumentException("Receita não encontrada."); }
                request.setAttribute("receitaExcluir", receita);
            }
            Object mensagem = session.getAttribute("mensagemReceita");
            if (mensagem != null) {
                request.setAttribute("mensagem", mensagem);
                request.setAttribute("tipoMensagem", "sucesso");
                session.removeAttribute("mensagemReceita");
            }
        } catch (IllegalArgumentException ex) {
            request.setAttribute("mensagem", ex.getMessage());
        } catch (SQLException ex) {
            erroBanco(request, ex);
        }
        mostrar(request, response);
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        // Serializa os comandos do mesmo rascunho e impede duplicacao ao clicar duas vezes em Finalizar.
        synchronized (session) {
            String opcao = request.getParameter("opcao");
            try {
                if ("excluir".equals(opcao)) {
                    int receitaId = id(request, "id_receita");
                    receitaDao.excluirComItens(receitaId);
                    RascunhoReceita atual = (RascunhoReceita) session.getAttribute("rascunhoReceita");
                    if (atual != null && Integer.valueOf(receitaId).equals(atual.getReceita().getIdReceita())) {
                        session.removeAttribute("rascunhoReceita");
                    }
                    sucesso(request, response, "Receita e seus medicamentos excluídos com sucesso.");
                    return;
                }
                if ("iniciar".equals(opcao)) {
                    if (session.getAttribute("rascunhoReceita") != null) {
                        throw new IllegalArgumentException("Já existe uma receita em edição. Finalize ou cancele antes de iniciar outra.");
                    }
                    Receita receita = lerCabecalho(request, null);
                    session.setAttribute("rascunhoReceita", new RascunhoReceita(receita));
                    sucesso(request, response, "Dados definidos. Adicione os medicamentos da receita.");
                    return;
                }
                RascunhoReceita rascunho = (RascunhoReceita) session.getAttribute("rascunhoReceita");
                if (rascunho == null || !rascunho.getIdentificador().equals(request.getParameter("rascunhoId"))) {
                    throw new IllegalArgumentException("Esta edição já foi encerrada ou aberta em outra aba. Abra a receita novamente.");
                }
                if ("cancelar".equals(opcao)) {
                    session.removeAttribute("rascunhoReceita");
                    sucesso(request, response, "Edição cancelada. As alterações não foram gravadas.");
                    return;
                }
                if (opcao != null && opcao.startsWith("remover:")) {
                    int indice = Integer.parseInt(opcao.substring(8));
                    if (indice < 0 || indice >= rascunho.getItens().size()) {
                        throw new IllegalArgumentException("O item já foi removido. Confira a lista atual.");
                    }
                    rascunho.getItens().remove(indice);
                    sucesso(request, response, "Medicamento removido da lista.");
                    return;
                }
                if (!"adicionar".equals(opcao) && !"finalizar".equals(opcao) && !"atualizar".equals(opcao)) {
                    throw new IllegalArgumentException("Ação inválida.");
                }
                rascunho.setReceita(lerCabecalho(request, rascunho.getReceita().getIdReceita()));
                if ("adicionar".equals(opcao)) {
                    if (rascunho.getItens().size() >= 50) {
                        throw new IllegalArgumentException("Uma receita pode ter até 50 medicamentos.");
                    }
                    ItemReceita item = new ItemReceita();
                    item.setMedicamentoId(id(request, "medicamentoId"));
                    item.setDosagem(texto(request, "dosagem"));
                    item.setPosologia(texto(request, "posologia"));
                    service.validarItem(item);
                    Remedio remedio = remedioDao.buscarRemedioPorId(item.getMedicamentoId());
                    if (remedio == null) { throw new IllegalArgumentException("Selecione um medicamento cadastrado."); }
                    for (ItemReceita existente : rascunho.getItens()) {
                        if (existente.getMedicamentoId().equals(item.getMedicamentoId())) {
                            throw new IllegalArgumentException("Este medicamento já está na receita. Remova-o para alterar a dosagem ou a posologia.");
                        }
                    }
                    item.setMedicamentoNome(remedio.getNome());
                    rascunho.getItens().add(item);
                    sucesso(request, response, "Medicamento adicionado à receita.");
                } else if ("finalizar".equals(opcao)) {
                    int receitaId = service.salvar(rascunho.getReceita(), rascunho.getItens());
                    session.removeAttribute("rascunhoReceita");
                    sucesso(request, response, "Receita #" + receitaId + " e seus medicamentos salvos com sucesso.");
                } else {
                    sucesso(request, response, "Dados da receita atualizados. Finalize para salvar.");
                }
                return;
            } catch (IllegalArgumentException ex) {
                request.setAttribute("mensagem", ex instanceof NumberFormatException
                        ? "Selecione uma opção válida e confira os campos informados." : ex.getMessage());
            } catch (SQLException ex) {
                erroBanco(request, ex);
            }
            mostrar(request, response);
        }
    }

    private Receita lerCabecalho(HttpServletRequest request, Integer receitaId) {
        Receita receita = new Receita();
        receita.setIdReceita(receitaId);
        receita.setConsultaId(id(request, "consultaId"));
        try { receita.setDataEmissao(Date.valueOf(request.getParameter("dataEmissao"))); }
        catch (RuntimeException ex) { throw new IllegalArgumentException("Informe uma data de emissão válida."); }
        receita.setObservacoes(texto(request, "observacoes"));
        service.validarCabecalho(receita);
        return receita;
    }

    private int id(HttpServletRequest request, String campo) {
        String valor = request.getParameter(campo);
        if (valor == null || valor.isBlank()) { throw new IllegalArgumentException("Selecione a consulta ou o medicamento desejado."); }
        int id = Integer.parseInt(valor);
        if (id < 1) { throw new IllegalArgumentException("Identificador inválido."); }
        return id;
    }

    private String texto(HttpServletRequest request, String campo) {
        String valor = request.getParameter(campo);
        return valor == null ? "" : valor.trim();
    }

    private void sucesso(HttpServletRequest request, HttpServletResponse response, String mensagem) throws IOException {
        request.getSession().setAttribute("mensagemReceita", mensagem);
        response.sendRedirect(request.getContextPath() + WebConstante.BASE_PATH + "/ReceitaControlador");
    }

    private void erroBanco(HttpServletRequest request, SQLException ex) {
        getServletContext().log("Falha na operação de receita.", ex);
        request.setAttribute("mensagem", "Não foi possível salvar a operação. Confira a consulta e os medicamentos. Seus dados de edição foram mantidos.");
    }

    private void mostrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RascunhoReceita rascunho = (RascunhoReceita) request.getSession().getAttribute("rascunhoReceita");
        request.setAttribute("rascunho", rascunho);
        Receita receita = rascunho == null ? new Receita() : rascunho.getReceita();
        if (receita.getDataEmissao() == null) { receita.setDataEmissao(Date.valueOf(LocalDate.now())); }
        request.setAttribute("receitaEdicao", receita);
        request.setAttribute("consultas", consultaDao.buscarTodasConsultas());
        request.setAttribute("remedios", remedioDao.buscarTodosRemedios());
        try { request.setAttribute("receitas", receitaDao.buscarTodasReceitas()); }
        catch (SQLException ex) { erroBanco(request, ex); }
        request.getRequestDispatcher("/CadastroReceita.jsp").forward(request, response);
    }
}
