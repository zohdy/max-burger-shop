package com.zohdy.maxburger.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zohdy.maxburger.common.Common;
import com.zohdy.maxburger.interfaces.Constants;
import com.zohdy.maxburger.models.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peterzohdy on 15/11/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    //Constants for db name and version
    private static final String DATABASE_NAME = Constants.SQLITE_DB_NAME;
    private static final int DATABASE_VERSION = 2;
    private static DatabaseHelper databaseInstance = null;


    // ******************************* SINGLETON ******************************* //
    public static DatabaseHelper getInstance(Context context) {
        if(databaseInstance == null) {
            databaseInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return databaseInstance;
    }

    // Constructor is private
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // *******************************************************************************

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.DB_TABLE_ORDER_DETAILS);
        onCreate(db);
    }

    // Create the columns for the Database
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE " + Constants.DB_TABLE_ORDER_DETAILS + "( " +
                    Constants.DB_COLUMN_KEY_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, "  +
                    Constants.DB_COLUMN_FOOD_ID + " TEXT, " +
                    Constants.DB_COLUMN_FOOD_QUANTITY + " TEXT, " +
                    Constants.DB_COLUMN_FOOD_NAME + " TEXT, " +
                    Constants.DB_COLUMN_FOOD_PRICE + " TEXT" +
                    ")");
        } catch (SQLException e) {
            Common.createLog(" Error creating database " + e.getMessage());
        }
    }



    // **************** All CRUD(Create, Read, Update, Delete) Operations **************** //

    public void addOrderToCart(Order order) {

        // Get write access to the database
        SQLiteDatabase database = this.getWritableDatabase();

        // Get the relevant values from the current order and store them in a ContentValues object
        ContentValues currentOrder = new ContentValues();
        currentOrder.put(Constants.DB_COLUMN_FOOD_ID, order.getFoodId());
        currentOrder.put(Constants.DB_COLUMN_FOOD_NAME, order.getFoodName());
        currentOrder.put(Constants.DB_COLUMN_FOOD_QUANTITY, order.getQuantity());
        currentOrder.put(Constants.DB_COLUMN_FOOD_PRICE, order.getPrice());

        // Use the insert command and insert the ContentValues object
        database.insert(Constants.DB_TABLE_ORDER_DETAILS, null, currentOrder);
        database.close();
    }

    // Get all orders from SQLite DB and return them in an arraylist
    public List<Order> getItemsInCart() {

        List<Order> orderList = new ArrayList<>();

        // Select all from table
        String selectQuery = "SELECT * FROM " + Constants.DB_TABLE_ORDER_DETAILS;
        SQLiteDatabase database = this.getReadableDatabase();

        // Init a cursor object to move through the values obtained by the query
        Cursor cursor = database.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
       if(cursor.moveToFirst()) {
            do {
                // add each value to an arrayList of Order items
                Order order = new Order();
                order.setKeyId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.DB_COLUMN_KEY_ID))));
                order.setFoodId(cursor.getString(cursor.getColumnIndex(Constants.DB_COLUMN_FOOD_ID)));
                order.setFoodName(cursor.getString(cursor.getColumnIndex(Constants.DB_COLUMN_FOOD_NAME)));
                order.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.DB_COLUMN_FOOD_QUANTITY)));
                order.setPrice(cursor.getString(cursor.getColumnIndex(Constants.DB_COLUMN_FOOD_PRICE)));
                orderList.add(order);

            } while (cursor.moveToNext());
        }

        // return the values from the query in an arraylist
        return orderList;
    }

    // Deleting 1 order from cart
    public void deleteOrderItem(List<Order> cartItems, int clickedItem) {
        SQLiteDatabase database = this.getWritableDatabase();

        // The Order object model has a keyID which is mapped to the SQLite column key id.
        // it uses this ID to query and delete the chosen item
        database.delete(Constants.DB_TABLE_ORDER_DETAILS, Constants.DB_COLUMN_KEY_ID + " =?", new String[] {
                String.valueOf(cartItems.get(clickedItem).getKeyId())
        });

        database.close();
    }

    // Deleting all orders
    public void clearCart() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(Constants.DB_TABLE_ORDER_DETAILS,null,null);
        database.close();
    }

}
