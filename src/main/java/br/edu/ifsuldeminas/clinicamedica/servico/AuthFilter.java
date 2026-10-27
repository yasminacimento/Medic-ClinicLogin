package br.edu.ifsuldeminas.clinicamedica.servico;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");
        res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setHeader("Referrer-Policy", "no-referrer");
        res.setHeader("X-Content-Type-Options", "nosniff");
        String path = req.getServletPath();
        String login = WebConstante.BASE_PATH + "/LoginControlador";
        String recuperacao = WebConstante.BASE_PATH + "/RecuperacaoSenhaControlador";
        boolean publico = path.equals("/login.jsp") || path.equals("/CadastrarUsuario.jsp")
                || path.equals("/EsqueciSenha.jsp") || path.equals(login) || path.equals(recuperacao)
                || path.startsWith("/css/") || path.startsWith("/js/")
                || path.startsWith("/images/") || path.equals("/favicon.ico");
        HttpSession session = req.getSession(false);
        if (!publico && (session == null || session.getAttribute("user") == null)) {
            res.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        boolean formularioProtegido = path.equals(login) || path.equals(recuperacao)
                || path.equals(WebConstante.BASE_PATH + "/ReceitaControlador")
                || path.equals(WebConstante.BASE_PATH + "/RemedioControlador");
        if ("POST".equals(req.getMethod()) && formularioProtegido) {
            String enviado = req.getParameter("csrfToken");
            Object esperado = session == null ? null : session.getAttribute("csrfToken");
            if (esperado == null || !esperado.equals(enviado)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Formulário expirado. Reabra a página e tente novamente.");
                return;
            }
        }
        // Cria o token dos formularios depois da verificacao de acesso.
        if (!path.startsWith("/css/") && !path.startsWith("/js/") && !path.startsWith("/images/")) {
            session = req.getSession();
            if (session.getAttribute("csrfToken") == null) {
                session.setAttribute("csrfToken", Seguranca.gerarToken());
            }
        }
        chain.doFilter(request, response);
    }
}
