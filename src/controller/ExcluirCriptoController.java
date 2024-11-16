
package controller;

import view.ExcluirCripto;
import DAO.BancoDAO;
import DAO.Conexao;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.SQLException;

public class ExcluirCriptoController {
    private ExcluirCripto view;

    public ExcluirCriptoController(ExcluirCripto view) {
        this.view = view;
    }

    public void removerColunaCripto() {
        String nomeColuna = view.getTxtNomeCripto().getText().trim(); // Captura o nome da coluna digitada pelo usuário
        
        Conexao conexao = new Conexao();
        
        try (Connection conn = conexao.getConnection()) {
            BancoDAO dao = new BancoDAO(conn);

            // Verifica se a coluna existe
            if (dao.verificarColunaExiste(nomeColuna)) {
                // Pergunta ao usuário se ele realmente deseja excluir a coluna
                int option = JOptionPane.showConfirmDialog(view, "Deseja realmente excluir a coluna " + nomeColuna + "?");

                if (option == JOptionPane.YES_OPTION) {
                    // Exclui a coluna
                    dao.excluirColuna(nomeColuna);
                    JOptionPane.showMessageDialog(view, "Coluna '" + nomeColuna + "' removida com sucesso.");
                } else {
                    JOptionPane.showMessageDialog(view, "Exclusão cancelada.");
                }
            } else {
                JOptionPane.showMessageDialog(view, "Coluna '" + nomeColuna + "' não encontrada.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Erro ao excluir coluna: " + e.getMessage());
        }
    }
}
