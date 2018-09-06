package com.example.khaled.book2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.khaled.book2.data.BookContract.BookEntry;

/**
 * Created by khaled on 2/15/18.
 */

public class BookDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "boo.db";
    private static final int DATABASE_VERSION = 1;

    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PRODUCT_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + " ("
                + BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookEntry.COLUMN_Book_NAME + " TEXT NOT NULL,"
                + BookEntry.COLUMN_Book_Price + " INTEGER NOT NULL,"
                + BookEntry.COLUMN_Book_Quantity + " INTEGER NOT NULL,"
                + BookEntry.COLUMN_Book_SupplierName + " TEXT NOT NULL,"
                + BookEntry.COLUMN_Book_SupplierEmail + " TEXT,"
                + BookEntry.COLUMN_Book_Supplier_Phone_Number + " TEXT);";

        db.execSQL(SQL_CREATE_PRODUCT_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
