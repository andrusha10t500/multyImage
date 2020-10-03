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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }

}
