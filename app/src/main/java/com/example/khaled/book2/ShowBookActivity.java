package com.example.khaled.book2;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khaled.book2.data.BookContract.BookEntry;


public class ShowBookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView EditName;

    private TextView EdintPrice;

    private TextView EditQuantity;

    private TextView EditSupplierName;

    private TextView EditSupplierEmail;

    private TextView EditSupplierPhoneNumber;
    private Button edit;
    private Button delete;
    private Button inc;
    private Button dec;
    private Button order;

    private Uri BookUri;

    private static final int Book_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book);

        Intent i = getIntent();
        BookUri = i.getData();

        getLoaderManager().initLoader(Book_LOADER, null, this);

        order = findViewById(R.id.order);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", EditSupplierPhoneNumber.getText().toString(), null));
                startActivity(intent);

            }
        });

        inc = findViewById(R.id.inc);
        inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = EditQuantity.getText().toString();

                int value;

                if (quantity.isEmpty()) {
                    value = 0;
                } else {
                    value = Integer.parseInt(quantity);
                }
                EditQuantity.setText(String.valueOf(value + 1));

                ContentValues values = new ContentValues();
                values.put(BookEntry.COLUMN_Book_Quantity, value + 1);
                getContentResolver().update(BookUri, values, null, null);

            }
        });

        dec = findViewById(R.id.dec);
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = EditQuantity.getText().toString();

                int value;

                if (quantity.isEmpty()) {
                    return;
                } else if (quantity.equals("0")) {
                    return;
                } else {
                    value = Integer.parseInt(quantity);
                    EditQuantity.setText(String.valueOf(value - 1));
                    ContentValues values = new ContentValues();
                    values.put(BookEntry.COLUMN_Book_Quantity, value - 1);
                    getContentResolver().update(BookUri, values, null, null);
                }
            }

        });


        delete = findViewById(R.id.Delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });

        edit = findViewById(R.id.Edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowBookActivity.this, EditorActivity.class);

                intent.setData(BookUri);

                startActivity(intent);
            }
        });
        EditName = findViewById(R.id.nameShow);
        EdintPrice = findViewById(R.id.priceShow);
        EditQuantity = findViewById(R.id.quantityShow);
        EditSupplierName = findViewById(R.id.supplierNameShow);
        EditSupplierEmail = findViewById(R.id.supplierEmailShow);
        EditSupplierPhoneNumber = findViewById(R.id.supplierPhoneNumberShow);

    }


    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.Case_Edit:
                EditBooks();
                return true;
            case R.id.Case_Delete:
                showDeleteConfirmationDialog();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private void EditBooks() {
        Intent intent = new Intent(ShowBookActivity.this, EditorActivity.class);

        intent.setData(BookUri);

        startActivity(intent);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteBook() {
        if (BookUri != null) {
            int rowsDeleted = getContentResolver().delete(BookUri, null, null);

            Intent i = new Intent(ShowBookActivity.this, MenuActivity.class);
            startActivity(i);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_Book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_Book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }


    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_Book_NAME,
                BookEntry.COLUMN_Book_Price,
                BookEntry.COLUMN_Book_Quantity,
                BookEntry.COLUMN_Book_SupplierName,
                BookEntry.COLUMN_Book_SupplierEmail,
                BookEntry.COLUMN_Book_Supplier_Phone_Number};

        return new CursorLoader(this,
                BookUri,
                projection,
                null,
                null,
                null);
    }


    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }


        if (cursor.moveToFirst()) {
            int COLUMN_Book_NAME = cursor.getColumnIndex(BookEntry.COLUMN_Book_NAME);
            int COLUMN_Book_Price = cursor.getColumnIndex(BookEntry.COLUMN_Book_Price);
            int COLUMN_Book_Quantity = cursor.getColumnIndex(BookEntry.COLUMN_Book_Quantity);
            int COLUMN_Book_SupplierName = cursor.getColumnIndex(BookEntry.COLUMN_Book_SupplierName);
            int COLUMN_Book_SupplierEmail = cursor.getColumnIndex(BookEntry.COLUMN_Book_SupplierEmail);
            int COLUMN_Book_Supplier_Phone_Number = cursor.getColumnIndex(BookEntry.COLUMN_Book_Supplier_Phone_Number);


            String get_name = cursor.getString(COLUMN_Book_NAME);
            String get_Sname = cursor.getString(COLUMN_Book_SupplierName);
            String get_SEmail = cursor.getString(COLUMN_Book_SupplierEmail);
            int get_price = cursor.getInt(COLUMN_Book_Price);
            int get_quantity = cursor.getInt(COLUMN_Book_Quantity);
            String get_PhoneN = cursor.getString(COLUMN_Book_Supplier_Phone_Number);


            EditName.setText(get_name);
            EditSupplierName.setText(get_Sname);
            EditSupplierEmail.setText(get_SEmail);
            EdintPrice.setText(Integer.toString(get_price));
            EditQuantity.setText(Integer.toString(get_quantity));
            EditSupplierPhoneNumber.setText(get_PhoneN);
        }
    }

    public void onLoaderReset(android.content.Loader<Cursor> loader) {

        EditName.setText("");
        EditSupplierName.setText("");
        EditSupplierEmail.setText("");
        EdintPrice.setText("");
        EditQuantity.setText("");
        EditSupplierPhoneNumber.setText("");
    }
}

