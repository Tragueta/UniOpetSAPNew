package com.example.guilhermetragueta.uniopetsap20.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.guilhermetragueta.uniopetsap20.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class AlterarSenha extends Activity {

    private EditText editNovaSenha;
    private EditText editConfirmarSenha;
    private EditText editSenhaAtual;
    private Button btnSalvarSenha;
    private Button btnVoltarAlterarDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);

        editSenhaAtual = findViewById(R.id.editSenhaAtual);
        editNovaSenha = findViewById(R.id.editNovaSenha);
        editConfirmarSenha = findViewById(R.id.editConfirmarSenha);
        btnSalvarSenha = findViewById(R.id.btnSalvarSenha);
        btnVoltarAlterarDados = findViewById(R.id.btnVoltarAlterarDados);

        // Metodo usado para validar as senhas informadas para posteriormente realizar a alteracao
        btnSalvarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String senhaAtual = editSenhaAtual.getText().toString();
                String novaSenha = editNovaSenha.getText().toString();
                String confirmarSenha = editConfirmarSenha.getText().toString();

                if (validarSenhas(novaSenha, confirmarSenha)) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    AuthCredential credenciais = EmailAuthProvider.getCredential(user.getEmail(),senhaAtual);

                    user.reauthenticate(credenciais).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(!task.isSuccessful()){
                                try{
                                    throw  task.getException();
                                }catch (FirebaseAuthInvalidCredentialsException e){
                                    Toast.makeText(AlterarSenha.this, "Ops! Foi identificado um erro durante a validação de duas credencias." +
                                            "\n Por gentileza, verifique a senha atual informada e tente novamente.", Toast.LENGTH_LONG).show();
                                } catch(Exception e){
                                    e.printStackTrace();
                                }
                            } else {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String novaSenha = editNovaSenha.getText().toString();

                                if(novaSenha.isEmpty() || novaSenha.equals("")){
                                    editNovaSenha.setError("Favor preencher corretamente o campo referente a sua senha atual.");
                                    editNovaSenha.requestFocus();
                                }

                                user.updatePassword(novaSenha).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AlterarSenha.this, "Senha alterada com sucesso!",
                                                    Toast.LENGTH_SHORT).show();
                                            callActivity(Home.class);
                                        } else {
                                            Toast.makeText(AlterarSenha.this, "Ops! Houve um problema durante a atualização da senha." +
                                                            "\n Por gentileza, tente mais tarde.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        // Metodo que ira redirecionar o usuario para tela de educao dos dados.
        btnVoltarAlterarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recuperando os dados do usuario
                ArrayList<String> listDadosUsuarioLogado = receberDadosUsuario();

                Intent alterarDados = new Intent(AlterarSenha.this, AlterarPerfilUsuario.class);
                alterarDados.putStringArrayListExtra("listDadosAcesso", listDadosUsuarioLogado);
                startActivity(alterarDados);
                finish();
            }
        });

    }

    // Metodo usado para validar a troca de senha
    public boolean validarSenhas(String novaSenha, String confirmarSenha) {

        if(novaSenha.isEmpty() || novaSenha.equals("")){
            editNovaSenha.setError("Favor preencher o campo referente a nova senha.");
            editNovaSenha.requestFocus();
            return false;
        } else if(novaSenha.length() < 6){
            editNovaSenha.setError(" A sua senha deve conter no minímo 6 digitos.\n Por gentileza, verifique e tente novamente.");
            editNovaSenha.requestFocus();
            return false;
        }else if (confirmarSenha.isEmpty() || confirmarSenha.equals("")) {
            editConfirmarSenha.setError("Favor preencher o campo referente a confirmação da senha.");
            editConfirmarSenha.requestFocus();
            return false;
        }  else if (!confirmarSenha.equals(novaSenha)){
            editNovaSenha.requestFocus();
            editConfirmarSenha.requestFocus();
            Toast.makeText(AlterarSenha.this, "As senhas informadas não conferem. \n Por gentileza, verifique.",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    // Metodo resposavel por apresentar a nova tela
    private void callActivity(Class newActivity) {
        Intent newIntent = new Intent(AlterarSenha.this, newActivity);
        startActivity(newIntent);
        finish();
    }

    // Metodo utilizado para recuperar uma lista de string com os dados de acesso do usuario logado
    public ArrayList<String> receberDadosUsuario() {
        Intent intent = getIntent();
        ArrayList<String> listDadosAcesso = new ArrayList<>();
        return listDadosAcesso = intent.getStringArrayListExtra("listDadosAcesso");
    }

}
