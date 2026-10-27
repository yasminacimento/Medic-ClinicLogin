/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.clinicamedica.controlador;

import br.edu.ifsuldeminas.clinicamedica.modelo.dao.ProntuarioDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.dao.ConsultaDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Consulta;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Prontuario;
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
@WebServlet(WebConstante.BASE_PATH + "/ProntuarioControlador")
public class ProntuarioControlador extends HttpServlet {

    private final ProntuarioDao prontuarioDao = new ProntuarioDao();
    private final ConsultaDao consultaDao = new ConsultaDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String opcao = request.getParameter("opcao");
            if (opcao == null || opcao.isEmpty()) {
                opcao = "cadastrar";
            }

            switch (opcao) {
                case "cadastrar":
                    cadastrar(request, response);
                    break;
                case "enviarAlterar":
                    enviarAlterar(request, response);
                    break;
                case "enviarExcluir":
                    enviarExcluir(request, response);
                    break;
                case "confirmarAlterar":
                    confirmarAlterar(request, response);
                    break;
                case "confirmarExcluir":
                    confirmarExcluir(request, response);
                    break;
                case "cancelar":
                    cancelar(request, response);
                    break;
                default:
                    throw new IllegalArgumentException("Opção inválida: " + opcao);
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
            Prontuario p = new Prontuario();
            p.setDiagnostico(request.getParameter("diagnostico"));
            p.setDescricaoSintomas(request.getParameter("descricaoSintomas"));
            p.setDataRegistro(LocalDate.parse(request.getParameter("dataRegistro")));
            p.setConsultaId(Integer.parseInt(request.getParameter("consultaId")));

            prontuarioDao.salvar(p);
            request.setAttribute("mensagem", "Prontuário cadastrado com sucesso!");
        } catch (Exception e) {
            request.setAttribute("mensagem", "Erro ao cadastrar prontuário: " + e.getMessage());
            e.printStackTrace();
        }

        request.setAttribute("opcao", "cancelar");
        encaminharParaPagina(request, response);
    }

    private void enviarAlterar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("id_prontuario", request.getParameter("id_prontuario"));
        request.setAttribute("diagnostico", request.getParameter("diagnostico"));
        request.setAttribute("descricaoSintomas", request.getParameter("descricaoSintomas"));
        request.setAttribute("dataRegistro", request.getParameter("dataRegistro"));
        request.setAttribute("consultaId", request.getParameter("consultaId"));
        request.setAttribute("opcao", "confirmarAlterar");
        request.setAttribute("mensagem", "Edite os dados e clique em salvar.");

        encaminharParaPagina(request, response);
    }

    private void enviarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("id_prontuario", request.getParameter("id_prontuario"));
        request.setAttribute("diagnostico", request.getParameter("diagnostico"));
        request.setAttribute("descricaoSintomas", request.getParameter("descricaoSintomas"));
        request.setAttribute("dataRegistro", request.getParameter("dataRegistro"));
        request.setAttribute("consultaId", request.getParameter("consultaId"));

        request.setAttribute("opcao", "confirmarExcluir");
        request.setAttribute("mensagem", "Confirme os dados e clique em Confirmar Exclusão.");

        encaminharParaPagina(request, response);
    }

    private void confirmarAlterar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Prontuario p = new Prontuario();
        p.setIdProntuario(Integer.parseInt(request.getParameter("id_prontuario")));
        p.setDiagnostico(request.getParameter("diagnostico"));
        p.setDescricaoSintomas(request.getParameter("descricaoSintomas"));
        p.setDataRegistro(LocalDate.parse(request.getParameter("dataRegistro")));
        p.setConsultaId(Integer.parseInt(request.getParameter("consultaId")));

        prontuarioDao.alterar(p);
        request.setAttribute("mensagem", "Prontuário alterado com sucesso!");
        encaminharParaPagina(request, response);
    }

    private void confirmarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Prontuario p = new Prontuario();
        p.setIdProntuario(Integer.parseInt(request.getParameter("id_prontuario")));
        prontuarioDao.excluir(p);

        request.setAttribute("mensagem", "Prontuário excluído com sucesso!");
        encaminharParaPagina(request, response);
    }

    private void cancelar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("id_prontuario", "");
        request.setAttribute("diagnostico", "");
        request.setAttribute("descricaoSintomas", "");
        request.setAttribute("dataRegistro", "");
        request.setAttribute("consultaId", "");
        request.setAttribute("opcao", "cadastrar");

        encaminharParaPagina(request, response);
    }

    private void encaminharParaPagina(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Prontuario> prontuarios = prontuarioDao.buscarTodosProntuarios();
        List<Consulta> consultas = consultaDao.buscarTodasConsultas();

        request.setAttribute("prontuarios", prontuarios);
        request.setAttribute("consultas", consultas);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/CadastroProntuario.jsp");
        dispatcher.forward(request, response);
    }
}
