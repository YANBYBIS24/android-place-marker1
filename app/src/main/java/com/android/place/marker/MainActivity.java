package com.android.place.marker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    TableRow StartJourney, ViewLocation;
    double Lat = 0, Lng = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!weHavePermissionToReadFiles())
        {
            requestReadFilePermissionFirst();
        }
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = new MenuInflater(MainActivity.this);
        mi.inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.logout) {

            SharedPreferences pref = getSharedPreferences("Place_Marker", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            finish();

            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);

        } else if (item.getItemId() == R.id.start_journey){

            GPS_Tracker gps_tracker = new GPS_Tracker(MainActivity.this, MainActivity.this);
            if (gps_tracker.canGetLocation()) {
                Lat = gps_tracker.getLatitude();
                Lng = gps_tracker.getLongitude();

                if (Lat != 0 && Lng != 0) {

                    Intent intent = new Intent(MainActivity.this, StartJourney_Activity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(MainActivity.this, "Determining Your cordinates,Click Again", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Enable Your GPS(Location)", Toast.LENGTH_SHORT).show();
            }

        }else if (item.getItemId() == R.id.view_location){

            Intent intent = new Intent(MainActivity.this,ViewLocation_Activity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    public void init() {

        StartJourney = (TableRow) findViewById(R.id.start_journey_btn);
        ViewLocation = (TableRow) findViewById(R.id.view_location_btn);


        StartJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                GPS_Tracker gps_tracker = new GPS_Tracker(MainActivity.this, MainActivity.this);
                if (gps_tracker.canGetLocation()) {
                    Lat = gps_tracker.getLatitude();
                    Lng = gps_tracker.getLongitude();

                    if (Lat != 0 && Lng != 0) {

//                        Toast.makeText(MainActivity.this, Lat + "," + Lng, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this, StartJourney_Activity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(MainActivity.this, "Determining Your cordinates,Click Again", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Enable Your GPS(Location)", Toast.LENGTH_SHORT).show();
                }

            }
        });




        ViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,ViewLocation_Activity.class);
                startActivity(intent);
            }
        });


    }


    private boolean weHavePermissionToReadFiles()
    {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadFilePermissionFirst()
    {
        if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) && (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))) {
            requestForResultFilePermission();
        } else {
            requestForResultFilePermission();
        }
    }

    private void requestForResultFilePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 333);
    }



}
