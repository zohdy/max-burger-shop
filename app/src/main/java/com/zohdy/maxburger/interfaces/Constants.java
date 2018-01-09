package com.zohdy.maxburger.interfaces;

/**
 * Created by peterzohdy on 07/11/2017.
 */

public interface Constants {


    // Firebase DB Related
    String FIREBASE_DB_TABLE_FOOD = "Food";
    String FIREBASE_DB_TABLE_USER = "User";
    String FIREBASE_DB_TABLE_CATEGORY = "Category";
    String FIREBASE_DB_TABLE_ORDER_REQUESTS = "OrderRequest";
    String FIREBASE_DB_TABLE_ORDER_HISTORY = "OrderHistory";

    String FIREBASE_DB_DATA_PHONENUMBER = "phoneNumber";


    // SQLite Related
    String SQLITE_DB_NAME = "maxburger.db";
    String DB_TABLE_ORDER_DETAILS = "orderdetails";
    String DB_COLUMN_KEY_ID = "_id";
    String DB_COLUMN_FOOD_ID = "food_id";
    String DB_COLUMN_FOOD_NAME = "food_name";
    String DB_COLUMN_FOOD_PRICE = "food_price";
    String DB_COLUMN_FOOD_QUANTITY = "quantity";


    // Misc
    String ORDER_REQUEST_OBJECT = "order_request_object";
    String CATEGORY_NAME = "category_name";
    String CATEGORY_DESCRIPTION = "category_description";
    String CATEGORY_ID = "category_id";
    String FOOD_ID = "food_id";
    String LOG = "com.zohyd.maxburger.log";
    String ORDER_READY_ID = "order_ready_id";
}
