package br.edu.ifsuldeminas.clinicamedica.controlador;

import br.edu.ifsuldeminas.clinicamedica.servico.RecuperacaoSenhaService;
import br.edu.ifsuldeminas.clinicamedica.servico.WebConstante;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(WebConstante.BASE_PATH + "/RecuperacaoSenhaControlador")
public class RecuperacaoSenhaControlador extends HttpServlet {
    private final RecuperacaoSenhaService service = new RecuperacaoSenhaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/EsqueciSenha.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!"redefinir".equals(request.getParameter("opcao"))) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Opção inválida.");
            return;
        }
        try {
            if (!service.redefinir(request.getParameter("email"),
                    request.getParameter("password"), request.getParameter("confirmacao"))) {
                mostrarErro(request, response, "Nenhum usuário cadastrado com esse e-mail.");
                return;
            }
            HttpSession session = request.getSession(false);
            if (session != null) { session.invalidate(); }
            response.sendRedirect(request.getContextPath() + "/login.jsp?estado=redefinida");
        } catch (IllegalArgumentException ex) {
            mostrarErro(request, response, ex.getMessage());
        } catch (SQLException ex) {
            getServletContext().log("Falha no serviço de recuperação de senha.", ex);
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            mostrarErro(request, response, "Não foi possível acessar o banco. Tente novamente mais tarde.");
        }
    }

    private void mostrarErro(HttpServletRequest request, HttpServletResponse response, String mensagem)
            throws ServletException, IOException {
        request.setAttribute("mensagem", mensagem);
        request.getRequestDispatcher("/EsqueciSenha.jsp").forward(request, response);
    }
}
