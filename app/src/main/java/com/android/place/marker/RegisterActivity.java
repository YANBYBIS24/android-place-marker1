package com.android.place.marker;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

/**

 */

public class RegisterActivity extends AppCompatActivity {

    protected EditText name, phoneNumber, emailID, password, confirmPassword;
    protected Button register;
    protected RelativeLayout relativeLayout;
    ProgressDialog dl;
    DatabaseHelper mysql;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mysql = new DatabaseHelper(RegisterActivity.this);

        init();
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


    protected void init() {

        dl = new ProgressDialog(this);
        dl.setMessage("Loading...");


        name = (EditText) findViewById(R.id.userName);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        emailID = (EditText) findViewById(R.id.emailID);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_registration);
        register = (Button) findViewById(R.id.registerButton);

        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (checkCriteria()) {

                            String match = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                            if (name.getText().toString().equals("")) {
                                Snackbar.make(relativeLayout, "Name is required", Snackbar.LENGTH_LONG).show();
                            } else if (phoneNumber.getText().toString().equals("")) {
                                Snackbar.make(relativeLayout, "Password is required", Snackbar.LENGTH_LONG).show();
                            } else if (phoneNumber.getText().toString().length() != 10) {
                                Snackbar.make(relativeLayout, "Invalid Number,Must Be 10 Digits", Snackbar.LENGTH_LONG).show();
                            } else if (emailID.getText().toString().equals("")) {
                                Snackbar.make(relativeLayout, "Email Id is required", Snackbar.LENGTH_LONG).show();
                            } else if (!emailID.getText().toString().matches(match)) {
                                Snackbar.make(relativeLayout, "Please Fallow Email Standards", Snackbar.LENGTH_LONG).show();
                            } else if (password.getText().toString().equals("")) {
                                Snackbar.make(relativeLayout, "Password is required", Snackbar.LENGTH_LONG).show();
                            } else if (confirmPassword.getText().toString().equals("")) {
                                Snackbar.make(relativeLayout, " Confirm Password is required", Snackbar.LENGTH_LONG).show();
                            } else if (password.getText().toString().compareTo(confirmPassword.getText().toString()) != 0) {
                                Snackbar.make(relativeLayout, "Password Does Not Match", Snackbar.LENGTH_LONG).show();
                            } else {

//                                mysql.open();
                                Boolean res = mysql.checkemail(emailID.getText().toString());
//                                mysql.close();

                                if (res) {
                                    emailID.setError("Email Already Exists!");
                                    emailID.requestFocus();
                                } else {
//                                String userName, String userNumber, String userEmail, String userPassword
                                    RegisterTask(name.getText().toString(), phoneNumber.getText().toString(), emailID.getText().toString(),
                                            password.getText().toString());
                                }
                            }
                        } else {
                            new AlertDialog.Builder(RegisterActivity.this)
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
        if ((name.getText().toString()).equals("")) {
            b = false;
        }
        return b;
    }


    public void RegisterTask(String userName, String userNumber, String userEmail, String userPassword) {

        boolean isInserted = mysql.insert_RegistrationData(userName, userNumber, userEmail, userPassword);

        if (isInserted == true) {
            Toast.makeText(RegisterActivity.this, "User Added", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(RegisterActivity.this, "Problem in Registration, Please Try again", Toast.LENGTH_SHORT).show();
        }

    }


}
