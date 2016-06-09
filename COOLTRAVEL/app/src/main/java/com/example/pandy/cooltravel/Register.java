package com.example.pandy.cooltravel;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Register extends ActionBarActivity implements View.OnClickListener{

    Button Rregister;
    EditText fname, lname, age, Rusername, Rpassword;
    UserLocalStore userLocalStore;
    User newuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //init fields and actual register button
        fname = (EditText) findViewById(R.id.firstnametextbox);
        lname = (EditText) findViewById(R.id.lastnametextbox);
        Rusername = (EditText) findViewById(R.id.Rusernametextbox);
        Rpassword = (EditText) findViewById(R.id.Rpasswordtextbox);
        age = (EditText) findViewById(R.id.age);

        Rregister = (Button) findViewById(R.id.Rregister);
        Rregister.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Rregister:
                try
                {
                    //registering in happens here
                    String regfname = fname.getText().toString();
                    String reglname = lname.getText().toString();
                    String regusername = Rusername.getText().toString();
                    String regpassword = Rpassword.getText().toString();
                    int regage = Integer.parseInt(age.getText().toString());

                    if (regfname.trim().isEmpty() || regfname.isEmpty())
                    {
                        Toast.makeText(this, "Please enter your first name.", Toast.LENGTH_SHORT).show();
                        fname.requestFocus();
                    }
                    else if (reglname.trim().isEmpty() || reglname.isEmpty())
                    {
                        Toast.makeText(this, "Please enter your last name.", Toast.LENGTH_SHORT).show();
                        lname.requestFocus();
                    }
                    else if (regusername.trim().isEmpty() || regusername.isEmpty())
                    {
                        Toast.makeText(this, "Please enter a username.", Toast.LENGTH_SHORT).show();
                        Rusername.requestFocus();
                    }
                    else if (regpassword.trim().isEmpty() || regpassword.isEmpty())
                    {
                        Toast.makeText(this, "Please enter your last name.", Toast.LENGTH_SHORT).show();
                        Rpassword.requestFocus();
                    }
                    else if (regage < 0 || regage > 150)
                    {
                        Toast.makeText(this, "Please enter a valid age.", Toast.LENGTH_SHORT).show();
                        age.requestFocus();
                    }
                    else
                    {
                        newuser = new User(regfname,reglname,regusername,regpassword,regage);
                        checkUsername(newuser);
                    }
                }
                catch(Exception e)
                {
                    Toast.makeText(this, "Please check all fields are correctly filled.", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void registerUser(User user)
    {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                //do validation check through boolean field above

                startActivity(new Intent(Register.this, MainActivity.class));
            }
        });
    }

    public void checkUsername(User user)
    {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.checkRegisterUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                keepUsername(returnedUser);
                compare();
            }
        });
    }

    public void compare()
    {
        String usedUsername = userLocalStore.getUsedUsername();
        if(!usedUsername.equals(newuser.getUsername()))
        {
            registerUser(newuser);
            //sync of callback of async task
        }
        else
        {
            Toast.makeText(this, "Username taken! Please choose a different username", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Register.this, Register.class));
            userLocalStore.clearUserData();
        }

    }

    public void keepUsername(User user)
    {
        userLocalStore.storeUsedUsername(user);
    }
}
