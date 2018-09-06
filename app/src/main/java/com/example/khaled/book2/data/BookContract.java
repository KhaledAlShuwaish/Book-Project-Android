package com.example.khaled.book2.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by khaled on 2/15/18.
 */
public final class BookContract {

    public static final String CONTENT_AUTHORITY = "com.example.khaled.book2";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_BOOK = "book2";

    private BookContract() {
    }

    public static final class BookEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOK);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOK;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOK;
        public static final String TABLE_NAME = "book";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_Book_NAME = "Name";
        public static final String COLUMN_Book_Price = "Price";
        public static final String COLUMN_Book_Quantity = "Quantity";
        public static final String COLUMN_Book_SupplierName = "SupplierName";
        public static final String COLUMN_Book_SupplierEmail = "SupplierEmail";
        public static final String COLUMN_Book_Supplier_Phone_Number = "SupplierPhoneNumber";
    }
}
