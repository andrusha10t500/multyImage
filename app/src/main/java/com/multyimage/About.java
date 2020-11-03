package com.multyimage;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {
    DB dbh;
    @Override
    public void onCreate( Bundle savedInstanceState) {
            dbh = new DB(this);
            Cursor cursor = dbh.getSettings();
            try{
                switch (cursor.getString(cursor.getColumnIndex("view_theme"))) {
                    case "Светлая" :
                        setTheme(R.style.AppThemeLight);
                        break;
                    case "Темная" :
                        setTheme(R.style.AppThemeDark);
                        break;
                    default:
                        setTheme(R.style.AppThemeLight);
                }
            } catch (CursorIndexOutOfBoundsException e) {
                setTheme(R.style.AppThemeLight);
            }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
    }
}
