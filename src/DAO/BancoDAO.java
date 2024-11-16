package DAO;

import model.Pessoa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; // Importação do ResultSet
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.OutrasMoedas;

public class BancoDAO {

    private Connection conn;

    public BancoDAO(Connection conn) {
        this.conn = conn;
    }

    public void inserir(Pessoa pessoa) throws SQLException {
        String sql = "INSERT INTO public.\"Investidor\" (nome, senha, cpf) VALUES (?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, pessoa.getNome());
        statement.setString(2, pessoa.getSenha());
        statement.setString(3, pessoa.getCpf());
        statement.executeUpdate();
        statement.close();
    }

    public Pessoa consultarPorCPF(String cpf) throws SQLException {
        String sql = "SELECT nome, senha, cpf, reais FROM public.\"Investidor\" WHERE cpf = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, cpf);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String nome = resultSet.getString("nome");
            String senha = resultSet.getString("senha");
            double saldo = resultSet.getDouble("reais");  // Pega o saldo da coluna "reais"
            Pessoa pessoa = new Pessoa(nome, cpf, senha);
            return pessoa;
        } else {
            return null; // Retorna null se não encontrar a pessoa
        }
    }

    public void excluir(Pessoa pessoa) throws SQLException {
        String sql = "DELETE FROM public.\"Investidor\" WHERE cpf = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, pessoa.getCpf());
        statement.executeUpdate();
        statement.close();
    }

   public void inserirMoeda(OutrasMoedas moeda) throws SQLException {
    // Inserir a nova moeda na tabela cotacao
    String sqlCotacao = "INSERT INTO cotacao (moedas, cotacao) VALUES (?, ?)";
    try (PreparedStatement statement = conn.prepareStatement(sqlCotacao)) {
        statement.setString(1, moeda.getNome());
        statement.setDouble(2, moeda.getCotacao());
        statement.execute();
    }

    // Adicionar a nova coluna na tabela "Investidor"
    String sqlInvestidor = "ALTER TABLE \"Investidor\" ADD COLUMN \"" + moeda.getNome() + "\" DOUBLE PRECISION";
    try (PreparedStatement statement = conn.prepareStatement(sqlInvestidor)) {
        statement.executeUpdate();
        System.out.println("Coluna " + moeda.getNome() + " adicionada na tabela Investidor");
    }
}

    public void adicionarColuna(String nomeColuna) throws SQLException {
        // Define a coluna como VARCHAR(50)
        String sql = "ALTER TABLE \"Investidor\" ADD COLUMN " + nomeColuna + " VARCHAR(50)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.executeUpdate();
            System.out.println("Coluna adicionada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao adicionar coluna: " + e.getMessage());
            throw e;
        }
    }

    public void excluirCripto(OutrasMoedas moeda) throws SQLException {
        String sql = "DELETE FROM moedas WHERE \"Nome\" = ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, moeda.getNome());
            statement.executeUpdate();
        }
    }

    // Método para verificar se a coluna existe na tabela Investidor
    public boolean verificarColunaExiste(String nomeColuna) throws SQLException {
        String sql = "SELECT column_name FROM information_schema.columns WHERE table_name = 'Investidor' AND column_name = ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, nomeColuna);
            ResultSet rs = statement.executeQuery();
            return rs.next(); // Retorna true se a coluna existir
        }
    }

    // Método para excluir a coluna
    public void excluirColuna(String nomeColuna) throws SQLException {
        String sql = "ALTER TABLE \"Investidor\" DROP COLUMN IF EXISTS \"" + nomeColuna + "\"";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }

   public ResultSet consultarSaldoCompleto(String cpf) throws SQLException {
    // Consulta para buscar as colunas da tabela Investidor
    String sqlColunas = "SELECT column_name FROM information_schema.columns WHERE table_name = 'Investidor'";
    PreparedStatement statementColunas = conn.prepareStatement(sqlColunas);
    ResultSet rsColunas = statementColunas.executeQuery();

    StringBuilder colunas = new StringBuilder();
    while (rsColunas.next()) {
        String coluna = rsColunas.getString("column_name");
        // Ignorar colunas fixas
        if (!coluna.equals("cpf") && !coluna.equals("senha") && !coluna.equals("nome")) {
            if (colunas.length() > 0) {
                colunas.append(", ");
            }
            colunas.append(coluna);
        }
    }
    rsColunas.close();
    statementColunas.close();

    // Consulta principal para buscar os valores das colunas
    String sql = "SELECT nome, cpf, senha, " + colunas.toString() + " FROM \"Investidor\" WHERE cpf = ?";
    PreparedStatement statement = conn.prepareStatement(sql);
    statement.setString(1, cpf);
    return statement.executeQuery();
}

    public List<String> obterTransacoesPorCPF(String cpf) throws SQLException {
        List<String> transacoes = new ArrayList<>();
        String sqlSelect = "SELECT extrato FROM public.extrato WHERE cpf = ?";

        try (PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {
            stmtSelect.setString(1, cpf);
            ResultSet rs = stmtSelect.executeQuery();

            while (rs.next()) {
                transacoes.add(rs.getString("extrato"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Lança a exceção para tratá-la em outra parte do código
        }
        return transacoes;
    }
    
   public void atualizarCotacoes() throws SQLException {
    // Consulta para obter todas as moedas e suas cotações da tabela 'cotacao'
    String sqlSelect = "SELECT moedas, cotacao FROM cotacao";
    String sqlUpdate = "UPDATE cotacao SET cotacao = ? WHERE moedas = ?";

    try (PreparedStatement selectStmt = conn.prepareStatement(sqlSelect);
         PreparedStatement updateStmt = conn.prepareStatement(sqlUpdate)) {

        // Executa a consulta para buscar as cotações atuais
        ResultSet rs = selectStmt.executeQuery();

        while (rs.next()) {
            String moeda = rs.getString("moedas");  // Nome da moeda
            double cotacaoAtual = rs.getDouble("cotacao");

            // Calcula a nova cotação com variação aleatória
            double novaCotacao = calcularNovaCotacao(cotacaoAtual);

            // Define os valores atualizados no comando de atualização
            updateStmt.setDouble(1, novaCotacao);
            updateStmt.setString(2, moeda);

            // Executa a atualização da cotação
            updateStmt.executeUpdate();
            System.out.println("Cotação da " + moeda + " foi atualizada para: " + novaCotacao);
        }
    }
}

// Método auxiliar para calcular a nova cotação com variação de -5% a +5%
private double calcularNovaCotacao(double valorAtual) {
    // Gera uma variação aleatória entre -0.05 (-5%) e +0.05 (+5%)
    double variacaoPercentual = (Math.random() * 0.1) - 0.05;  // Gera um valor entre -0.05 e 0.05
    return valorAtual * (1 + variacaoPercentual);  // Aplica a variação à cotação atual
}

public void inserirNovaMoedaNaCotacao(OutrasMoedas moeda) throws SQLException {
    String sql = "INSERT INTO cotacao (moedas, cotacao) VALUES (?, ?)";

    try (PreparedStatement statement = conn.prepareStatement(sql)) {
        statement.setString(1, moeda.getNome());  // Nome da moeda
        statement.setDouble(2, moeda.getCotacao());  // Cotação da moeda
        statement.executeUpdate();  // Executa o comando de inserção no banco
        System.out.println("Moeda " + moeda.getNome() + " cadastrada com a cotação de: " + moeda.getCotacao());
    }
}


}
