package com.app.luis.androidapp.utils;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
 * Created by Luis Macias on 22/02/2016.
 */
public class Utils {
    public static int getColor(Context context, int recurso_color) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, recurso_color);
        } else {
            return context.getResources().getColor(recurso_color);
        }
    }
}
