package br.edu.ifsuldeminas.clinicamedica.controlador;

import br.edu.ifsuldeminas.clinicamedica.servico.WebConstante;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Compatibilidade com os atalhos antigos. Os itens agora pertencem ao editor de receitas. */
@WebServlet(WebConstante.BASE_PATH + "/ItemReceitaControlador")
public class ItemReceitaControlador extends HttpServlet {
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + WebConstante.BASE_PATH + "/ReceitaControlador");
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
