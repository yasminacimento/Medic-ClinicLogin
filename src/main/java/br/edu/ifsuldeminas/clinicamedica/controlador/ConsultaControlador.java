/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.clinicamedica.controlador;

import br.edu.ifsuldeminas.clinicamedica.modelo.dao.ConsultaDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.dao.MedicoDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.dao.PacienteDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.dao.ConsultorioDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Consulta;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Consultorio;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Medico;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Paciente;
import br.edu.ifsuldeminas.clinicamedica.servico.WebConstante;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 *
 * @author Alexandre
 */
@WebServlet(WebConstante.BASE_PATH + "/ConsultaControlador")
public class ConsultaControlador extends HttpServlet {

    private final ConsultaDao consultaDao = new ConsultaDao();
    private final MedicoDao medicoDao = new MedicoDao();
    private final PacienteDao pacienteDao = new PacienteDao();
    private final ConsultorioDao consultorioDao = new ConsultorioDao();

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
            Consulta c = new Consulta();
            c.setData(LocalDate.parse(request.getParameter("data")));
            c.setHora(LocalTime.parse(request.getParameter("hora")));
            c.setStatus("agendada");
            c.setMedicoId(Integer.parseInt(request.getParameter("medicoId")));
            c.setPacienteId(Integer.parseInt(request.getParameter("pacienteId")));
            c.setConsultorioId(Integer.parseInt(request.getParameter("consultorioId")));

            consultaDao.salvar(c);
            request.setAttribute("mensagem", "Consulta cadastrada com sucesso!");
        } catch (Exception e) {
            request.setAttribute("mensagem", "Erro ao cadastrar consulta: " + e.getMessage());
            e.printStackTrace();
        }

        request.setAttribute("opcao", "cancelar");
        encaminharParaPagina(request, response);
    }

    private void enviarAlterar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("id_consulta", request.getParameter("id_consulta"));
        request.setAttribute("data", request.getParameter("data"));
        request.setAttribute("hora", request.getParameter("hora"));
        request.setAttribute("status", request.getParameter("status"));
        request.setAttribute("medicoId", request.getParameter("medicoId"));
        request.setAttribute("pacienteId", request.getParameter("pacienteId"));
        request.setAttribute("consultorioId", request.getParameter("consultorioId"));
        request.setAttribute("opcao", "confirmarAlterar");
        request.setAttribute("mensagem", "Edite os dados e clique em salvar.");

        encaminharParaPagina(request, response);
    }

    private void enviarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("id_consulta", request.getParameter("id_consulta"));
        request.setAttribute("data", request.getParameter("data"));
        request.setAttribute("hora", request.getParameter("hora"));
        request.setAttribute("status", request.getParameter("status"));
        request.setAttribute("medicoId", request.getParameter("medicoId"));
        request.setAttribute("pacienteId", request.getParameter("pacienteId"));
        request.setAttribute("consultorioId", request.getParameter("consultorioId"));
        request.setAttribute("opcao", "confirmarExcluir");
        request.setAttribute("mensagem", "Confirme os dados e clique em Confirmar Exclusão.");

        encaminharParaPagina(request, response);
    }

    private void confirmarAlterar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Consulta c = new Consulta();
        c.setIdConsulta(Integer.parseInt(request.getParameter("id_consulta")));
        c.setData(LocalDate.parse(request.getParameter("data")));
        c.setHora(LocalTime.parse(request.getParameter("hora")));
        c.setStatus(request.getParameter("status"));
        c.setMedicoId(Integer.parseInt(request.getParameter("medicoId")));
        c.setPacienteId(Integer.parseInt(request.getParameter("pacienteId")));
        c.setConsultorioId(Integer.parseInt(request.getParameter("consultorioId")));

        consultaDao.alterar(c);
        request.setAttribute("mensagem", "Consulta alterada com sucesso!");
        encaminharParaPagina(request, response);
    }

    private void confirmarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Consulta c = new Consulta();
        c.setIdConsulta(Integer.parseInt(request.getParameter("id_consulta")));
        consultaDao.excluir(c);

        request.setAttribute("mensagem", "Consulta excluída com sucesso!");
        encaminharParaPagina(request, response);
    }

    private void cancelar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("id_consulta", "");
        request.setAttribute("data", "");
        request.setAttribute("hora", "");
        request.setAttribute("status", "agendada");
        request.setAttribute("medicoId", "");
        request.setAttribute("pacienteId", "");
        request.setAttribute("consultorioId", "");
        request.setAttribute("opcao", "cadastrar");

        encaminharParaPagina(request, response);
    }

    private void encaminharParaPagina(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Consulta> consultas = consultaDao.buscarTodasConsultas();
        List<Medico> medicos = medicoDao.buscarTodos();
        List<Paciente> pacientes = pacienteDao.buscarTodosPacientes();
        List<Consultorio> consultorios = consultorioDao.buscarTodasConsultorios();

        request.setAttribute("consultas", consultas);
        request.setAttribute("medicos", medicos);
        request.setAttribute("pacientes", pacientes);
        request.setAttribute("consultorios", consultorios);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/CadastroConsulta.jsp");
        dispatcher.forward(request, response);
    }
}