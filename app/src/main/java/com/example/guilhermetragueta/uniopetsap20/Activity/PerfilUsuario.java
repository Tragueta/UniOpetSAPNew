package com.example.guilhermetragueta.uniopetsap20.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guilhermetragueta.uniopetsap20.Class.Usuario;
import com.example.guilhermetragueta.uniopetsap20.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PerfilUsuario extends Activity {

    private TextView txtNome;
    private TextView txtSobrenome;
    private TextView txtEmail;
    private Button btnAlterarDados;
    private Button btnVoltarHome;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference database;
    private Usuario usuarioLogado;
    ArrayList<String> listDadosUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        txtNome = findViewById(R.id.txtNome);
        txtSobrenome = findViewById(R.id.txtSobrenome);
        txtEmail = findViewById(R.id.txtEmail);
        btnAlterarDados = findViewById(R.id.btnAlterarDados);
        btnVoltarHome = findViewById(R.id.btnVoltarHome);
        firebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //Recebendo o usuario logado no sistema
        ArrayList<String> listDadosAcesso = receberDadosUsuario();

        // Recuperacao e apresentação dos dados de acesso
        String idAcesso = listDadosAcesso.get(0);

        // Busca o usuario logado no sistema na base do Firebase
        database = firebaseDatabase.getReference("Usuario/" + idAcesso);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Montando o objeto usuario
                Usuario usuarioLogado = dataSnapshot.getValue(Usuario.class);
                txtNome.setText(usuarioLogado.getNome());
                txtSobrenome.setText(usuarioLogado.getSobrenome());
                txtEmail.setText(usuarioLogado.getEmailAcesso());

                convertUserToList(usuarioLogado);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PerfilUsuario.this, "Ops! Houve um problema durante a recuperação do dados. Por gentileza, tente novamente.", Toast.LENGTH_SHORT).show();
            }
        });

        btnAlterarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Recebendo o usuario logado no sistema
                ArrayList<String> listDadosAcesso = receberDadosUsuario();

                Intent alterarDados = new Intent(PerfilUsuario.this, AlterarPerfilUsuario.class);
                alterarDados.putStringArrayListExtra("listDadosAcesso", listDadosAcesso);
                startActivity(alterarDados);
                finish();
            }
        });

        // Metodo utilizado para redirecionar o usuario para tela 'Home'
        btnVoltarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivity(Home.class);
            }
        });

    }

    public void convertUserToList(Usuario user){
        this.listDadosUsuarioLogado.add(0, user.getidAcesso());
        this.listDadosUsuarioLogado.add(1, user.getEmailAcesso());
        this.listDadosUsuarioLogado.add(2, user.getId());
        this.listDadosUsuarioLogado.add(3, user.getNome());
        this.listDadosUsuarioLogado.add(4,user.getSobrenome());
    }

    // Metodo utilizado para recuperar uma lista de string com os dados de acesso do usuario logado
    public ArrayList<String> receberDadosUsuario() {
        Intent intent = getIntent();
        return this.listDadosUsuarioLogado = intent.getStringArrayListExtra("dadosAcesso");
    }

    // Metodo usado para trocar de telas
    private void callActivity(Class newActivity) {
        Intent newIntent = new Intent(PerfilUsuario.this,newActivity);
        startActivity(newIntent);
        finish();
    }
}
