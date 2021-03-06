package com.example.guilhermetragueta.uniopetsap20.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guilhermetragueta.uniopetsap20.R;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.CallbackManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends Activity {

    private FirebaseAuth firebaseAuth;
    private EditText editEmail;
    private EditText editSenha;
    private TextView txtNovoCadastro;
    private Button btnEntrar;
    private CallbackManager callbackManager;
    private LoginButton btnFacebookLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        txtNovoCadastro = findViewById(R.id.txtNovoCadastro);
        btnEntrar = findViewById(R.id.btnEntrar);

        // Metodo responsavel por redirecionar o usuario para tela de 'Cadastrar Novo Usuario'
        txtNovoCadastro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                callActivity(CadastrarUsuarioActivity.class);
            }
        });

        callbackManager = CallbackManager.Factory.create();
        btnFacebookLogin = findViewById(R.id.btnFacebookLogin);

        // Metodo usado para realizar o login no app com o Facebook
        btnFacebookLogin.setReadPermissions("email");

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(MainActivity.this, "Seja bem vindo!",
                                Toast.LENGTH_SHORT).show();
                        callActivity(HomeFB.class);
                    }

                    @Override
                    public void onCancel() {
                          Toast.makeText(MainActivity.this, "Ops! A sua solicitação foi cancelada. Tente novamente.",
                                  Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(MainActivity.this, "Ops! Ocorreu um erro durante a autenticação. Por gentileza, verifique",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        // Metodo responsavel por realizar o login no app.
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String novoEmail = editEmail.getText().toString();
                String novaSenha = editSenha.getText().toString();

                // Validando o campo e-mail e o campo senha
                if(novoEmail.isEmpty() || novoEmail.equals("")){
                    Toast.makeText(MainActivity.this,"Favor preencher o campo 'E-mail'.", Toast.LENGTH_SHORT).show();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(novoEmail).matches()){
                    Toast.makeText(MainActivity.this,"O endereço de e-mail informado está incorreto." +
                            "Por gentileza, verifique e tente novamente.", Toast.LENGTH_SHORT).show();
                } else if(novaSenha.isEmpty() || novaSenha.equals("")){
                    Toast.makeText(MainActivity.this,"Favor preencher o campo 'Senha'.", Toast.LENGTH_SHORT).show();
                } else if (novaSenha.length() < 6){
                    Toast.makeText(MainActivity.this,"A sua senha deve conter no minímo 6 digitos. " +
                                    "Por gentileza, verifique e tente novamente",
                            Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.signInWithEmailAndPassword(novoEmail, novaSenha)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {
                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            updateUI(user);
                                            Toast.makeText(MainActivity.this, "Seja bem vindo!",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(MainActivity.this, "Falha na autenticação.",
                                                    Toast.LENGTH_SHORT).show();
                                            updateUI(null);
                                        }
                                }
                            });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void updateUI(FirebaseUser currentUser){
        if(currentUser != null){
            callActivity(Home.class);
        }
    }

    // Metodo usado para trocar de telas
    private void callActivity(Class newActivity) {
        Intent newIntent = new Intent(MainActivity.this,newActivity);
        startActivity(newIntent);
        finish();
    }
}
