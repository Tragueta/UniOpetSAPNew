package com.example.guilhermetragueta.uniopetsap20;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CadastrarUsuarioActivity extends Activity {

    private FirebaseAuth firebaseAuth;
    private EditText editEmail;
    private EditText editSenha;
    private Button btnVoltar;
    private Button btnCadastrarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        firebaseAuth = FirebaseAuth.getInstance();
        editEmail = findViewById(R.id.editNovoEmail);
        editSenha = findViewById(R.id.editNovaSenha);
        btnCadastrarUsuario = findViewById(R.id.btnCadastrar);
        btnVoltar = findViewById(R.id.btnVoltar);

        // Metodo responsavel por redirecionar o usuario para tela inicial
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivity(MainActivity.class);
            }
        });

        // Metodo usado para realizar o cadastro de um novo usuario
        btnCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String senha = editSenha.getText().toString();

                // Validando o campo e-mail e o campo senha
                if(email.isEmpty() || email.equals("")){
                    Toast.makeText(CadastrarUsuarioActivity.this,"Favor preencher o campo 'E-mail'.", Toast.LENGTH_SHORT).show();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(CadastrarUsuarioActivity.this,"O endereço de e-mail informado está incorreto." +
                            "Por gentileza, verifique e tente novamente.", Toast.LENGTH_SHORT).show();
                } else if(senha.isEmpty() || senha.equals("")){
                    Toast.makeText(CadastrarUsuarioActivity.this,"Favor preencher o campo 'Senha'.", Toast.LENGTH_SHORT).show();
                } else if (senha.length() < 6){
                    Toast.makeText(CadastrarUsuarioActivity.this,"A sua senha deve conter no minímo 6 digitos. " +
                                    "Por gentileza, verifique e tente novamente",
                            Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(email, senha)
                            .addOnCompleteListener(CadastrarUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        updateUI(user);
                                    } else {
                                        Log.w("ERRORCAD", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(CadastrarUsuarioActivity.this, "Falha na autenticação.", Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            });
                }
            }
        });
    }


    // Metodo resposavel por apresentar a nova tela
    private void callActivity(Class newActivity) {
        Intent newIntent = new Intent(CadastrarUsuarioActivity.this, newActivity);
        startActivity(newIntent);
        finish();
    }

    // Metodo responsavel por redirecionar o usuario para tela inicial
    public void voltarMain() {
        callActivity(MainActivity.class);
    }

    public void updateUI(FirebaseUser currentUser){
        if(currentUser != null)
            callActivity(Home.class);
        else
            callActivity(CadastrarUsuarioActivity.class);
    }
}
