package com.example.pandy.cooltravel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateTrip extends AppCompatActivity {

    Button addTripBtn;
    UserLocalStore userLocalStore;
    Vehicle currentVehicle;
    EditText origin,destination,details,date,price, pricepennies ,seats,seatsRemaining;
    Trip trip;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        userLocalStore = new UserLocalStore(this);

        user = userLocalStore.getLoggedInUser();
        currentVehicle = userLocalStore.getCurrentVehicle();
        if(currentVehicle.getPlate().trim().isEmpty())
        {
            getMyVehicle();

        }


        origin = (EditText) findViewById(R.id.corigin);
        destination = (EditText) findViewById(R.id.cdestination);
        details = (EditText) findViewById(R.id.cdetails);
        date = (EditText) findViewById(R.id.cdate);
        price = (EditText) findViewById(R.id.cprice);
        seats = (EditText) findViewById(R.id.cseats);
        seatsRemaining = (EditText) findViewById(R.id.cseatsremaining);
        pricepennies = (EditText) findViewById(R.id.cpricepennies);


        addTripBtn = (Button) findViewById(R.id.addtripbtn);
        addTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                addTrip();
            }
        });

    }

    public void getMyVehicle() {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.getVehicleDataInBackground(user, new GetVehicleCallback() {
            @Override
            public void done(Vehicle returnedVehicle) {
                currentVehicle = returnedVehicle;
                secondaryCheck();
            }
        });
    }

    public void secondaryCheck()
    {
        if(currentVehicle.getPlate().trim().isEmpty())
        {
            Toast.makeText(this,"Oops! You need to register a vehicle before creating a trip.",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, VehicleDetails.class));
            finish();
        }
    }

    public void addTrip()
    {
        if(currentVehicle.getPlate().isEmpty())
        {
            Toast.makeText(this, "You have no registered vehicle, Please add one before creating a trip.",Toast.LENGTH_LONG).show();
        }
        else
        {
            try
            {
                String Origin = origin.getText().toString();
                String Destination = destination.getText().toString();
                String joinedPrice = price.getText().toString().trim()+pricepennies.getText().toString().trim();
                int Price = Integer.parseInt(joinedPrice);
                int Seats = Integer.parseInt(seats.getText().toString());
                int SeatsRemaining = Integer.parseInt(seatsRemaining.getText().toString());
                String Details = details.getText().toString();
                String Date = date.getText().toString();
                trip = new Trip(Origin, Destination, Price, Seats, SeatsRemaining, Details, Date);

                ServerRequest serverRequest = new ServerRequest(this);
                serverRequest.addTripDataInBackground(trip, user, currentVehicle, new GetTripCallback() {
                    @Override
                    public void done(String returnedTrips) {
                        Toast.makeText(getBaseContext(), "Trip Added!", Toast.LENGTH_SHORT).show();
                    }
                });
                startActivity(new Intent(this, Mainmenu.class));
            }
            catch (Exception e)
            {
                Toast.makeText(this, "Please ensure all fields are correctly filled.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
