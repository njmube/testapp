package com.app.luis.androidapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.app.luis.androidapp.R;
import com.app.luis.androidapp.helpers.DataValidator;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.Transition;

public class Login extends AppCompatActivity implements KenBurnsView.TransitionListener {

    private static final int TRANSITIONS_TO_SWITCH = 3;
    @Bind(R.id.viewSwitcher)
    ViewSwitcher mViewSwitcher;
    @Bind(R.id.bg_login)
    KenBurnsView bgLogin;
    @Bind(R.id.bg_login2)
    KenBurnsView bgLogin2;
    @Bind(R.id.editText_email)
    EditText editTextEmail;
    @Bind(R.id.editText_password)
    EditText editTextPassword;
    @Bind(R.id.button_entrar)
    Button botonEntrar;
    @Bind(R.id.textView_nueva_cuenta)
    TextView textViewNuevaCuenta;
    @Bind(R.id.textView_olvida_password)
    TextView textViewOlvidaPassword;
    @Bind(R.id.login_button)
    LoginButton loginButton;
    private CallbackManager callbackManager;
    private int mTransitionsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.layout_login);
        ButterKnife.bind(this);

        // KenBurnsView de fondo
        bgLogin.setTransitionListener(this);
        bgLogin2.setTransitionListener(this);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
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

    @OnClick(R.id.button_entrar)
    public void entrarLogin() {
        String email = this.editTextEmail.getText().toString();
        String password = this.editTextPassword.getText().toString();

        Toast.makeText(getApplicationContext(), "Email: " + email + "\nPassword: " + password, Toast.LENGTH_LONG).show();

        Intent intentHome = new Intent(this, Home.class);
        startActivity(intentHome);
    }

    @OnClick(R.id.textView_nueva_cuenta)
    public void nuevaCuenta() {
        Intent intentNuevaCuenta = new Intent(this, NuevaCuenta.class);
        startActivity(intentNuevaCuenta);
    }

    @OnClick(R.id.textView_olvida_password)
    public void showDialogOlvidaPassword() {
        new MaterialDialog.Builder(this)
                .title(R.string.DIALOG_TITULO_RESTABLECE_PASSWORD)
                .content("Escribe tu correo electrónico para enviarte un e-mail de recuperación.")
                .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .positiveText(R.string.BUTTON_RESTABLECER)
                .positiveColorRes(R.color.primary)
                .alwaysCallInputCallback() // this forces the callback to be invoked with every input change
                .input(R.string.HINT_EMAIL, 0, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (emptyFieldDialog(input.toString())) {
                            dialog.setContent(R.string.INPUT_ERROR_CORREO_INVALIDO);
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                        } else {
                            dialog.setContent("Escribe tu correo electrónico para enviarte un e-mail de recuperación.");
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                        }
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Toast.makeText(getApplicationContext(), "Entro "+dialog.getInputEditText().getText().toString(), Toast.LENGTH_LONG).show();
                    }
                })
                .theme(Theme.LIGHT)
                .show();
    }


//    public boolean emptyFieldDialog() {
//        if (Utils.checkEditTextNotEmpty(this.editTextEmailRestablece)) {
//            if (!DataValidator.isValidEmail(this.editTextEmailRestablece.getText().toString())) {
//                this.editTextEmailRestablece.setError(Utils.stringFromResource(getApplicationContext(), R.string.INPUT_ERROR_CORREO_INVALIDO));
//                return false;
//            }
//        } else {
//            this.editTextEmailRestablece.setError(Utils.stringFromResource(Login.this, R.string.CAMPO_NO_PUEDE_SER_VACIO));
//            return false;
//        }
//        return true;
//    }

    public boolean emptyFieldDialog(String string) {
        return string.isEmpty() || !DataValidator.isValidEmail(string);
    }
}
