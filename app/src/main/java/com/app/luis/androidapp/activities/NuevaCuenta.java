package com.app.luis.androidapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.app.luis.androidapp.R;
import com.app.luis.androidapp.helpers.DataValidator;
import com.app.luis.androidapp.helpers.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NuevaCuenta extends AppCompatActivity {

    @Bind(R.id.toolbar_actionbar)
    Toolbar toolbar;
    @Bind(R.id.edit_text_nombre)
    EditText editTextNombre;
    @Bind(R.id.edit_text_apellido)
    EditText editTextApellido;
    @Bind(R.id.edit_text_email)
    EditText editTextEmail;
    @Bind(R.id.edit_text_password)
    EditText editTextPassword;
    @Bind(R.id.edit_text_confirm_password)
    EditText editTextConfirmPassword;

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
                if(emptyFields()) {
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
        }else {
            editTextEmail.setError(Utils.stringFromResource(getApplicationContext(), R.string.CAMPO_NO_PUEDE_SER_VACIO));
            return false;
        }

        if (Utils.checkEditTextNotEmpty(this.editTextPassword)) {
            if(!(editTextPassword.getText().toString().length() > 5 && editTextPassword.getText().toString().length() < 40)) {
                editTextPassword.setError("Esribe mínimo 5 y máximo 40 letras");
                return false;
            }
        } else {
            editTextPassword.setError(Utils.stringFromResource(getApplicationContext(), R.string.CAMPO_NO_PUEDE_SER_VACIO));
            return false;
        }

        if (Utils.checkEditTextNotEmpty(this.editTextConfirmPassword)) {
            if(!editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
                editTextConfirmPassword.setError("Debes escribir la misma contraseña");
                return false;
            }

        }else {
            editTextConfirmPassword.setError(Utils.stringFromResource(getApplicationContext(), R.string.CAMPO_NO_PUEDE_SER_VACIO));
            return false;
        }
        return true;
    }
}
