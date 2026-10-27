package br.edu.ifsuldeminas.clinicamedica.controlador;

import br.edu.ifsuldeminas.clinicamedica.modelo.dao.UsuarioDAO;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Usuario;
import br.edu.ifsuldeminas.clinicamedica.servico.Seguranca;
import br.edu.ifsuldeminas.clinicamedica.servico.WebConstante;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(WebConstante.BASE_PATH + "/LoginControlador")
public class LoginControlador extends HttpServlet {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String opcao = request.getParameter("opcao");
        String pagina = "cadastrar".equals(opcao) ? "/CadastrarUsuario.jsp" : "/login.jsp";
        try {
            if ("logout".equals(opcao)) {
                HttpSession session = request.getSession(false);
                if (session != null) { session.invalidate(); }
                response.sendRedirect(request.getContextPath() + "/login.jsp?estado=saiu");
            } else if ("cadastrar".equals(opcao)) {
                String nome = nomeUsuario(request);
                if (!nome.matches("[\\p{L}\\p{N}_.-]{3,255}")) {
                    throw new IllegalArgumentException("Use de 3 a 255 letras, números, pontos, traços ou sublinhados no usuário.");
                }
                String senha = request.getParameter("password");
                Seguranca.validarSenha(senha, request.getParameter("confirmacao"));
                String email = Seguranca.normalizarEmail(request.getParameter("email"));
                usuarioDAO.registrarUsuario(nome, senha, email);
                response.sendRedirect(request.getContextPath() + "/login.jsp?estado=cadastrado");
            } else if ("login".equals(opcao)) {
                String nome = nomeUsuario(request);
                Usuario usuario = nome.length() <= 255 ? usuarioDAO.buscarUsuarioPorNomeUsuario(nome) : null;
                if (usuario == null || !Seguranca.verificarSenha(request.getParameter("password"), usuario.getSenhaHash())) {
                    throw new IllegalArgumentException("Usuário ou senha inválidos.");
                }
                HttpSession anterior = request.getSession(false);
                if (anterior != null) { anterior.invalidate(); }
                // Uma nova sessao impede a reutilizacao do ID anterior ao login.
                HttpSession session = request.getSession(true);
                session.setAttribute("user", usuario.getNomeUsuario());
                session.setAttribute("usuarioId", usuario.getId());
                session.setAttribute("csrfToken", Seguranca.gerarToken());
                session.setMaxInactiveInterval(30 * 60);
                response.sendRedirect(request.getContextPath() + "/Home.jsp");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Opção inválida.");
            }
        } catch (IllegalArgumentException ex) {
            mostrarErro(request, response, pagina, ex.getMessage());
        } catch (SQLException ex) {
            if ("cadastrar".equals(opcao) && (ex.getErrorCode() == 1062 || "23505".equals(ex.getSQLState()))) {
                mostrarErro(request, response, pagina, "Nome de usuário ou e-mail já cadastrado.");
            } else {
                getServletContext().log("Falha no acesso ao banco de autenticação.", ex);
                response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                mostrarErro(request, response, pagina, "Não foi possível acessar o banco. Tente novamente mais tarde.");
            }
        }
    }

    private String nomeUsuario(HttpServletRequest request) {
        String nome = request.getParameter("username");
        return nome == null ? "" : nome.trim();
    }

    private void mostrarErro(HttpServletRequest request, HttpServletResponse response, String pagina, String mensagem)
            throws ServletException, IOException {
        request.setAttribute("mensagem", mensagem);
        request.getRequestDispatcher(pagina).forward(request, response);
    }
}
