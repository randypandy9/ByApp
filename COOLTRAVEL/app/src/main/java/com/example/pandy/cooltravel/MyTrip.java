package com.example.pandy.cooltravel;

/**
 * Created by pandy on 08/03/2016.
 */
public class MyTrip
{

    private int mytrip_id,trip_id;
    private String username;

    public MyTrip()
    {

    }
    public MyTrip(int mytrip_id, String username, int trip_id)
    {
        this.mytrip_id = mytrip_id;
        this.username = username;
        this.trip_id = trip_id;
    }

    public int getMytrip_id() {
        return mytrip_id;
    }

    public void setMytrip_id(int mytrip_id) {
        this.mytrip_id = mytrip_id;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
