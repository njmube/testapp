package com.app.luis.androidapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.luis.androidapp.R;
import com.app.luis.androidapp.helpers.DataValidator;
import com.app.luis.androidapp.helpers.Utils;
import com.app.luis.androidapp.utils.EnvironmentData;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import org.json.JSONException;
import org.json.JSONObject;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class NuevaCuenta extends AppCompatActivity {

    private final String[] generos = new String[]{"Hombre", "Mujer"};
    @Bind(R.id.toolbar_actionbar)
    Toolbar toolbar;
    @Bind(R.id.edit_text_nombre)
    EditText editTextNombre;
    @Bind(R.id.edit_text_apellido)
    EditText editTextApellido;
    @Bind(R.id.edit_text_genero)
    EditText editTextGenero;
    @Bind(R.id.edit_text_email)
    EditText editTextEmail;
    @Bind(R.id.edit_text_fecha_nacimiento)
    EditText editTextFechaNacimiento;
    @Bind(R.id.edit_text_password)
    EditText editTextPassword;
    @Bind(R.id.edit_text_confirm_password)
    EditText editTextConfirmPassword;
    private Thread mThread;
    private int genero = -1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_nueva_cuenta);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done_action:
                if (emptyFields()) {
                    new MaterialDialog.Builder(this)
                            .content("Espera un momento...")
                            .progress(true, 0)
                            .progressIndeterminateStyle(false)
                            .cancelable(false)
                            .widgetColorRes(R.color.primary)
                            .theme(Theme.LIGHT)
                            .showListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(final DialogInterface dialog) {
                                    final MaterialDialog materialDialog = (MaterialDialog) dialog;

                                    startThread(new Runnable() {

                                        @Override
                                        public void run() {

                                            Map<String, String> params = new HashMap<>();

                                            // the POST parameters:
                                            params.put("nombre", editTextNombre.getText().toString());
                                            params.put("apellido", editTextApellido.getText().toString());
                                            params.put("sexo", editTextGenero.getText().toString().charAt(0) + "");
                                            params.put("email", editTextEmail.getText().toString());
                                            params.put("fecha_nacimiento", editTextFechaNacimiento.getText().toString());
                                            params.put("password", editTextPassword.getText().toString());

                                            Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {

                                                    Log.d("RESPONSE_SERVER", response.toString());
                                                    String token = "";
                                                    try {
                                                        token = response.getString("token");
                                                        mThread.interrupt();
                                                        mThread = null;
                                                        materialDialog.cancel();
                                                        Toast.makeText(getApplicationContext(), "Token: \n" + token, Toast.LENGTH_LONG).show();
                                                        finish();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(getApplicationContext(), "Error: \n" + e.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            };

                                            Response.ErrorListener errorListener = new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    try {
                                                        int statusCode = error.networkResponse.statusCode;
                                                        String responseBody = new String(error.networkResponse.data, "utf-8");
                                                        JSONObject jsonObject = new JSONObject(responseBody);

                                                        String msg = "Error " + statusCode + ": \n";

                                                        JSONObject errors = jsonObject.getJSONObject("errors");

                                                        Iterator iterator   = errors.keys();
                                                        while(iterator.hasNext()) {
                                                            String campo = (String) iterator.next();
                                                            msg += errors.getJSONArray(campo).toString();
                                                        }

                                                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                                        Log.d("RESPONSE_SERVER", msg);

                                                        materialDialog.cancel();
                                                        mThread.interrupt();
                                                        mThread = null;

                                                    } catch (JSONException e) {
                                                        //Handle a malformed json response
                                                    } catch (UnsupportedEncodingException e) {

                                                    }
                                                }
                                            };

                                            JsonObjectRequest postRequest = new JsonObjectRequest(
                                                    Request.Method.POST,
                                                    EnvironmentData.BASE_URL + "usuarios",
                                                    new JSONObject(params),
                                                    jsonObjectListener,
                                                    errorListener) {
                                                @Override
                                                public Map<String, String> getHeaders() throws AuthFailureError {
                                                    HashMap<String, String> map = new HashMap<String, String>();
                                                    map.put("Accept", EnvironmentData.ACCEPT_HEADER);
                                                    map.put("Content-Type", EnvironmentData.CONTENT_TYPE);
                                                    return map;
                                                }
                                            };

                                            Volley.newRequestQueue(NuevaCuenta.this).add(postRequest);
                                        }
                                    });
                                }
                            })
                            .show();
                }
                break;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @OnEditorAction(R.id.edit_text_apellido)
    public boolean onEditorActionApellido(TextView v, int actionId, KeyEvent key) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            showGenreDialog();
        }
        return false;
    }

    @OnClick(R.id.edit_text_genero)
    public void showGenreDialog() {
        new MaterialDialog.Builder(this)
                .title("Género")
                .items(this.generos)
                .itemsCallbackSingleChoice(this.genero, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        genero = which;
                        editTextGenero.setText(text);
                        return true; // allow selection
                    }
                })
                .theme(Theme.LIGHT)
                .show();
    }

    @OnEditorAction(R.id.edit_text_email)
    public boolean onEditorAction(TextView v, int actionId, KeyEvent key) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            showDatePickerDialog();
        }
        return false;
    }

    @OnClick(R.id.edit_text_fecha_nacimiento)
    public void showDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String date = dayOfMonth + "/" + (++monthOfYear) + "/" + year;
                        editTextFechaNacimiento.setText(date);
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.vibrate(true);
        datePickerDialog.dismissOnPause(true);
        datePickerDialog.showYearPickerFirst(true);
        datePickerDialog.setMaxDate(Calendar.getInstance());
        datePickerDialog.setAccentColor(Color.parseColor(getResources().getString(R.color.primary)));
        datePickerDialog.setTitle(getResources().getString(R.string.HINT_FECHA_NACIMIENTO));
        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
    }


    /**
     * Validacion de campos no sean vacíos
     *
     * @return boolean
     */
    public boolean emptyFields() {
        if (!Utils.checkEditTextNotEmpty(this.editTextNombre)) {
            editTextNombre.setError(Utils.stringFromResource(getApplicationContext(), R.string.CAMPO_NO_PUEDE_SER_VACIO));
            return false;
        }

        if (!Utils.checkEditTextNotEmpty(this.editTextApellido)) {
            editTextApellido.setError(Utils.stringFromResource(getApplicationContext(), R.string.CAMPO_NO_PUEDE_SER_VACIO));
            return false;
        }

        if (Utils.checkEditTextNotEmpty(this.editTextEmail)) {
            if (!DataValidator.isValidEmail(this.editTextEmail.getText().toString())) {
                editTextEmail.setError(Utils.stringFromResource(getApplicationContext(), R.string.INPUT_ERROR_CORREO_INVALIDO));
                return false;
            }
        } else {
            editTextEmail.setError(Utils.stringFromResource(getApplicationContext(), R.string.CAMPO_NO_PUEDE_SER_VACIO));
            return false;
        }

        if (!Utils.checkEditTextNotEmpty(this.editTextFechaNacimiento)) {
            editTextFechaNacimiento.setError(Utils.stringFromResource(getApplicationContext(), R.string.CAMPO_NO_PUEDE_SER_VACIO));
            return false;
        }

        if (Utils.checkEditTextNotEmpty(this.editTextPassword)) {
            if (!(editTextPassword.getText().toString().length() > 5 && editTextPassword.getText().toString().length() < 40)) {
                editTextPassword.setError("Esribe mínimo 5 y máximo 40 letras");
                return false;
            }
        } else {
            editTextPassword.setError(Utils.stringFromResource(getApplicationContext(), R.string.CAMPO_NO_PUEDE_SER_VACIO));
            return false;
        }

        if (Utils.checkEditTextNotEmpty(this.editTextConfirmPassword)) {
            if (!editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
                editTextConfirmPassword.setError("Debes escribir la misma contraseña");
                return false;
            }

        } else {
            editTextConfirmPassword.setError(Utils.stringFromResource(getApplicationContext(), R.string.CAMPO_NO_PUEDE_SER_VACIO));
            return false;
        }
        return true;
    }

    private void startThread(Runnable run) {
        if (mThread != null)
            mThread.interrupt();

        mThread = new Thread(run);
        mThread.start();
    }
}
