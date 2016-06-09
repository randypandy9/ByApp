package com.example.pandy.cooltravel;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pandy.cooltravel.GetTripCallback;
import com.example.pandy.cooltravel.MapsActivity;
import com.example.pandy.cooltravel.R;
import com.example.pandy.cooltravel.ServerRequest;
import com.example.pandy.cooltravel.Trip;
import com.example.pandy.cooltravel.User;
import com.example.pandy.cooltravel.UserLocalStore;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TripView extends AppCompatActivity {

    TextView origin,destination,price,seats,seatsRemaining,details,date;
    UserLocalStore userLocalStore;
    Button booktripbtn, route;
    User currentUser;
    ArrayList<Trip> gottenAllTrips = new ArrayList<>();
    int gottenTrip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_view);
        userLocalStore = new UserLocalStore(this);


        try {
            gottenAllTrips = userLocalStore.getAllTrips();
            currentUser = userLocalStore.getLoggedInUser();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        booktripbtn = (Button) findViewById(R.id.booktripbtn);
        route = (Button) findViewById(R.id.routebtn);

        Intent intent = getIntent();
        gottenTrip = intent.getIntExtra("currentTripPos",0);

        origin = (TextView) findViewById(R.id.origintxt);
        origin.setText(gottenAllTrips.get(gottenTrip).getOrigin().toString());

        destination = (TextView) findViewById(R.id.destinationtxt);
        destination.setText(gottenAllTrips.get(gottenTrip).getDestination().toString());

        price = (TextView) findViewById(R.id.pricetxt);
        price.setText("£"+gottenAllTrips.get(gottenTrip).getDisplayPrice(gottenAllTrips.get(gottenTrip).getPrice()));

        seats = (TextView) findViewById(R.id.seatstxt);
        seats.setText(Integer.toString(gottenAllTrips.get(gottenTrip).getSeats()));

        seatsRemaining = (TextView) findViewById(R.id.seatsRemainingtxt);
        seatsRemaining.setText(Integer.toString(gottenAllTrips.get(gottenTrip).getSeatsRemaining()));

        details = (TextView) findViewById(R.id.detailstxt);
        details.setText(gottenAllTrips.get(gottenTrip).getDetails().toString());

        date = (TextView) findViewById(R.id.datetxt);
        date.setText(gottenAllTrips.get(gottenTrip).getDate().toString());


        route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //open maps
                //start intent to maps
                Intent tomaps = new Intent(v.getContext(), MapsActivity.class);
                //pass in x co ordinate
                tomaps.putExtra("x",gottenAllTrips.get(gottenTrip).getOrigin().toString());
                //pass in y co ordinate
                tomaps.putExtra("y",gottenAllTrips.get(gottenTrip).getDestination().toString());
                //start maps
                startActivity(tomaps);

            }
        });
//        booktripbtn.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                if(checkSeatsRemaining())
//                {
//                    Toast.makeText(getBaseContext(), "Oops! There are no more seats available on this Trip!", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    executeAdd();
//                }
//
//            }
//        });
    }

    private void hideBookButton()
    {
        booktripbtn.setEnabled(false);
    }

    public boolean checkSeatsRemaining()
    {
        if(gottenAllTrips.get(gottenTrip).getSeatsRemaining() == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void executeAdd()
    {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.addTripToUsersList(gottenAllTrips.get(gottenTrip), currentUser, new GetTripCallback() {
            @Override
            public void done(String returnedTrips)
            {
                Toast.makeText(getBaseContext(),"Booking Confirmed!",Toast.LENGTH_LONG).show();
                hideBookButton();

            }
        });
    }
}