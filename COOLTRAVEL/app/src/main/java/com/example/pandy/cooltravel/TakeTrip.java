package com.example.pandy.cooltravel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TakeTrip extends AppCompatActivity {

    UserLocalStore userLocalStore;
    SearchView sv;
    ArrayAdapter<String> adapter;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_trip);
        userLocalStore = new UserLocalStore(this);
        sv =(SearchView) findViewById(R.id.tripSearchView);
        try {
            populateListView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        registerClickCallback();


        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void registerClickCallback()
    {
        ListView list = (ListView) findViewById(R.id.triplist);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                Intent intent = new Intent(viewClicked.getContext(), TripDetails.class);
                intent.putExtra("currentTripPos", position);
                startActivity(intent);
                //Toast.makeText(TakeTrip.this, Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void populateListView() throws JSONException {

        Trip atrip = new Trip();
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.getAllTripsDataInBackground(atrip, new GetTripCallback() {
            @Override
            public void done(String returnedTrips) {
                userLocalStore.storeAllTrips(returnedTrips);
                finishoff();
                //raw string comes in
                //here it would be done and i am to display stuff
            }
        });
    }

    public void finishoff()
    {
        ArrayList<Trip> gottenAllTrips = null;
        try {
            gottenAllTrips = userLocalStore.getAllTrips();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] myItems = new String[gottenAllTrips.size()];

        for(int i=0; i<gottenAllTrips.size();i++)
        {
            myItems[i] = gottenAllTrips.get(i).toString();
        }

        adapter = new ArrayAdapter<>(this,R.layout.eachtaketrip, myItems);

        list = (ListView) findViewById(R.id.triplist);

        list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            populateListView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
