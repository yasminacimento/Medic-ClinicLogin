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
import br.edu.ifsuldeminas.clinicamedica.modelo.dao.ConsultorioDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Consultorio;

@WebServlet(WebConstante.BASE_PATH + "/ConsultorioControlador")
public class ConsultorioControlador extends HttpServlet {

    ConsultorioDao consultorioDao = new ConsultorioDao();

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

        String numeroStr = request.getParameter("numero");
        String andarStr = request.getParameter("andar");
        String capacidadeStr = request.getParameter("capacidade");

        if (numeroStr == null || numeroStr.isBlank()) {
            encaminharParaPagina(request, response);
            return;
        }

        Consultorio consultorio = new Consultorio();
        consultorio.setNumero(Integer.parseInt(numeroStr));
        consultorio.setAndar(Integer.parseInt(andarStr));
        consultorio.setCapacidade(Integer.parseInt(capacidadeStr));

        consultorioDao.salvar(consultorio);

        request.setAttribute("mensagem", "Consultório cadastrado com sucesso!");
        request.setAttribute("opcao", "cadastrar");

        encaminharParaPagina(request, response);
    }

    private void enviarAlterar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id_consultorio");
        String numero = request.getParameter("numero");
        String andar = request.getParameter("andar");
        String capacidade = request.getParameter("capacidade");

        request.setAttribute("id_consultorio", id);
        request.setAttribute("numero", numero);
        request.setAttribute("andar", andar);
        request.setAttribute("capacidade", capacidade);
        request.setAttribute("opcao", "confirmarAlterar");
        request.setAttribute("mensagem", "Edite os dados e clique em salvar.");

        encaminharParaPagina(request, response);
    }

    private void enviarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id_consultorio");
        String numero = request.getParameter("numero");
        String andar = request.getParameter("andar");
        String capacidade = request.getParameter("capacidade");

        request.setAttribute("id_consultorio", id);
        request.setAttribute("numero", numero);
        request.setAttribute("andar", andar);
        request.setAttribute("capacidade", capacidade);
        request.setAttribute("opcao", "confirmarExcluir");
        request.setAttribute("mensagem", "Confirme os dados e clique em salvar para EXCLUIR.");

        encaminharParaPagina(request, response);
    }

    private void confirmarAlterar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id_consultorio");
        String numeroStr = request.getParameter("numero");
        String andarStr = request.getParameter("andar");
        String capacidadeStr = request.getParameter("capacidade");

        Consultorio consultorio = new Consultorio();
        consultorio.setId_consultorio(Integer.parseInt(id));
        consultorio.setNumero(Integer.parseInt(numeroStr));
        consultorio.setAndar(Integer.parseInt(andarStr));
        consultorio.setCapacidade(Integer.parseInt(capacidadeStr));

        consultorioDao.alterar(consultorio);

        request.setAttribute("mensagem", "Consultório alterado com sucesso!");

        encaminharParaPagina(request, response);
    }

    private void confirmarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id_consultorio");

        Consultorio consultorio = new Consultorio();
        consultorio.setId_consultorio(Integer.parseInt(id));

        consultorioDao.excluir(consultorio);

        request.setAttribute("mensagem", "Consultório excluído com sucesso!");

        encaminharParaPagina(request, response);
    }

    private void encaminharParaPagina(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Consultorio> consultorios = consultorioDao.buscarTodasConsultorios();
        request.setAttribute("consultorios", consultorios);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/CadastroConsultorio.jsp");
        dispatcher.forward(request, response);
    }

    private void cancelar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("id_consultorio", "");
        request.setAttribute("numero", "");
        request.setAttribute("andar", "");
        request.setAttribute("capacidade", "");
        request.setAttribute("opcao", "cadastrar");

        encaminharParaPagina(request, response);
    }
}