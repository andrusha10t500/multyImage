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

    public DB(Context context) {
        super(context, DB_Name, null, DB_Version);
    }

//    @Override
//    public SQLiteDatabase getWritableDatabase() {
//        return getWritableDatabase();
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Таблица настроек
//        String SQL_CREATE_SETTINGS = "CREATE TABLE settings (" +
//        Contracts.Settings.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//        Contracts.Settings.method + " TEXT NOT NULL, " +
//        Contracts.Settings.dest + " TEXT NOT NULL, " +
//        Contracts.Settings.format + " TEXT NOT NULL, " +
//        Contracts.Settings.quality + " INTEGER NOT NULL DEFAULT 3, " +
//        Contracts.Settings.resolution + " TEXT NOT NULL, " +
//        Contracts.Settings.compression + " TEXT NOT NULL, " +
//        Contracts.Settings.view_scale + " INTEGER NOT NULL DEFAULT 3, " +
//        Contracts.Settings.view_sort + " TEXT NOT NULL, " +
//        Contracts.Settings.view_Theme + " TEXT NOT NULL, " +
//        Contracts.Settings.view + " TEXT NOT NULL DEFAULT 1);";

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
//        String CREATE_TABLE_TRANZIT = "CREATE TABLE tranzit (" +
//        Contracts.Tranzit.sort + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//        Contracts.Tranzit.icon + " TEXT NOT NULL, " +
//        Contracts.Tranzit.name + " TEXT NOT NULL, " +
//        Contracts.Tranzit.size + " REAL NOT NULL, " +
//        Contracts.Tranzit.all_size + " REAL);";

        db.execSQL("CREATE TABLE tranzit (" +
                "sort INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "icon TEXT, " +
                "name TEXT, " +
                "size REAL, " +
                "all_size REAL) ");

        //Триггер для подсчёта суммы занятого пространства файлов
//        String CRAETE_TRIGGER_SUMM_WEIGHT = "CREATE TRIGGER all_size_trigger AFTER INSERT on " +
//        "tranzit BEGIN " +
//        "UPDATE tranzit SET " + Contracts.Tranzit.all_size + "=0; " +
//        "UPDATE tranzit SET " + Contracts.Tranzit.all_size + "=(SELECT sum(" + Contracts.Tranzit.size + ") FROM tranzit)" +
//        " WHERE " + Contracts.Tranzit.sort + "=(SELECT count(*) FROM tranzit); " +
//        "END;";

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
