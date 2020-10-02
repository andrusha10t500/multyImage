package com.multyimage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class Settings extends AppCompatActivity {
    private static SQLiteDatabase db;
    //Класс для отработки Настроек приложения
//    SQLiteDatabase db;
    private static DB dbh;
    private static Cursor settings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbh = new DB(this);
        db=dbh.getWritableDatabase();
        if(this.getSettings().getCount()<2) {
            db.execSQL("INSERT INTO settings (method, dest, format, quality, resolution, compression, view_theme) VALUES" +
                    "('from','//storage/sdcard1/Download/', 'A4', 100, 100, 'LZV', 0)");
        }
    }

    public static Cursor getSettings() {
        try {
            settings = db.rawQuery("SELECT * FROM settings WHERE _id=(CASE WHEN (SELECT COUNT(*) FROM settings)>1 THEN 2 ELSE 1 END);", null);
            settings.moveToFirst();
        } catch (NullPointerException e) {
            Log.w("error zhorick: ",e.getMessage());
        }

        return settings;
    }

    public static String id () {
        return settings.getString(settings.getColumnIndex("_id"));
    }
    public static String method () {
        return settings.getString(settings.getColumnIndex("method"));
    }

}
