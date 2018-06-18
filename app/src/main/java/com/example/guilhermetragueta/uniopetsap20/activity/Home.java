package com.example.guilhermetragueta.uniopetsap20.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.guilhermetragueta.uniopetsap20.R;
import com.facebook.AccessToken;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Home extends Activity {

    private Button btnLogout;
    private Button btnPerfil;
    private FirebaseAuth firebaseAuth;
    private TextView txtLocaliza;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        btnLogout = findViewById(R.id.btnLogout);
        txtLocaliza = findViewById(R.id.txtLocaliza);
        btnPerfil = findViewById(R.id.btnPerfil);

        // Recebendo o usuario logado no sistema
        FirebaseUser user = firebaseAuth.getCurrentUser();

        // Metodo usado para redirecionar o usuario para tela de 'Localizar campus UniOpet'
        txtLocaliza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent localizaIntent = new Intent(Home.this, UnidadesUniOpet.class);
                startActivity(localizaIntent);
                finish();
            }
        });

        // Metodo ustilizado para redirecionar o usuario para tela de "Pefil/Dados"
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Recebendo o usuario logado no sistema
                FirebaseUser user = firebaseAuth.getCurrentUser();

                // Passando os dados do usuario logado para um arraylist de String
                ArrayList<String> dadosString = new ArrayList<String>();
                dadosString.add(0, user.getUid().toString());
                dadosString.add(1, user.getEmail().toString());

                Intent perfilIntent = new Intent(Home.this, PerfilUsuario.class);
                perfilIntent.putStringArrayListExtra("dadosAcesso", dadosString);
                startActivity(perfilIntent);
                finish();

            }
        });

        // Metodo utilizado para realizar o logout do app
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessToken.setCurrentAccessToken(null);
                firebaseAuth.signOut();
                Intent loginIntent = new Intent(Home.this, MainActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

    }
}
