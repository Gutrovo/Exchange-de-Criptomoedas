/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;


import DAO.BancoDAO;
import model.Pessoa;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.SQLException;

public class ExtratoController {
    private List<String> transacoes;  // Lista para armazenar as transações
    private BancoDAO dao;

    public ExtratoController(BancoDAO dao) {
        this.dao = dao;
        this.transacoes = new ArrayList<>();
    }

    // Método para registrar transações no extrato
  

    // Método para obter todas as transações registradas
     public List<String> getTransacoes(String cpf) {
    List<String> transacoes = new ArrayList<>();
    try {
        // Aqui chamamos o método do BancoDAO que obtém todas as transações para o CPF
        transacoes = dao.obterTransacoesPorCPF(cpf);
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return transacoes;
}
}
