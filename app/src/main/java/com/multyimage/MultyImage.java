package com.multyimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.lowagie.text.pdf.codec.TiffImage;

import org.beyka.tiffbitmapfactory.CompressionScheme;
import org.beyka.tiffbitmapfactory.TiffSaver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.multyimage.*;
//Класс для обработки файлов, заполнение резервной таблицы tranzit согласно параметрам из таблицы settings
public class MultyImage {
    //Надо выловить из БД

    int  format = 1;        //JPEG
    int quality = 100;
    int scale = 100;
    String compression = "LZV";

    public MultyImage(String path) {
        //Конструктор позволяет понять какой метод нужно использовать
        if(path.substring(path.length()-3,3).equals("pdf") || path.substring(path.length()-3,3).equals("tif")) {
            this.To(path);
        }
        else {
            this.From(path);
        }
    }
    //Метод для получения множества файлов из одного многостраничного файла
    private void To(String path) {

        if(path.substring(path.length()-3,3).equals("pdf")) {
            //Случай для многостраничного PDF - файла

            try {
                PdfReader doc = new PdfReader(path);
                for (int i=0; i<=doc.getNumberOfPages(); i++) {
//                    Rectangle rectangle = doc.getPageSize(i);
//                    PdfObject pdfObject = doc.getPdfObject(i);
//                    pdfObject.getBytes();
//                    Image image = Image.getInstance((int)rectangle.getWidth(),(int)rectangle.getHeight(),pdfObject.getBytes(),null);

                    FileInputStream fileInputStream = new FileInputStream(path);
                    Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);

                    if(format==1) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG,quality,new FileOutputStream(i+path));
                    } else if (format==2) {
                        bitmap.compress(Bitmap.CompressFormat.PNG,quality,new FileOutputStream(i+path));
                    } else if (format==3) {
                        bitmap.compress(Bitmap.CompressFormat.WEBP,quality,new FileOutputStream(i+path));
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(path.substring(path.length()-3,3).equals("tif")) {
            //Случай для многостраничного tif - файла
            try {
                FileInputStream fileInputStream = new FileInputStream(path);
//                RandomAccessFileOrArray.InputStreamToArray((InputStream)fileInputStream);
                RandomAccessFileOrArray randomAccessFileOrArray = new RandomAccessFileOrArray(path);
                for(int i=0; i<=TiffImage.getNumberOfPages(randomAccessFileOrArray); i++) {
                    Image image = TiffImage.getTiffImage(randomAccessFileOrArray,i);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(TiffImage.getTiffImage(randomAccessFileOrArray,i).getOriginalData(),
                            0,
                            TiffImage.getTiffImage(randomAccessFileOrArray,i).getOriginalData().length);
                    if(format==1) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG,quality,new FileOutputStream(i+path));
                    } else if(format==2) {
                        bitmap.compress(Bitmap.CompressFormat.PNG,quality,new FileOutputStream(i+path));
                    } else if(format==3) {
                        bitmap.compress(Bitmap.CompressFormat.WEBP,quality,new FileOutputStream(i+path));
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void From(String path) {

        File[] file = new File(path).listFiles();
        Document document = new Document();
        for(int i=0; i<=file.length; i++) {
            //Случай если из JPEG-ов, или PNG или jpg или tif собираем многостраничный PDF
            if(file[i].getAbsolutePath().substring(file[i].getAbsolutePath().length()-3,3).equals("jpeg") ||
                    file[i].getAbsolutePath().substring(file[i].getAbsolutePath().length()-3,3).equals("png") ||
                        file[i].getAbsolutePath().substring(file[i].getAbsolutePath().length()-3,3).equals("jpg") ||
                            file[i].getAbsolutePath().substring(file[i].getAbsolutePath().length()-3,3).equals("tif")) {
                try {
                    Image image = Image.getInstance(file[i].getAbsolutePath());
//                    Rectangle rectangle = new Rectangle(image.getWidth(),image.getHeight());
                    Rectangle rectangle = document.getPageSize();
                    if(image.getWidth() > image.getHeight()) {
                        document.setPageSize(PageSize.A4.rotate());
                        image.scaleAbsolute(document.getPageSize().getHeight(),document.getPageSize().getWidth());
                    } else {
                        document.setPageSize(PageSize.A4);
                        image.scaleAbsolute(document.getPageSize().getWidth(),document.getPageSize().getHeight());
                    }
                    document.newPage();
                    document.add(image);

                } catch (BadElementException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            } else if (file[i].getAbsolutePath().substring(file[i].getAbsolutePath().length()-3,3).equals("pdf")) {
                //Случай если из pdf собираем многостраничный PDF
                try {
                    Image image = Image.getInstance(file[i].getAbsolutePath());
                    Rectangle rectangle = document.getPageSize();
                    float width = rectangle.getWidth();
                    float height = rectangle.getHeight();
                    if(image.getHeight()<image.getWidth()) {
                        document.setPageSize(PageSize.A4.rotate());
                        image.scaleAbsolute(height,width);
                    } else {
                        document.setPageSize(PageSize.A4);
                        image.scaleAbsolute(width,height);
                    }
                    document.newPage();
                    document.add(image);
                } catch (BadElementException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
            if(file[i].getAbsolutePath().substring(file[i].getAbsolutePath().length()-3,3).equals("jpeg") ||
                file[i].getAbsolutePath().substring(file[i].getAbsolutePath().length()-3,3).equals("png") ||
                    file[i].getAbsolutePath().substring(file[i].getAbsolutePath().length()-3,3).equals("jpg") ||
                        file[i].getAbsolutePath().substring(file[i].getAbsolutePath().length()-3,3).equals("tif") ||
                            file[i].getAbsolutePath().substring(file[i].getAbsolutePath().length()-3,3).equals("pdf")) {
                //Случай если из jpeg, png, jpg, pdf, tif создаём многостраничный TIF
                Bitmap bitmap = BitmapFactory.decodeFile(file[i].getAbsolutePath());
                TiffSaver.SaveOptions options = new TiffSaver.SaveOptions();
                if(compression.equals("LZV")) {
                    options.compressionScheme = CompressionScheme.LZW;
                } else {
                    options.compressionScheme = CompressionScheme.JPEG;
                }
                if(i!=0) {
                    TiffSaver.appendBitmap(path.substring(path.lastIndexOf("/"), path.length() - path.lastIndexOf("/"))+".tif", bitmap, options);
                } else {
                    TiffSaver.saveBitmap(path.substring(path.lastIndexOf("/"), path.length() - path.lastIndexOf("/"))+".tif", bitmap, options);
                }
            }
        }
        document.close();
    }
}
