package com.example.pandy.cooltravel;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class MapsActivity extends FragmentActivity {

    private GoogleMap map; // Might be null if Google Play services APK is not available.
    private static Marker marker = null;

    private static Marker Origmarker = null;
    private static Marker Destmarker = null;

    private static LatLng OriglatLng;
    private static LatLng DestlatLng;

    private static LatLng destinationPoints;
    private static Polyline polyline;

    public static void setDestinationPoint(LatLng point){
        destinationPoints = point;
    }

    public LatLng getDestinationPoints(){
        return destinationPoints;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        LatLng temp;
        String x = getIntent().getExtras().getString("x");
        String y = getIntent().getExtras().getString("y");

        List<Address> originAddress = new ArrayList<>();
        List<Address> destinationAddress = new ArrayList<>();
        Geocoder geo = new Geocoder(this);
        try
        {
            originAddress = geo.getFromLocationName(x, 1);
            destinationAddress = geo.getFromLocationName(y, 1);
        }
        catch (IOException e)
        {
            Toast.makeText(this, "Could not recognise origin and destination addresses!",Toast.LENGTH_SHORT).show();
        }
        setUpMapIfNeeded();
        if (originAddress.isEmpty() || destinationAddress.isEmpty())
        {
            Toast.makeText(getBaseContext(), "Could not recognise origin and destination addresses!", Toast.LENGTH_SHORT).show();
            setUpMapIfNeeded();
        }
        else
        {
            Address Origaddress = originAddress.get(0);
            Address Destaddress = destinationAddress.get(0);
            OriglatLng = new LatLng(Origaddress.getLatitude(), Origaddress.getLongitude());
            DestlatLng = new LatLng(Destaddress.getLatitude(), Destaddress.getLongitude());

            if (Origmarker != null || Destmarker != null)
            {
                Origmarker.remove();
                Origmarker = map.addMarker(new MarkerOptions().position(OriglatLng).title("Origin"));
                setDestinationPoint(OriglatLng);
                directionsAtoB(destinationPoints);
                Destmarker.remove();
                Destmarker = map.addMarker(new MarkerOptions().position(DestlatLng).title("Destination"));
                setDestinationPoint(OriglatLng);
                directionsAtoB(destinationPoints);
            }
            else
            {
                Origmarker = map.addMarker(new MarkerOptions().position(OriglatLng).title("Origin"));
                setDestinationPoint(OriglatLng);
                directionsAtoB(destinationPoints);
                Destmarker = map.addMarker(new MarkerOptions().position(DestlatLng).title("Destination"));
                setDestinationPoint(OriglatLng);
                directionsAtoB(destinationPoints);
            }


            LatLngBounds.Builder b = new LatLngBounds.Builder();

            b.include(Origmarker.getPosition());
            b.include(Destmarker.getPosition());

            LatLngBounds bounds = b.build();
//Change the padding as per needed
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 750, 750, 250);
            map.animateCamera(cu);

//            CameraUpdate center = CameraUpdateFactory.newLatLng(OriglatLng);
//            CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
//
//            map.moveCamera(center);
//            map.animateCamera(zoom);

        }
        setUpMapIfNeeded();
    }



    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Shows the location searched for
     *
     * @param view
     */
    public void onSearch(View view) {
        EditText location_TF = (EditText) findViewById(R.id.TFaddress);
        String location = location_TF.getText().toString();
        List<Address> addressList = null;

        if (location != null && !location.equals("") && !location.isEmpty())
        {
            Geocoder geocoder = new Geocoder(this);
            try
            {
                addressList = geocoder.getFromLocationName(location, 1);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            if (addressList.isEmpty())
            {
                Toast.makeText(getBaseContext(), "Please enter a valid search term.", Toast.LENGTH_SHORT).show();
                setUpMapIfNeeded();
            }
            else
            {

                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                if (marker != null)
                {
                    marker.remove();
                    marker = map.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                    setDestinationPoint(latLng);
                    directionsAtoB(destinationPoints);
                }
                else
                {
                    marker = map.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                    setDestinationPoint(latLng);
                    directionsAtoB(destinationPoints);
                }
                //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));


                CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);

                map.moveCamera(center);
                map.animateCamera(zoom);
            }
        }
        else
        {
            Toast.makeText(getBaseContext(), "Please enter a search term.", Toast.LENGTH_SHORT).show();
            setUpMapIfNeeded();
        }
    }

    public void changeType(View view) {
        if (map.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

//    public void onZoom(View view) {
//        if (view.getId() == R.id.Bzoomin) {
//            mMap.animateCamera(CameraUpdateFactory.zoomIn());
//        }
//        if (view.getId() == R.id.Bzoomout) {
//            mMap.animateCamera(CameraUpdateFactory.zoomOut());
//        }
//    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #map} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (map != null) {
                setUpMap();
            }
        }
        //route plan
        directionsAtoB(destinationPoints);
    }

    public void directionsAtoB(final LatLng destin) {

        if (map != null) {

// Enable MyLocation Button in the Map
            map.setMyLocationEnabled(true);

// Setting onclick event listener for the map
            map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location point) {

                    Location tempOrigin = point; //map.getMyLocation();
                    LatLng originCoordinates = new LatLng(tempOrigin.getLatitude(), tempOrigin.getLongitude());


                    LatLng origin = (LatLng) originCoordinates;
                    LatLng dest = (LatLng) destin;

// Getting URL to the Google Directions API
                    String url = getDirectionsUrl(OriglatLng, DestlatLng);

                    DownloadTask downloadTask = new DownloadTask();

// Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                }

            });
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near UEA. This method is called at the start
     * <p/>
     * This should only be called once and when we are sure that {@link #map} is not null.
     */
    private void setUpMap() {
        //set a marker
        LatLng latLng = new LatLng(52.6219247, 1.2369874);

        //lock camera in the uea marker and zoom in
        CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);

        map.moveCamera(center);
        map.animateCamera(zoom);
        //gps location
        map.setMyLocationEnabled(true);

    }

    /**
     * @param origin coordinates of origin
     * @param dest   coordinates of destination
     * @return returns the directions url as a string
     */
    public String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // travelling mode
        String mode = "mode=walking";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    protected String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception url load", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>
    {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            if (polyline != null) {
                polyline.remove();
            }

            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.GREEN);


            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                polyline = map.addPolyline(lineOptions);
            }else{
                Toast.makeText(getBaseContext(), "Route plan to this location is not available", Toast.LENGTH_SHORT).show();
                setUpMapIfNeeded();
            }
        }
    }
}