package com.example.guilhermetragueta.uniopetsap20.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.guilhermetragueta.uniopetsap20.Class.Usuario;
import com.example.guilhermetragueta.uniopetsap20.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class AlterarPerfilUsuario extends Activity {

    private EditText nomeAlterado;
    private EditText sobrenomeAlterado;
    private EditText emailAlterado;
    private Button btnSalvarDados;
    private Button btnVoltarPerfilUsuario;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_perfil_usuario);

        nomeAlterado = findViewById(R.id.nomeAlterado);
        sobrenomeAlterado = findViewById(R.id.sobrenomeAterado);
        emailAlterado = findViewById(R.id.emailAlterado);
        btnSalvarDados = findViewById(R.id.btnSalvarDados);
        btnVoltarPerfilUsuario = findViewById(R.id.btnVoltarPerfilUsuario);

        // Recuperando os dados do usuario
        ArrayList<String> listDadosUsuarioLogado = receberDadosUsuario();

        nomeAlterado.setText(listDadosUsuarioLogado.get(3));
        sobrenomeAlterado.setText(listDadosUsuarioLogado.get(4));
        emailAlterado.setText(listDadosUsuarioLogado.get(1));


        btnSalvarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> listDadosUsuarioLogado = receberDadosUsuario();

                String idAcesso = listDadosUsuarioLogado.get(0);
                String idUsuario = listDadosUsuarioLogado.get(2);

                Usuario usuarioAlterado = new Usuario();
                usuarioAlterado.setNome(nomeAlterado.getText().toString());
                usuarioAlterado.setSobrenome(sobrenomeAlterado.getText().toString());
                usuarioAlterado.setEmailAcesso(emailAlterado.getText().toString());



                // TO DO: VERIFICAR METOOD DE UPDATE CHILDREN DO FIRE BASE PARA ATUALIZAR OS DADOS





            }
        });

        btnVoltarPerfilUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivity(Home.class);
            }
        });


    }

    // Metodo utilizado para recuperar uma lista de string com os dados de acesso do usuario logado
    public ArrayList<String> receberDadosUsuario() {
        Intent intent = getIntent();
        ArrayList<String> listDadosAcesso = new ArrayList<>();
        return listDadosAcesso = intent.getStringArrayListExtra("listDadosAcesso");
    }

    // Metodo resposavel por apresentar a nova tela
    private void callActivity(Class newActivity) {
        Intent newIntent = new Intent(AlterarPerfilUsuario.this, newActivity);
        startActivity(newIntent);
        finish();
    }

}
