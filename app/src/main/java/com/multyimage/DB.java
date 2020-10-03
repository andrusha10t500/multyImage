package com.multyimage;

import android.content.Context;
import android.database.Cursor;
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

        db.execSQL("CREATE TABLE SETTINGS (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "method TEXT DEFAULT 'from', " +
                "dest TEXT DEFAULT '//storage/sdcard1/Download/', " +
                "format text DEFAULT 'A4', " +
                "quality INTEGER DEFAULT 100," +
                "resolution TEXT DEFAULT '640x480', " +
                "compression TEXT DEFAULT 'LZV', " +
                "view_scale INTEGER DEFAULT 100," +
                "view_sort TEXT DEFAULT '_id', " +
                "view_theme TEXT DEFAULT '0', " +
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
                "('from', '//storage/sdcard1/Download/', 'A4', 100, '640x480', 'LZV', 100, '_id', '0')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.w("SQLITE","Update version database "+ i + ".."+i1);
        db.execSQL("DROP TABLE IF EXISTS settings; DROP TABLE IF EXIST tranzit;");
        onCreate(db);
    }

    private void defaultSettings() {
        this.getWritableDatabase().execSQL("INSERT INTO settings " +
                "(method, dest, format, quality, resolution, compression, view_scale, view_sort, view_theme) VALUES " +
                "('from', '//storage/sdcard1/Download/', 'A4', 100, '640x480', 'LZV', 100, '_id', '0')");
    }

    public Cursor getSettings() {
        Cursor setting = this.getWritableDatabase().rawQuery("SELECT * FROM settings WHERE _id=(CASE WHEN (SELECT count(*) FROM settings)>1 THEN 2 ELSE 1 END)",null);
        setting.moveToFirst();
        return setting;
    }

    public void newTheme(int theme) {
        this.getWritableDatabase().execSQL("UPDATE settings SET view_theme='"+theme+"' WHERE _id=(CASE WHEN (SELECT count(*) FROM settings)>1 then 2 else 1 end)");
    }

    public String whichSettings() {
        Cursor which = this.getWritableDatabase().rawQuery("SELECT CASE WHEN (SELECT count(*) FROM settings)>1 THEN 'USER_SETTING' ELSE 'DEFAULT_SETTING' END", null);
        which.moveToFirst();
        String whichSetting=which.getString(0);

        return whichSetting;
    }

    public String getFormat(){
        Cursor c = this.getWritableDatabase().rawQuery("SELECT format FROM settings WHERE _id=" + this.getSettings().getCount(), null);
        c.moveToFirst();
        return c.getString(0);
    }
    public String getQuality() {
        Cursor c= this.getWritableDatabase().rawQuery("SELECT quality FROM settings WHERE _id=" + this.getSettings().getCount(), null);
        c.moveToFirst();
        return c.getString(0);
    }
    public String getScale() {
        Cursor c= this.getWritableDatabase().rawQuery("SELECT view_scale FROM settings WHERE _id=" + this.getSettings().getCount(),null);
        c.moveToFirst();
        return c.getString(0);
    }
}
