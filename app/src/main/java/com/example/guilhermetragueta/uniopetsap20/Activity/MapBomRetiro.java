package com.example.guilhermetragueta.uniopetsap20.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.guilhermetragueta.uniopetsap20.R;
import com.example.guilhermetragueta.uniopetsap20.Util.Utility;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapBomRetiro extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private boolean retorno;
    private Button btnBRVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_bom_retiro);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        retorno = Utility.checkPermission(MapBomRetiro.this);

        if(retorno) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        // Metodo utilizado para redirecionar o usuario para tela de 'Localiza Campus'
        btnBRVoltar = findViewById(R.id.btnBRVoltar);
        btnBRVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivity(LocalizarUniOpet.class);
            }
        });
    }

    // Solicita e valida a permissao para realizar o uso do GPS
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);
                } else {
                    Toast.makeText(MapBomRetiro.this, "Problemas durante a permissão de uso do GPS.",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // Metodo responsavel pela exibicao do mapa com os marcadores
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(Utility.checkPermission(MapBomRetiro.this)) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(MapBomRetiro.this, Locale.getDefault());
                                List<Address> addresses = null;
                                try {

                                    // Localizacao atual do usuario
                                    LatLng localUsuario = new LatLng(location.getLatitude(), location.getLongitude());

                                    // Localizacao do campus 'Bom Retiro'
                                    LatLng bomRetiro = new LatLng(-25.405887, -49.276768);
                                    mMap.addMarker(new MarkerOptions().position(bomRetiro).title("Campus Bom Retiro"));

                                    mMap.addMarker(new MarkerOptions().position(localUsuario).title("Você"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(bomRetiro));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }
    }

    // Metodo usado para trocar de telas
    private void callActivity(Class newActivity) {
        Intent newIntent = new Intent(MapBomRetiro.this,newActivity);
        startActivity(newIntent);
        finish();
    }

}
