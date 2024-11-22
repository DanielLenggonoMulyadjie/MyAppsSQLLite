package com.example.myapplicationsqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper (Context context) {
        super(context, "catatan.db", null, 1);
    }

    //Perintah membuat tabel catatan dan kolom-kolom untuk atribut catatan
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE catatan_table(id INTEGER PRIMARY KEY, judul TEXT, catatan TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS catatan_table");
    }

    //Memasukkan data catatan ke dalam SQLite database
    public boolean masukkanCatatan (SetterGetterData sgd) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("judul", sgd.getJudul());
        cv.put("catatan", sgd.getCatatan());
        return db.insert("catatan_table", null, cv) > 0;
    }

    //Mendapatkan seluruh data catatan dari Database
    public Cursor dapatkanSemuaCatatan () {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("Select * from " + "catatan_table", null);
    }

    //Memperbaharui catatan
    public boolean perbaharuiCatatan(SetterGetterData sgd, int id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("judul", sgd.getJudul());
        cv.put("catatan", sgd.getCatatan());
        return db.update("catatan_table", cv, "id" + "=" + id,
                null) > 0;
    }

    //Metode menghapus sebuah catatan
    public void hapusCatatan (int id) {
        SQLiteDatabase db = getReadableDatabase();
        db.delete("catatan_table", "id" + "=" + id, null);
    }

    //Menghapus seluruh data catatan
    public void hapusSemuaCatatan () {
        SQLiteDatabase db = getReadableDatabase();
        db.delete("catatan_table", null, null);
    }

}
