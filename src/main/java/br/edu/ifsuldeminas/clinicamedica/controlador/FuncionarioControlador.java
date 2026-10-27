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
import br.edu.ifsuldeminas.clinicamedica.modelo.dao.FuncionarioDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Funcionario;

@WebServlet(WebConstante.BASE_PATH + "/FuncionarioControlador")
public class FuncionarioControlador extends HttpServlet {

    FuncionarioDao funcionarioDao = new FuncionarioDao();

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
        String telefone = request.getParameter("telefone");
        String email = request.getParameter("email");
        String endereco = request.getParameter("endereco");
        String cargo = request.getParameter("cargo");
        String salarioStr = request.getParameter("salario");

        if (nome == null || nome.isBlank()) {
            encaminharParaPagina(request, response);
            return;
        }

        Funcionario funcionario = new Funcionario();
        funcionario.setNome(nome);
        funcionario.setTelefone(telefone);
        funcionario.setEmail(email);
        funcionario.setEndereco(endereco);
        funcionario.setCargo(cargo);
        
        if (salarioStr != null && !salarioStr.isBlank()) {
            funcionario.setSalario(Double.parseDouble(salarioStr));
        }

        funcionarioDao.salvar(funcionario);

        request.setAttribute("mensagem", "Funcionário cadastrado com sucesso!");
        request.setAttribute("opcao", "cadastrar");

        encaminharParaPagina(request, response);
    }

    private void enviarAlterar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id_funcionario");
        String nome = request.getParameter("nome");
        String telefone = request.getParameter("telefone");
        String email = request.getParameter("email");
        String endereco = request.getParameter("endereco");
        String cargo = request.getParameter("cargo");
        String salario = request.getParameter("salario");

        request.setAttribute("id_funcionario", id);
        request.setAttribute("nome", nome);
        request.setAttribute("telefone", telefone);
        request.setAttribute("email", email);
        request.setAttribute("endereco", endereco);
        request.setAttribute("cargo", cargo);
        request.setAttribute("salario", salario);
        request.setAttribute("opcao", "confirmarAlterar");
        request.setAttribute("mensagem", "Edite os dados e clique em salvar.");

        encaminharParaPagina(request, response);
    }

    private void enviarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id_funcionario");
        String nome = request.getParameter("nome");
        String telefone = request.getParameter("telefone");
        String email = request.getParameter("email");
        String endereco = request.getParameter("endereco");
        String cargo = request.getParameter("cargo");
        String salario = request.getParameter("salario");

        request.setAttribute("id_funcionario", id);
        request.setAttribute("nome", nome);
        request.setAttribute("telefone", telefone);
        request.setAttribute("email", email);
        request.setAttribute("endereco", endereco);
        request.setAttribute("cargo", cargo);
        request.setAttribute("salario", salario);
        request.setAttribute("opcao", "confirmarExcluir");
        request.setAttribute("mensagem", "Confirme os dados e clique em salvar para EXCLUIR.");

        encaminharParaPagina(request, response);
    }

    private void confirmarAlterar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id_funcionario");
        String nome = request.getParameter("nome");
        String telefone = request.getParameter("telefone");
        String email = request.getParameter("email");
        String endereco = request.getParameter("endereco");
        String cargo = request.getParameter("cargo");
        String salarioStr = request.getParameter("salario");

        Funcionario funcionario = new Funcionario();
        funcionario.setId_funcionario(Integer.parseInt(id));
        funcionario.setNome(nome);
        funcionario.setTelefone(telefone);
        funcionario.setEmail(email);
        funcionario.setEndereco(endereco);
        funcionario.setCargo(cargo);
        
        if (salarioStr != null && !salarioStr.isBlank()) {
            funcionario.setSalario(Double.parseDouble(salarioStr));
        }

        funcionarioDao.alterar(funcionario);

        request.setAttribute("mensagem", "Funcionário alterado com sucesso!");

        encaminharParaPagina(request, response);
    }

    private void confirmarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id_funcionario");

        Funcionario funcionario = new Funcionario();
        funcionario.setId_funcionario(Integer.parseInt(id));

        funcionarioDao.excluir(funcionario);

        request.setAttribute("mensagem", "Funcionário excluído com sucesso!");

        encaminharParaPagina(request, response);
    }

    private void encaminharParaPagina(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Funcionario> funcionarios = funcionarioDao.buscarTodasFuncionarios();
        request.setAttribute("funcionarios", funcionarios);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/CadastroFuncionario.jsp");
        dispatcher.forward(request, response);
    }

    private void cancelar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("id_funcionario", "");
        request.setAttribute("nome", "");
        request.setAttribute("telefone", "");
        request.setAttribute("email", "");
        request.setAttribute("endereco", "");
        request.setAttribute("cargo", "");
        request.setAttribute("salario", "");
        request.setAttribute("opcao", "cadastrar");

        encaminharParaPagina(request, response);
    }
}