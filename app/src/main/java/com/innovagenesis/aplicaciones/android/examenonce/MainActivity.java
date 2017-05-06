package com.innovagenesis.aplicaciones.android.examenonce;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.Manifest;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity
implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback{

    private static final String[] PERMISOS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static int REQUEST_CODE = 1;
    private GoogleApiClient googleApiClient;
    private Location location;

    private double latitud;
    private double longitud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int leer = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (leer == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,PERMISOS, REQUEST_CODE);
        }
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /* Seccion del GoogleApiClient */


        if (googleApiClient == null){

            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


    }



    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Tiene que estar definido aca porque siempre los tiene que pedir
        int leer = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (leer == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,PERMISOS, REQUEST_CODE);
        }

        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null){

            latitud = location.getLatitude();
            longitud = location.getLongitude();

            Toast.makeText(this, "Latitud: " + latitud + "Logitud: " + longitud, Toast.LENGTH_SHORT).show();

            /** Instancia el fragment despues del connect para asignar los marcadores */
            SupportMapFragment mapFragment =(SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        final LatLng CIUDAD = new LatLng(latitud,longitud);

        googleMap.addMarker(new MarkerOptions()
        .title("Esta es su posicion")
        .position(CIUDAD));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CIUDAD,16));

        Toast.makeText(this, "Latitud: " + latitud + "Logitud: " + longitud, Toast.LENGTH_SHORT).show();



    }
}
