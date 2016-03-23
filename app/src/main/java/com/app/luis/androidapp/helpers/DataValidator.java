package com.app.luis.androidapp.helpers;

import android.util.Patterns;

/**
 * Created by Luis on 13/10/2015.
 */
public class DataValidator {

    public static boolean isValidEmail(String param) {
        return (param != null) && (Patterns.EMAIL_ADDRESS.matcher(param).matches());
    }
}
