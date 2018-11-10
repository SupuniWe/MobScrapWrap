package com.db.cdap.scrapwrap.location;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.db.cdap.scrapwrap.MainActivity;
//import com.db.cdap.scrapwrap.Manifest;
import com.db.cdap.scrapwrap.R;
import com.db.cdap.scrapwrap.models.PlaceInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MapsActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    //Global variables
    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));
    private static final int PLACE_PICKER_REQUEST = 1;

    //Widgets
    private AutoCompleteTextView mSearchText;
    private ImageView mGPS, mInfo, mPlacePicker, mBins, mObjects, mVerify, mNotification;

    //Variables
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private GeoDataClient mGeoDataClient;
    private PlaceInfo mPlace;
    private Marker mMarker;
    private Boolean displayBin = false;
    private ChildEventListener mChildEventListener;
    private Marker binMarker;
    private Marker objectMarker;
    //private Location currentLocation;

    //Database
    private DatabaseReference mDatabaseBins;
    private DatabaseReference mDatabaseObjects;

    //Verification
    private int binCount = 0;
    private int objCount = 0;
    private Double binLatitudes[] = new Double[100];
    private Double binLongitudes[] = new Double[100];
    private Double objLatitudes[] = new Double[100];
    private Double objLongitudes[] = new Double[100];;
    private HashMap<String, Double> hashMapObjLatitudes = new HashMap<String, Double>();
    private HashMap<String, Double> hashMapObjLongitudes = new HashMap<String, Double>();
    private Double verifiedObjLatitudes[] = new Double[100];;
    private Double verifiedObjLongitudes[] = new Double[100];;
    private int verifiedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mSearchText = (AutoCompleteTextView)findViewById(R.id.input_search);
        mGPS = (ImageView)findViewById(R.id.ic_gps);
        mInfo = (ImageView)findViewById(R.id.place_info);
        mPlacePicker = (ImageView)findViewById(R.id.place_picker);
        mBins = (ImageView)findViewById(R.id.displayBins);
        mObjects = (ImageView)findViewById(R.id.displayObjects);
        mVerify = (ImageView)findViewById(R.id.verifyObjects);
        mNotification = (ImageView)findViewById(R.id.openNotifications);
        ChildEventListener mChildEventListener;

        mDatabaseBins = FirebaseDatabase.getInstance().getReference().child("Bins");
        mDatabaseBins.push().setValue(binMarker);

        mDatabaseObjects = FirebaseDatabase.getInstance().getReference().child("Objects");
        mDatabaseObjects.push().setValue(objectMarker);

        isServicesOK();

        getLocationPermission();

        //mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        init();


    }

    private void init(){
        Log.d(TAG, "init:Initializing");

        mSearchText.setOnItemClickListener(mAutoCompleteClickListener);

        mGeoDataClient = Places.getGeoDataClient(this, null);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(mPlaceAutocompleteAdapter);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                    //Execute our method for searching
                    geoLocate();
                }
                return false;
            }
        });

        mGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
            }
        });

        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked place info");

                try{
                    if(mMarker.isInfoWindowShown()){
                        mMarker.hideInfoWindow();
                    }
                    else{
                        Log.d(TAG, "onClick: place info: " + mPlace.toString());
                        mMarker.showInfoWindow();
                    }
                }catch(NullPointerException e){
                    Log.e(TAG, "onClick: NullPointerException: " + e.getMessage());
                }
            }
        });

        mPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(MapsActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.e(TAG, "onClick: GooglePlayServicesRepairableException: " + e.getMessage());
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e(TAG, "onClick: GooglePlayServicesNotAvailableException: " + e.getMessage());
                }
            }
        });

        mBins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                /*if(displayBin == false){
                    displayBin = true;*/
                /*if(displayBin == false)
                {
                    displayBin = true;*/
                    mDatabaseBins.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot s : dataSnapshot.getChildren())
                            {
                                BinInformation bin = s.getValue(BinInformation.class);
                                LatLng location = new LatLng(bin.getLatitude(), bin.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(location).title(bin.getName())).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                                binMarker = mMap.addMarker(new MarkerOptions().position(location).title(bin.getName()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Toast.makeText(MapsActivity.this, "Loaded Bins Successfully", Toast.LENGTH_SHORT).show();
                /*}
                else
                {

                }*/

               // }
                /*else
                {
                    displayBin = false;
                    m*/
            }
        });

        mObjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseObjects.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot s: dataSnapshot.getChildren())
                        {
                            ObjectInformation object = s.getValue(ObjectInformation.class);

                            LatLng objectLocation = new LatLng(object.getLatitude(), object.getLongitude());

                            if(object.getStatus().equals("Cleaned"))
                            {
                                mMap.addMarker(new MarkerOptions().position(objectLocation).title(object.getName())).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                //objectMarker = mMap.addMarker(new MarkerOptions().position(objectLocation).title(object.getName()));
                            }
                            else if (object.getStatus().equals("Verified"))
                            {
                                mMap.addMarker(new MarkerOptions().position(objectLocation).title(object.getName())).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            }
                            else if (object.getStatus().equals("Uncleaned"))
                            {
                                mMap.addMarker(new MarkerOptions().position(objectLocation).title(object.getName())).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                            }
                        }

                        Toast.makeText(MapsActivity.this, "Loaded Objects Successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        mVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binCount = 0;
                objCount = 0;
                verifiedCount =0;

                mDatabaseBins.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot s: dataSnapshot.getChildren())
                        {
                            BinInformation bin = s.getValue(BinInformation.class);
                            binLatitudes[binCount] = bin.getLatitude();
                            binLongitudes[binCount] = bin.getLongitude();
                            binCount++;
                        }

                        mDatabaseObjects.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot s: dataSnapshot.getChildren())
                                {
                                    ObjectInformation object = s.getValue(ObjectInformation.class);

                                    if(object.getStatus().equals("Cleaned"))
                                    {
                                        objLatitudes[objCount] = object.getLatitude();
                                        objLongitudes[objCount] = object.getLongitude();
                                        hashMapObjLatitudes.put(object.getName(),object.getLatitude());
                                        hashMapObjLongitudes.put(object.getName(),object.getLongitude());
                                        objCount++;
                                    }
                                }

                                for(int i=0; i<objCount; i++)
                                {
                                    double objLat = objLatitudes[i];
                                    double objLng = objLongitudes[i];

                                    for(int j=0; j<binCount; j++)
                                    {
                                        double binLat = binLatitudes[j];
                                        double binLng = binLongitudes[j];

                                        if((objLat == binLat) && (objLng == binLng))
                                        {
                                            verifiedObjLatitudes[verifiedCount] = objLat;
                                            verifiedObjLongitudes[verifiedCount] = objLng;
                                            verifiedCount++;
                                            break;
                                        }
                                    }
                                }

                                if(verifiedCount > 0)
                                {
                                    for(int x =0; x<verifiedCount; x++)
                                    {
                                        double vLatitude = verifiedObjLatitudes[x];
                                        double vLongitde = verifiedObjLongitudes[x];

                                        for(Map.Entry<String, Double> latEntry : hashMapObjLatitudes.entrySet())
                                        {
                                            for(Map.Entry<String, Double> lngEntry : hashMapObjLongitudes.entrySet())
                                            {
                                                if(latEntry.getKey() == lngEntry.getKey())
                                                {
                                                    if((hashMapObjLatitudes.get(latEntry.getKey()) == vLatitude) && (hashMapObjLongitudes.get(lngEntry.getKey()) == vLongitde))
                                                    {
                                                        Toast.makeText(MapsActivity.this, "Objects Verified Successfully", Toast.LENGTH_SHORT).show();
                                                        mDatabaseObjects.child(latEntry.getKey().toString()).child("status").setValue("Verified");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    Toast.makeText(MapsActivity.this, "No objects to verify!", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

        mNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(MapsActivity.this, NotificationActivity.class);
                startActivity(regIntent);
            }
        });

        hideSoftKeyboard();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, place.getId());

                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            }
        }
    }

    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geoCoder = new Geocoder(MapsActivity.this);

        List<Address> list = new ArrayList<>();

        try{
            list = geoCoder.getFromLocationName(searchString, 1);
        }catch(IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if(list.size()>0){
            Address address = list.get(0);
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM));

            MarkerOptions options = new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())).title(address.getAddressLine(0));

            mMap.addMarker(options);

            hideSoftKeyboard();
        }
    }
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the current device's location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted) {
                Toast.makeText(this, "getDeviceLocation", Toast.LENGTH_LONG).show();
                Task location = mFusedLocationProviderClient.getLastLocation();
                if(location == null)
                {
                    Log.e(TAG, "Exception: null");
                }
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Toast.makeText(MapsActivity.this, "Found location", Toast.LENGTH_SHORT).show();
                            Location currentLocation = (Location) task.getResult();
                            //moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                            moveCamera(new LatLng(6.914807, 79.973130), DEFAULT_ZOOM);
                            //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM));
                            MarkerOptions options = new MarkerOptions().position(new LatLng(6.914807, 79.973130)).title("My Location");

                            mMap.addMarker(options);
                            //mMap.setMyLocationEnabled(true);
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latlng, float zoom, PlaceInfo placeInfo) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latlng.latitude + ", long: " + latlng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

        mMap.clear();

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));

        if(placeInfo != null){
            try{
                String snippet = "Address: " + placeInfo.getAddress() + "\n" + "Phone Number: " + placeInfo.getPhoneNumber() + "\n" + "Website: " + placeInfo.getWebsiteUri() + "\n" +"Price Rating: " + placeInfo.getRating() + "\n";

                MarkerOptions options = new MarkerOptions().position(latlng).title(placeInfo.getName()).snippet(snippet);
                mMarker = mMap.addMarker(options);
            }catch (NullPointerException e){
                Log.e(TAG, "moveCamera: NullPointerException: " + e.getMessage());
            }
        }
        else{
            mMap.addMarker(new MarkerOptions().position(latlng));
        }

        hideSoftKeyboard();
    }

    private void moveCamera(LatLng latlng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latlng.latitude + ", long: " + latlng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

        hideSoftKeyboard();
    }

    private void initMap() {
        Log.d(TAG, "initMap: Initiliazing map");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.lMap);

        mapFragment.getMapAsync(MapsActivity.this);
    }
    /*private void init(){
        Button btnMap = (Button)findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });
    }*/

    //Checks the version
    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: Checking Google Services Version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapsActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //Everything works fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //An error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MapsActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    //Explicitly check the permissions
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: Getting location permissions");
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionGranted = true;

                    //Initialize Map
                    initMap();
                }
            }
        }
       // updateLocationUI();
    }

/*    private void updateLocationUI(){
        if(mMap == null){
            return;
        }
        try{
            if(mLocationPermissionGranted){
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
            else{
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                currentLocation = null;
                getLocationPermission();
            }
        } catch(SecurityException e){
            Log.e(TAG, "updateLocationUI:Exception: " + e.getMessage());
        }
    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is ready");
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            //updateLocationUI();

            getDeviceLocation();

            init();
        }
            /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Toast.makeText(this, "Setting Location", Toast.LENGTH_SHORT).show();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);*/
        //}
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /*
       ------------google places API autocomplete suggestions----------
     */

    private AdapterView.OnItemClickListener mAutoCompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           hideSoftKeyboard();

           final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(position);
           final String placeID =  item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeID);

            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }

            final Place place = places.get(0);

            try{
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                mPlace.setAddress(place.getAddress().toString());
                //mPlace.setAttributions(place.getAttributions().toString());
                mPlace.setId(place.getId());
                mPlace.setLatlng(place.getLatLng());
                mPlace.setRating(place.getRating());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                mPlace.setWebsiteUri(place.getWebsiteUri());

                Log.d(TAG, "onResult: place: " + mPlace.toString());
            }catch(NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException:" + e.getMessage());
            }

            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getViewport().getCenter().latitude, place.getViewport().getCenter().longitude), DEFAULT_ZOOM));
            moveCamera(new LatLng(place.getViewport().getCenter().latitude, place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace);
            MarkerOptions options = new MarkerOptions().position(new LatLng(place.getViewport().getCenter().latitude, place.getViewport().getCenter().longitude)).title(mPlace.getName());

            mMap.addMarker(options);

            places.release();
        }
    };

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
