package com.app.luis.androidapp.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.app.luis.androidapp.R;
import com.app.luis.androidapp.helpers.DataValidator;
import com.app.luis.androidapp.helpers.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

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
    private int genero = -1;

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
                    Toast.makeText(getApplicationContext(), "Envia los datos", Toast.LENGTH_LONG).show();
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
}
