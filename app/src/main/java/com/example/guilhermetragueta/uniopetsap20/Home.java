package com.example.guilhermetragueta.uniopetsap20;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends Activity {

    private Button btnLogout;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                callActivity(MainActivity.class);
            }
        });


    }

    private void callActivity(Class newActivity) {
        Intent newIntent = new Intent(Home.this,newActivity);
        startActivity(newIntent);
        finish();
    }
}
