package controller;

import DAO.BancoDAO;
import DAO.Conexao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;

public class SaldoController {

    public String consultarExtrato(String cpf, String senha) {
        StringBuilder resultado = new StringBuilder();

        try {
            Connection conn = new Conexao().getConnection();
            BancoDAO dao = new BancoDAO(conn);
            ResultSet rs = dao.consultarSaldoCompleto(cpf);

            if (rs.next()) {
                String nome = rs.getString("nome");
                String cpfBanco = rs.getString("cpf");
                String senhaBanco = rs.getString("senha"); 

                if (senhaBanco.equals(senha)) {
                    // Exibir informações principais
                    resultado.append("Nome: ").append(nome).append("\n");
                    resultado.append("CPF: ").append(cpfBanco).append("\n\n");

                    // Iterar dinamicamente sobre todas as colunas do ResultSet
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);

                        // Ignorar colunas fixas
                        if (!columnName.equals("cpf") && !columnName.equals("senha") && !columnName.equals("nome")) {
                            String valor = rs.getString(columnName);
                            if (valor == null) {
                                valor = "0"; // Exibir "0" caso o valor seja nulo
                            }
                            resultado.append(columnName).append(": ").append(valor).append("\n");
                        }
                    }
                } else {
                    resultado.append("CPF ou senha incorretos.");
                }
            } else {
                resultado.append("Investidor não encontrado.");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            resultado.append("Erro ao consultar saldo: ").append(e.getMessage());
        }
        return resultado.toString();
    }
}
