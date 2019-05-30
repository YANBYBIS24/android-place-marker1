package com.android.place.marker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**

 */

public class PlaceDetail_Activity extends AppCompatActivity implements OnMapReadyCallback {

    double Lat,Lng ;
    SharedPreferences pref;
    String str;
    private GoogleMap mMap;
    Marker marker;
    String PlaceName;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placedetail_layout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.journey_map);


        pref = getSharedPreferences("Place_Marker", Context.MODE_PRIVATE);
        str = pref.getString("userEmail", "");

        Intent intent = getIntent();
        PlaceName = intent.getStringExtra("PlaceName");
        Lat  = Double.parseDouble(intent.getStringExtra("LatLocation"));
        Lng = Double.parseDouble(intent.getStringExtra("LngLocation"));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(PlaceName);
        mapFragment.getMapAsync(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        LatLng ll = new LatLng(Lat, Lng);
        MarkerOptions mo = new MarkerOptions();
        mo.position(ll);
        mo.title(PlaceName);
        mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(mo);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));

    }


}
