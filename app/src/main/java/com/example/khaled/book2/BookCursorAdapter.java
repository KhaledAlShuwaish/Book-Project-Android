package com.example.khaled.book2;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khaled.book2.data.BookContract.BookEntry;

/**
 * Created by khaled on 2/15/18.
 */

public class BookCursorAdapter extends CursorAdapter {
    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView price = (TextView) view.findViewById(R.id.price);
        final TextView qunatity = (TextView) view.findViewById(R.id.quantity);
        ImageView sale = view.findViewById(R.id.sale);


        int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_Book_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_Book_Price);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_Book_Quantity);
        int id = cursor.getInt(cursor.getColumnIndex(BookEntry._ID));


        String BookName = cursor.getString(nameColumnIndex);
        int BookPrice = cursor.getInt(priceColumnIndex);
        final int BookQuantity = cursor.getInt(quantityColumnIndex);

        final Uri uri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);


        name.setText("Name : " + BookName);

        price.setText("Price:  " + Integer.toString(BookPrice));


        qunatity.setText("Quantity:  " + BookQuantity);


        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentResolver res = v.getContext().getContentResolver();
                ContentValues value = new ContentValues();
                if (BookQuantity > 0) {


                    int newquantity = BookQuantity;

                    value.put(BookEntry.COLUMN_Book_Quantity, --newquantity);
                    res.update(uri,
                            value,
                            null,
                            null);

                    qunatity.setText("Quantity:  " + newquantity);

                    context.getContentResolver().notifyChange(uri, null);

                } else {
                    Toast.makeText(context, "Quantity of Book is 0", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


}
