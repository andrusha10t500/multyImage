package com.multyimage;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //Основное активити:
    //1. Если открываем многостраничник должна открыться форма From
    //2. Если открываем папку с картинками должна открыться форма To
    DB dbh;
    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        setContentView(R.layout.activity_main);
    }

    public void from(View view) {
        dbh.setMethod("from");
        Intent intent = new Intent(this,SecondActivity.class);
        startActivity(intent);
    }

    public void to(View view) {
        dbh.setMethod("to");
        Intent intent = new Intent(this,SecondActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    public void SettingsClick(MenuItem item) {

        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void AboutClick(MenuItem item) {
        Intent intent =new Intent(this, About.class);
        startActivity(intent);
//        Toast.makeText(getApplicationContext(),"Вы выбрали меню: " + item.getTitle(), Toast.LENGTH_SHORT).show();
    }

    public void ThemeClick(MenuItem item) {
        String theme;
        if(item.isChecked()) {
            theme="Темная";    //Светлая тема
        } else {
            theme="Светлая";    //Темная тема
        }
        dbh.newTheme(theme);
        recreate();
    }
}