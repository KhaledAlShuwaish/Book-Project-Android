package com.example.khaled.book2;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

public class InsertActivity extends AppCompatActivity {

    private Uri BookUri;

    private EditText EditName;

    private EditText EdintPrice;

    private EditText EditQuantity;

    private EditText EditSupplierName;

    private EditText EditSupplierEmail;

    private EditText EditSupplierPhoneNumber;


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
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


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

        String Name = EditName.getText().toString().trim();
        String Price = EdintPrice.getText().toString().trim();
        String Quantity = EditQuantity.getText().toString().trim();
        String SupplierName = EditSupplierName.getText().toString().trim();
        String SupplierEmail = EditSupplierEmail.getText().toString().trim();
        String SupplierPhoneNumber = EditSupplierPhoneNumber.getText().toString().trim();

        if (BookUri == null && TextUtils.isEmpty(Name) && TextUtils.isEmpty(Price) &&
                TextUtils.isEmpty(Quantity) && TextUtils.isEmpty(SupplierName) &&
                TextUtils.isEmpty(SupplierEmail) && TextUtils.isEmpty(SupplierPhoneNumber)) {
            return;
        }


        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_Book_NAME, Name);

        int priceInt = 0;
        if (!TextUtils.isEmpty(Price)) {
            priceInt = Integer.parseInt(Price);
        }
        values.put(BookEntry.COLUMN_Book_Price, priceInt);

        int quantityInt = 0;
        if (!TextUtils.isEmpty(Quantity)) {
            quantityInt = Integer.parseInt(Quantity);
        }
        values.put(BookEntry.COLUMN_Book_Quantity, quantityInt);
        values.put(BookEntry.COLUMN_Book_SupplierName, SupplierName);
        values.put(BookEntry.COLUMN_Book_SupplierEmail, SupplierEmail);
        values.put(BookEntry.COLUMN_Book_Supplier_Phone_Number, SupplierPhoneNumber);

        Uri uri = getContentResolver().insert(BookEntry.CONTENT_URI, values);

        if (uri == null) {
            Toast.makeText(this, getString(R.string.editor_insert_Book_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.editor_insert_Book_successful),
                    Toast.LENGTH_SHORT).show();
        }

    }


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


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.insertbook, menu);

        return true;
    }


    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        String Item_name = EditName.getText().toString().trim();
        String Item_price = EdintPrice.getText().toString().trim();
        String Item_quantity = EditQuantity.getText().toString().trim();
        String Item_supplierName = EditSupplierName.getText().toString().trim();
        String Item_supplierEmail = EditSupplierEmail.getText().toString().trim();
        String Item_supplierPhoneNumber = EditSupplierPhoneNumber.getText().toString().trim();

        switch (item.getItemId()) {
            case R.id.action_insert:

                saveBook();

                if (TextUtils.isEmpty(Item_name) || TextUtils.isEmpty(Item_price) || TextUtils.isEmpty(Item_quantity)
                        || TextUtils.isEmpty(Item_supplierName) || TextUtils.isEmpty(Item_supplierEmail) || TextUtils.isEmpty(Item_supplierPhoneNumber)) {
                    Toast.makeText(this, getString(R.string.empty),
                            Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
                return true;
            case android.R.id.home:
                if (!BookHasChanged) {
                    NavUtils.navigateUpFromSameTask(InsertActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(InsertActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}