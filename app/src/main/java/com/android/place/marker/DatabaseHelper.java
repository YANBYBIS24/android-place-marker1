package com.android.place.marker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**

 */

public class DatabaseHelper extends SQLiteOpenHelper {

    Context context;
    //Database Name
    public static final String DATABASE_NAME = "PlaceMarker.db";

    //Table Name
    public static final String REGISTRATION_TABLE = "Registration_Table";
    public static final String LOCATION_TABLE = "Location_Table";

    //Column Name

    //Column for Register Table
    public static final String UserName = "UserName";
    public static final String UserNumber = "UserNumber";
    public static final String UserEmail = "UserEmail";
    public static final String UserPassword = "UserPassword";


    //Column for Location Table
    public static final String LocationId = "LocationId";
    public static final String UserId = "UserId";
    public static final String PlaceName = "PlaceName";
    public static final String PlaceType = "PlaceType";
    public static final String PlaceDescription = "PlaceDescription";
    public static final String PlaceLocality = "Locality";
    public static final String Place_Latitude = "Latitude";
    public static final String Place_Longitude = "Longitude";


    DatabaseHelper dbh;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table " + REGISTRATION_TABLE + " (UserName Text,UserNumber Text,UserEmail Text,UserPassword Text)");
        db.execSQL("Create Table " + LOCATION_TABLE + " (LocationId INTEGER  PRIMARY KEY AUTOINCREMENT,UserId Text,PlaceName Text,PlaceType Text,PlaceDescription Text,Locality Text,Latitude Text,Longitude Text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP table if exist " + REGISTRATION_TABLE);
        db.execSQL("DROP table if exist " + LOCATION_TABLE);
        onCreate(db);
    }


    public boolean insert_RegistrationData(String userName, String userNumber, String userEmail, String userPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserName, userName);
        contentValues.put(UserNumber, userNumber);
        contentValues.put(UserEmail, userEmail);
        contentValues.put(UserPassword, userPassword);
        long result = db.insert(REGISTRATION_TABLE, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }


    public boolean insert_LocationData(String userId, String placeName, String placeType, String placeDescription,
                                       String placeLocality, String place_Latitude, String place_Longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserId, userId);
        contentValues.put(PlaceName, placeName);
        contentValues.put(PlaceType, placeType);
        contentValues.put(PlaceDescription, placeDescription);
        contentValues.put(PlaceLocality, placeLocality);
        contentValues.put(Place_Latitude, place_Latitude);
        contentValues.put(Place_Longitude, place_Longitude);
        long result = db.insert(LOCATION_TABLE, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }


    public Cursor getAllData1() {
        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        Cursor res = db.query(LOCATION_TABLE, new String[]{LocationId, UserId,PlaceName,PlaceType,PlaceDescription,
                PlaceLocality,Place_Latitude,Place_Longitude}, null, null, null, null, null);
        return res;
    }


//    public DatabaseHelper open()
//    {
//        dbh = new DatabaseHelper(context);
//        sqldb = dbh.getWritableDatabase();
//        return this;
//    }

//    public void close()
//    {
//        dbh.close();
//    }



    public Cursor getAllData(String userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.query(LOCATION_TABLE, new String[]{LocationId,UserId,PlaceName, PlaceType,PlaceDescription,PlaceLocality
        ,Place_Latitude,Place_Longitude}, UserId + " =  '" + userID + "'", null, null, null, null);
        return res;
    }


    public Boolean deleteData(String UserID,String locationId) {

        Boolean ans = false;
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(LOCATION_TABLE,LocationId + " =  "+locationId+"" + " AND " +UserId + " = '" +UserID+ "'" ,null);

        if (result == -1)
            return false;
        else
            return true;

    }



    public Boolean checkemail(String email)
    {
        Boolean ans=false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(REGISTRATION_TABLE,null,UserEmail + " = '"+email+"'",null,null,null,null);
        if(c.getCount()>0)
        {
            ans=true;
        }
        return ans;
    }


    public Boolean login(String user, String pass)
    {
        Boolean ans=false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(REGISTRATION_TABLE,null,UserEmail + " = '"+user+"' AND " + UserPassword + " = '"+pass+"' ",null,null,null,null);
        if(c.getCount()>0)
        {
            ans=true;
        }
        return ans;
    }


    public String getdetails(String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String ans="no";
        Cursor c = db.rawQuery("select LocationId,UserId,PlaceName,PlaceType,PlaceDescription,Locality,Latitude,Longitude from "+LOCATION_TABLE+" where "+UserId+" = '"+email+"'",null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            ans=c.getString(0)+"*"+c.getString(1)+"*"+c.getString(2)+"*"+c.getString(3)+c.getString(4)+"*"+c.getString(5)+"*"+c.getString(6)+"*"+c.getString(0)+"#";
        }
        return ans;
    }




}
