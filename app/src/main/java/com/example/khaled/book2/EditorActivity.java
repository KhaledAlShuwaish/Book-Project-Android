package com.example.khaled.book2;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.khaled.book2.data.BookContract.BookEntry;


public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText EditName;

    private EditText EdintPrice;

    private EditText EditQuantity;

    private EditText EditSupplierName;

    private EditText EditSupplierEmail;

    private EditText EditSupplierPhoneNumber;

    private static final int Book_LOADER = 0;

    private Uri BookUri;

    private boolean BookHasChanged = false;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            BookHasChanged = true;
            return false;
        }
    };

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        BookUri = intent.getData();
        getLoaderManager().initLoader(Book_LOADER, null, this);
        EditName = findViewById(R.id.edit_book_name);
        EdintPrice = findViewById(R.id.edit_book_price);
        EditQuantity = findViewById(R.id.edit_book_quantity);
        EditSupplierName = findViewById(R.id.edit_book_SupplierNam);
        EditSupplierEmail = findViewById(R.id.edit_book_SupplierEmail);
        EditSupplierPhoneNumber = findViewById(R.id.edit_book_Supplier_Phone_Number);

        EditName.setOnTouchListener(mTouchListener);
        EdintPrice.setOnTouchListener(mTouchListener);
        EditQuantity.setOnTouchListener(mTouchListener);
        EditSupplierName.setOnTouchListener(mTouchListener);
        EditSupplierEmail.setOnTouchListener(mTouchListener);
        EditSupplierPhoneNumber.setOnTouchListener(mTouchListener);
    }

    private void saveBook() {

        String SaveName = EditName.getText().toString().trim();
        String SavePrice = EdintPrice.getText().toString().trim();
        String SaveQuantity = EditQuantity.getText().toString().trim();
        String SaveSupplierName = EditSupplierName.getText().toString().trim();
        String SaveSupplierEmail = EditSupplierEmail.getText().toString().trim();
        String SaveSupplierPhoneNumber = EditSupplierPhoneNumber.getText().toString().trim();

        if (BookUri == null && TextUtils.isEmpty(SaveName) && TextUtils.isEmpty(SavePrice) &&
                TextUtils.isEmpty(SaveQuantity)
                && TextUtils.isEmpty(SaveSupplierName) && TextUtils.isEmpty(SaveSupplierEmail) &&
                TextUtils.isEmpty(SaveSupplierPhoneNumber)) {
            return;
        }

        if (TextUtils.isEmpty(SaveName) || TextUtils.isEmpty(SavePrice)
                || TextUtils.isEmpty(SaveQuantity)
                || TextUtils.isEmpty(SaveSupplierName) || TextUtils.isEmpty(SaveSupplierName)
                || TextUtils.isEmpty(SaveSupplierPhoneNumber)) {
            return;
        }


        ContentValues values = new ContentValues();

        values.put(BookEntry.COLUMN_Book_NAME, SaveName);


        int priceInt = 0;
        if (!TextUtils.isEmpty(SavePrice)) {
            priceInt = Integer.parseInt(SavePrice);
        }
        values.put(BookEntry.COLUMN_Book_Price, priceInt);

        int quantityInt = 0;
        if (!TextUtils.isEmpty(SaveQuantity)) {
            quantityInt = Integer.parseInt(SaveQuantity);
        }
        values.put(BookEntry.COLUMN_Book_Quantity, quantityInt);


        values.put(BookEntry.COLUMN_Book_SupplierName, SaveSupplierName);
        values.put(BookEntry.COLUMN_Book_SupplierEmail, SaveSupplierEmail);
        values.put(BookEntry.COLUMN_Book_Supplier_Phone_Number, SaveSupplierPhoneNumber);

        int rowsAffected = getContentResolver().update(BookUri, values, null, null);

        if (rowsAffected == 0) {
            Toast.makeText(this, getString(R.string.editor_update_Book_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.editor_update_Book_successful),
                    Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        if (!BookHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.insertbook, menu);
        return true;
    }

    @Override


    public boolean onOptionsItemSelected(MenuItem item) {

        String ItemName = EditName.getText().toString().trim();
        String ItemPrice = EdintPrice.getText().toString().trim();
        String ItemQuantity = EditQuantity.getText().toString().trim();
        String ItemSupplierName = EditSupplierName.getText().toString().trim();
        String ItemSupplierEmail = EditSupplierEmail.getText().toString().trim();
        String ItemSupplierPhoneNumber = EditSupplierPhoneNumber.getText().toString().trim();

        switch (item.getItemId()) {
            case R.id.action_insert:
                saveBook();
                if (TextUtils.isEmpty(ItemName) || TextUtils.isEmpty(ItemPrice) || TextUtils.isEmpty(ItemQuantity)
                        || TextUtils.isEmpty(ItemSupplierName) || TextUtils.isEmpty(ItemSupplierEmail) ||
                        TextUtils.isEmpty(ItemSupplierPhoneNumber)) {
                    Toast.makeText(this, getString(R.string.empty),
                            Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
                return true;
            case android.R.id.home:
                if (!BookHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
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

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            return;
        }

        if (data.moveToFirst()) {
            int COLUMN_Book_NAME = data.getColumnIndex(BookEntry.COLUMN_Book_NAME);
            int COLUMN_Book_Price = data.getColumnIndex(BookEntry.COLUMN_Book_Price);
            int COLUMN_Book_Quantity = data.getColumnIndex(BookEntry.COLUMN_Book_Quantity);
            int COLUMN_Book_SupplierName = data.getColumnIndex(BookEntry.COLUMN_Book_SupplierName);
            int COLUMN_Book_SupplierEmail = data.getColumnIndex(BookEntry.COLUMN_Book_SupplierEmail);
            int COLUMN_Book_Supplier_Phone_Number = data.getColumnIndex(BookEntry.COLUMN_Book_Supplier_Phone_Number);

            String getName = data.getString(COLUMN_Book_NAME);
            int getPrice = data.getInt(COLUMN_Book_Price);
            int getQuantity = data.getInt(COLUMN_Book_Quantity);
            String getSupplierName = data.getString(COLUMN_Book_SupplierName);
            String getSupplierEmail = data.getString(COLUMN_Book_SupplierEmail);
            String getSupplierPhoneNumber = data.getString(COLUMN_Book_Supplier_Phone_Number);

            EditName.setText(getName);
            EdintPrice.setText(Integer.toString(getPrice));
            EditQuantity.setText(Integer.toString(getQuantity));
            EditSupplierName.setText(getSupplierName);
            EditSupplierEmail.setText(getSupplierEmail);
            EditSupplierPhoneNumber.setText(getSupplierPhoneNumber);
        }

    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        EditName.setText("");
        EdintPrice.setText("");
        EditQuantity.setText("");
        EditSupplierName.setText("");
        EditSupplierEmail.setText("");
        EditSupplierPhoneNumber.setText("");
    }
}
