package com.multyimage;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.List;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class Settings extends AppCompatActivity {
    private static SQLiteDatabase db;
    //Класс для отработки Настроек приложения
    DB dbh;
    TextView format,quality;
    Spinner spinnerformat, spinnertheme, spinnersort, spinnercompression;
    EditText txtquality, size, scale;
    Button settingsDefault;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        setContentView(R.layout.settings);

        spinnerformat = (Spinner)findViewById(R.id.spinnerformat);
        txtquality = (EditText)findViewById(R.id.txtquality);
        size = (EditText)findViewById(R.id.size);
        scale = (EditText)findViewById(R.id.scale);
        spinnersort = (Spinner)findViewById(R.id.spinnersort);
        spinnertheme = (Spinner)findViewById(R.id.spinnertheme);
        spinnercompression = (Spinner)findViewById(R.id.spinnercompression);
        settingsDefault = (Button)findViewById(R.id.settingsDefault);
        //Заполнение полей
        spinnerformat.setSelection(dbh.getFormat().equals("TIF") ? 0 : 1);

        spinnersort.setSelection(dbh.getSort().equals("_id") ? 0 :
                dbh.getSort().equals("имя") ? 1 :
                        dbh.getSort().equals("размер") ? 2 :
                                dbh.getSort().equals("сортировка") ? 3 : 0);

        spinnertheme.setSelection(dbh.getTheme().equals("Светлая") ? 0 : 1);

        spinnercompression.setSelection(dbh.getCompression().equals("LZV") ? 0 : 1);

        txtquality.setText(dbh.getQuality());
        size.setText(dbh.getSize());
        scale.setText(dbh.getScale());
        settingsDefault.setEnabled(dbh.getWritableDatabase().rawQuery("SELECT * FROM settings",null).getCount()>1);
    }

    public void acceptSettings(View view) {
        //применение настроек
        //если нет настроек пользователя, то создаём ещё одну
        if(!settingsDefault.isEnabled()) {
            dbh.getWritableDatabase().execSQL("INSERT INTO settings (" +
                    "quality," +
                    "view_sort," +
                    "view_theme," +
                    "compression," +
                    "view_scale," +
                    "resolution," +
                    "format) VALUES (" +
                    txtquality.getText() + ",'" +
                    spinnersort.getSelectedItem().toString() + "','" +
                    spinnertheme.getSelectedItem().toString()+ "','" +
                    spinnercompression.getSelectedItem().toString()+ "'," +
                    scale.getText()+ ",'" +
                    size.getText()+ "','" +
                    spinnerformat.getSelectedItem().toString() + "')");
        } else {
            dbh.getWritableDatabase().execSQL("UPDATE settings SET " +
                    "quality=" + txtquality.getText() + ", " +
                    "view_sort='" + spinnersort.getSelectedItem().toString() + "', " +
                    "view_theme='" + spinnertheme.getSelectedItem().toString() + "', " +
                    "compression='" + spinnercompression.getSelectedItem().toString() + "', " +
                    "view_scale=" + scale.getText() + ", " +
                    "resolution='" + size.getText() + "', " +
                    "format='" + spinnerformat.getSelectedItem().toString() +  "'" +
                    "where _id=(SELECT MAX(_id) FROM settings)" );
        }



        recreate();

    }

    public void settingsDefault(View view) {
        //Сброс настроек по умолчанию
        //удаляем 2-ю строку настроек (настройки пользователя), остаются настройки по умолчанию
        dbh.getWritableDatabase().execSQL("DELETE FROM settings WHERE _id>1");

        recreate();
    }

    public void getPathDest(View view) {
        Intent intent = new Intent(this,SelectDirectory.class);
        startActivity(intent);
    }
}
