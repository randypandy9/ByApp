package com.example.pandy.cooltravel;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by pandy on 03/07/2015.
 */
interface GetTripCallback
{
    public abstract void done(String returnedTrips);
}
