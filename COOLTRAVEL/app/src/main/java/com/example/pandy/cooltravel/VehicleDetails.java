package com.example.pandy.cooltravel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VehicleDetails extends AppCompatActivity {


    EditText make, model, plate;
    Button saveVehicleBtn;
    UserLocalStore userLocalStore;
    User currentUser;
    Vehicle vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);

        userLocalStore = new UserLocalStore(this);
        currentUser = userLocalStore.getLoggedInUser();
        make = (EditText) findViewById(R.id.maketxt);
        model = (EditText) findViewById(R.id.modeltxt);
        plate = (EditText) findViewById(R.id.platetxt);

//        vehicle = userLocalStore.getCurrentVehicle();
//        if(!vehicle.getMake().trim().isEmpty())
//        {
//            make.setText(vehicle.getMake());
//            model.setText(vehicle.getModel());
//            plate.setText(vehicle.getPlate());
//        }
//        else
//        {
//            getVehicle();
//        }
        getVehicle();


        saveVehicleBtn = (Button) findViewById(R.id.saveVehiclebtn);
        saveVehicleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(make.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getBaseContext(),"Enter your Make",Toast.LENGTH_SHORT).show();
                    make.requestFocus();
                }
                else if(model.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getBaseContext(),"Enter your Model",Toast.LENGTH_SHORT).show();
                    model.requestFocus();
                }
                else if(plate.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getBaseContext(),"Enter your Registration Plate",Toast.LENGTH_SHORT).show();
                    plate.requestFocus();
                }
                else
                {
                    updateVehicleLocal();
                    updateVehicle();
                }
            }
        });

    }

    public void updateVehicle() {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.updateVehicleDataInBackground(currentUser, vehicle, new GetVehicleCallback() {
            @Override
            public void done(Vehicle returnedVehicle) {
                Toast.makeText(getApplicationContext(), "Vehicle Updated!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateVehicleLocal()
    {
        vehicle = new Vehicle(make.getText().toString(),model.getText().toString(),plate.getText().toString());
        userLocalStore.storeCurrentVehicle(vehicle);
    }

    public void getVehicle(){
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.getVehicleDataInBackground(currentUser, new GetVehicleCallback() {
            @Override
            public void done(Vehicle returnedVehicle) {
                setUpVehicleDetails(returnedVehicle);
            }
        });
    }

    public void setUpVehicleDetails(Vehicle gotVehicle)
    {
        vehicle = gotVehicle;
        make.setText(gotVehicle.getMake());
        model.setText(gotVehicle.getModel());
        plate.setText(gotVehicle.getPlate());
    }
}
