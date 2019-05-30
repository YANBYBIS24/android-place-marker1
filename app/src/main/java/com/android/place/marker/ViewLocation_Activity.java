package com.android.place.marker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**

 */

public class ViewLocation_Activity extends AppCompatActivity {

    ListView list;
    ProgressDialog mDialog;
    SharedPreferences pref;
    DatabaseHelper mysql;
    String str;
    ArrayList<String> data;
    ArrayList<Integer> tempValue;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewplace_layout);
        mysql = new DatabaseHelper(ViewLocation_Activity.this);

        data = new ArrayList<String>();

        list = (ListView) findViewById(R.id.location_listview);
        pref = getSharedPreferences("Place_Marker", Context.MODE_PRIVATE);
        str = pref.getString("userEmail", "");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        LocationList();


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


    public class Adapter extends ArrayAdapter<String> {

        Context con;
        ArrayList<String> dataset;
        ArrayList<Integer> temps;
        int values;

        public Adapter(Context context, ArrayList<String> data, ArrayList<Integer> temp1) {
            super(context, R.layout.list_row_layout, data);
            con = context;
            dataset = data;
            temps = temp1;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(con).inflate(R.layout.list_row_layout, null, true);

            TextView place_name = (TextView) v.findViewById(R.id.place_name_text);
            TextView place_locality = (TextView) v.findViewById(R.id.locality_name_text);
//            ImageView delete_place =(ImageView) v.findViewById(R.id.delete_place_btn);
            CardView cardView = (CardView) v.findViewById(R.id.card_view);

            final TableRow hideView = (TableRow) v.findViewById(R.id.extra_layout);
            Button delete_place = (Button) v.findViewById(R.id.delete_place_btn);
            Button view_place = (Button) v.findViewById(R.id.view_location_btn);

//            LocationId,UserId,PlaceName, PlaceType,PlaceDescription,PlaceLocality
//                    ,Place_Latitude,Place_Longitude

            values = temps.get(position);

            if (values == 0) {
                hideView.setVisibility(View.GONE);
            } else {
                hideView.setVisibility(View.VISIBLE);
            }


            final String temp[] = dataset.get(position).split("\\*");

            place_name.setText(temp[2]);
            place_locality.setText(temp[5]);


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    values = temps.get(position);
                    if (values == 0) {
                        temps.add(position, 1);
                        hideView.setVisibility(View.VISIBLE);
                    } else {
                        temps.add(position, 0);
                        hideView.setVisibility(View.GONE);
                    }


                }
            });


            view_place.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ViewLocation_Activity.this, PlaceDetail_Activity.class);
                    intent.putExtra("LatLocation", temp[6]);
                    intent.putExtra("LngLocation", temp[7]);
                    intent.putExtra("PlaceName", temp[2]);
                    startActivity(intent);


                }
            });


            delete_place.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(con).setTitle("Remove")
                            .setMessage("Are you sure to remove ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

//                                    String UserID,String locationId

//                                    Toast.makeText(ViewLocation_Activity.this, temp[1]+"\n"+temp[0], Toast.LENGTH_SHORT).show();
                                    Boolean res = mysql.deleteData(temp[1], temp[0]);

                                    if (res == true) {

                                        Toast.makeText(ViewLocation_Activity.this, "Address Deleted", Toast.LENGTH_SHORT).show();
                                        LocationList();
                                    } else {

                                        Toast.makeText(ViewLocation_Activity.this, "Problem in address deletion", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            })
                            .setNegativeButton(android.R.string.no, null).show();

                }
            });


            return v;
        }
    }


    public void LocationList() {

        Cursor res = mysql.getAllData(str);

        data = new ArrayList<String>();
        tempValue = new ArrayList<Integer>();


        if (res.getCount() == 0) {
            list.setAdapter(null);
            Snackbar.make(list, "There is No Place available!", Snackbar.LENGTH_SHORT).show();
        } else {

            while (res.moveToNext()) {

                data.add(res.getString(0) + "*" + res.getString(1) + "*" + res.getString(2) + "*" + res.getString(3) + "*" + res.getString(4)
                        + "*" + res.getString(5) + "*" + res.getString(6) + "*" + res.getString(7));

                tempValue.add(0);
            }

            Adapter adapt = new Adapter(ViewLocation_Activity.this, data, tempValue);
            list.setAdapter(adapt);
        }
    }


}
