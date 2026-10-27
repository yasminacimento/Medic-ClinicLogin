/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.clinicamedica.controlador;

import br.edu.ifsuldeminas.clinicamedica.modelo.dao.SolicitacaoExameDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.dao.ConsultaDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.dao.TipoExameDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.dao.FuncionarioDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Consulta;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Funcionario;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.SolicitacaoExame;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.TipoExame;
import br.edu.ifsuldeminas.clinicamedica.servico.WebConstante;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Alexandre
 */
@WebServlet(WebConstante.BASE_PATH + "/SolicitacaoExameControlador")
public class SolicitacaoExameControlador extends HttpServlet {

    private final SolicitacaoExameDao solicitacaoDao = new SolicitacaoExameDao();
    private final ConsultaDao consultaDao = new ConsultaDao();
    private final TipoExameDao tipoExameDao = new TipoExameDao();
    private final FuncionarioDao funcionarioDao = new FuncionarioDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String opcao = request.getParameter("opcao");
            if (opcao == null || opcao.isEmpty()) {
                opcao = "cadastrar";
            }

            switch (opcao) {
                case "cadastrar": cadastrar(request, response); break;
                case "enviarAlterar": enviarAlterar(request, response); break;
                case "enviarExcluir": enviarExcluir(request, response); break;
                case "confirmarAlterar": confirmarAlterar(request, response); break;
                case "confirmarExcluir": confirmarExcluir(request, response); break;
                case "cancelar": cancelar(request, response); break;
                default: throw new IllegalArgumentException("Opção inválida: " + opcao);
            }
        } catch (Exception e) {
            request.setAttribute("mensagem", "Erro: " + e.getMessage());
            e.printStackTrace();
            encaminharParaPagina(request, response);
        }
    }

    private void cadastrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            SolicitacaoExame se = new SolicitacaoExame();
            se.setData(LocalDate.parse(request.getParameter("data")));
            se.setResultado(request.getParameter("resultado"));
            se.setConsultaId(Integer.parseInt(request.getParameter("consultaId")));
            se.setTipoExameId(Integer.parseInt(request.getParameter("tipoExameId")));
            se.setFuncionarioId(Integer.parseInt(request.getParameter("funcionarioId")));

            solicitacaoDao.salvar(se);
            request.setAttribute("mensagem", "Solicitação de exame cadastrada com sucesso!");
        } catch (Exception e) {
            request.setAttribute("mensagem", "Erro ao cadastrar solicitação: " + e.getMessage());
            e.printStackTrace();
        }

        request.setAttribute("opcao", "cancelar");
        encaminharParaPagina(request, response);
    }

    private void enviarAlterar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("id_solicitacaoExame", request.getParameter("id_solicitacaoExame"));
        request.setAttribute("data", request.getParameter("data"));
        request.setAttribute("resultado", request.getParameter("resultado"));
        request.setAttribute("consultaId", request.getParameter("consultaId"));
        request.setAttribute("tipoExameId", request.getParameter("tipoExameId"));
        request.setAttribute("funcionarioId", request.getParameter("funcionarioId"));
        request.setAttribute("opcao", "confirmarAlterar");
        request.setAttribute("mensagem", "Edite os dados e clique em salvar.");

        encaminharParaPagina(request, response);
    }

    private void enviarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("id_solicitacaoExame", request.getParameter("id_solicitacaoExame"));
        request.setAttribute("data", request.getParameter("data"));
        request.setAttribute("resultado", request.getParameter("resultado"));
        request.setAttribute("consultaId", request.getParameter("consultaId"));
        request.setAttribute("tipoExameId", request.getParameter("tipoExameId"));
        request.setAttribute("funcionarioId", request.getParameter("funcionarioId"));
        request.setAttribute("opcao", "confirmarExcluir");
        request.setAttribute("mensagem", "Confirme os dados e clique em Confirmar Exclusão.");

        encaminharParaPagina(request, response);
    }

    private void confirmarAlterar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SolicitacaoExame se = new SolicitacaoExame();
        se.setIdSolicitacaoExame(Integer.parseInt(request.getParameter("id_solicitacaoExame")));
        se.setData(LocalDate.parse(request.getParameter("data")));
        se.setResultado(request.getParameter("resultado"));
        se.setConsultaId(Integer.parseInt(request.getParameter("consultaId")));
        se.setTipoExameId(Integer.parseInt(request.getParameter("tipoExameId")));
        se.setFuncionarioId(Integer.parseInt(request.getParameter("funcionarioId")));

        solicitacaoDao.alterar(se);
        request.setAttribute("mensagem", "Solicitação alterada com sucesso!");
        encaminharParaPagina(request, response);
    }

    private void confirmarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SolicitacaoExame se = new SolicitacaoExame();
        se.setIdSolicitacaoExame(Integer.parseInt(request.getParameter("id_solicitacaoExame")));
        solicitacaoDao.excluir(se);

        request.setAttribute("mensagem", "Solicitação excluída com sucesso!");
        encaminharParaPagina(request, response);
    }

    private void cancelar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("id_solicitacaoExame", "");
        request.setAttribute("data", "");
        request.setAttribute("resultado", "");
        request.setAttribute("consultaId", "");
        request.setAttribute("tipoExameId", "");
        request.setAttribute("funcionarioId", "");
        request.setAttribute("opcao", "cadastrar");

        encaminharParaPagina(request, response);
    }

    private void encaminharParaPagina(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<SolicitacaoExame> solicitacoes = solicitacaoDao.buscarTodasSolicitacoes();
        List<Consulta> consultas = consultaDao.buscarTodasConsultas();
        List<TipoExame> tiposExame = tipoExameDao.buscarTodosTipoExames();
        List<Funcionario> funcionarios = funcionarioDao.buscarTodasFuncionarios();

        request.setAttribute("solicitacoes", solicitacoes);
        request.setAttribute("consultas", consultas);
        request.setAttribute("tiposExame", tiposExame);
        request.setAttribute("funcionarios", funcionarios);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/CadastroSolicitacaoExame.jsp");
        dispatcher.forward(request, response);
    }
}