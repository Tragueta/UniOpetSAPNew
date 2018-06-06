package com.example.guilhermetragueta.uniopetsap20.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.guilhermetragueta.uniopetsap20.R;
import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends Activity {

    private Button btnLogout;
    private FirebaseAuth firebaseAuth;
    private TextView txtLocaliza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        btnLogout = findViewById(R.id.btnLogout);
        txtLocaliza = findViewById(R.id.txtLocaliza);

        // Metodo usado para redirecionar o usuario para tela de 'Localizar campus UniOpet'
        txtLocaliza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent localizaIntent = new Intent(Home.this, LocalizarUniOpet.class);
                startActivity(localizaIntent);
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
