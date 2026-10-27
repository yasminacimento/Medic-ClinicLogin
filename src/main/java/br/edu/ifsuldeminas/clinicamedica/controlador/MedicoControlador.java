/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.clinicamedica.controlador;

import br.edu.ifsuldeminas.clinicamedica.modelo.dao.MedicoDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.dao.EspecialidadeDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Especialidade;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Medico;
import br.edu.ifsuldeminas.clinicamedica.servico.WebConstante;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Alexandre
 */
@WebServlet(WebConstante.BASE_PATH + "/MedicoControlador")
public class MedicoControlador extends HttpServlet {

    private final MedicoDao medicoDao = new MedicoDao();
    private final EspecialidadeDao especialidadeDao = new EspecialidadeDao();

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

        Medico medico = new Medico();
        medico.setNome(request.getParameter("nome"));
        medico.setTelefone(request.getParameter("telefone"));
        medico.setCrm(request.getParameter("crm"));
        medico.setEspecialidadeId(Integer.parseInt(request.getParameter("especialidadeId")));
        medico.setEmail(request.getParameter("email"));
        medico.setEndereco(request.getParameter("endereco"));

        medicoDao.salvar(medico);

        request.setAttribute("mensagem", "Médico cadastrado com sucesso!");
        request.setAttribute("opcao", "cancelar");
        encaminharParaPagina(request, response);
    }

    private void enviarAlterar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("id_medico", request.getParameter("id_medico"));
        request.setAttribute("nome", request.getParameter("nome"));
        request.setAttribute("telefone", request.getParameter("telefone"));
        request.setAttribute("crm", request.getParameter("crm"));
        request.setAttribute("especialidadeId", request.getParameter("especialidadeId"));
        request.setAttribute("email", request.getParameter("email"));
        request.setAttribute("endereco", request.getParameter("endereco"));
        request.setAttribute("opcao", "confirmarAlterar");
        request.setAttribute("mensagem", "Edite os dados e clique em salvar.");

        encaminharParaPagina(request, response);
    }

    private void enviarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("id_medico", request.getParameter("id_medico"));
        request.setAttribute("nome", request.getParameter("nome"));
        request.setAttribute("telefone", request.getParameter("telefone"));
        request.setAttribute("crm", request.getParameter("crm"));
        request.setAttribute("especialidadeId", request.getParameter("especialidadeId"));
        request.setAttribute("email", request.getParameter("email"));
        request.setAttribute("endereco", request.getParameter("endereco"));

        request.setAttribute("opcao", "confirmarExcluir");
        request.setAttribute("mensagem", "Confirme os dados abaixo e clique em 'Confirmar Exclusão'.");

        encaminharParaPagina(request, response);
    }

    private void confirmarAlterar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Medico medico = new Medico();
        medico.setIdMedico(Integer.parseInt(request.getParameter("id_medico")));
        medico.setNome(request.getParameter("nome"));
        medico.setTelefone(request.getParameter("telefone"));
        medico.setCrm(request.getParameter("crm"));
        medico.setEspecialidadeId(Integer.parseInt(request.getParameter("especialidadeId")));
        medico.setEmail(request.getParameter("email"));
        medico.setEndereco(request.getParameter("endereco"));

        medicoDao.alterar(medico);

        request.setAttribute("mensagem", "Médico alterado com sucesso!");
        encaminharParaPagina(request, response);
    }

    private void confirmarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Medico medico = new Medico();
        medico.setIdMedico(Integer.parseInt(request.getParameter("id_medico")));

        medicoDao.excluir(medico);

        request.setAttribute("mensagem", "Médico excluído com sucesso!");
        encaminharParaPagina(request, response);
    }

    private void cancelar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("id_medico", "");
        request.setAttribute("nome", "");
        request.setAttribute("telefone", "");
        request.setAttribute("crm", "");
        request.setAttribute("especialidadeId", "");
        request.setAttribute("email", "");
        request.setAttribute("endereco", "");
        request.setAttribute("opcao", "cadastrar");

        encaminharParaPagina(request, response);
    }

    private void encaminharParaPagina(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Medico> medicos = medicoDao.buscarTodos();
        List<Especialidade> especialidades = especialidadeDao.buscarTodasEspecialidades();

        request.setAttribute("medicos", medicos);
        request.setAttribute("especialidades", especialidades);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/CadastroMedico.jsp");
        dispatcher.forward(request, response);
    }
}
