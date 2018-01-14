package com.zohdy.maxburger.common;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.zohdy.maxburger.interfaces.Constants;
import com.zohdy.maxburger.models.User;

/**
 * Created by peterzohdy on 06/11/2017.
 */

public class Common {

    // TODO Refactor to singleton or application class?!
    // Static variable to hold the current user
    public static User currentUser;


    public static void createToast(Context context, String textInput) {
        Toast.makeText(context, textInput, Toast.LENGTH_SHORT).show();
    }

    public static void createLog(String message) {
        Log.i(Constants.LOG, message);
    }
}
