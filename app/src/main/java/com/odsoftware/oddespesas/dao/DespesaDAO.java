package com.odsoftware.oddespesas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.odsoftware.oddespesas.database.DatabaseHelper;
import com.odsoftware.oddespesas.model.Despesa;

import java.util.ArrayList;
import java.util.List;

public class DespesaDAO {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DespesaDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long inserir(Despesa despesa) {
        ContentValues values = new ContentValues();
        values.put("descricao", despesa.getDescricao());
        values.put("valor", despesa.getValor());
        values.put("data", despesa.getData());
        values.put("categoria", despesa.getCategoria());

        return database.insert("despesa", null, values);
    }

    public List<Despesa> listarTodos() {
        List<Despesa> despesas = new ArrayList<>();
        Cursor cursor = database.query("despesa",
                new String[]{"descricao", "valor", "data", "categoria"},
                null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Despesa despesa = new Despesa(
                    cursor.getString(0),
                    cursor.getDouble(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
            despesas.add(despesa);
            cursor.moveToNext();
        }
        cursor.close();
        return despesas;
    }

    public void deletar(Despesa despesa) {
        // Implement delete logic if needed
    }

    public void atualizar(Despesa despesa) {
        // Implement update logic if needed
    }

}
