package com.zohdy.maxburger.interfaces;

/**
 * Created by peterzohdy on 07/11/2017.
 */

public interface Constants {

    // Table Names in Firebase DB
    String FIREBASE_DB_TABLE_FOOD = "Food";
    String FIREBASE_DB_TABLE_USER = "User";
    String FIREBASE_DB_TABLE_CATEGORY = "Category";
    String FIREBASE_DB_TABLE_REQUESTS = "Requests";

    //Constants for identifying table and columns
    String DB_TABLE_ORDER_DETAILS = "orderdetails";
    String DB_COLUMN_KEY_ID = "_id";
    String DB_COLUMN_FOOD_ID = "food_id";
    String DB_COLUMN_FOOD_NAME = "food_name";
    String DB_COLUMN_FOOD_PRICE = "food_price";
    String DB_COLUMN_FOOD_QUANTITY = "quantity";
    String[] DB_ALL_COLUMNS = {
            Constants.DB_COLUMN_FOOD_ID,
            Constants.DB_COLUMN_FOOD_NAME,
            Constants.DB_COLUMN_FOOD_PRICE,
            Constants.DB_COLUMN_FOOD_QUANTITY
    };

    String NOTIFICATIONS_CHANNEL_ID = "channel_01";
    CharSequence NOTIFICATIONS_CHANNEL_NAME = "Channel 01";

    String CATEGORY_NAME = "category_name";
    String CATEGORY_DESCRIPTION = "category_description";
    String CATEGORY_ID = "category_id";
    String FOOD_ID = "food_id";
    String LOG = "com.zohyd.maxburger.log";
}
