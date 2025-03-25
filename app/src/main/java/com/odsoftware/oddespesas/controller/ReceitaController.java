package com.odsoftware.oddespesas.controller;

import android.content.Context;

import com.odsoftware.oddespesas.dao.ReceitaDAO;
import com.odsoftware.oddespesas.model.Receita;

import java.util.List;

public class ReceitaController {

    private ReceitaDAO receitaDAO;

    public ReceitaController(Context context) {
        receitaDAO = new ReceitaDAO(context);
    }

    public boolean salvarReceita(String descricao, double valor, String data, String categoria) {
        receitaDAO.open();
        Receita novaReceita = new Receita(descricao, valor, data, categoria);
        long resultado = receitaDAO.inserir(novaReceita);
        receitaDAO.close();
        return resultado != -1;
    }

    public List<Receita> listarReceitas() {
        receitaDAO.open();
        List<Receita> receitas = receitaDAO.listarTodos();
        receitaDAO.close();
        return receitas;
    }

    public double calcularTotalReceitas() {
        List<Receita> receitas = listarReceitas();
        double total = 0;
        for (Receita receita : receitas) {
            total += receita.getValor();
        }
        return total;
    }

}
