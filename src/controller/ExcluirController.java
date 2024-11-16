
package controller;


import DAO.BancoDAO;
import DAO.Conexao;
import model.Pessoa;  // Agora usando Pessoa em vez de Investidor
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.SQLException;
import view.Excluir;

public class ExcluirController {
    private Excluir view;  // A classe Excluir é a sua interface gráfica

    public ExcluirController(Excluir view) {
        this.view = view;
    }

    public void remover() {
        String cpfInvest = view.getTxtCpfInvest().getText();
        Pessoa pessoa = new Pessoa(null, cpfInvest, null); // Cria uma Pessoa com CPF para consulta
        Conexao conexao = new Conexao();

        try {
            Connection conn = conexao.getConnection();
            BancoDAO dao = new BancoDAO(conn);
            Pessoa p = dao.consultarPorCPF(pessoa.getCpf());  // Usando Pessoa ao invés de Investidor

            if (p != null) {
                String nome = p.getNome();
                int option = JOptionPane.showConfirmDialog(view, "Deseja realmente excluir " + nome + "?");

                if (option == JOptionPane.YES_OPTION) {
                    dao.excluir(p);  // Exclui a pessoa do banco de dados
                    JOptionPane.showMessageDialog(view, "Investidor excluído com sucesso!");
                }

            } else {
                JOptionPane.showMessageDialog(view, "Investidor não encontrado.");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Erro de conexão.");
        }
    }
}
       


   
