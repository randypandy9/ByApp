package com.example.pandy.cooltravel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

public class CreatedTrips extends AppCompatActivity {

    UserLocalStore userLocalStore;
    SearchView sv;
    ArrayAdapter<String> adapter;
    ListView list;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_trips);

        userLocalStore = new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser();


        sv =(SearchView) findViewById(R.id.createdtripSearchView);
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
        start();

    }
    public void start()
    {
        Trip atrip = new Trip();
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.getAllTripsDataInBackground(atrip, new GetTripCallback() {
            @Override
            public void done(String returnedTrips) {
                storeall(returnedTrips);

                //raw string comes in
                //here it would be done and i am to display stuff
            }
        });
    }

    public void storeall(String returned)
    {
        String ret = returned;
        userLocalStore.storeAllTrips(returned);


        ServerRequest serverRequest1 = new ServerRequest(this);
        serverRequest1.getCreatedTripsInBackground(user, new GetTripCallback() {
            @Override
            public void done(String returnedTrips) {
                storemine(returnedTrips);

            }
        });

    }

    public void storemine(String returned)
    {
        String ret = returned;
        userLocalStore.storeCreatedTrips(returned);

        try {
            populateListView();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void registerClickCallback()
    {
        ListView list = (ListView) findViewById(R.id.createdtriplist);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                Intent intent = new Intent(viewClicked.getContext(), TripView.class);
                intent.putExtra("currentTripPos", position);
                startActivity(intent);
            }
        });
    }

    public void populateListView() throws JSONException {


        ArrayList<MyTrip> gottenCreatedTrips = null;
        ArrayList<Trip> gottenAllTrips = null;
        ArrayList<Trip> gotten = new ArrayList<>();
        try
        {
            gottenAllTrips = userLocalStore.getAllTrips();
            gottenCreatedTrips = userLocalStore.getCreatedTrips();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        for(int i=0;i<gottenCreatedTrips.size();i++)
        {
            for(int j=0;j<gottenAllTrips.size();j++)
            {
                if(gottenCreatedTrips.get(i).getTrip_id() == gottenAllTrips.get(j).getTrip_id())
                {
                    gotten.add(gottenAllTrips.get(j));
                }
            }
        }

        String[] myItems = new String[gotten.size()];

        for(int i=0; i<gotten.size();i++)
        {
            myItems[i] = gotten.get(i).toString();
        }

        adapter = new ArrayAdapter<>(this,R.layout.eachcreatedtrip, myItems);

        list = (ListView) findViewById(R.id.createdtriplist);

        list.setAdapter(adapter);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        try {
//            populateListView();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}