package com.example.enrique.rfid_app;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.*;
import java.lang.*;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import android.os.AsyncTask;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String TAG = MapsActivity.class.getSimpleName();

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        contactList = new ArrayList<>();
        //new GetContacts().execute();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;





        //Getting the info JSON
        class GetContacts extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(MapsActivity.this, "Json Data is " +
                        "downloading", Toast.LENGTH_LONG).show();
                contactList = new ArrayList<>();

            }

            @Override
            protected Void doInBackground(Void... arg0) {
                HttpHandler sh = new HttpHandler();
                // Making a request to url and getting response
                String url = "https://data.sparkfun.com/output/YGaXdmAVl7tjl7OxpRzd.json";
                String jsonStr = sh.makeServiceCall(url);

                Log.e(TAG, "Response from url: " + jsonStr);
                if (jsonStr != null) {
                    try {
                        //JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        JSONArray contacts = new JSONArray(jsonStr);

                        // looping through All Contacts
                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject c = contacts.getJSONObject(i);
                            String id = c.getString("id");
                            String lat = c.getString("latitude");
                            String lon = c.getString("longitude");
                            String city = c.getString("city");
                            String region = c.getString("region");
                            String timestamp = c.getString("timestamp");


                            // tmp hash map for single contact
                            HashMap<String, String> contact = new HashMap<>();

                            // adding each child node to HashMap key => value
                            contact.put("id", id);
                            contact.put("timestamp", timestamp);
                            contact.put("latitude", lat);
                            contact.put("longitude", lon);
                            contact.put("city", city);
                            contact.put("region", region);

                            // adding contact to contact list (Not needed in MAPS)
                            contactList.add(contact);
                        }
                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                } else {
                    Log.e(TAG, "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);


                String Latty = "", oldLatty = "";
                String Longy = "", oldLongy = "";
                String Timy = "", oldTimy = "";
                String Id = "", oldId = "";

                //Toast.makeText(MapsActivity.this, "Loading " +
                        //"Marks", Toast.LENGTH_LONG).show();

                //ITERATOR for Hash List
                for (int a = 0; a < contactList.size(); a++)
                //for (int a = 0; a < 2; a++)
                {
                    HashMap<String, String> tmpData = (HashMap<String, String>) contactList.get(a);
                    Set<String> key = tmpData.keySet();
                    Iterator it = key.iterator();

                    //Toast.makeText(MapsActivity.this, "Decending " +
                            //" Array List", Toast.LENGTH_LONG).show();


                    while (it.hasNext()) {
                        String hmKey = (String)it.next();
                        String hmData = (String) tmpData.get(hmKey);

                        if(hmKey == "id"){Id = hmData;}
                        if(hmKey == "latitude"){Latty = hmData;}
                        if(hmKey == "longitude"){Longy = hmData;}
                        if(hmKey == "timestamp") {Timy = hmData;}
                        if (Latty != oldLatty && Longy != oldLongy && Timy != oldTimy && Id != oldId){
                            LatLng markermark = new LatLng(Double.parseDouble(Latty), Double.parseDouble(Longy));
                            mMap.addMarker(new MarkerOptions().position(markermark).title(Timy).snippet("UID = "+Id));

                            oldLatty = Latty;
                            oldLongy = Longy;
                            oldTimy = Timy;
                            oldId = Id;
                        }

                        /*while (it.hasNext()) {
                            String hmKey = (String)it.next();
                            String hmData = (String) tmpData.get(hmKey);

                            if(hmKey == "latitude"){Latty = hmData;}
                            if(hmKey == "longitude"){Longy = hmData;}
                            if(hmKey == "timestamp") {Timy = hmData;}
                            if (Latty != oldLatty && Longy != oldLongy && Timy != oldTimy){
                                LatLng markermark = new LatLng(Double.parseDouble(Latty), Double.parseDouble(Longy));
                                mMap.addMarker(new MarkerOptions().position(markermark).title(Timy));

                                oldLatty = Latty;
                                oldLongy = Longy;
                                oldTimy = Timy;
                            }*/

                        it.remove(); // avoids a ConcurrentModificationException

                    }

                }

            }

        }

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);

        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(markermark));

        new GetContacts().execute();
    }

}