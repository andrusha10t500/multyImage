package com.multyimage;

import android.provider.BaseColumns;

final class Contracts {
    //Класс для контракта БД
    private Contracts() {}

    public static final class Settings implements BaseColumns {
        public final static String ID = BaseColumns._ID;
        public final static String method = "method";    //идентификатор настроек их 2 по умолчанию To:
                                                    //1. From
                                                    //2. To
        //To
        public final static String dest = "dest"; //Папка для вывода по умолчанию
        //From & To
        public final static String format = "format";          //формат изображения
                                                            //По умолчанию PNG потому что максимальный набор настроек
        public final static String quality = "quality";              //качество
                                                            //По умолчанию 100
        public final static String resolution = "resolution";       //Кирилица?? -нет
                                                            //По умолчанию А4
        public final static String compression = "compression";    //Сжатие по умолчанию jpeg
        //изображения на форме
        public final static String view_scale = "view_scale";           //масштаб картинки отображаемой на форме
        public final static String view_sort = "view_sort";        //Сортировка по умолчанию по полю "номер строки" в алфавитном порядке

        public final static String view_Theme = "view_Theme";    //тема оформления по умолчанию светлая

        public final static String view = "view";                   //1 - вид мозайки
                                                            //2 - табличный вид
    }

    public static class Tranzit implements BaseColumns {
        //Класс для транзитных значений файлов для формирования ..
        //на форме (в основном для табличного вида), а так же для обработки
        public static String sort = BaseColumns._COUNT;             //Сортировка по умолчанию - по количеству
        public static String icon = "icon";                         //путь к изображению
        public static String name = "name";                         //Имена файлов
        public static String size = "size";                         //Размер изображения
        public static String all_size = "all_size";                 //Размер изображений
        public static String include = "include";                   //Признак включения|исключения файла(ов)
        public static String path = "path";                         //Путь для рабочих файлов
        public static String dest = "dest";                         //Папка назначения
    }
}
