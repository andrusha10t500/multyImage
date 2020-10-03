package com.multyimage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    //Основное активити:
    //1. Если открываем многостраничник должна открыться форма From
    //2. Если открываем папку с картинками должна открыться форма To
    DB dbh;
//    SQLiteDatabase db = dbh.getWritableDatabase();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbh = new DB(this);
        Cursor cursor = dbh.getSettings();
        try{
            switch (cursor.getString(cursor.getColumnIndex("view_theme")).toCharArray()[0]) {
                case '1' :
                    setTheme(R.style.AppThemeLight);
                    break;
                case '0' :
                    setTheme(R.style.AppThemeDark);
                    break;
                default:
                    setTheme(R.style.AppThemeLight);
            }
        } catch (CursorIndexOutOfBoundsException e) {
            setTheme(R.style.AppThemeLight);
        }


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }


    public void SettingsClick(MenuItem item) {
        Toast.makeText(getApplicationContext(),"Вы выбрали меню: " + item.getTitle(), Toast.LENGTH_SHORT).show();
    }

    public void AboutClick(MenuItem item) {
        Toast.makeText(getApplicationContext(),"Вы выбрали меню: " + item.getTitle(), Toast.LENGTH_SHORT).show();
    }

    public void ThemeClick(MenuItem item) {
        int theme;
        if(item.isChecked()) {
            theme=0;    //Светлая тема
        } else {
            theme=1;    //Темная тема
        }
        dbh.newTheme(theme);
        recreate();
    }
}