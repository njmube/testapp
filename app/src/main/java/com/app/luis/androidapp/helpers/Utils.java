package com.app.luis.androidapp.helpers;

import android.content.Context;
import android.widget.EditText;

/**
 * Created by Luis on 13/10/2015.
 */
public class Utils {

    /**
     * Validar que los EditText no sean vacios
     * @param editText
     * @return
     */
    public static boolean checkEditTextNotEmpty(EditText editText) {
        return (editText != null) && (editText.getText() != null) && (!editText.getText().toString().isEmpty());
    }

    /**
     * Retorna un string del XML segun su id
     * @param context
     * @param id
     * @return
     */
    public static String stringFromResource(Context context, int id) {
        if ((context != null) && (context.getResources() != null)) {
            return  context.getString(id);
        }
        return "";
    }
}
