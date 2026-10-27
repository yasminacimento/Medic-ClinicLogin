/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.clinicamedica.controlador;

import br.edu.ifsuldeminas.clinicamedica.modelo.dao.PacienteDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.dao.ConvenioDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Paciente;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Convenio;
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
@WebServlet(WebConstante.BASE_PATH + "/PacienteControlador")
public class PacienteControlador extends HttpServlet {

    private final PacienteDao pacienteDao = new PacienteDao();
    private final ConvenioDao convenioDao = new ConvenioDao();

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
        Paciente p = new Paciente();
        p.setNome(request.getParameter("nome"));
        p.setCpf(request.getParameter("cpf"));
        p.setTelefone(request.getParameter("telefone"));
        p.setEmail(request.getParameter("email"));
        p.setEndereco(request.getParameter("endereco"));

        // Data de Nascimento
        String dataStr = request.getParameter("dataNascimento");
        if (dataStr != null && !dataStr.trim().isEmpty()) {
            p.setDataNascimento(LocalDate.parse(dataStr));
        }

        // Convênio
        String convenioStr = request.getParameter("convenioId");
        if (convenioStr != null && !convenioStr.trim().isEmpty()) {
            p.setConvenioId(Integer.parseInt(convenioStr));
        }

        pacienteDao.salvar(p);

        request.setAttribute("mensagem", "Paciente cadastrado com sucesso!");
    } catch (Exception e) {
        request.setAttribute("mensagem", "Erro ao cadastrar: " + e.getMessage());
        e.printStackTrace();
    }

    request.setAttribute("opcao", "cancelar");
    encaminharParaPagina(request, response);
}

    private void enviarAlterar(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    request.setAttribute("id_paciente", request.getParameter("id_paciente"));
    request.setAttribute("nome", request.getParameter("nome"));
    request.setAttribute("dataNascimento", request.getParameter("dataNascimento"));
    request.setAttribute("cpf", request.getParameter("cpf"));
    request.setAttribute("convenioId", request.getParameter("convenioId"));
    request.setAttribute("telefone", request.getParameter("telefone"));
    request.setAttribute("email", request.getParameter("email"));
    request.setAttribute("endereco", request.getParameter("endereco"));
    request.setAttribute("opcao", "confirmarAlterar");
    request.setAttribute("mensagem", "Edite os dados e clique em salvar.");

    encaminharParaPagina(request, response);
}

    private void enviarExcluir(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    request.setAttribute("id_paciente", request.getParameter("id_paciente"));
    request.setAttribute("nome", request.getParameter("nome"));
    request.setAttribute("dataNascimento", request.getParameter("dataNascimento"));
    request.setAttribute("cpf", request.getParameter("cpf"));
    request.setAttribute("convenioId", request.getParameter("convenioId"));   
    request.setAttribute("telefone", request.getParameter("telefone"));
    request.setAttribute("email", request.getParameter("email"));
    request.setAttribute("endereco", request.getParameter("endereco"));
    
    request.setAttribute("opcao", "confirmarExcluir");
    request.setAttribute("mensagem", "Confirme os dados e clique em Confirmar Exclusão.");

    encaminharParaPagina(request, response);
}

    private void confirmarAlterar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Paciente p = new Paciente();
        p.setIdPaciente(Integer.parseInt(request.getParameter("id_paciente")));
        p.setNome(request.getParameter("nome"));
        p.setCpf(request.getParameter("cpf"));
        p.setTelefone(request.getParameter("telefone"));
        p.setEmail(request.getParameter("email"));
        p.setEndereco(request.getParameter("endereco"));

        String dataStr = request.getParameter("dataNascimento");
        if (dataStr != null && !dataStr.isEmpty()) {
            p.setDataNascimento(LocalDate.parse(dataStr));
        }

        String convenioStr = request.getParameter("convenioId");
        if (convenioStr != null && !convenioStr.isEmpty()) {
            p.setConvenioId(Integer.parseInt(convenioStr));
        }

        pacienteDao.alterar(p);
        request.setAttribute("mensagem", "Paciente alterado com sucesso!");
        encaminharParaPagina(request, response);
    }

    private void confirmarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Paciente p = new Paciente();
        p.setIdPaciente(Integer.parseInt(request.getParameter("id_paciente")));
        pacienteDao.excluir(p);

        request.setAttribute("mensagem", "Paciente excluído com sucesso!");
        encaminharParaPagina(request, response);
    }

    private void cancelar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("id_paciente", "");
        request.setAttribute("nome", "");
        request.setAttribute("dataNascimento", "");
        request.setAttribute("cpf", "");
        request.setAttribute("convenioId", "");
        request.setAttribute("telefone", "");
        request.setAttribute("email", "");
        request.setAttribute("endereco", "");
        request.setAttribute("opcao", "cadastrar");

        encaminharParaPagina(request, response);
    }

    private void encaminharParaPagina(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Paciente> pacientes = pacienteDao.buscarTodosPacientes();
        List<Convenio> convenios = convenioDao.buscarTodasConvenios();

        request.setAttribute("pacientes", pacientes);
        request.setAttribute("convenios", convenios);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/CadastroPaciente.jsp");
        dispatcher.forward(request, response);
    }
}