package com.app.luis.androidapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.Transition;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        email = (EditText) findViewById(R.id.editText_email);
        password = (EditText) findViewById(R.id.editText_password);

        entrar = (Button) findViewById(R.id.button_entrar);

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

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String semail = email.getText().toString();
                String spassword = password.getText().toString();

                Toast.makeText(LoginActivity.this, "Email: "+semail+"\nPassword: "+spassword, Toast.LENGTH_LONG).show();
            }
        });
    }
}
