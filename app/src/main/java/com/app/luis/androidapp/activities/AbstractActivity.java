package com.app.luis.androidapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.app.luis.androidapp.R;
import com.app.luis.androidapp.utils.Utils;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Luis Macias on 30/03/2016.
 */
abstract class AbstractActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Montserrat-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        if (!Utils.isConnect(AbstractActivity.this)) {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.TEXT_NO_CONEXON_INTERNET), Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(com.app.luis.androidapp.utils.Utils.getColor(AbstractActivity.this, R.color.accent))
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).show();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
