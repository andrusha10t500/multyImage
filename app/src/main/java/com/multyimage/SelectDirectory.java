package com.multyimage;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SelectDirectory extends AppCompatActivity {

    ListView listView;
    TextView path;
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
//            strPath[i]=dbh.getPathDest() + strPath[i];
            strPath[i]=strPath[i];
        }

        path = (TextView) findViewById(R.id.path);
        path.setText(dbh.getPathDest());

        listAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,strPath);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(new File(path.getText() + adapterView.getItemAtPosition(i).toString()).isDirectory()) {
                    path.setText(path.getText() + adapterView.getItemAtPosition(i).toString() + "/");
                    String[] strPath = new File(path.getText().toString()).list();
                    ArrayList<String> strOut = new ArrayList<>();


                    for (int j=0; j<=strPath.length-1; j++) {
                        //нужна дополнительная проверка на то, какой сейчас выбран режим to/from
                        if(dbh.getMethod().equals("from")) {
                            //если from, то нужно выбрать файл
                            //и дать ему имя, а расширение храниться в базе данных в поле format

                            if (checkValidFilesForFrom(path.getText().toString())) {

                                strOut.add(strPath[j]);
                            }

                        } else {
                            //если to, то нужно выбрать папку с файлами
                            if(checkValidFilesForTo(path.getText().toString())) {
                                strOut.add(strPath[j]);
                            }
                        }
                    }

                    listAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, strOut);
                    listView.setAdapter(listAdapter);
                    //нужно чтобы показывал именно те директории, в которых есть файлы для обработки.
                } else {
                    path.setText(path.getText() + adapterView.getItemAtPosition(i).toString() );
                    if(path.getText().toString().substring(path.getText().toString().length()-3,path.getText().toString().length()).equals("tif")
                            || path.getText().toString().substring(path.getText().toString().length()-3,path.getText().toString().length()).equals("png")
                            || path.getText().toString().substring(path.getText().toString().length()-3,path.getText().toString().length()).equals("jpg")
                            || path.getText().toString().substring(path.getText().toString().length()-3,path.getText().toString().length()).equals("pdf")
                            || path.getText().toString().substring(path.getText().toString().length()-4,path.getText().toString().length()).equals("jpeg")) {
                        //проверка если нажат файл - то записываем в базу данных и закрываем активити
                        dbh.setPathDest(path.getText().toString());
                        finish();
                    }
                }
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
                        path.substring(path.length() - 4, path.length()).equals("jpeg") ||
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

    public void backDirectory(View view) {
        String DOWNLOAD = path.getText().toString().substring(0,path.getText().toString().lastIndexOf("/"));
        String myPath = DOWNLOAD.substring(0,DOWNLOAD.lastIndexOf("/")+1);
        path.setText(myPath);
        String[] directories = new File(myPath).list();
        if(directories.length>0) {

            view.setEnabled(true);
            ArrayList<String> strOut = new ArrayList<>();
            for(int i=0; i<=directories.length-1; i++) {
                if(checkValidFilesForTo(myPath + directories[i].toString())) {
                    strOut.add(directories[i].toString());
                }
            }

            listAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, strOut);
            listView.setAdapter(listAdapter);
        } else {
            view.setEnabled(false);
        }

    }

    public void selectDirectory(View view) {
        //Сохраняем нужную директорию для
        dbh.setPathDest(path.getText().toString());
        Intent intent = getIntent();
        finish();
    }
}
