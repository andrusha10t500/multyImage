package com.multyimage;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.Externalizable;

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
        db.execSQL("CREATE TABLE SETTINGS (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "method TEXT DEFAULT 'from', " +
                "dest TEXT DEFAULT '" +  Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/', " +
                "format text DEFAULT 'PDF', " +
                "quality INTEGER DEFAULT 100," +
                "resolution TEXT DEFAULT '640x480', " +
                "compression TEXT DEFAULT 'LZV', " +
                "view_scale INTEGER DEFAULT 100," +
                "view_sort TEXT DEFAULT '_id', " +
                "view_theme TEXT DEFAULT 'Светлая', " +
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

        db.execSQL("INSERT INTO settings " +
                "(method, dest, format, quality, resolution, compression, view_scale, view_sort, view_theme) VALUES " +
                "('from', '" +  Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/', 'PDF', 100, '640x480', 'LZV', 100, '_id', 'Светлая')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.w("SQLITE","Update version database "+ i + ".."+i1);
        db.execSQL("DROP TABLE IF EXISTS settings; DROP TABLE IF EXIST tranzit;");
        onCreate(db);
    }
    //получение настроек
    public Cursor getSettings() {
        Cursor setting = this.getWritableDatabase().rawQuery("SELECT * FROM settings WHERE _id=(SELECT MAX(_id) FROM settings)",null);
        setting.moveToFirst();
        return setting;
    }
    //новая тема
    public void newTheme(String theme) {
        this.getWritableDatabase().execSQL("UPDATE settings SET view_theme='"+theme+"' WHERE _id=(SELECT MAX(_id) FROM settings)");
    }
    //----------------------для настроек----------------------
    public String getTheme(){
        Cursor c = this.getWritableDatabase().rawQuery("SELECT view_theme FROM settings WHERE _id=(SELECT MAX(_id) FROM settings)", null);
        c.moveToFirst();
        return c.getString(0);
    }

    public String getFormat(){
        Cursor c = this.getWritableDatabase().rawQuery("SELECT format FROM settings WHERE _id=(SELECT MAX(_id) FROM settings)", null);
        c.moveToFirst();
        return c.getString(0);
    }
    public String getQuality() {
        Cursor c= this.getWritableDatabase().rawQuery("SELECT quality FROM settings WHERE _id=(SELECT MAX(_id) FROM settings)" , null);
        c.moveToFirst();
        return c.getString(0);
    }
    public String getScale() {
        Cursor c= this.getWritableDatabase().rawQuery("SELECT view_scale FROM settings WHERE _id=(SELECT MAX(_id) FROM settings)",null);
        c.moveToFirst();
        return c.getString(0);
    }
    public String getSize() {
        Cursor c= this.getWritableDatabase().rawQuery("SELECT resolution FROM settings WHERE _id=(SELECT MAX(_id) FROM settings)" ,null);
        c.moveToFirst();
        return c.getString(0);
    }
    public String getSort() {
        Cursor c= this.getWritableDatabase().rawQuery("SELECT view_sort FROM settings WHERE _id=(SELECT MAX(_id) FROM settings)" ,null);
        c.moveToFirst();
        return c.getString(0);
    }
    public String getCompression() {
        Cursor c= this.getWritableDatabase().rawQuery("SELECT compression FROM settings WHERE _id=(SELECT MAX(_id) FROM settings)" ,null);
        c.moveToFirst();
        return c.getString(0);
    }
    public String getPathDest() {
        Cursor c= this.getWritableDatabase().rawQuery("SELECT dest FROM settings WHERE _id=(SELECT MAX(_id) FROM settings)" ,null);
        c.moveToFirst();
        return c.getString(0);
    }
    public void setPathDest(String path) {
        this.getWritableDatabase().execSQL("UPDATE settings SET dest='" + path + "' WHERE _id=(SELECT MAX(_id) FROM settings)");
    }
    public String getMethod() {
        Cursor c= this.getWritableDatabase().rawQuery("SELECT method FROM settings WHERE _id=(SELECT MAX(_id) FROM settings)" ,null);
        c.moveToFirst();
        return c.getString(0);
    }
    //Обновление метода
    public void setMethod(String method) {
        this.getWritableDatabase().execSQL("UPDATE settings SET method='" + method + "' WHERE _id=(SELECT MAX(_id) FROM settings)");
    }
    //----------------------конец----------------------

    //----------------------для фрагментов----------------------

    //----------------------конец----------------------
}
