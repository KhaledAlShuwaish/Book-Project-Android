package com.example.khaled.book2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.khaled.book2.data.BookContract.BookEntry;

/**
 * Created by khaled on 2/15/18.
 */

public class BookProvider extends ContentProvider {

    public static final String LOG_TAG = BookProvider.class.getSimpleName();

    private static final int BOOK = 100;
    private static final int BOOK_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOK, BOOK);
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOK + "/#", BOOK_ID);
    }

    private BookDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new BookDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case BOOK:
                cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BOOK_ID:
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOK:
                return BookEntry.CONTENT_LIST_TYPE;
            case BOOK_ID:
                return BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOK:
                return insertBook(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }


    private Uri insertBook(Uri uri, ContentValues values) {
        String name = values.getAsString(BookEntry.COLUMN_Book_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Book requires a name");
        }

        Integer price = values.getAsInteger(BookEntry.COLUMN_Book_Price);
        if (price == null) {
            throw new IllegalArgumentException("Book requires valid price");
        }

        Integer quantity = values.getAsInteger(BookEntry.COLUMN_Book_Quantity);
        if (quantity == null && quantity < 0) {
            throw new IllegalArgumentException("Book requires valid Quantity");
        }

        String SName = values.getAsString(BookEntry.COLUMN_Book_SupplierName);
        if (SName == null) {
            throw new IllegalArgumentException("Book requires a supplier Name");
        }


        String supplierEmail = values.getAsString(BookEntry.COLUMN_Book_SupplierEmail);
        if (supplierEmail == null) {
            throw new IllegalArgumentException("Book requires a supplier email");
        }
        String SPhoneN = values.getAsString(BookEntry.COLUMN_Book_Supplier_Phone_Number);
        if (SPhoneN == null) {
            throw new IllegalArgumentException("Book requires a supplier phone number");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(BookEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOK:
                rowsDeleted = database.delete(BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BOOK_ID:
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOK:
                return updateBook(uri, values, selection, selectionArgs);
            case BOOK_ID:
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(BookEntry.COLUMN_Book_NAME)) {
            String name = values.getAsString(BookEntry.COLUMN_Book_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Book requires a name");
            }
        }


        if (values.containsKey(BookEntry.COLUMN_Book_Price)) {
            Integer price = values.getAsInteger(BookEntry.COLUMN_Book_Price);
            if (price == null) {
                throw new IllegalArgumentException("Book requires valid gender");
            }
        }

        if (values.containsKey(BookEntry.COLUMN_Book_Quantity)) {
            Integer quantity = values.getAsInteger(BookEntry.COLUMN_Book_Quantity);

            if (quantity == null && quantity < 0) {
                throw new IllegalArgumentException("Book requires a quantity ");
            }
        }


        if (values.containsKey(BookEntry.COLUMN_Book_SupplierName)) {
            String Sname = values.getAsString(BookEntry.COLUMN_Book_SupplierName);
            if (Sname == null) {
                throw new IllegalArgumentException("Book requires a Supplier name");
            }
        }


        if (values.containsKey(BookEntry.COLUMN_Book_SupplierEmail)) {
            String Semail = values.getAsString(BookEntry.COLUMN_Book_SupplierEmail);
            if (Semail == null) {
                throw new IllegalArgumentException("Book requires a ِEmail ");
            }
        }

        if (values.containsKey(BookEntry.COLUMN_Book_Supplier_Phone_Number)) {
            String Sphone = values.getAsString(BookEntry.COLUMN_Book_Supplier_Phone_Number);

            if (Sphone == null) {
                throw new IllegalArgumentException("Book requires a Phone number ");
            }
        }
        if ((values.size() == 0)) {
            return 0;
        }


        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(BookEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

}
