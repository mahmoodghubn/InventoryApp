package com.example.mahmood.inventoryapp.data;

import android.provider.BaseColumns;

public class InventoryContract {

    private InventoryContract(){

    }
    public static final class InventoryEntry implements BaseColumns {

        public final static String TABLE_NAME = "Inventory";

        //columns of the table
        public final static String _ID = BaseColumns._ID;

        public final static String PRODUCT_NAME ="name";

        public final static String PRODUCT_PRICE = "price";

        public final static String PRODUCT_QUANTITY = "quantity";

        public final static String SUPPLIER_NAME = "supplier";

        public final static String SUPPLIER_PHONE_NUMBER = "phone";

        //constants
        public static final int COMPUTER = 0;
        public static final int PHONE = 1;
        public static final int TV = 2;
        public static final int WASHER = 3;


    }
}
