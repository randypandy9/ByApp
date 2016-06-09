package com.example.pandy.cooltravel;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by pandy on 03/07/2015.
 */

public class ServerRequest {
    ProgressDialog progressDialog;


    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://imranali.uphero.com/";
    //public static final String SERVER_ADDRESS = "http://imranali.x10host.com/";

    public ServerRequest(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(User user, GetUserCallback callback) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, callback).execute();
    }

    public void fetchUserDataInBackground(User user, GetUserCallback callback) {
        progressDialog.show();
        new FetchUserDataAsyncTask(user, callback).execute();
    }

    public void updateUserDataInBackground(User user, GetUserCallback callback) {
        progressDialog.show();
        new UpdateUserDataAsyncTask(user, callback).execute();
    }

    public void checkRegisterUserDataInBackground(User user, GetUserCallback callback) {
        progressDialog.show();
        new CheckRegisterUserDataAsyncTask(user, callback).execute();
    }

    public void getAllTripsDataInBackground(Trip trip, GetTripCallback callback) {
        progressDialog.show();
        new GetAllTripsDataAsyncTask(trip, callback).execute();
    }

    public void addTripToUsersList(Trip trip, User user, GetTripCallback callback) {
        progressDialog.show();
        new AddTripToUsersList(trip, user, callback).execute();
    }

    public void getTripToUserInBackground(User user, GetTripCallback callback) {
        progressDialog.show();
        new GetTripToUserInBackground(user, callback).execute();
    }

    public void addTripDataInBackground(Trip trip, User user, Vehicle vehicle,GetTripCallback callback) {
        progressDialog.show();
        new AddTripDataInBackground(trip, user,vehicle, callback).execute();
    }

    public void updateVehicleDataInBackground(User user, Vehicle vehicle,GetVehicleCallback callback) {
        progressDialog.show();
        new UpdateVehicleDataInBackground(user,vehicle,callback).execute();
    }

    public void getVehicleDataInBackground(User user, GetVehicleCallback callback) {
        progressDialog.show();
        new GetVehicleDataInBackground(user, callback).execute();
    }

    public void getCreatedTripsInBackground(User user,GetTripCallback callback) {
        progressDialog.show();
        new GetCreatedTripsInBackground(user, callback).execute();

    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        User user;
        GetUserCallback callback;

        public StoreUserDataAsyncTask(User user, GetUserCallback callback) {
            this.user = user;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<BasicNameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("fname", user.fname));
            dataToSend.add(new BasicNameValuePair("lname", user.lname));
            dataToSend.add(new BasicNameValuePair("username", user.username));
            dataToSend.add(new BasicNameValuePair("password", user.password));
            dataToSend.add(new BasicNameValuePair("age", user.age + ""));

            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParam);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "Register.php");
            //test starts here
            //a > 'no' initialised and didn't reach php file
            //a > returns as 'done' means connection made and statement executed (or made it to end
            //of php file at least)

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);
//                HttpEntity entity = httpResponse.getEntity();
//                String val = EntityUtils.toString(entity);
//                JSONObject jObject = new JSONObject(val);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            callback.done(null);

        }
    }

    public class FetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallback callback;

        public FetchUserDataAsyncTask(User user, GetUserCallback callback) {
            this.user = user;
            this.callback = callback;
        }

        @Override
        protected User doInBackground(Void... params) {
            //error here. can't pass user details through its always empty hard coded a and a

            //Post data
            ArrayList<BasicNameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", user.username));
            dataToSend.add(new BasicNameValuePair("password", user.password));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchUserData.php");

            User returnedUser = new User();

            //Encoding Post Data
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));


                //errors here too
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() != 0) {


                    String fname = jObject.getString("fname");
                    String lname = jObject.getString("lname");
                    String username = jObject.getString("username");
                    String password = jObject.getString("password");
                    int age = jObject.getInt("age");


                    //errors here?
                    returnedUser.fname = fname;
                    returnedUser.lname = lname;
                    returnedUser.username = username;
                    returnedUser.password = password;
                    returnedUser.age = age;

                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException f) {
                f.printStackTrace();
            }

            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            super.onPostExecute(returnedUser);
            progressDialog.dismiss();
            callback.done(returnedUser);

        }
    }

    public class UpdateUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        User user;
        GetUserCallback callback;

        public UpdateUserDataAsyncTask(User user, GetUserCallback callback) {
            this.user = user;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<BasicNameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("fname", user.fname));
            dataToSend.add(new BasicNameValuePair("lname", user.lname));
            dataToSend.add(new BasicNameValuePair("username", user.username));
            dataToSend.add(new BasicNameValuePair("password", user.password));
            dataToSend.add(new BasicNameValuePair("age", user.age + ""));

            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParam);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "UserUpdate.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);
//                HttpEntity entity = httpResponse.getEntity();
//                String val = EntityUtils.toString(entity);
//                JSONObject jObject = new JSONObject(val);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            callback.done(null);

        }
    }

    public class CheckRegisterUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallback callback;

        public CheckRegisterUserDataAsyncTask(User user, GetUserCallback callback) {
            this.user = user;
            this.callback = callback;
        }

        @Override
        protected User doInBackground(Void... params) {
            ArrayList<BasicNameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", user.username));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "CheckUser.php");

            User returnedUser = new User();

            //Encoding Post Data
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));

                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() != 0) {


                    String fname = jObject.getString("fname");
                    String lname = jObject.getString("lname");
                    String username = jObject.getString("username");
                    String password = jObject.getString("password");
                    int age = jObject.getInt("age");


                    //errors here?
                    returnedUser.fname = fname;
                    returnedUser.lname = lname;
                    returnedUser.username = username;
                    returnedUser.password = password;
                    returnedUser.age = age;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException f) {
                f.printStackTrace();
            }

            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            super.onPostExecute(returnedUser);
            progressDialog.dismiss();
            callback.done(returnedUser);
        }

    }

    public class GetAllTripsDataAsyncTask extends AsyncTask<Void, Void, String> {
        Trip trip;
        GetTripCallback callback;

        public GetAllTripsDataAsyncTask(Trip trip, GetTripCallback callback) {
            this.trip = trip;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchTrips.php");

            String returnedTrips = "";

            //Encoding Post Data
            try {

                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                returnedTrips = result;

            }
            catch (IOException f) {
                f.printStackTrace();
            }
            return returnedTrips;
        }

        @Override
        protected void onPostExecute(String returnedTrips) {
            super.onPostExecute(returnedTrips);
            progressDialog.dismiss();
            callback.done(returnedTrips);

        }
    }

    public class AddTripToUsersList extends AsyncTask<Void, Void, Void> {
        Trip trip;
        User user;
        GetTripCallback callback;

        public AddTripToUsersList(Trip trip, User user, GetTripCallback callback) {
            this.trip = trip;
            this.user = user;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<BasicNameValuePair> dataToSend = new ArrayList<>();

            dataToSend.add(new BasicNameValuePair("trip_id", trip.getTrip_id()+""));
            dataToSend.add(new BasicNameValuePair("username", user.getUsername()));

            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParam);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "AddTripToUser.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);
//                HttpEntity entity = httpResponse.getEntity();
//                String val = EntityUtils.toString(entity);
//                JSONObject jObject = new JSONObject(val);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            String done = "done";
            callback.done(done);

        }
    }

    public class AddTripDataInBackground extends AsyncTask<Void, Void, Void> {
        User user;
        GetTripCallback callback;
        Trip trip;
        Vehicle vehicle;

        public AddTripDataInBackground(Trip trip, User user, Vehicle vehicle, GetTripCallback callback) {
            this.user = user;
            this.callback = callback;
            this.trip = trip;
            this.vehicle = vehicle;
        }

        @Override
        protected Void doInBackground(Void... params) {


            trip.setDetails(vehicle.toString() + "\nDriver: " + user.getFname() + " " + user.getLname() + "\n\n" + trip.getDetails());
            ArrayList<BasicNameValuePair> dataToSend = new ArrayList<>();


            dataToSend.add(new BasicNameValuePair("origin", trip.getOrigin()));
            dataToSend.add(new BasicNameValuePair("destination", trip.getDestination()));
            dataToSend.add(new BasicNameValuePair("price", trip.getPrice()+""));
            dataToSend.add(new BasicNameValuePair("seats", trip.getSeats()+""));
            dataToSend.add(new BasicNameValuePair("seatsRemaining", trip.getSeatsRemaining()+""));
            dataToSend.add(new BasicNameValuePair("details", trip.getDetails()));
            dataToSend.add(new BasicNameValuePair("date", trip.getDate()));
            dataToSend.add(new BasicNameValuePair("username", user.getUsername()));


            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParam);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "NewAddTrip.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            String done = "done";
            callback.done(done);
        }
    }

    public class UpdateVehicleDataInBackground extends AsyncTask<Void, Void, Void> {
        User user;
        GetVehicleCallback callback;
        Vehicle vehicle;

        public UpdateVehicleDataInBackground(User user, Vehicle vehicle, GetVehicleCallback callback) {
            this.user = user;
            this.callback = callback;
            this.vehicle = vehicle;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<BasicNameValuePair> dataToSend = new ArrayList<>();

            dataToSend.add(new BasicNameValuePair("username", user.getUsername()));
            dataToSend.add(new BasicNameValuePair("make", vehicle.getMake()));
            dataToSend.add(new BasicNameValuePair("model", vehicle.getModel()));
            dataToSend.add(new BasicNameValuePair("plate", vehicle.getPlate()));

            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParam);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "UpdateVehicle.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    public class GetVehicleDataInBackground extends AsyncTask<Void, Void, Vehicle> {
        User user;
        GetVehicleCallback callback;

        public GetVehicleDataInBackground(User user, GetVehicleCallback callback) {
            this.user = user;
            this.callback = callback;
        }

        @Override
        protected Vehicle doInBackground(Void... params) {

            ArrayList<BasicNameValuePair> dataToSend = new ArrayList<>();

            dataToSend.add(new BasicNameValuePair("username", user.getUsername()));

            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParam);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "GetVehicle.php");

            Vehicle vehicle = new Vehicle();

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() != 0)
                {
                    int vehicle_id = jObject.getInt("vehicle_id");
                    String make = jObject.getString("make");
                    String model = jObject.getString("model");
                    String plate = jObject.getString("plate");


                    vehicle.setVehicle_id(vehicle_id);
                    vehicle.setMake(make);
                    vehicle.setModel(model);
                    vehicle.setPlate(plate);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return vehicle;
        }

        @Override
        protected void onPostExecute(Vehicle vehicle) {
            super.onPostExecute(vehicle);
            progressDialog.dismiss();
            callback.done(vehicle);
        }
    }

    public class GetTripToUserInBackground extends AsyncTask<Void, Void, String> {
        User user;
        GetTripCallback callback;

        public GetTripToUserInBackground(User user, GetTripCallback callback) {
            this.user = user;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... params) {


            ArrayList<BasicNameValuePair> dataToSend = new ArrayList<>();

            dataToSend.add(new BasicNameValuePair("username", user.getUsername()));
            Log.d("sent: ", user.getUsername());


            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParam);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "GetMyTrips.php");

            String returnedTrips = "";

            //Encoding Post Data
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                returnedTrips = result;
            } catch (IOException f) {
                f.printStackTrace();
            }
            Log.d("got: ", returnedTrips);
            return returnedTrips;
        }

        @Override
        protected void onPostExecute(String returnedTrips) {
            super.onPostExecute(returnedTrips);
            progressDialog.dismiss();
            callback.done(returnedTrips);

        }

    }

    public class GetCreatedTripsInBackground extends AsyncTask<Void, Void, String> {
        User user;
        GetTripCallback callback;

        public GetCreatedTripsInBackground(User user, GetTripCallback callback) {
            this.user = user;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... params) {


            ArrayList<BasicNameValuePair> dataToSend = new ArrayList<>();

            dataToSend.add(new BasicNameValuePair("username", user.getUsername()));
            Log.d("sent: ", user.getUsername());


            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParam);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "GetCreatedTrips.php");

            String returnedTrips = "";

            //Encoding Post Data
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                returnedTrips = result;
            } catch (IOException f) {
                f.printStackTrace();
            }
            Log.d("got: ", returnedTrips);
            return returnedTrips;
        }

        @Override
        protected void onPostExecute(String returnedTrips) {
            super.onPostExecute(returnedTrips);
            progressDialog.dismiss();
            callback.done(returnedTrips);

        }
    }
}
//MAPS
//deletion of trips
//need connection to get all users for driver to see whos in their trip
//add rating system