package com.example.bm_admin.timekeeper;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bm_admin.timekeeper.R;
import com.example.bm_admin.timekeeper.bean.Converter;
import com.example.bm_admin.timekeeper.fragments.HomeFragement;
import com.example.bm_admin.timekeeper.utility.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    String TAG = "MapsActivity";
    Marker marker;

    Geocoder geocoder;
    private StringBuilder sb = new StringBuilder();

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
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//
//
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.setOnMarkerClickListener(MapsActivity.this);

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

                    Utils.setTimeZone( address.getCountryName());
               /*Intent intent = new Intent(getApplicationContext(), HomeFragement.class);
                sta*/
                    Intent data = new Intent();
                    data.putExtra("LOCATION", sb.toString());
                    data.putExtra("LOCALE", Converter.gson.toJson(address));
                    data.putExtra("LATLNG", latLng);
                    data.putExtra("ID", 2);
                    setResult(155, data);
                    //---close the activity---
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
}
