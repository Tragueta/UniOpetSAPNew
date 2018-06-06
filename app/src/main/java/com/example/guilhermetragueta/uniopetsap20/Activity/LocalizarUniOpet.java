package com.example.guilhermetragueta.uniopetsap20.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.guilhermetragueta.uniopetsap20.R;

public class LocalizarUniOpet extends Activity {

    private Button btnBomRetiro;
    private Button btnReboucas;
    private Button btnVoltarHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizar_uni_opet);

        btnBomRetiro = findViewById(R.id.btnBomRetiro);
        btnReboucas = findViewById(R.id.btnReboucas);
        btnVoltarHome = findViewById(R.id.btnVoltarHome);

        // Metodo utilizado para localizar o campus 'Bom Retiro'
        btnBomRetiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivity(MapBomRetiro.class);
            }
        });

        // Metodo utilizado para localizar o campus 'Reboucas'
        btnReboucas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivity(MapReboucas.class);
            }
        });

        // Metodo utilizado para redirecionar o usuario para Home
        btnVoltarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivity(Home.class);
            }
        });

    }

    // Metodo usado para trocar de telas
    private void callActivity(Class newActivity) {
        Intent newIntent = new Intent(LocalizarUniOpet.this,newActivity);
        startActivity(newIntent);
        finish();
    }

}
