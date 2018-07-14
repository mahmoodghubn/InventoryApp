package com.example.mahmood.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahmood.inventoryapp.data.DbHelper;
import com.example.mahmood.inventoryapp.data.InventoryContract.InventoryEntry;

public class MainActivity extends AppCompatActivity {
    private Spinner mNameSpinner;

    private EditText mPriceEditText;

    private EditText mQuantityEditText;

    private EditText mSupplierEditText;

    private EditText mPhoneEditText;

    private int productName;

    Button insert;
    Button display;
    private DbHelper mDbHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNameSpinner = findViewById(R.id.name);
        mPriceEditText = findViewById(R.id.price);
        mQuantityEditText = findViewById(R.id.quantity);
        mSupplierEditText = findViewById(R.id.supplier);
        mPhoneEditText = findViewById(R.id.phone);
        insert = findViewById(R.id.insert);
        insert.setOnClickListener(new TextView.OnClickListener() {

            @Override
            public void onClick(View v) {
                insertProduct();
            }
        });
        display = findViewById(R.id.display);
        display.setOnClickListener(new TextView.OnClickListener() {

            @Override
            public void onClick(View v) {
                displayDatabaseInfo();
            }
        });
        mDbHelper = new DbHelper(this);

        setupSpinner();
    }
    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();


        String[] projection= {InventoryEntry._ID,InventoryEntry.PRODUCT_NAME,InventoryEntry.PRODUCT_PRICE,
                InventoryEntry.PRODUCT_QUANTITY,InventoryEntry.SUPPLIER_NAME,InventoryEntry.SUPPLIER_PHONE_NUMBER};

        Cursor cursor = db.query(InventoryEntry.TABLE_NAME,projection,null,null,
                null,null,null,null);

        TextView displayView = (TextView) findViewById(R.id.database);

        try {

            displayView.setText("The pets table contains " + cursor.getCount() + " pets.\n\n");
            displayView.append(InventoryEntry._ID + " - " +
                    InventoryEntry.PRODUCT_NAME +" - " +
                    InventoryEntry.PRODUCT_PRICE+" - " +
                    InventoryEntry.PRODUCT_QUANTITY+" - " +
                    InventoryEntry.SUPPLIER_NAME+" - " +
                    InventoryEntry.SUPPLIER_PHONE_NUMBER+"\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.PRODUCT_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(InventoryEntry.SUPPLIER_NAME);
            int phoneColumnIndex = cursor.getColumnIndex(InventoryEntry.SUPPLIER_PHONE_NUMBER);


            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentPrice = cursor.getString(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplier = cursor.getString(supplierColumnIndex);
                int currentPhone = cursor.getInt(phoneColumnIndex);

                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - "+ currentPrice + " - "+ currentQuantity+ " - " + currentSupplier+ " - " + currentPhone));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.product, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mNameSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.computer))) {
                        productName = InventoryEntry.COMPUTER;
                    } else if (selection.equals(getString(R.string.phone))) {
                        productName = InventoryEntry.PHONE;
                    } else if (selection.equals(getString(R.string.TV))){
                        productName = InventoryEntry.TV;
                    } else {
                        productName = InventoryEntry.WASHER;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                productName = InventoryEntry.COMPUTER;
            }
        });
    }
    private void insertProduct() {
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String supplierName = mSupplierEditText.getText().toString().trim();
        String supplierPhone = mPhoneEditText.getText().toString().trim();
        int price = Integer.parseInt(priceString);
        int quantity = Integer.parseInt(quantityString);
        int phone = Integer.parseInt(supplierPhone);

        // Create database helper
        DbHelper mDbHelper = new DbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.PRODUCT_NAME, productName);
        values.put(InventoryEntry.PRODUCT_PRICE, price);
        values.put(InventoryEntry.PRODUCT_QUANTITY, quantity);
        values.put(InventoryEntry.SUPPLIER_NAME, supplierName);
        values.put(InventoryEntry.SUPPLIER_PHONE_NUMBER,phone );


        // Insert a new row for product in the database, returning the ID of that new row.
        long newRowId = db.insert(InventoryEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving product", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Product saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }
}
