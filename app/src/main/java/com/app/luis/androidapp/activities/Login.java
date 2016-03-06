package com.app.luis.androidapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.app.luis.androidapp.R;
import com.app.luis.androidapp.enums.UsuarioEnum;
import com.app.luis.androidapp.helpers.DataValidator;
import com.app.luis.androidapp.helpers.Utils;
import com.app.luis.androidapp.models.Usuario;
import com.app.luis.androidapp.utils.AppConstants;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.Transition;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

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
    Button button_entrar;
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

        SharedPreferences preferences = getSharedPreferences(AppConstants.USER_PREFERENCES, Context.MODE_PRIVATE);
        String token = preferences.getString(UsuarioEnum.TOKEN.getValue(), "");

        if (!TextUtils.isEmpty(token)) {
            Intent i = new Intent(getApplicationContext(), Home.class);
            i.putExtra(UsuarioEnum.TOKEN.getValue(), token);
            startActivity(i);
            finish();
        }

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
                .titleColorRes(R.color.primary)
                .content("Escribe tu dirección de email para enviarte un mensaje de recuperación.")
                .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .positiveText(R.string.BUTTON_RESTABLECER)
                .negativeText(R.string.BUTTON_CANCELAR)
                .positiveColorRes(R.color.primary)
                .negativeColorRes(R.color.primary)
                .widgetColorRes(R.color.primary)
                .input(R.string.HINT_EMAIL, 0, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {

                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (dialog.getInputEditText().getText().toString().isEmpty() || !DataValidator.isValidEmail(dialog.getInputEditText().getText().toString())) {
                            Toast.makeText(getApplicationContext(), "Escribe un correo válido", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Envío a " + dialog.getInputEditText().getText().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .theme(Theme.LIGHT)
                .show();
    }

    @OnTextChanged(R.id.editText_email)
    public void onEditorActionEmail() {
        button_entrar.setEnabled(enableLoginButton());
    }

    @OnTextChanged(R.id.editText_password)
    public void onEditorActionPassword() {
        button_entrar.setEnabled(enableLoginButton());
    }

    public boolean enableLoginButton() {
        if (!Utils.checkEditTextNotEmpty(this.editTextPassword)) {
            return false;
        }

        if (Utils.checkEditTextNotEmpty(this.editTextEmail)) {
            if (!DataValidator.isValidEmail(this.editTextEmail.getText().toString())) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

}
