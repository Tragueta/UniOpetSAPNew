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

import java.util.List;
import java.util.Locale;

public class MapReboucas extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private boolean retorno;
    private Button btnRVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_reboucas);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        retorno = Utility.checkPermission(MapReboucas.this);

        if(retorno) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        // Metodo utilizado para redirecionar o usuario para tela de 'Localiza Campus'
        btnRVoltar = findViewById(R.id.btnRVoltar);
        btnRVoltar.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(MapReboucas.this, "Problemas durante a permissão de uso do GPS.",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    // Metodo responsavel pela exibicao do mapa com os marcadores
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(Utility.checkPermission(MapReboucas.this)) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(MapReboucas.this, Locale.getDefault());
                                List<Address> addresses = null;
                                try {

                                    // Localizacao atual do usuario
                                    LatLng localUsuario = new LatLng(location.getLatitude(), location.getLongitude());

                                    // Localizacao do campus 'Reboucas'
                                    LatLng campusReboucas = new LatLng(-25.4435359, -49.268599);
                                    mMap.addMarker(new MarkerOptions().position(campusReboucas).title("Campus Rebouças"));

                                    mMap.addMarker(new MarkerOptions().position(localUsuario).title("Você"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(campusReboucas));
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
        Intent newIntent = new Intent(MapReboucas.this,newActivity);
        startActivity(newIntent);
        finish();
    }

}
