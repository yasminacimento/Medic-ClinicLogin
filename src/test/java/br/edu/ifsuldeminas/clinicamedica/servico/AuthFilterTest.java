package br.edu.ifsuldeminas.clinicamedica.servico;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthFilterTest {
    @Test void receitaEMedicamentoExigemCsrfMesmoComLogin() throws Exception {
        for (String controlador : new String[] {"ReceitaControlador", "RemedioControlador"}) {
            Requisicao caso = new Requisicao(WebConstante.BASE_PATH + "/" + controlador, "POST");
            caso.temSessao = true; caso.atributos.put("user", "aluno");
            caso.atributos.put("csrfToken", "correto"); caso.csrfEnviado = "incorreto";
            caso.executar(); assertEquals(403, caso.status); assertFalse(caso.liberado);
            caso.csrfEnviado = "correto"; caso.executar(); assertTrue(caso.liberado);
        }
    }
    @Test void bloqueiaPaginasEControladoresSemCriarSessao() throws Exception {
        for (String path : new String[] {"/Home.jsp", "/CadastroPaciente.jsp", "/Menu.jsp", "/index.jsp",
                WebConstante.BASE_PATH + "/PacienteControlador", "/resources/jakartaee10"}) {
            Requisicao caso = new Requisicao(path, "GET");
            caso.executar();
            assertFalse(caso.liberado, path);
            assertEquals("/ClinicaMedica/login.jsp", caso.redirect);
            assertFalse(caso.criouSessao, path);
        }
    }

    @Test void permiteSessaoAutenticadaEImpedeCache() throws Exception {
        Requisicao caso = new Requisicao("/Home.jsp", "GET");
        caso.atributos.put("user", "aluno");
        caso.temSessao = true;
        caso.executar();
        assertTrue(caso.liberado);
        assertTrue(caso.headers.get("Cache-Control").contains("no-store"));
        assertEquals("no-referrer", caso.headers.get("Referrer-Policy"));
    }

    @Test void formulariosPublicosRecebemCsrf() throws Exception {
        for (String path : new String[] {"/login.jsp", "/CadastrarUsuario.jsp", "/EsqueciSenha.jsp",
                WebConstante.BASE_PATH + "/RecuperacaoSenhaControlador"}) {
            Requisicao caso = new Requisicao(path, "GET");
            caso.executar();
            assertTrue(caso.liberado, path);
            assertTrue(((String) caso.atributos.get("csrfToken")).matches("[A-Za-z0-9_-]{43}"));
        }
    }

    @Test void arquivosEstaticosNaoCriamSessao() throws Exception {
        Requisicao caso = new Requisicao("/css/autenticacao.css", "GET");
        caso.executar();
        assertTrue(caso.liberado);
        assertFalse(caso.criouSessao);
    }

    @Test void postSemCsrfOuComTokenIncorretoERejeitado() throws Exception {
        for (String controlador : new String[] {"LoginControlador", "RecuperacaoSenhaControlador"}) {
            Requisicao semSessao = new Requisicao(WebConstante.BASE_PATH + "/" + controlador, "POST");
            semSessao.executar();
            assertEquals(403, semSessao.status);
            assertFalse(semSessao.liberado);
            Requisicao errado = new Requisicao(WebConstante.BASE_PATH + "/" + controlador, "POST");
            errado.temSessao = true;
            errado.atributos.put("csrfToken", "correto");
            errado.csrfEnviado = "incorreto";
            errado.executar();
            assertEquals(403, errado.status);
            assertFalse(errado.liberado);
        }
    }

    @Test void postComCsrfCorretoELiberado() throws Exception {
        Requisicao caso = new Requisicao(WebConstante.BASE_PATH + "/LoginControlador", "POST");
        caso.temSessao = true;
        caso.csrfEnviado = "correto";
        caso.atributos.put("csrfToken", "correto");
        caso.executar();
        assertTrue(caso.liberado);
    }

    /** Dubles pequenos da API Servlet; nenhuma conexao externa e necessaria. */
    private static class Requisicao {
        final String path;
        final String method;
        final Map<String, Object> atributos = new HashMap<>();
        final Map<String, String> headers = new HashMap<>();
        boolean temSessao;
        boolean criouSessao;
        boolean liberado;
        String csrfEnviado;
        String redirect;
        int status;

        Requisicao(String path, String method) { this.path = path; this.method = method; }

        void executar() throws Exception {
            HttpSession sessao = (HttpSession) Proxy.newProxyInstance(getClass().getClassLoader(),
                    new Class<?>[] {HttpSession.class}, (obj, metodo, args) -> {
                        if (metodo.getName().equals("getAttribute")) { return atributos.get(args[0]); }
                        if (metodo.getName().equals("setAttribute")) { atributos.put((String) args[0], args[1]); }
                        return null;
                    });
            HttpServletRequest req = (HttpServletRequest) Proxy.newProxyInstance(getClass().getClassLoader(),
                    new Class<?>[] {HttpServletRequest.class}, (obj, metodo, args) -> {
                        switch (metodo.getName()) {
                            case "getServletPath": return path;
                            case "getContextPath": return "/ClinicaMedica";
                            case "getMethod": return method;
                            case "getParameter": return csrfEnviado;
                            case "getSession":
                                boolean criar = args == null || args.length == 0 || Boolean.TRUE.equals(args[0]);
                                if (criar && !temSessao) { criouSessao = true; temSessao = true; }
                                return temSessao ? sessao : null;
                            default: return null;
                        }
                    });
            HttpServletResponse res = (HttpServletResponse) Proxy.newProxyInstance(getClass().getClassLoader(),
                    new Class<?>[] {HttpServletResponse.class}, (obj, metodo, args) -> {
                        switch (metodo.getName()) {
                            case "sendRedirect": redirect = (String) args[0]; break;
                            case "sendError": status = (int) args[0]; break;
                            case "setHeader": headers.put((String) args[0], (String) args[1]); break;
                            default: break;
                        }
                        return null;
                    });
            new AuthFilter().doFilter(req, res, (request, response) -> liberado = true);
        }
    }
}
