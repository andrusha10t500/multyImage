package com.multyimage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB extends SQLiteOpenHelper {
    //Класс для работы с Базой данных SQLite
    //Здесь будет производиться сохранение логов, временных значений, локальных настроек программы
    private static final String DB_Name = "database.db";
    private static final int DB_Version = 1;
    private static SQLiteDatabase current_db;
    public DB(Context context) {
        super(context, DB_Name, null, DB_Version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        //Таблица настроек
        current_db=db;


        db.execSQL("CREATE TABLE SETTINGS (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "method TEXT, " +
                "dest TEXT, " +
                "format text, " +
                "quality INTEGER," +
                "resolution TEXT, " +
                "compression TEXT, " +
                "view_scale INTEGER," +
                "view_sort TEXT, " +
                "view_theme TEXT, " +
                "view TEXT DEFAULT 1) ");

        //Таблица для транзитных значений файлов


        db.execSQL("CREATE TABLE tranzit (" +
                "sort INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "icon TEXT, " +
                "name TEXT, " +
                "size REAL, " +
                "all_size REAL) ");

        //Триггер для подсчёта суммы занятого пространства файлов


        db.execSQL("CREATE TRIGGER all_size_trigger AFTER INSERT ON tranzit " +
                "BEGIN " +
                "UPDATE tranzit SET all_size=0; " +
                "UPDATE tranzit SET all_size=(SELECT sum(size) FROM tranzit) WHERE sort=(SELECT count(*) FROM tranzit); " +
                "END;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.w("SQLITE","Update version database "+ i + ".."+i1);
        db.execSQL("DROP TABLE IF EXISTS settings; DROP TABLE IF EXIST tranzit;");
        onCreate(db);
    }


}
