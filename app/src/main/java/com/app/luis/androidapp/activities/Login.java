package com.app.luis.androidapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.luis.androidapp.R;
import com.app.luis.androidapp.api.factory.FactoryResponse;
import com.app.luis.androidapp.api.models.ErrorResponse;
import com.app.luis.androidapp.enums.UsuarioEnum;
import com.app.luis.androidapp.helpers.DataValidator;
import com.app.luis.androidapp.helpers.Utils;
import com.app.luis.androidapp.models.Usuario;
import com.app.luis.androidapp.utils.AppConstants;
import com.app.luis.androidapp.utils.Environment;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.Transition;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class Login extends AppCompatActivity implements KenBurnsView.TransitionListener {

    private static final int NUEVA_CUENTA = 0;
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
        if (requestCode == NUEVA_CUENTA) {
            if (resultCode == RESULT_OK) {
                Intent i = new Intent(getApplicationContext(), Home.class);
                startActivity(i);
                finish();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
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

        //Toast.makeText(getApplicationContext(), "Email: " + email + "\nPassword: " + password, Toast.LENGTH_LONG).show();

        Intent intentHome = new Intent(this, Home.class);
        startActivity(intentHome);
        finish();
    }

    @OnClick(R.id.login_button)
    public void loginFB() {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Espera un momento...", true);
        // Login con Facebook
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile, email, user_birthday, user_friends"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            private ProfileTracker profileTracker;

            @Override
            public void onSuccess(final LoginResult loginResult) {
                progressDialog.setMessage("Obteniendo usuario...");
                if (Profile.getCurrentProfile() == null) {
                    profileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            this.stopTracking();
                        }
                    };
                    profileTracker.startTracking();
                }

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                progressDialog.setMessage("Guardando usuario...");

                                Log.d("CUMPLE_PUTO", object.toString());

                                Map<String, String> params = new HashMap<>();

                                try {
                                    // the POST parameters:
                                    params.put("id_facebook", object.getString("id"));
                                    params.put("nombre", object.getString("first_name"));
                                    params.put("apellido", object.getString("last_name"));
                                    params.put("sexo", (object.getString("gender").equals("female")) ? "M" : "H");
                                    params.put("email", object.getString("email"));
                                    params.put("fecha_nacimiento", object.getString("birthday"));
                                    params.put("password", object.getString("id"));

                                } catch (JSONException e) {

                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Error: \n" + e.getMessage(), Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }

                                Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {

                                            Usuario usuario = new Gson().fromJson(response.getJSONObject("data").toString(), Usuario.class);
                                            storeSharedPref(usuario);
                                            Toast.makeText(getApplicationContext(), "Token: \n" + usuario.getToken(), Toast.LENGTH_LONG).show();

                                            Intent i = new Intent(getApplicationContext(), Home.class);
                                            startActivity(i);
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
                                            String responseBody = new String(error.networkResponse.data, "utf-8");
                                            ErrorResponse responseClass = new FactoryResponse().createHttpResponse(error.networkResponse.statusCode);
                                            ErrorResponse errorResponse = new Gson().fromJson(responseBody, responseClass.getClass());

                                            LoginManager.getInstance().logOut();

                                            Toast.makeText(getApplicationContext(), errorResponse.getUserMessage(), Toast.LENGTH_LONG).show();

                                        } catch (UnsupportedEncodingException | NullPointerException e) {

                                            e.printStackTrace();
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                        }
                                    }
                                };

                                JsonObjectRequest postRequest = new JsonObjectRequest(
                                        Request.Method.POST,
                                        Environment.getInstance(getApplicationContext()).getBASE_URL() + "registro/fb",
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

                                Volley.newRequestQueue(Login.this).add(postRequest);
                            }
                        }
                );

                Bundle bundle = new Bundle();
                bundle.putString("fields", "id, first_name, last_name, birthday, email, gender");
                request.setParameters(bundle);
                request.executeAsync();

//                Toast.makeText(Login.this, "User ID: "
//                        + loginResult.getAccessToken().getUserId()
//                        + "\n" +
//                        "Auth Token: "
//                        + loginResult.getAccessToken().getToken()
//                        + "\n" +
//                        "Permissions: "
//                        + loginResult.getAccessToken().getPermissions(), Toast.LENGTH_LONG).show();
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

    @OnClick(R.id.textView_nueva_cuenta)
    public void nuevaCuenta() {
        Intent intentNuevaCuenta = new Intent(this, NuevaCuenta.class);
        startActivityForResult(intentNuevaCuenta, NUEVA_CUENTA);
        //startActivity(intentNuevaCuenta);
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

    public void storeSharedPref(Usuario usuario) {
        SharedPreferences preferences = getSharedPreferences(AppConstants.USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(UsuarioEnum.TOKEN.getValue(), usuario.getToken());
        editor.putString(UsuarioEnum.ID.getValue(), usuario.getId());
        editor.putString(UsuarioEnum.ID_FACEBOOK.getValue(), usuario.getId_facebook());
        editor.putString(UsuarioEnum.NOMBRE.getValue(), usuario.getNombre());
        editor.putString(UsuarioEnum.APELLIDO.getValue(), usuario.getApellido());
        editor.putString(UsuarioEnum.FECHA_NACIMIENTO.getValue(), usuario.getFecha_nacimiento().toString());
        editor.putString(UsuarioEnum.EMAIL.getValue(), usuario.getEmail());
        editor.putString(UsuarioEnum.SEXO.getValue(), usuario.getSexo() + "");
        editor.commit();
    }

}
