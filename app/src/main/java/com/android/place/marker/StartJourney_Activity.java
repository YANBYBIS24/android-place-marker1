package com.android.place.marker;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**

 */

public class StartJourney_Activity extends AppCompatActivity implements OnMapReadyCallback {

    SharedPreferences pref;
    String str;
    private GoogleMap mMap;
    Button SavePlace;
    Marker marker;
    GPS_Tracker gps_tracker;
    String DestinationText = "";
    double CLat = 0.0, CLng = 0.0;

    Timer timer;
    TimerTask timerTask;
    Handler handler = new Handler();

    DatabaseHelper mysql;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startjourney_layout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.journey_map);


        pref = getSharedPreferences("Place_Marker", Context.MODE_PRIVATE);
        str = pref.getString("userEmail", "");

        gps_tracker = new GPS_Tracker(StartJourney_Activity.this, StartJourney_Activity.this);
        mysql = new DatabaseHelper(StartJourney_Activity.this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Location Tracker");

        mapFragment.getMapAsync(this);

        SavePlace = (Button) findViewById(R.id.save_place);


        SavePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (gps_tracker.canGetLocation()) {
                    CLat = gps_tracker.getLatitude();
                    CLng = gps_tracker.getLongitude();

                    if (CLat != 0 && CLng != 0) {

                        double Lat, Lng;
                        Lat = CLat;
                        Lng = CLng;

                        Geocoder g = new Geocoder(StartJourney_Activity.this);
                        List<Address> list = null;
                        try {
                            list = g.getFromLocation(Lat, Lng, 1);
                            Address add = list.get(0);
                            DestinationText = add.getSubLocality();
                            if (DestinationText.compareTo("") == 0 || DestinationText == null) {

                                DestinationText = add.getLocality();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        savePlaceDialog(DestinationText, Lat + "", Lng + "");

                    }
                }


            }
        });

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


    public void savePlaceDialog(String locality, String Lat, final String Lng) {

        final Dialog d;
        Button savePlace;
        final EditText PlaceName, PlaceType, PlaceDescription, PlaceLocality, PlaceLocation;

        final String LatLocation = Lat;
        final String LngLocation = Lng;

        d = new Dialog(StartJourney_Activity.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.addplace_dialog);

        savePlace = (Button) d.findViewById(R.id.save_place_btn);

        PlaceName = (EditText) d.findViewById(R.id.place_name);
        PlaceType = (EditText) d.findViewById(R.id.place_type);
        PlaceDescription = (EditText) d.findViewById(R.id.place_description);
        PlaceLocality = (EditText) d.findViewById(R.id.locality);
        PlaceLocation = (EditText) d.findViewById(R.id.location);

        PlaceLocality.setText(locality);
        PlaceLocality.setEnabled(false);
        PlaceLocation.setText(Lat + "," + Lng);
        PlaceLocation.setEnabled(false);


        savePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                String userId, String placeName, String placeType, String placeDescription,
//                        String placeLocality, String place_Latitude, String place_Longitude

                if (PlaceName.getText().toString().equals("")) {

                    Toast.makeText(StartJourney_Activity.this, "Place Name is required", Toast.LENGTH_SHORT).show();

                } else if (PlaceType.getText().toString().equals("")) {

                    Toast.makeText(StartJourney_Activity.this, "Place Type is required", Toast.LENGTH_SHORT).show();
                } else if (PlaceDescription.getText().toString().equals("")) {

                    Toast.makeText(StartJourney_Activity.this, "Place Description is required", Toast.LENGTH_SHORT).show();

                } else {
                    d.cancel();

                    boolean isInserted = mysql.insert_LocationData(str, PlaceName.getText().toString(), PlaceType.getText().toString(),
                            PlaceDescription.getText().toString(), PlaceLocality.getText().toString(), LatLocation, LngLocation);

                    if (isInserted == true) {
                        Toast.makeText(StartJourney_Activity.this, "Place Added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(StartJourney_Activity.this, "Problem in Place Adding, Please Try again", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
        d.show();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        timer = new Timer();
        getcurrentLocation();
        timer.schedule(timerTask, 0, 500);


    }


    public void getcurrentLocation() {

        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (gps_tracker.canGetLocation()) {
                            CLat = gps_tracker.getLatitude();
                            CLng = gps_tracker.getLongitude();

                            if (CLat != 0 && CLng != 0) {

//                                Toast.makeText(StartJourney_Activity.this, CLat + "," + CLng, Toast.LENGTH_SHORT).show();
                                if (marker != null) {
                                    marker.remove();
                                }
                                LatLng latLng = new LatLng(CLat, CLng);
                                MarkerOptions mark = new MarkerOptions();
                                mark.position(latLng);
                                marker = mMap.addMarker(mark);
                                marker.showInfoWindow();
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));


                            } else {
//                                Toast.makeText(StartGurney.this, "Determining Your cordinates,Click Again", Toast.LENGTH_SHORT).show();
                            }
                        } else {
//                            Toast.makeText(StartGurney.this, "Enable Your GPS(Location)", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        };

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timerTask.cancel();
    }


}
