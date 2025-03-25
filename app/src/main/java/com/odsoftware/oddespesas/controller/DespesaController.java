package com.odsoftware.oddespesas.controller;

import android.content.Context;

import com.odsoftware.oddespesas.dao.DespesaDAO;
import com.odsoftware.oddespesas.model.Despesa;

import java.util.List;

public class DespesaController {

    private DespesaDAO despesaDAO;

    public DespesaController(Context context) {
        despesaDAO = new DespesaDAO(context);
    }

    public boolean salvarDespesa(String descricao, double valor, String data, String categoria) {

        despesaDAO.open();
        Despesa novaDespesa = new Despesa(descricao, valor, data, categoria);
        long resultado = despesaDAO.inserir(novaDespesa);
        despesaDAO.close();
        return resultado != -1;

    }

    public List<Despesa> listarDespesas() {
        despesaDAO.open();
        List<Despesa> despesas = despesaDAO.listarTodos();
        despesaDAO.close();
        return despesas;
    }

    public double calcularTotalDespesas() {
        List<Despesa> despesas = listarDespesas();
        double total = 0;
        for (Despesa despesa : despesas) {
            total += despesa.getValor();
        }
        return total;
    }

}
