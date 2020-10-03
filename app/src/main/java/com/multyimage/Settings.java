package com.multyimage;

import android.content.Context;
import android.database.Cursor;
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
    Spinner spinnerformat;
    EditText txtquality;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppThemeLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        dbh = new DB(this);


        spinnerformat = (Spinner)findViewById(R.id.spinnerformat);
        txtquality = (EditText)findViewById(R.id.txtquality);
        //Заполнение полей
        txtquality.setText(dbh.getScale());

    }

}
