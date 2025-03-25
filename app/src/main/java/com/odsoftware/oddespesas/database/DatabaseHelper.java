package com.odsoftware.oddespesas.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "oddespesas.db";
    private static final int DATABASE_VERSION = 1;

    // Table creation SQL statements
    private static final String CREATE_TABLE_CATEGORIA_DESPESA =
            "CREATE TABLE categoria_despesa (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome TEXT NOT NULL)";

    private static final String CREATE_TABLE_CATEGORIA_RECEITA =
            "CREATE TABLE categoria_receita (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome TEXT NOT NULL)";

    private static final String CREATE_TABLE_DESPESA =
            "CREATE TABLE despesa (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "descricao TEXT NOT NULL, " +
                    "valor REAL NOT NULL, " +
                    "data TEXT NOT NULL, " +
                    "categoria TEXT NOT NULL)";

    private static final String CREATE_TABLE_RECEITA =
            "CREATE TABLE receita (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "descricao TEXT NOT NULL, " +
                    "valor REAL NOT NULL, " +
                    "data TEXT NOT NULL, " +
                    "categoria TEXT NOT NULL)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_CATEGORIA_DESPESA);
        db.execSQL(CREATE_TABLE_CATEGORIA_RECEITA);
        db.execSQL(CREATE_TABLE_DESPESA);
        db.execSQL(CREATE_TABLE_RECEITA);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS categoria_despesa");
        db.execSQL("DROP TABLE IF EXISTS categoria_receita");
        db.execSQL("DROP TABLE IF EXISTS despesa");
        db.execSQL("DROP TABLE IF EXISTS receita");

        // Create tables again
        onCreate(db);
    }
}
