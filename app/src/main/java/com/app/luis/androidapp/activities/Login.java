package com.app.luis.androidapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail, editTextPassword, dialogEditTextRecoverEmail;
    private Button buttonEntrar;
    private CallbackManager callbackManager;

    private TextView textViewNuevaCuenta, textViewOlvidaPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.layout_login);

        editTextEmail = (EditText) findViewById(R.id.editText_email);
        dialogEditTextRecoverEmail = (EditText) findViewById(R.id.dialog_edit_text_recover_email);
        editTextPassword = (EditText) findViewById(R.id.editText_password);
        buttonEntrar = (Button) findViewById(R.id.button_entrar);
        textViewNuevaCuenta = (TextView) findViewById(R.id.textView_nueva_cuenta);
        textViewOlvidaPassword = (TextView) findViewById(R.id.textView_olvida_password);
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        // KenBurnsView de fondo
        KenBurnsView kenBurnsView = (KenBurnsView) findViewById(R.id.backgroud);
        kenBurnsView.setTransitionListener(new KenBurnsView.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {

            }
        });

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

                new MaterialDialog.Builder(this)
                        .title("Restablecer contraseña")
                        .customView(R.layout.dialog_reset_password, false)
                        .positiveText("Restablecer")
                        .negativeText("Cancelar")
                        .titleColorRes(R.color.primary)
                        .positiveColorRes(R.color.primary)
                        .negativeColorRes(R.color.primary)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {

                            }
                        })
                        .show();

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

    public boolean emptyField() {
        if (Utils.checkEditTextNotEmpty(dialogEditTextRecoverEmail)) {
            if (!DataValidator.isValidEmail(dialogEditTextRecoverEmail.getText().toString())) {
                dialogEditTextRecoverEmail.setError("Correo inválido");
                return false;
            }
        } else {
            dialogEditTextRecoverEmail.setError(Utils.stringFromResource(Login.this, R.string.CAMPO_NO_PUEDE_SER_VACIO));
            return false;
        }
        return true;
    }
}
