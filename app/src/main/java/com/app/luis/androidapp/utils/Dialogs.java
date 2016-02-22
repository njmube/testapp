package com.app.luis.androidapp.utils;

import android.content.Context;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by Luis Macias on 22/02/2016.
 */
public class Dialogs {

    public static MaterialDialog dialogBasic(Context context, String content, String positiveText) {
        return new MaterialDialog.Builder(context)
                .content(content)
                .positiveText(positiveText).show();
    }
}
