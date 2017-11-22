package com.zohdy.maxburger.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.zohdy.maxburger.common.Common;
import com.zohdy.maxburger.interfaces.Constants;
import com.zohdy.maxburger.models.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peterzohdy on 15/11/2017.
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    //Constants for db name and version
    private static final String DATABASE_NAME = "maxburger.db";
    private static final int DATABASE_VERSION = 2;

    private static DBOpenHelper databaseInstance = null;

    public static DBOpenHelper newInstance(Context context) {
        if(databaseInstance == null) {
            databaseInstance = new DBOpenHelper(context.getApplicationContext());
        }
        return databaseInstance;
    }

    private DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.DB_TABLE_ORDER_DETAILS);
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    public void addOrderToCart(Order order) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.DB_COLUMN_FOOD_ID, order.getFoodId());
        values.put(Constants.DB_COLUMN_FOOD_NAME, order.getFoodName());
        values.put(Constants.DB_COLUMN_FOOD_QUANTITY, order.getQuantity());
        values.put(Constants.DB_COLUMN_FOOD_PRICE, order.getPrice());

        // Inserting Row
        database.insert(Constants.DB_TABLE_ORDER_DETAILS, null, values);
        database.close();
    }

    // Get all orders from SQLite DB and return them in an arraylist
    public List<Order> getItemsInCart() {
        List<Order> orderList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Constants.DB_TABLE_ORDER_DETAILS;
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
       if(cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setKeyId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.DB_COLUMN_KEY_ID))));
                order.setFoodId(cursor.getString(cursor.getColumnIndex(Constants.DB_COLUMN_FOOD_ID)));
                order.setFoodName(cursor.getString(cursor.getColumnIndex(Constants.DB_COLUMN_FOOD_NAME)));
                order.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.DB_COLUMN_FOOD_QUANTITY)));
                order.setPrice(cursor.getString(cursor.getColumnIndex(Constants.DB_COLUMN_FOOD_PRICE)));
                orderList.add(order);

            } while (cursor.moveToNext());
        }

        return orderList;
    }

    // Deleting 1 order from cart
    public void deleteOrderItem(List<Order> cartItems, int clickedItem) {
        SQLiteDatabase database = this.getWritableDatabase();
        Common.createLog(String.valueOf(cartItems.get(clickedItem).getKeyId()));
        database.delete(Constants.DB_TABLE_ORDER_DETAILS, Constants.DB_COLUMN_KEY_ID + " =?", new String[] {
                String.valueOf(cartItems.get(clickedItem).getKeyId())});
        database.close();
    }

    // Deleting all orders
    public void clearCart() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(Constants.DB_TABLE_ORDER_DETAILS,null,null);
        database.close();
    }

}
