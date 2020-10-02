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
    SQLiteDatabase db;
    private Toolbar tb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int theme;
        dbh = new DB(this);
        db = dbh.getWritableDatabase();

//        Cursor cursor = db.rawQuery("SELECT view_theme FROM settings WHERE _id=1", null);
        Cursor cursor = Settings.getSettings();
        try{
//            cursor.moveToFirst();
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


//        try {
//            theme=getIntent().getExtras().getInt("theme");
//        } catch (NullPointerException e) {
//            theme=2;
//        }
//        if(theme<2) {
//            if(theme==1) {
//                setTheme(R.style.AppThemeLight);
//            } else {
//                setTheme(R.style.AppThemeDark);
//            }
//        } else {
//            setTheme(R.style.AppThemeLight);
//        }



        super.onCreate(savedInstanceState);
//        tb = findViewById(R.id.toolbar);
//        setSupportActionBar(tb);

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
//        Intent intent = getIntent();
        int theme;
        if(item.isChecked()) {
//            item.setTitle("Тема: Светлая");
            theme=0;
        } else {
//            item.setTitle("Тема: Темная");
            theme=1;
        }
//        db.execSQL("INSERT INTO settings ( view_theme ) VALUES ('" + theme + "')" );
        db.execSQL("UPDATE settings SET view_theme='" + theme + "' WHERE _id=1");
//        intent.putExtra("theme",theme);
        recreate();
    }
}