package br.edu.ifsuldeminas.clinicamedica.controlador;

import br.edu.ifsuldeminas.clinicamedica.modelo.dao.RemedioDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.dao.ItemReceitaDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Remedio;
import br.edu.ifsuldeminas.clinicamedica.servico.WebConstante;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(WebConstante.BASE_PATH + "/RemedioControlador")
public class RemedioControlador extends HttpServlet {
    private final RemedioDao remedioDao = new RemedioDao();
    private final ItemReceitaDao itemReceitaDao = new ItemReceitaDao();

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("opcao", "cadastrar");
        String opcao = request.getParameter("opcao");
        try {
            if ("enviarAlterar".equals(opcao) || "enviarExcluir".equals(opcao)) {
                Remedio remedio = remedioDao.buscarRemedioPorId(Integer.parseInt(request.getParameter("id_remedio")));
                if (remedio == null) { throw new IllegalArgumentException("Medicamento não encontrado."); }
                request.setAttribute("id_remedio", remedio.getIdRemedio());
                request.setAttribute("nome", remedio.getNome());
                request.setAttribute("opcao", "enviarAlterar".equals(opcao) ? "confirmarAlterar" : "confirmarExcluir");
                request.setAttribute("mensagem", "enviarAlterar".equals(opcao)
                        ? "Edite o nome e salve as alterações." : "Confirme a exclusão do medicamento selecionado.");
            }
            Object mensagem = request.getSession().getAttribute("mensagemRemedio");
            if (mensagem != null) {
                request.setAttribute("mensagem", mensagem);
                request.getSession().removeAttribute("mensagemRemedio");
            }
        } catch (IllegalArgumentException | SQLException ex) {
            request.setAttribute("mensagem", "Não foi possível abrir o medicamento selecionado.");
        }
        mostrar(request, response);
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String opcao = request.getParameter("opcao");
        request.setAttribute("opcao", opcao);
        request.setAttribute("id_remedio", request.getParameter("id_remedio"));
        request.setAttribute("nome", request.getParameter("nome"));
        try {
            Remedio remedio = new Remedio();
            if (!"cadastrar".equals(opcao)) { remedio.setIdRemedio(Integer.valueOf(request.getParameter("id_remedio"))); }
            String nome = request.getParameter("nome");
            if (nome == null || nome.isBlank() || nome.trim().length() > 150) {
                throw new IllegalArgumentException("Informe o nome do medicamento, com até 150 caracteres.");
            }
            remedio.setNome(nome.trim());
            String mensagem;
            if ("cadastrar".equals(opcao)) {
                remedioDao.salvar(remedio);
                mensagem = "Medicamento cadastrado com sucesso.";
            } else if ("confirmarAlterar".equals(opcao)) {
                remedioDao.alterar(remedio);
                mensagem = "Medicamento alterado com sucesso.";
            } else if ("confirmarExcluir".equals(opcao)) {
                if (itemReceitaDao.contarPorMedicamento(remedio.getIdRemedio()) > 0) {
                    throw new IllegalArgumentException("Este medicamento está em uma receita e não pode ser excluído.");
                }
                remedioDao.excluir(remedio);
                mensagem = "Medicamento excluído com sucesso.";
            } else { throw new IllegalArgumentException("Ação inválida."); }
            request.getSession().setAttribute("mensagemRemedio", mensagem);
            response.sendRedirect(request.getContextPath() + WebConstante.BASE_PATH + "/RemedioControlador");
            return;
        } catch (IllegalArgumentException ex) {
            request.setAttribute("mensagem", ex instanceof NumberFormatException ? "Selecione um medicamento válido." : ex.getMessage());
        } catch (IllegalStateException ex) {
            getServletContext().log("Falha no cadastro de medicamento.", ex);
            Throwable causa = ex.getCause();
            request.setAttribute("mensagem", causa instanceof SQLException && ((SQLException) causa).getErrorCode() == 1062
                    ? "Já existe um medicamento com esse nome." : "Não foi possível salvar o medicamento. Tente novamente.");
        }
        mostrar(request, response);
    }

    private void mostrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("remedios", remedioDao.buscarTodosRemedios());
        request.getRequestDispatcher("/CadastroRemedio.jsp").forward(request, response);
    }
}
