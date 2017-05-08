package com.example.bm_admin.timekeeper;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import com.example.bm_admin.timekeeper.bean.Converter;
import com.example.bm_admin.timekeeper.utility.DirectionsJSONParser;
import com.example.bm_admin.timekeeper.utility.Utils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    String TAG = "MapsActivity";
    Marker marker;

    Geocoder geocoder;
    private StringBuilder sb = new StringBuilder();
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        Log.d(TAG, "map : " + mMap);
        geocoder = new Geocoder(this, Locale.getDefault());

        latLng = getIntent().getParcelableExtra("LATLNG");


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.w("#MAP ","onMapReady");

        mMap = googleMap;

        if (latLng != null)
            loadLocation(latLng);

        /*
        LatLng origin = new LatLng(3.214732, 101.747047);
        LatLng dest = new LatLng(3.214507, 101.749697);

        String url = getDirectionsUrl(origin, dest);
        Log.w("#MAP ",url);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);*/

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {


            @Override
            public void onMapClick(LatLng point) {
                try {
                    //save current location
                    LatLng latLng = point;
                    android.location.Address address = null;

                    List<Address> addresses = new ArrayList<>();
                    try {
                        addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                        address = addresses.get(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if (address != null) {
                        //   StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i) + "\n");
                        }
                        System.out.println("LOCALE 1:" + address.getAdminArea());
                        System.out.println("LOCALE 2:" + address.getLocality());
                        System.out.println("LOCALE 3:" + address.getFeatureName());
                        System.out.println("LOCALE 4:" + address.getSubAdminArea());
                        System.out.println("LOCALE 5:" + address.getSubLocality());
                        //Toast.makeText(MapsActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
                    }

                    //remove previously placed Marker
                    if (marker != null) {
                        marker.remove();
                    }
                    //place marker where user just clicked
                    marker = mMap.addMarker(new MarkerOptions().position(point).title("Marker")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                    Utils.setTimeZone(address.getCountryName());

                    Intent data = new Intent();
                    data.putExtra("LOCATION", sb.toString());
                    data.putExtra("LOCALE", Converter.gson.toJson(address));
                    data.putExtra("LATLNG", latLng);
                    data.putExtra("ID", 2);
                    setResult(155, data);
                    //---close the activity---
                    //finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadLocation(LatLng latLng) {
        Log.w("#LoadLocation Called", latLng.latitude + " : " + latLng.longitude);
        try {
            //save current location
            // = point;
            android.location.Address address = null;

            List<Address> addresses = new ArrayList<>();
            try {
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                address = addresses.get(0);
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (address != null) {
                //   StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i) + "\n");
                }
                System.out.println("LOCALE 1:" + address.getAdminArea());
                System.out.println("LOCALE 2:" + address.getLocality());
                System.out.println("LOCALE 3:" + address.getFeatureName());
                System.out.println("LOCALE 4:" + address.getSubAdminArea());
                System.out.println("LOCALE 5:" + address.getSubLocality());
                //Toast.makeText(MapsActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
            }

            //remove previously placed Marker
            if (marker != null) {
                marker.remove();
            }
            //place marker where user just clicked
            marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Marker")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

            Utils.setTimeZone(address.getCountryName());
               /*Intent intent = new Intent(getApplicationContext(), HomeFragement.class);
                sta*/
            Intent data = new Intent();
            data.putExtra("LOCATION", sb.toString());
            data.putExtra("LOCALE", Converter.gson.toJson(address));
            data.putExtra("LATLNG", latLng);
            data.putExtra("ID", 2);
            //setResult(155, data);
            //---close the activity---
            //finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        try {
            LatLng newLat = marker.getPosition();
            mMap.addMarker(new MarkerOptions().position(newLat).title("My Spot").snippet("This is my spot!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {


        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }


    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
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
            Log.d("Exception while url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

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
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
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
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }
}
