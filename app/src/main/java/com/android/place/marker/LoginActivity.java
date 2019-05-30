package com.android.place.marker;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;


/**

 */

public class LoginActivity extends AppCompatActivity {

    SharedPreferences pref;
    protected EditText Email, Password;
    protected Button SignIn, SignUp;
    protected RelativeLayout relativeLayout;
    ProgressDialog dl;
    double Lat = 0, Lng = 0;
    String str;
    DatabaseHelper mysql;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mysql = new DatabaseHelper(LoginActivity.this);


        if(!weHavePermissionToReadFiles())
        {
            requestReadFilePermissionFirst();
        }


        pref = getSharedPreferences("Place_Marker", Context.MODE_PRIVATE);
        str = pref.getString("userEmail", "");
        getSupportActionBar().hide();


        if(str.compareTo("") !=0){

            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }else {
            setContentView(R.layout.login_activity);
            init();

        }


    }


    protected void init() {

        dl = new ProgressDialog(this);
        dl.setMessage("Loading...");


        Email = (EditText) findViewById(R.id.loginUserName);
        Password = (EditText) findViewById(R.id.loginPassword);
        SignIn = (Button) findViewById(R.id.loginButton);
        SignUp = (Button) findViewById(R.id.signUp);

        relativeLayout = (RelativeLayout) findViewById(R.id.activity_login);


        SignUp.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivity(intent);
                    }
                }
        );

        SignIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (checkCriteria()) {

                            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


                            if (Email.getText().toString().equals("")) {
                                Snackbar.make(relativeLayout, "Email Id is required", Snackbar.LENGTH_LONG).show();
                            } else if (!((Email.getText().toString()).trim()).matches(emailPattern)) {
                                Snackbar.make(relativeLayout, "Enter Valid Email Id", Snackbar.LENGTH_LONG).show();
                            } else if (Password.getText().toString().equals("")) {
                                Snackbar.make(relativeLayout, "Password is required", Snackbar.LENGTH_LONG).show();
                            } else {

                                Boolean ans = mysql.login(Email.getText().toString(),Password.getText().toString());

                                if (ans){

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("userEmail", Email.getText().toString());
                                    editor.apply();
                                    editor.commit();
                                    finish();
                                }else {

                                    Email.setText("");
                                    Password.setText("");
                                    Email.requestFocus();

                                    Snackbar snack = Snackbar.make(v, "Wrong Credentials", Snackbar.LENGTH_SHORT);
                                    View vs = snack.getView();
                                    TextView txt = (TextView) vs.findViewById(android.support.design.R.id.snackbar_text);
                                    txt.setTextColor(Color.RED);
                                    snack.show();
                                }

                            }

                        } else {
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setMessage("All fields are mandatary. Please enter all details")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    }
                }
        );


    }

    protected boolean checkCriteria() {
        boolean b = true;
        if ((Email.getText().toString()).equals("")) {
            b = false;
        }
        return b;
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



