package com.app.luis.androidapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.luis.androidapp.R;
import com.app.luis.androidapp.api.factory.FactoryResponse;
import com.app.luis.androidapp.api.models.ErrorResponse;
import com.app.luis.androidapp.helpers.DataValidator;
import com.app.luis.androidapp.helpers.Utils;
import com.app.luis.androidapp.models.PerfilActivo;
import com.app.luis.androidapp.models.Usuario;
import com.app.luis.androidapp.utils.Environment;
import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import org.json.JSONException;
import org.json.JSONObject;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NuevaCuenta extends AppCompatActivity {

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
    private final String[] generos = new String[]{"Hombre", "Mujer"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_nueva_cuenta);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                                                    try {
                                                        Usuario usuario = new Gson().fromJson(response.getJSONObject("data").toString(), Usuario.class);
                                                        PerfilActivo.getInstance().setUsuario(usuario);
                                                        PerfilActivo.getInstance().updateInfo(getApplicationContext());
                                                        setResult(RESULT_OK);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(getApplicationContext(), "Error: \n" + e.getMessage(), Toast.LENGTH_LONG).show();
                                                        setResult(RESULT_CANCELED);
                                                    } finally {
                                                        materialDialog.cancel();
                                                        mThread.interrupt();
                                                        mThread = null;
                                                        finish();
                                                    }
                                                }
                                            };

                                            Response.ErrorListener errorListener = new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    try {
                                                        String responseBody = new String(error.networkResponse.data, "utf-8");
                                                        ErrorResponse responseClass = new FactoryResponse().createHttpResponse(error.networkResponse.statusCode);
                                                        ErrorResponse errorResponse = new Gson().fromJson(responseBody, responseClass.getClass());

                                                        Toast.makeText(getApplicationContext(), errorResponse.getUserMessage(), Toast.LENGTH_LONG).show();
                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                                    } finally {
                                                        materialDialog.cancel();
                                                        mThread.interrupt();
                                                        mThread = null;
                                                    }
                                                }
                                            };

                                            JsonObjectRequest postRequest = new JsonObjectRequest(
                                                    Request.Method.POST,
                                                    Environment.getInstance(getApplicationContext()).getBASE_URL() + "registro",
                                                    new JSONObject(params),
                                                    jsonObjectListener,
                                                    errorListener) {
                                                @Override
                                                public Map<String, String> getHeaders() throws AuthFailureError {
                                                    HashMap<String, String> map = new HashMap<String, String>();
                                                    map.put("Accept", Environment.ACCEPT_HEADER);
                                                    map.put("Content-Type", Environment.CONTENT_TYPE);
                                                    return map;
                                                }
                                            };

                                            postRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                    15000,
                                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                            Volley.newRequestQueue(NuevaCuenta.this).add(postRequest);
                                        }
                                    });
                                }
                            }).show();
                }
                break;

            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                setResult(RESULT_CANCELED);
                finish();
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
        datePickerDialog.setAccentColor(com.app.luis.androidapp.utils.Utils.getColor(getApplicationContext(), R.color.primary));
        datePickerDialog.setTitle(getResources().getString(R.string.HINT_FECHA_NACIMIENTO));
        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
    }


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
