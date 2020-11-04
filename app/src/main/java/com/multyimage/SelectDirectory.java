package com.multyimage;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SelectDirectory extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> listAdapter;
    DB dbh;
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
        setContentView(R.layout.select_directory);

        dbh = new DB(this);
        listView = (ListView)findViewById(R.id.listdir);
        String[] strPath = new File(dbh.getPathDest()).list();
        for(int i=0; i<=strPath.length-1; i++){
            strPath[i]=dbh.getPathDest() + strPath[i];
        }

        listAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,strPath);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //нужно чтобы переходил в нужную директорию

            String[] strPath = new File(adapterView.getItemAtPosition(i).toString()).list();
            ArrayList<String> strOut = new ArrayList<>();

            for (int j=0; j<=strPath.length-1; j++) {
                //нужна дополнительная проверка на то, какой сейчас выбран режим to/from
                if(dbh.getMethod().equals("from")) {
                    //если from, то нужно выбрать файл
                    //и дать ему имя, а расширение храниться в базе данных в поле format
                    if (checkValidFilesForFrom(adapterView.getItemAtPosition(i).toString() + "/" + strPath[j])) {
                        strOut.add(adapterView.getItemAtPosition(i).toString() + "/" + strPath[j]);
                    }
                } else {
                    //если to, то нужно выбрать папку с файлами
                    if(checkValidFilesForTo(adapterView.getItemAtPosition(i).toString() + "/" + strPath[j])) {
                        strOut.add(adapterView.getItemAtPosition(i).toString()+ "/" + strPath[j]);
                    }
                }
            }

            listAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, strOut);
            listView.setAdapter(listAdapter);
            //нужно чтобы показывал именно те директории, в которых есть файлы для обработки.
            }
        });
    }
    //проверка есть ли нужные файлы в директории для метода from
    private boolean checkValidFilesForFrom(String path) {
        boolean ret=false;
        if(new File(path).isDirectory()) {
            String[] pathArr = new File(path).list();
            for(int i=0; i<=pathArr.length-1; i++){
                if(checkValidFilesForFrom(path + "/" + pathArr[i])) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            if (path.length() > 3) {
                if (path.substring(path.length() - 3, path.length()).equals("png") ||
                        path.substring(path.length() - 3, path.length()).equals("jpg") ||
                        path.substring(path.length() - 3, path.length()).equals("jpeg") ||
                        path.substring(path.length() - 3, path.length()).equals("tif") ||
                        path.substring(path.length() - 3, path.length()).equals("pdf")) {
                    //условия to (в файл) или from (из файла)
                    //Если предыдущая папка является path, то нужно прописать в бд новый путь назначения и выйти в Settings для From
                    ret = true;
                    return ret;
                }
            }
        }

        return ret;
    }

    //проверка есть ли нужные Директории для метода To
    private boolean checkValidFilesForTo(String path) {
        boolean ret=false;
        String[] pathArr = new File(path).list();
        File f = new File(path);
        if(f.isDirectory()) {
            if (f.listFiles().length > 0) {

                for (int i = 0; i <= f.listFiles().length; i++) {
                    if (checkValidFilesForFrom(path + "/" + pathArr[i])) {
                        ret = true;
                        return true;
                    } else {
                        ret = false;
                        return false;
                    }
                }
            }
        }

        return ret;
    }

}
