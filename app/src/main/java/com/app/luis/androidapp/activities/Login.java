package com.app.luis.androidapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.app.luis.androidapp.R;
import com.app.luis.androidapp.helpers.DataValidator;
import com.app.luis.androidapp.helpers.Utils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.Transition;

public class Login extends AppCompatActivity implements KenBurnsView.TransitionListener, View.OnClickListener {

    private EditText editTextEmail, editTextPassword, editTextEmailRestablece;
    private CallbackManager callbackManager;
    private AlertDialog alertDialog;
    private ViewSwitcher mViewSwitcher;

    private static final int TRANSITIONS_TO_SWITCH = 3;
    private int mTransitionsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.layout_login);

        editTextEmail = (EditText) findViewById(R.id.editText_email);
        editTextPassword = (EditText) findViewById(R.id.editText_password);

        final Button buttonEntrar = (Button) findViewById(R.id.button_entrar);
        final TextView textViewNuevaCuenta = (TextView) findViewById(R.id.textView_nueva_cuenta);
        final TextView textViewOlvidaPassword = (TextView) findViewById(R.id.textView_olvida_password);
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        // KenBurnsView de fondo
        mViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        KenBurnsView bgLogin = (KenBurnsView) findViewById(R.id.bg_login);
        KenBurnsView bgLogin2 = (KenBurnsView) findViewById(R.id.bg_login2);
        bgLogin.setTransitionListener(this);
        bgLogin2.setTransitionListener(this);


        // Botones
        buttonEntrar.setOnClickListener(this);
        textViewNuevaCuenta.setOnClickListener(this);
        textViewOlvidaPassword.setOnClickListener(this);

        // Login con Facebook
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(Login.this, "User ID: "
                        + loginResult.getAccessToken().getUserId()
                        + "\n" +
                        "Auth Token: "
                        + loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(Login.this, "Cancelado", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Login.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // KenBurnsView de fondo
    @Override
    public void onTransitionStart(Transition transition) {

    }

    // KenBurnsView de fondo
    @Override
    public void onTransitionEnd(Transition transition) {
        mTransitionsCount++;
        if (mTransitionsCount == TRANSITIONS_TO_SWITCH) {
            mViewSwitcher.showNext();
            mTransitionsCount = 0;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_entrar:
                String email = this.editTextEmail.getText().toString();
                String password = this.editTextPassword.getText().toString();
                Toast.makeText(getApplicationContext(), "Email: " + email + "\nPassword: " + password, Toast.LENGTH_LONG).show();
                break;
            case R.id.textView_nueva_cuenta:

                Intent intent = new Intent(this, NuevaCuenta.class);
                startActivity(intent);

                break;
            case R.id.textView_olvida_password:

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final View dialogView = getLayoutInflater().inflate(R.layout.dialog_reset_password, null);
                builder.setView(dialogView);

                editTextEmailRestablece = (EditText) dialogView.findViewById(R.id.dialog_edit_text_recover_email);
                TextView textViewCancelar = (TextView) dialogView.findViewById(R.id.button_dialog_cancel);
                TextView textViewRestablecer = (TextView) dialogView.findViewById(R.id.button_dialog_confirm);

                textViewCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                textViewRestablecer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (emptyFieldDialog()) {
                            Toast.makeText(getApplicationContext(), "Envía correo", Toast.LENGTH_LONG).show();
                            alertDialog.dismiss();
                        }
                    }
                });

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    public boolean emptyFieldDialog() {
        if (Utils.checkEditTextNotEmpty(this.editTextEmailRestablece)) {
            if (!DataValidator.isValidEmail(this.editTextEmailRestablece.getText().toString())) {
                this.editTextEmailRestablece.setError(Utils.stringFromResource(getApplicationContext(), R.string.INPUT_ERROR_CORREO_INVALIDO));
                return false;
            }
        } else {
            this.editTextEmailRestablece.setError(Utils.stringFromResource(Login.this, R.string.CAMPO_NO_PUEDE_SER_VACIO));
            return false;
        }
        return true;
    }
}
