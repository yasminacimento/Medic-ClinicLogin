package br.edu.ifsuldeminas.clinicamedica.controlador;

import br.edu.ifsuldeminas.clinicamedica.servico.WebConstante;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import br.edu.ifsuldeminas.clinicamedica.modelo.dao.EspecialidadeDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Especialidade;

@WebServlet(WebConstante.BASE_PATH + "/EspecialidadeControlador")
public class EspecialidadeControlador extends HttpServlet {

    EspecialidadeDao especialidadeDao = new EspecialidadeDao();

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
        String descricao = request.getParameter("descricao");

        if (nome == null || nome.isBlank()) {
            encaminharParaPagina(request, response);
            return;
        }

        Especialidade especialidade = new Especialidade();
        especialidade.setNome(nome);
        especialidade.setDescricao(descricao);

        especialidadeDao.salvar(especialidade);

        request.setAttribute("mensagem", "Especialidade cadastrada com sucesso!");
        request.setAttribute("opcao", "cadastrar");

        encaminharParaPagina(request, response);
    }

    private void enviarAlterar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id_especialidade");
        String nome = request.getParameter("nome");
        String descricao = request.getParameter("descricao");

        request.setAttribute("id_especialidade", id);
        request.setAttribute("nome", nome);
        request.setAttribute("descricao", descricao);
        request.setAttribute("opcao", "confirmarAlterar");
        request.setAttribute("mensagem", "Edite os dados e clique em salvar.");

        encaminharParaPagina(request, response);
    }

    private void enviarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id_especialidade");
        String nome = request.getParameter("nome");
        String descricao = request.getParameter("descricao");

        request.setAttribute("id_especialidade", id);
        request.setAttribute("nome", nome);
        request.setAttribute("descricao", descricao);
        request.setAttribute("opcao", "confirmarExcluir");
        request.setAttribute("mensagem", "Confirme os dados e clique em salvar para EXCLUIR.");

        encaminharParaPagina(request, response);
    }

    private void confirmarAlterar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id_especialidade");
        String nome = request.getParameter("nome");
        String descricao = request.getParameter("descricao");

        Especialidade especialidade = new Especialidade();
        especialidade.setId_especialidade(Integer.parseInt(id));
        especialidade.setNome(nome);
        especialidade.setDescricao(descricao);

        especialidadeDao.alterar(especialidade);

        request.setAttribute("mensagem", "Especialidade alterada com sucesso!");

        encaminharParaPagina(request, response);
    }

    private void confirmarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id_especialidade");

        Especialidade especialidade = new Especialidade();
        especialidade.setId_especialidade(Integer.parseInt(id));

        especialidadeDao.excluir(especialidade);

        request.setAttribute("mensagem", "Especialidade excluída com sucesso!");

        encaminharParaPagina(request, response);
    }

    private void encaminharParaPagina(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Especialidade> especialidades = especialidadeDao.buscarTodasEspecialidades();
        request.setAttribute("especialidades", especialidades);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/CadastroEspecialidade.jsp");
        dispatcher.forward(request, response);
    }

    private void cancelar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("id_especialidade", "");
        request.setAttribute("nome", "");
        request.setAttribute("descricao", "");
        request.setAttribute("opcao", "cadastrar");

        encaminharParaPagina(request, response);
    }
}