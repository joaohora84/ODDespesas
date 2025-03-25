package com.odsoftware.oddespesas.model;

public class Despesa {

    private String descricao;
    private double valor;
    private String data;
    private String categoria;

    public Despesa(String descricao, double valor, String data, String categoria) {
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }

    public String getData() {
        return data;
    }

    public String getCategoria() {
        return categoria;
    }
}
