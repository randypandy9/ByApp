package com.example.pandy.cooltravel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Mainmenu extends AppCompatActivity implements View.OnClickListener{

    UserLocalStore userLocalStore;
    Button taketrip, maketrip, profilebtn, vehicleDetailsbtn, myTripsbtn, createdTripsbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        userLocalStore = new UserLocalStore(this);
        taketrip = (Button) findViewById(R.id.taketripbutton);
        maketrip = (Button) findViewById(R.id.maketripbutton);
        profilebtn = (Button) findViewById(R.id.menuprofilebtn);
        vehicleDetailsbtn = (Button) findViewById(R.id.vehicleBtn);
        myTripsbtn = (Button) findViewById(R.id.mytripsbtn);
        createdTripsbtn = (Button) findViewById(R.id.createdtripsbtn);
        taketrip.setOnClickListener(this);
        maketrip.setOnClickListener(this);
        profilebtn.setOnClickListener(this);
        vehicleDetailsbtn.setOnClickListener(this);
        myTripsbtn.setOnClickListener(this);
        createdTripsbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.maketripbutton:
                startActivity(new Intent(this, CreateTrip.class));

                break;
            case R.id.taketripbutton:
                startActivity(new Intent(this, TakeTrip.class));

                break;
            case R.id.menuprofilebtn:
                startActivity(new Intent(this, Profile.class));

                break;
            case R.id.vehicleBtn:
                startActivity(new Intent(this, VehicleDetails.class));

                break;
            case R.id.mytripsbtn:
                startActivity(new Intent(this, MyTrips.class));

                break;
            case R.id.createdtripsbtn:
                startActivity(new Intent(this, CreatedTrips.class));

                break;
        }
    }
}
