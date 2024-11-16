/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author Vinicius
 */
import DAO.BancoDAO;
import DAO.Conexao;
import java.sql.Connection;
import java.sql.SQLException;

public class AtualizarCotacaoController {

    public String atualizarCotacoes() {
        try {
            Connection conn = new Conexao().getConnection();
            BancoDAO dao = new BancoDAO(conn);
            dao.atualizarCotacoes(); // Chama o método no DAO
            conn.close();
            return "Cotações atualizadas com sucesso!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao atualizar cotações: " + e.getMessage();
        }
    }
}
