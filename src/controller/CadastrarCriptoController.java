package controller;

import DAO.BancoDAO;
import DAO.Conexao;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.OutrasMoedas;
import view.NovaMoeda;

public class CadastrarCriptoController {

    private NovaMoeda view;

    public CadastrarCriptoController(NovaMoeda view) {
        this.view = view;
    }

    public void salvarCripto() {
        String nome = view.getTxtNomeCripto().getText();
        double cotacao = Double.parseDouble(view.getTxtCotacaoCripto().getText());
        float taxaCompra = Float.parseFloat(view.getTxtTaxaCompra().getText());
        float taxaVenda = Float.parseFloat(view.getTxtTaxaVenda().getText());

        OutrasMoedas moeda = new OutrasMoedas(nome, cotacao, taxaCompra, taxaVenda);
        Conexao conexao = new Conexao();

        try {
            Connection conn = conexao.getConnection();
            BancoDAO dao = new BancoDAO(conn);
            dao.inserirMoeda(moeda);
            JOptionPane.showMessageDialog(view, "Moeda cadastrada");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Moeda nao cadastrada");
        }
    }

    public void adicionarNovaColunaMoeda() {
    String nomeColuna = view.getTxtNomeCripto().getText().trim();
    
    if (nomeColuna.isEmpty()) {
        JOptionPane.showMessageDialog(view, "O nome da coluna não pode estar vazio.");
        return;
    }

    Conexao conexao = new Conexao();
    try {
        Connection conn = conexao.getConnection();
        BancoDAO dao = new BancoDAO(conn);
        dao.adicionarColuna(nomeColuna);  // Agora usa VARCHAR por padrão
        JOptionPane.showMessageDialog(view, "Nova coluna '" + nomeColuna + "' adicionada com sucesso!");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(view, "Erro ao adicionar nova coluna: " + e.getMessage());
    }
}
}
