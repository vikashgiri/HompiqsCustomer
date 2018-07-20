package com.infoicon.powercoin.login;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.infoicon.powercoin.bottomNavigation.App;
import com.infoicon.powercoin.bottomNavigation.CardListResponce;
import com.infoicon.powercoin.main.MainActivity;
import com.infoicon.powercoin.utils.HelperMethods;
import com.infoicon.powercoin.utils.Keys;
import com.infoicon.powercoin.utils.SavePref;
import com.infoicon.powercoin.utils.UpdateCardListInterface;

import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import infoicon.com.powercoin.R;

/**
 * Created by HP-PC on 11/25/2017.
 */

public class CityListActivitys extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,OnMapReadyCallback,UpdateCardListInterface,
        LocationListener,
        ResultCallback<LocationSettingsResult> {
    //Ui widgets



    @Override
    public void updateCard(ArrayList<CardListResponce> cardListResponceArrayList) {

        }


    //Any random number you can take
    public static final int REQUEST_PERMISSION_LOCATION = 10;

    /**
     * Constant used in the location settings dialog.
     */
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */


    private GoogleMap mMap;

    private static String TAG = "MAP LOCATION";
    Context mContext;
    private LatLng mCenterLatLong;


    /**
     * Receiver registered with this activity to get the response from FetchAddressIntentService.
     */
    private AddressResultReceiver mResultReceiver;
    /**
     * The formatted location address.
     */
    JSONObject sendJson;

    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;
    EditText mLocationAddress;
    String current_city;
 
    private static final int REQUEST_CODE_PERMISSION = 2;
    String[] mPermission = {Manifest.permission.ACCESS_FINE_LOCATION};

    Button mLocationText, btn_location;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    String subLocality;

    private String getCityNameByCoordinates(double lat, double lon) {
        Geocoder mGeocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses = null;

        try {
            addresses = mGeocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0)
        {
            subLocality = addresses.get(0).getSubLocality();
            return addresses.get(0).getLocality();
            /*subLocality = mLocationAddress.getText().toString();
            return mLocationAddress.getText().toString();*/
        }
        return null;
            }
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    // Keys for storing activity state in the Bundle.
    protected final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    protected final static String KEY_LOCATION = "location";
    protected final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    protected LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;
int updateLocation=0;
    // Labels.
    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    protected String mLastUpdateTimeLabel;
    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;
    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;
    protected int RQS_GooglePlayServices = 0;

    @Override
    protected void onStart() {
        super.onStart();
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(this);
        if (resultCode == ConnectionResult.SUCCESS) {
            mGoogleApiClient.connect();
        } else {
            googleAPI.getErrorDialog(this, resultCode, RQS_GooglePlayServices);
        }
    }

    @Override
    protected void onResume() {
         mapFragment.onResume();
         super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            //  Toast.makeText(FusedLocationWithSettingsDialog.this, "location was already on so detecting location now", Toast.LENGTH_SHORT).show();
            startLocationUpdates();
        }

            checkLocationSettings();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mapFragment.onPause();

        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }




    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    SupportMapFragment mapFragment;

    public static Activity citylistActivity;

   Button current_location,search_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);
current_location=(Button)findViewById(R.id.btn_location);
        citylistActivity=this;
        HelperMethods.getCartItems(CityListActivitys.this);

                search_location=(Button)findViewById(R.id.btn_select_location);
current_location.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(),"Service Not Available",Toast.LENGTH_SHORT).show();
    }
});

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Kick off the process of building the GoogleApiClient, LocationRequest, and
        // LocationSettingsRequest objects.

        //step 1
        buildGoogleApiClient();

        //step 2
        createLocationRequest();

        //step 3
        buildLocationSettingsRequest();
/*
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkLocationSettings();
            }
        });*/
        mContext = this;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mLocationAddress = (EditText) findViewById(R.id.Address);
        mLocationText = (Button) findViewById(R.id.btn_select_location);

        btn_location = (Button) findViewById(R.id.btn_location);


        mLocationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAutocompleteActivity();

            }


        });
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCity(getCityNameByCoordinates(mCenterLatLong.latitude, mCenterLatLong.longitude), String.valueOf(mCenterLatLong.latitude), String.valueOf(mCenterLatLong.longitude));


            }


        });
        mapFragment.getMapAsync(this);
        mResultReceiver = new CityListActivitys.AddressResultReceiver(new Handler());

    }

    //step 1
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
ProgressDialog pDialog;
    JSONObject manJson;
    
    //step 2
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your

        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    //step 3
    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }


    //step 4

    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }


    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
        } else {
            goAndDetectLocation();
        }

    }

    public void goAndDetectLocation() {
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
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = true;
                //     setButtonsEnabledState();
            }
        });
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,
                this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = false;
                //   setButtonsEnabledState();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goAndDetectLocation();
                }
                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
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
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateLocationUI();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onLocationChanged(Location location) {
if(location==null)
{
    return;
}
        mCurrentLocation = location;
        if(updateLocation<=0)
        {
            changeMap(location);
            updateLocationUI();
            updateLocation++;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    //step 5
    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {

        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");

                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    //

                    //move to step 6 in onActivityResult to check what action user has taken on settings dialog
                    status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }


    /**
     *	This OnActivityResult will listen when
     *	case LocationSettingsStatusCodes.RESOLUTION_REQUIRED: is called on the above OnResult
     */
    //step 6:

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                // Check that the result was from the autocomplete widget.
                if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
                    if (resultCode == RESULT_OK) {
                        // Get the user's selected place from the Intent.
                        Place place = PlaceAutocomplete.getPlace(mContext, data);
if(place==null)
{
    return;
}
                        // TODO call location based filter
                        LatLng latLong;
                        latLong = place.getLatLng();
                        Location mLocation = new Location("");
                        mLocation.setLatitude(latLong.latitude);
                        mLocation.setLongitude(latLong.longitude);
                        mCurrentLocation=mLocation;

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLong).zoom(15f).tilt(70).build();
                        mMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));

                    }

                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(mContext, data);
                } else if (resultCode == RESULT_CANCELED) {
                    // Indicates that the activity closed before a selection was made. For example if
                    // the user pressed the back button.
                }
        
                break;
        }
    }





    /**
     * Sets the value of the UI fields for the location latitude, longitude and last update time.
     */
    private void updateLocationUI()
    {
        if (mCurrentLocation != null) {
            /*mLatitudeTextView.setText(String.format("%s: %f", mLatitudeLabel,
                    mCurrentLocation.getLatitude()));
            mLongitudeTextView.setText(String.format("%s: %f", mLongitudeLabel,
                    mCurrentLocation.getLongitude()));
            mLastUpdateTimeTextView.setText(String.format("%s: %s", mLastUpdateTimeLabel,
                    mLastUpdateTime));
*/
            updateCityAndPincode(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
        }
    }


    /**
     *	This updateCityAndPincode method uses Geocoder api to map the latitude and longitude into city location or pincode.
     *	We can retrieve many details using this Geocoder class.
     *
     And yes the Geocoder will not work unless you have data connection or wifi connected to internet.
     */





    private void updateCityAndPincode(double latitude, double longitude)
    {
        try
        {
            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0)
            {
                mLocationAddress.setText(addresses.get(0).getSubLocality()+","+addresses.get(0).getLocality()+","+addresses.get(0).getAdminArea());

                //  System.out.println(addresses.get(0).getLocality());
            }

        }

        catch (Exception e)
        {
            Log.e(TAG,"exception:"+e.toString());
        }

    }






    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady");
        mMap = googleMap;

    mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Camera postion change" + "", cameraPosition + "");
                mCenterLatLong = cameraPosition.target;
                try {

                   Location mLocation = new Location("");
                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);
                    mCurrentLocation=mLocation;
updateLocationUI();
// getCity(cityName,String.valueOf(mCenterLatLong.latitude),String.valueOf(mCenterLatLong.latitude));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void changeMap(Location location) {

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

        // check if map is created successfully or not
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(false);
            LatLng latLong;


            latLong = new LatLng(location.getLatitude(), location.getLongitude());

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(15f).tilt(70).build();

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
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






    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            String address="";
            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(AppUtils.LocationConstants.RESULT_DATA_KEY);
            mAreaOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_AREA);
            mCityOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_CITY);
            mStateOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_STREET);

            mLocationAddress.setText(mStateOutput);
            if (resultCode == AppUtils.LocationConstants.SUCCESS_RESULT) {
                //  showToast(getString(R.string.address_found));


            }


        }

    }

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */

    private void getCity(String city, final String lat, final String lan)
    {
        pDialog = new ProgressDialog(CityListActivitys.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        try {
            manJson = new JSONObject();
            manJson.put("method", "getCityIdByAddressOrLatLng");
            manJson.put("address", city);
            manJson.put("lat", lat);
            manJson.put("lng", lan);
            manJson.put("user_id",  SavePref.getPref(CityListActivitys.this,SavePref.User_id));
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://52.67.139.216/lab/application/index?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString = "";//your json string here
                        try {
                           // {"status":"success","data":{"id":5,"city_name":"Bengaluru","city_synonym":"banglore","country_id":1,"created_on":"2018-03-20 22:58:43","updated_on":"2018-05-06 19:24:29"},"service":{"status":"success"}}
                            JSONObject Object = new JSONObject(response);
                            JSONObject jObject = new JSONObject(response).getJSONObject("data");
                            // 77.59456269443035
                            // 12.97161246899558
                            if (Object.getString("status").equalsIgnoreCase("success"))
                            {

                                if (((App) getApplication()).getCardList().size() > 0 &&
                                        !SavePref.getPref(CityListActivitys.this, SavePref.city_id).equalsIgnoreCase(jObject.getString("id"))) {
                                    pDialog.dismiss();
                                    Dialogs(jObject, lat, lan);
                                    return;
                                }
                            }
if(new JSONObject(response).has("service")) {
    JSONObject servicejObject = new JSONObject(response).getJSONObject("service");
    if (!servicejObject.getString("status").equalsIgnoreCase("success"))
    {
        HelperMethods
                .showSnackBar(CityListActivitys.this, "Service Not Available in this city");
        pDialog.dismiss();
        return;
    }
}



                            if (Object.getString("status").equalsIgnoreCase("success"))
                            {

                                if (!((App) getApplication()).getCardList().isEmpty() &&
                                    !SavePref.getPref(CityListActivitys.this,SavePref.city_id).equalsIgnoreCase( jObject.getString("id")))
                                {
                                    Dialogs(jObject, lat, lan);
                                }
                                else
                                {
                                    Iterator<String> keys = jObject.keys();
                                    String key = keys.next();
                                    //JSONObject innerJObject = jObject.getJSONObject(key);
                                    SavePref.saveStringPref(CityListActivitys.this, SavePref.current_lat, String.valueOf(lat));
                                    SavePref.saveStringPref(CityListActivitys.this, SavePref.current_lon, lan);
                                    SavePref.saveStringPref(CityListActivitys.this, SavePref.City_name, jObject.getString("city_name"));
                                    SavePref.saveStringPref(CityListActivitys.this, SavePref.city_id, jObject.getString("id"));
                                    SavePref.saveStringPref(CityListActivitys.this, SavePref.city_locality,subLocality);
                                    SavePref.saveStringPref(CityListActivitys.this, SavePref.address_list,
                                            mLocationAddress.getText().toString());
                                    if(!getIntent().hasExtra("city_type"))
                                    {
                                        Intent intent = new Intent(CityListActivitys.this, MainActivity.class);
                                        startActivity(intent);
                                    }


                                    finish();

                                }

                            }
                            else {
                                HelperMethods
                                        .showSnackBar(CityListActivitys.this, "Service Not Available in this city");
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
        HelperMethods.showSnackBar(CityListActivitys.this, "Service Not Available in this city");
                                        }
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    HelperMethods.showSnackBar(CityListActivitys.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    HelperMethods.showSnackBar(CityListActivitys.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    HelperMethods.showSnackBar(CityListActivitys.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    HelperMethods.showSnackBar(CityListActivitys.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    HelperMethods.showSnackBar(CityListActivitys.this,"Parse Error!");
                }

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String,String>();
                params.put("parameters",manJson.toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }


    void Dialogs(final JSONObject jsonObject, final String lat, final String lan)
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(CityListActivitys.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(CityListActivitys.this);
        }
        builder.setTitle("Change City")
                .setMessage("Change city will delete your added items?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        updateCart(jsonObject,lat,lan);

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }



    private void updateCart(final JSONObject jsonObject,final String lat,final String lan)
    {
        pDialog = new ProgressDialog(CityListActivitys.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        try {
            sendJson = new JSONObject();
            sendJson.put("method", "addtocart");

          /*  if    (SavePref.get_credential(CityListActivitys.this,SavePref.is_loogedin).equalsIgnoreCase("true"))
            {

                sendJson.put("user_id", SavePref.getPref(CityListActivitys.this, SavePref.User_id));
            }
            else
            {
                sendJson.put("guest_user_id",Keys.device_id);


            }*/
            if(SavePref.getPref(CityListActivitys.this,SavePref.is_loogedin).equalsIgnoreCase("true"))
            {
                sendJson.put("user_id", SavePref.getPref(CityListActivitys.this,SavePref.User_id));
            }
            else
            {
                sendJson.put("guest_user_id", Keys.device_id);
            }
            sendJson.put("number_of_item","1");
            sendJson.put("merchant_inventry_id", "");
            sendJson.put("action", "clearcart");
            sendJson.put("item_name", "");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(CityListActivitys.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Keys.BASE_URL+"application/customer",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(""+response);
                        try
                        {
                            Iterator<String> keys = jsonObject.keys();
                            while (keys.hasNext()) {

                                com.infoicon.powercoin.main.CityListBeans cityListBeans = new com.infoicon.powercoin.main.CityListBeans();
                                String key = keys.next();
                                //JSONObject innerJObject = jObject.getJSONObject(key);

                                SavePref.saveStringPref(CityListActivitys.this, SavePref.current_lat, String.valueOf(lat));
                                SavePref.saveStringPref(CityListActivitys.this, SavePref.current_lon, lan);
                                SavePref.saveStringPref(CityListActivitys.this, SavePref.City_name, jsonObject.getString("city_name"));
                                SavePref.saveStringPref(CityListActivitys.this, SavePref.city_id, jsonObject.getString("id"));
                                SavePref.saveStringPref(CityListActivitys.this, SavePref.city_locality,subLocality);
                                Intent intent = new Intent(CityListActivitys.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        }catch (Exception e)
                        {
                            pDialog.dismiss();

                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(CityListActivitys.this, "Communication Error!", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(CityListActivitys.this, "Authentication Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(CityListActivitys.this, "Server Side Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(CityListActivitys.this, "Network Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(CityListActivitys.this, "Parse Error!", Toast.LENGTH_SHORT).show();
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