/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.clinicamedica.controlador;

/**
 *
 * @author 17652433606
 */

import br.edu.ifsuldeminas.clinicamedica.servico.WebConstante;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import br.edu.ifsuldeminas.clinicamedica.modelo.dao.ConvenioDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Convenio;

@WebServlet(WebConstante.BASE_PATH + "/ConvenioControlador")
public class ConvenioControlador extends HttpServlet {

    ConvenioDao convenioDao = new ConvenioDao();

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

        } catch (NumberFormatException e) {
            response.getWriter().println("Erro: parâmetros inválidos. " + e.getMessage());
        } catch (IllegalArgumentException e) {
            response.getWriter().println("Erro: " + e.getMessage());
        }
    }

    private void cadastrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nome = request.getParameter("nome");
        String registro_ans = request.getParameter("registro_ans");
        String telefone_contato = request.getParameter("telefone_contato");

        if (nome == null || nome.isBlank()) {
            encaminharParaPagina(request, response);
            return;
        }

        Convenio convenio = new Convenio();
        convenio.setNome(nome);
        convenio.setRegistro_ans(registro_ans);
        convenio.setTelefone_contato(telefone_contato);

        convenioDao.salvar(convenio);

        request.setAttribute("mensagem", "Convênio cadastrado com sucesso!");
        request.setAttribute("opcao", "cadastrar");

        encaminharParaPagina(request, response);
    }

    private void enviarAlterar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id_convenio");
        String nome = request.getParameter("nome");
        String registro_ans = request.getParameter("registro_ans");
        String telefone_contato = request.getParameter("telefone_contato");

        request.setAttribute("id_convenio", id);
        request.setAttribute("nome", nome);
        request.setAttribute("registro_ans", registro_ans);
        request.setAttribute("telefone_contato", telefone_contato);
        request.setAttribute("opcao", "confirmarAlterar");
        request.setAttribute("mensagem", "Edite os dados e clique em salvar.");

        encaminharParaPagina(request, response);
    }

    private void enviarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id_convenio");
        String nome = request.getParameter("nome");
        String registro_ans = request.getParameter("registro_ans");
        String telefone_contato = request.getParameter("telefone_contato");

        request.setAttribute("id_convenio", id);
        request.setAttribute("nome", nome);
        request.setAttribute("registro_ans", registro_ans);
        request.setAttribute("telefone_contato", telefone_contato);
        request.setAttribute("opcao", "confirmarExcluir");
        request.setAttribute("mensagem", "Confirme os dados e clique em salvar para EXCLUIR.");

        encaminharParaPagina(request, response);
    }

    private void confirmarAlterar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id_convenio");
        String nome = request.getParameter("nome");
        String registro_ans = request.getParameter("registro_ans");
        String telefone_contato = request.getParameter("telefone_contato");

        Convenio convenio = new Convenio();
        convenio.setId_convenio(Integer.parseInt(id));
        convenio.setNome(nome);
        convenio.setRegistro_ans(registro_ans);
        convenio.setTelefone_contato(telefone_contato);

        convenioDao.alterar(convenio);

        request.setAttribute("mensagem", "Convênio alterado com sucesso!");

        encaminharParaPagina(request, response);
    }

    private void confirmarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id_convenio");

        Convenio convenio = new Convenio();
        convenio.setId_convenio(Integer.parseInt(id));

        convenioDao.excluir(convenio);

        request.setAttribute("mensagem", "Convênio excluído com sucesso!");

        encaminharParaPagina(request, response);
    }

    private void encaminharParaPagina(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Convenio> convenios = convenioDao.buscarTodasConvenios();
        request.setAttribute("convenios", convenios);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/CadastroConvenio.jsp");
        dispatcher.forward(request, response);
    }

    private void cancelar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("id_convenio", "");
        request.setAttribute("nome", "");
        request.setAttribute("registro_ans", "");
        request.setAttribute("telefone_contato", "");
        request.setAttribute("opcao", "cadastrar");

        encaminharParaPagina(request, response);
    }
}