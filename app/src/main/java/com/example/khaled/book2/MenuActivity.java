package com.example.khaled.book2;

import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.khaled.book2.data.BookContract.BookEntry;

public class MenuActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int BOOK_LOADER = 0;
    BookCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        ListView BookListView = findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        BookListView.setEmptyView(emptyView);

        mCursorAdapter = new BookCursorAdapter(this, null);
        BookListView.setAdapter(mCursorAdapter);

        BookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MenuActivity.this, ShowBookActivity.class);

                Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);

                intent.setData(currentBookUri);

                startActivity(intent);

            }
        });

        getLoaderManager().initLoader(BOOK_LOADER, null, this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            insertBook();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertBook() {
        Intent intent = new Intent(MenuActivity.this, InsertActivity.class);
        startActivity(intent);
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_menue, menu);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle args) {
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_Book_NAME,
                BookEntry.COLUMN_Book_Price,
                BookEntry.COLUMN_Book_Quantity,
                BookEntry.COLUMN_Book_SupplierName,
                BookEntry.COLUMN_Book_SupplierEmail,
                BookEntry.COLUMN_Book_Supplier_Phone_Number};

        return new CursorLoader(this,
                BookEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
        getLoaderManager().restartLoader(0, null, this);


    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);


    }

}
