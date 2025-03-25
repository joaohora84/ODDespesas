package com.odsoftware.oddespesas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.odsoftware.oddespesas.database.DatabaseHelper;
import com.odsoftware.oddespesas.model.Receita;

import java.util.ArrayList;
import java.util.List;

public class ReceitaDAO {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public ReceitaDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long inserir(Receita receita) {
        ContentValues values = new ContentValues();
        values.put("descricao", receita.getDescricao());
        values.put("valor", receita.getValor());
        values.put("data", receita.getData());
        values.put("categoria", receita.getCategoria());

        return database.insert("receita", null, values);
    }

    public List<Receita> listarTodos() {
        List<Receita> receitas = new ArrayList<>();
        Cursor cursor = database.query("receita",
                new String[]{"descricao", "valor", "data", "categoria"},
                null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Receita receita = new Receita(
                    cursor.getString(0),
                    cursor.getDouble(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
            receitas.add(receita);
            cursor.moveToNext();
        }
        cursor.close();
        return receitas;
    }

    public void deletar(Receita receita) {
        // Implement delete logic if needed
    }

    public void atualizar(Receita receita) {
        // Implement update logic if needed
    }

}
