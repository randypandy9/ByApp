package com.example.pandy.cooltravel;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    Button login, register;
    EditText Lusernametextbox, Lpasswordtextbox;
    UserLocalStore userLocalStore;

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                //go to register activity
                startActivity(new Intent(this, Register.class));
                break;
            case R.id.login:
               //logging in happens here
                String username = Lusernametextbox.getText().toString();
                String password = Lpasswordtextbox.getText().toString();

                if (username.trim().isEmpty() || username.isEmpty())
                {
                    Toast.makeText(this, "Please enter your username.", Toast.LENGTH_SHORT).show();
                }
                else if (password.trim().isEmpty() || password.isEmpty())
                {
                    Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    User user = new User(username,password);

                    authenticate(user);
                    userLocalStore.storeUserData(user);
                    userLocalStore.setUserLoggedIn(true);
                    break;
                }
        }
    }

    public void authenticate(User user) {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.fetchUserDataInBackground(user, new GetUserCallback()
        {
            @Override
            public void done(User returnedUser) {
                if (returnedUser.getFname() == null)
                {
                    showError();
                }
                else
                {
                    logUserIn(returnedUser);
                }

            }
        });
    }

    public void showError() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setMessage("Incorrect User Details!");
        dialogBuilder.setPositiveButton("ok", null);
        dialogBuilder.show();
    }

    public void logUserIn(User returnedUser) {
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        startActivity(new Intent(this, Mainmenu.class));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Lusernametextbox = (EditText) findViewById(R.id.Lusernametextbox);
        Lpasswordtextbox = (EditText) findViewById(R.id.Lpasswordtextbox);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
    }
}
