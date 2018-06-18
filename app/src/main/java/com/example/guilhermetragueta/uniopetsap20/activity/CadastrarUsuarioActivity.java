package com.example.guilhermetragueta.uniopetsap20.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.guilhermetragueta.uniopetsap20.R;
import com.example.guilhermetragueta.uniopetsap20.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CadastrarUsuarioActivity extends Activity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private EditText novoNome;
    private EditText novoSobrenome;
    private EditText editEmail;
    private EditText editSenha;
    private Button btnVoltar;
    private Button btnCadastrarUsuario;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Usuario");

        novoNome = findViewById(R.id.editNovoNome);
        novoSobrenome = findViewById(R.id.editNovoSobrenome);
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
                String nome = novoNome.getText().toString();
                String sobrenome = novoSobrenome.getText().toString();
                String email = editEmail.getText().toString();
                String senha = editSenha.getText().toString();

                // Validando os campos do cadastro de usuario
                if(nome.isEmpty() || nome.equals("")){
                    novoNome.setError("Favor preencher o campo 'Nome'.");
                    novoNome.requestFocus();
                } else if(sobrenome.isEmpty() || sobrenome.equals("")){
                    novoSobrenome.setError("Favor preencher o campo 'Sobrenome'.");
                    novoSobrenome.requestFocus();
                } else if(email.isEmpty() || email.equals("")){
                    editEmail.setError("Favor preencher o campo 'E-mail'.");
                    editEmail.requestFocus();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editEmail.setError(" O endereço de e-mail informado está incorreto.\n Por gentileza, verifique e tente novamente.");
                    editEmail.requestFocus();
                } else if(senha.isEmpty() || senha.equals("")){
                    editSenha.setError("Favor preencher o campo 'Senha'.");
                    editSenha.requestFocus();
                } else if (senha.length() < 6){
                    editSenha.setError(" A sua senha deve conter no minímo 6 digitos.\n Por gentileza, verifique e tente novamente.");
                    editSenha.requestFocus();
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(email, senha)
                            .addOnCompleteListener(CadastrarUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthUserCollisionException e) {
                                            editEmail.setError("O e-mail informado já está sendo utilizado. Por gentileza, verifique.");
                                            editEmail.requestFocus();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        updateUI(user);

                                        String idUsuario = UUID.randomUUID().toString();

                                        Usuario novoUsuario = new Usuario();
                                        novoUsuario.setId(idUsuario);
                                        novoUsuario.setNome(novoNome.getText().toString());
                                        novoUsuario.setSobrenome(novoSobrenome.getText().toString());
                                        novoUsuario.setidAcesso(user.getUid().toString());
                                        novoUsuario.setEmailAcesso(user.getEmail().toString());
                                        novoUsuario.setImagemPerfil("");

                                        mDatabase.child(novoUsuario.getidAcesso()).setValue(novoUsuario);
                                        Toast.makeText(CadastrarUsuarioActivity.this,"Cadastro realizado com sucesso!",Toast.LENGTH_SHORT).show();

                                        callActivity(Home.class);
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
