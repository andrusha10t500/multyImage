package com.multyimage;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class Settings extends AppCompatActivity {
    private static SQLiteDatabase db;
    //Класс для отработки Настроек приложения
    DB dbh;
    TextView format,quality;
    Spinner spinnerformat, spinnertheme, spinnersort, spinnercompression;
    EditText txtquality, size, scale, sort;
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
        //Заполнение полей
        txtquality.setText(dbh.getQuality());
        size.setText(dbh.getSize());
        scale.setText(dbh.getScale());
    }

    public void acceptSettings(View view) {
        dbh.getWritableDatabase().execSQL("UPDATE settings SET " +
                "quality=" + txtquality.getText() + ", " +
                "view_sort='" + spinnersort.getSelectedItem().toString() + "', " +
                "view_theme='" + spinnertheme.getSelectedItem().toString() + "', " +
                "compression='" + spinnercompression.getSelectedItem().toString() + "', " +
                "view_scale=" + scale.getText() + ", " +
                "resolution='" + size.getText() + "' " +
                "where _id=(CASE WHEN (SELECT count(*) FROM settings)>1 THEN 2 ELSE 1 END)" );

        Log.w("LOGZHORA:", dbh.getSettings().getString(0));
        recreate();

    }
}
