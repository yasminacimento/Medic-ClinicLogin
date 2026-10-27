/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.clinicamedica.controlador;

import br.edu.ifsuldeminas.clinicamedica.modelo.dao.TipoExameDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.dao.FuncionarioDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.dao.ConsultorioDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Consultorio;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Funcionario;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.TipoExame;
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
@WebServlet(WebConstante.BASE_PATH + "/TipoExameControlador")
public class TipoExameControlador extends HttpServlet {

    private final TipoExameDao tipoExameDao = new TipoExameDao();
    private final FuncionarioDao funcionarioDao = new FuncionarioDao();
    private final ConsultorioDao consultorioDao = new ConsultorioDao();

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

        try {
            TipoExame te = new TipoExame();
            te.setNome(request.getParameter("nome"));
            te.setDescricao(request.getParameter("descricao"));
            te.setValor(Double.parseDouble(request.getParameter("valor")));
            te.setFuncionarioId(Integer.parseInt(request.getParameter("funcionarioId")));

            String consultorioStr = request.getParameter("consultorioId");
            if (consultorioStr != null && !consultorioStr.isEmpty()) {
                te.setConsultorioId(Integer.parseInt(consultorioStr));
            }

            tipoExameDao.salvar(te);
            request.setAttribute("mensagem", "Tipo de Exame cadastrado com sucesso!");
        } catch (Exception e) {
            request.setAttribute("mensagem", "Erro ao cadastrar: " + e.getMessage());
            e.printStackTrace();
        }

        request.setAttribute("opcao", "cancelar");
        encaminharParaPagina(request, response);
    }

    private void enviarAlterar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("id_tipoExame", request.getParameter("id_tipoExame"));
        request.setAttribute("nome", request.getParameter("nome"));
        request.setAttribute("descricao", request.getParameter("descricao"));
        request.setAttribute("valor", request.getParameter("valor"));
        request.setAttribute("funcionarioId", request.getParameter("funcionarioId"));
        request.setAttribute("consultorioId", request.getParameter("consultorioId"));
        request.setAttribute("opcao", "confirmarAlterar");
        request.setAttribute("mensagem", "Edite os dados e clique em salvar.");

        encaminharParaPagina(request, response);
    }

    private void enviarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("id_tipoExame", request.getParameter("id_tipoExame"));
        request.setAttribute("nome", request.getParameter("nome"));
        request.setAttribute("descricao", request.getParameter("descricao"));
        request.setAttribute("valor", request.getParameter("valor"));
        request.setAttribute("funcionarioId", request.getParameter("funcionarioId"));
        request.setAttribute("consultorioId", request.getParameter("consultorioId"));

        request.setAttribute("opcao", "confirmarExcluir");
        request.setAttribute("mensagem", "Confirme os dados e clique em Confirmar Exclusão.");

        encaminharParaPagina(request, response);
    }

    private void confirmarAlterar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        TipoExame te = new TipoExame();
        te.setIdTipoExame(Integer.parseInt(request.getParameter("id_tipoExame")));
        te.setNome(request.getParameter("nome"));
        te.setDescricao(request.getParameter("descricao"));
        te.setValor(Double.parseDouble(request.getParameter("valor")));
        te.setFuncionarioId(Integer.parseInt(request.getParameter("funcionarioId")));

        String consultorioStr = request.getParameter("consultorioId");
        if (consultorioStr != null && !consultorioStr.isEmpty()) {
            te.setConsultorioId(Integer.parseInt(consultorioStr));
        }

        tipoExameDao.alterar(te);
        request.setAttribute("mensagem", "Tipo de Exame alterado com sucesso!");
        encaminharParaPagina(request, response);
    }

    private void confirmarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        TipoExame te = new TipoExame();
        te.setIdTipoExame(Integer.parseInt(request.getParameter("id_tipoExame")));
        tipoExameDao.excluir(te);

        request.setAttribute("mensagem", "Tipo de Exame excluído com sucesso!");
        encaminharParaPagina(request, response);
    }

    private void cancelar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("id_tipoExame", "");
        request.setAttribute("nome", "");
        request.setAttribute("descricao", "");
        request.setAttribute("valor", "");
        request.setAttribute("funcionarioId", "");
        request.setAttribute("consultorioId", "");
        request.setAttribute("opcao", "cadastrar");

        encaminharParaPagina(request, response);
    }

    private void encaminharParaPagina(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<TipoExame> tiposExame = tipoExameDao.buscarTodosTipoExames();
        List<Funcionario> funcionarios = funcionarioDao.buscarTodasFuncionarios();
        List<Consultorio> consultorios = consultorioDao.buscarTodasConsultorios();

        request.setAttribute("tiposExame", tiposExame);
        request.setAttribute("funcionarios", funcionarios);
        request.setAttribute("consultorios", consultorios);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/CadastroTipoExame.jsp");
        dispatcher.forward(request, response);
    }
}
