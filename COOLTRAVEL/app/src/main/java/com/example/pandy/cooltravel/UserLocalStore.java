package com.example.pandy.cooltravel;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pandy on 30/06/2015.
 */
public class UserLocalStore
{
    public static final String SP_NAME = "currentUserDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context)
    {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUsedUsername(User user)
    {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("usedusername", user.username);
        spEditor.apply();
    }

    public String getUsedUsername()
    {
        String usedUsername = userLocalDatabase.getString("usedusername", "");
        return usedUsername;
    }

    public void storeUserData(User user)
    {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("fname", user.fname);
        spEditor.putString("lname", user.lname);
        spEditor.putString("username", user.username);
        spEditor.putString("password", user.password);
        spEditor.putInt("age", user.age);
        spEditor.commit();
    }

    public User getLoggedInUser()
    {
        String fname = userLocalDatabase.getString("fname", "");
        String lname = userLocalDatabase.getString("lname", "");
        String username = userLocalDatabase.getString("username", "");
        String password = userLocalDatabase.getString("password", "");
        int age = userLocalDatabase.getInt("age",-1);

        User currentUser = new User(fname,lname,username,password,age);
        return currentUser;
    }

    public void storeCurrentVehicle(Vehicle vehicle)
    {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("make", vehicle.getMake());
        spEditor.putString("model", vehicle.getModel());
        spEditor.putString("plate", vehicle.getPlate());
        spEditor.commit();
    }

    public Vehicle getCurrentVehicle()
    {
        String make = userLocalDatabase.getString("make", "");
        String model = userLocalDatabase.getString("model", "");
        String plate = userLocalDatabase.getString("plate", "");

        Vehicle currentVehicle = new Vehicle(make,model,plate);
        return currentVehicle;
    }

    public void setUserLoggedIn(boolean loggedIn)
    {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public void clearUserData()
    {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }

    public boolean getUserLoggedIn()
    {
        if(userLocalDatabase.getBoolean("loggedIn",false) == true)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void storeAllTrips(String alltrips)
    {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("alltrips", alltrips);
        spEditor.commit();
    }

    public void storeMyTrips(String mytrips)
    {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("mytrips", mytrips);
        spEditor.commit();
    }

    public void storeCreatedTrips(String createdtrips)
    {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("createdtrips", createdtrips);
        spEditor.commit();
    }

    public ArrayList<MyTrip> getCreatedTrips() throws JSONException {
        String rawtrips = userLocalDatabase.getString("createdtrips", "");

        ArrayList<MyTrip> convertedCreatedTrips = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(rawtrips);

        if (jsonArray.length()>0)
        {
            for(int i = 0; i < jsonArray.length();i++)
            {
                JSONObject atrip = jsonArray.getJSONObject(i);

                int createdtrip_id = atrip.getInt("createdtrip_id");
                String username = atrip.getString("username");
                int trip_id = atrip.getInt("trip_id");

                MyTrip extractedTrip = new MyTrip(createdtrip_id,username,trip_id);
                convertedCreatedTrips.add(extractedTrip);
            }
        }

        return convertedCreatedTrips;
    }

    public ArrayList<MyTrip> getMyTrips() throws JSONException {
        String rawtrips = userLocalDatabase.getString("mytrips", "");

        ArrayList<MyTrip> convertedMyTrips = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(rawtrips);

        if (jsonArray.length()>0)
        {
            for(int i = 0; i < jsonArray.length();i++)
            {
                JSONObject atrip = jsonArray.getJSONObject(i);

                int mytrip_id = atrip.getInt("mytrip_id");
                String username = atrip.getString("username");
                int trip_id = atrip.getInt("trip_id");

                MyTrip extractedTrip = new MyTrip(mytrip_id,username,trip_id);
                convertedMyTrips.add(extractedTrip);
            }
        }

        return convertedMyTrips;
    }

    public ArrayList<Trip> getAllTrips() throws JSONException {
        String rawtrips = userLocalDatabase.getString("alltrips", "");
        ArrayList<Trip> convertedTrips = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(rawtrips);

        if (jsonArray.length()>0)
        {
            for(int i = 0; i < jsonArray.length();i++)
            {
                JSONObject atrip = jsonArray.getJSONObject(i);

                int trip_id = atrip.getInt("trip_id");
                String origin = atrip.getString("origin");
                String destination = atrip.getString("destination");
                int price = atrip.getInt("price");
                int seats = atrip.getInt("seats");
                int seatsRemaining = atrip.getInt("seatsRemaining");
                String details = atrip.getString("details");
                String date = atrip.getString("date");

                Trip extractedTrip = new Trip(trip_id,origin,destination,price,seats,seatsRemaining,details,date);
                convertedTrips.add(extractedTrip);
            }
        }

        return convertedTrips;
    }

}
