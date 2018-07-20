package com.infoicon.powercoin.login;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.infoicon.powercoin.utils.HelperMethods;
import com.infoicon.powercoin.utils.Keys;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import infoicon.com.powercoin.R;

/**
 * Created by HP-PC on 11/25/2017.
 */

public class TrackingActivity extends AppCompatActivity implements OnMapReadyCallback {
    //Ui widgets


   private GoogleMap mMap;
int count=0;
    private static String TAG = "MAP LOCATION";
    Context mContext;
    private LatLng mCenterLatLong;


    /**
     * Receiver registered with this activity to get the response from FetchAddressIntentService.
     */
    /**
     * The formatted location address.
     */
    JSONObject sendJson;

    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;
    String current_city;
    String rider_image;

    private static final int REQUEST_CODE_PERMISSION = 2;
    String[] mPermission = {Manifest.permission.ACCESS_FINE_LOCATION};

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    String subLocality;

String deliveryLat,DeliveryLong;
    SupportMapFragment mapFragment;
   TextView time,name,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trackmap);
        getDriver();
getLocationFromAddress(getIntent().getStringExtra("address"));
 time=(TextView)findViewById(R.id.expected_time);
         name=(TextView)findViewById(R.id.tv_name);

         phone=(TextView)findViewById(R.id.tv_phone);

        mContext = this;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);


    }


    public String getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
        List<Address> address;


        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
        deliveryLat=String.valueOf(location.getLatitude());
            DeliveryLong=String.valueOf(location.getLongitude());


        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }




    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady");
        mMap = googleMap;



        Location location = new Location("Test");
        location.setLatitude(Double.valueOf(deliveryLat));
        location.setLongitude(Double.valueOf(DeliveryLong));
        Timer timer = new Timer();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {
                // your code here...
                getDriver();

            }
        };

// schedule the task to run starting now and then every hour...
        timer.schedule (hourlyTask, 0l, 30000);   // 1000*10*60 every 10 minut
     /*   mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Camera postion change" + "", cameraPosition + "");
                mCenterLatLong = cameraPosition.target;

                try {
                  // getCity(cityName,String.valueOf(mCenterLatLong.latitude),String.valueOf(mCenterLatLong.latitude));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/
    }


    private void changeMap(String lat,String lng)
    {
        Log.d(TAG, "Reaching map" + mMap);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
mMap.clear();
        // check if map is created successfully or not
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(false);
            LatLng latLong;
            latLong = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(14f).tilt(70).build();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
         /*   mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));*/
            try {
                //getCity(cityName,String.valueOf(mCenterLatLong.latitude),String.valueOf(mCenterLatLong.latitude));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }

    }




    private String getDirectionsUrl(String lat,String lng){



        String str_origin = "origin="+deliveryLat+","+DeliveryLong;

        String str_dest ="destination="+lat+","+lng;
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
        }finally{
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

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            TrackingActivity.ParserTask parserTask = new TrackingActivity.ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
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
            String distance = "";
            String duration = "";

            if(result.size()<1){
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);
                    if(j==0){    // Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        continue;
                    }
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
            }
            time.setText(""+duration+" to Reach");
           // tvDistanceDuration.setText("Distance:"+distance + ", Duration:"+duration);
            // Drawing polyline in the Google Map for the i-th route
            changeMap(driver_lat,driver_long);
            mMap.clear();
            mMap.addPolyline(lineOptions);
            setMarker();
        }
    }
    void setMarker()
    {

        // Origin of route
        //String str_origin = "origin="+mCurrentLocation.getLatitude()+","+mCurrentLocation.getLongitude();
        Location location = new Location("Test");
        location.setLatitude(Double.valueOf(deliveryLat));
        location.setLongitude(Double.valueOf(DeliveryLong));

        // Destination of route
        //  String str_dest ="destination=18.5204,73.8567";4

        // Creating a marker
        MarkerOptions markerOption = new MarkerOptions();

        // Setting the position for the marker
        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        markerOption.position(latLng);


        // Placing a marker on the touched position
        mMap.addMarker(markerOption);

        // Creating a marker
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting the position for the marker
        markerOptions.position(new LatLng(Double.parseDouble(driver_lat),Double.parseDouble(driver_long)));
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.scooter));
        // Placing a marker on the touched position
        mMap.addMarker(markerOptions);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

//the include method will calculate the min and max bound.
        builder.include(markerOption.getPosition());
        builder.include(markerOptions.getPosition());

        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen


        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);


        mMap.animateCamera(cu);

    }



    /**
     * Called after the autocomplete activity has finished to return its result.
     */

String driver_lat,driver_long;
    private void getDriver(){

        http://54.233.182.212/basketapi/application/customer?parameters=%7B%22method%22%3A%22CityListActivitysssssssssss%22%2C%22password%22%3A%2212345%22%2C%22new_password%22%3A%22123%22%2C%22user_id%22%3A1%7D
        try {
            sendJson = new JSONObject();
            sendJson.put("method", "getRiderList");
             sendJson.put("id", getIntent().getStringExtra("rider_id"));
           // sendJson.put("id", "13");
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Keys.BASE_URL+"application/index",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString="";//your json string here
                        try{
                            JSONObject jObjects= new JSONObject(response);
                            if(jObjects.getString("status").equalsIgnoreCase("success"))
                            {
                                JSONObject jObject = new JSONObject(response).getJSONObject("data");
                                Iterator<String> keys = jObject.keys();

                                while (keys.hasNext())
                                {
                                    String key = keys.next();
                                    JSONObject innerJObject = jObject.getJSONObject(key);
                                    name.setText(""+innerJObject.getString("name"));
                                    phone.setText(""+innerJObject.getString("mobile_number"));
                                    driver_lat=innerJObject.getString("lat");
                                            driver_long=innerJObject.getString("lng");
                                  //  changeMap(innerJObject.getString("lat"),innerJObject.getString("lng"));
                                    // Getting URL to the Google Directions API
                                    String url = getDirectionsUrl(innerJObject.getString("lat"),
                                            innerJObject.getString("lng"));
                                    try {

                                       rider_image = jObjects.getString("ROOTPATH") + "/" + jObjects.getString("image_type") + "/" + innerJObject.getString("image");
                                        ImageView rider_image = (ImageView) findViewById(R.id.riderimage);

                                        Glide.with(TrackingActivity.this).load(url)
                                                .thumbnail(0.5f)
                                                .crossFade()
                                                .placeholder(R.drawable.labimage)
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(rider_image);
                                    }catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                    DownloadTask downloadTask = new DownloadTask();
                                    // Start downloading json data from Google Directions API
                                    downloadTask.execute(url);
                                }
                             }
                            else
                            {
                                HelperMethods.showSnackBar(TrackingActivity.this,jObjects.getString("msg"));
                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    HelperMethods.showSnackBar(TrackingActivity.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    HelperMethods.showSnackBar(TrackingActivity.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    HelperMethods.showSnackBar(TrackingActivity.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    HelperMethods.showSnackBar(TrackingActivity.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    HelperMethods.showSnackBar(TrackingActivity.this,"Parse Error!");
                }

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String,String>();
                params.put("parameters",sendJson.toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }


}