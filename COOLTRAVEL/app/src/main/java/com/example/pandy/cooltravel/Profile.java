package com.example.pandy.cooltravel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Profile extends ActionBarActivity implements View.OnClickListener{

    Button logout, saveNewProfile;
    EditText fname, lname, username, password, age;
    UserLocalStore userLocalStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fname = (EditText) findViewById(R.id.firstnametextbox);
        lname = (EditText) findViewById(R.id.lastnametextbox);
        username = (EditText) findViewById(R.id.usernametextbox);
        password = (EditText) findViewById(R.id.passwordtextbox);
        age = (EditText) findViewById(R.id.age);

        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(this);

        saveNewProfile = (Button) findViewById(R.id.saveProfile);
        saveNewProfile.setOnClickListener(this);


        userLocalStore = new UserLocalStore(this);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
            builder.setMessage("Are you sure you wish to log out?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    startloggingout();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){}});
            AlertDialog dialog = builder.create();
            dialog.show();
            }
        });
    }

    private void startloggingout() {
        userLocalStore.clearUserData();
        userLocalStore.setUserLoggedIn(false);
        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
        finishAffinity();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.saveProfile:

                try
                {
                    String newfname = fname.getText().toString();
                    String newlname = lname.getText().toString();
                    String newusername = username.getText().toString();
                    String newpassword = password.getText().toString();
                    int newage = Integer.parseInt(age.getText().toString());

                    if (newfname.trim().isEmpty() || newfname.isEmpty())
                    {
                        Toast.makeText(this, "Please enter your first name.", Toast.LENGTH_SHORT).show();
                        fname.requestFocus();
                    }
                    else if (newlname.trim().isEmpty() || newlname.isEmpty())
                    {
                        Toast.makeText(this, "Please enter your last name.", Toast.LENGTH_SHORT).show();
                        lname.requestFocus();
                    }
                    else if (newpassword.trim().isEmpty() || newpassword.isEmpty())
                    {
                        Toast.makeText(this, "Please enter your last name.", Toast.LENGTH_SHORT).show();
                        password.requestFocus();
                    }
                    else if (newage < 0)
                    {
                        Toast.makeText(this, "Please enter a valid age.", Toast.LENGTH_SHORT).show();
                        age.requestFocus();
                    }
                    else
                    {
                        User newDetails = new User(newfname,newlname,newusername,newpassword,newage);
                        userLocalStore.clearUserData();
                        userLocalStore.storeUserData(newDetails);
                        displayUserDetails();
                        updateDBProfile(newDetails);
                        Toast.makeText(this, "Details Updated!", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(this, "Please check all fields are correctly filled.", Toast.LENGTH_SHORT).show();
                }

        }
    }

    /**
     * Dispatch onStart() to all fragments.  Ensure any created loaders are
     * now started.
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        if (authenticate() == true)
        {
            displayUserDetails();
        }
    }

    private boolean authenticate() {
        if (userLocalStore.getLoggedInUser() == null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return false;
        }
        return true;
    }



    public void displayUserDetails()
    {
        User user = userLocalStore.getLoggedInUser();
        fname.setText(user.fname);
        lname.setText(user.lname);
        username.setText(user.username);
        password.setText(user.password);
        age.setText(user.age + "");

    }

    public void updateDBProfile(User newUser)
    {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.updateUserDataInBackground(newUser, new GetUserCallback() {
            @Override
            public void done(User returnedUser)
            {

            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_profile, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
