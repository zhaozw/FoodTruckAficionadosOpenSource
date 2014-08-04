package com.justin.truckfinder.app;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

/*
 * Created by justin on 3/17/14.
 */

public class FoodTruckListViewFragment extends ListFragment implements LocationListener, FoodTruckDataGetter.OnDataReceivedListener, SensorEventListener, RealCompassView.SensorDataRequestListener{

    private ProgressBar myProgress;
    private TextView myTextView;
    private TextView myTextViewNoTrucks;

    private FoodTruckDataAdapter foodTruckDataAdapter;

    private LocationManager locationManager;
    protected Location lastUserLocation;
    protected String currentLocation;  //Format: "30.12341234,-90.11341234"

    private SensorManager sensorManager;
    protected Sensor mySensorAccelerometer;
    protected Sensor mySensorMagnetometer;

    protected ArrayList<FoodTruckData> mTheDataReceived;

    private float[] matrixR = {};
    private float[] matrixRremapped = {};
    private float[] matrixI = {};
    private float[] valuesAccelerometer = {};
    private float[] valuesMagneticField = {};
    private float[] matrixValues = {};
    protected int rotation = 0;

    //  AdMob
    protected AdView adView;

    //
    // Save the current location as a bundle, for landscape <--> portrait changes
    //
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Save custom values into the bundle
        outState.putString("MyKey", currentLocation);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);
    }

    //
    // Restore the state when fragment is re-created for landscape <--> portrait changes
    //
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //
        //
        if (savedInstanceState !=null) {
           currentLocation = savedInstanceState.getString("MyKey", "No Location");
        }
        //
        //

    }

    //
    // Callback to change visibility of the appropriate view if we have data
    // else inform the user there are no nearby food trucks.
    //
    @Override
    public void onDataReceived(ArrayList<FoodTruckData> theDataReceived) {
        //
        //  Check to see if you have more than 0 data. if so, then
        //  set the progress bar and the paired quote (Louis CK) to visible.
        //
        if(theDataReceived == null){
            myTextView.setVisibility(View.INVISIBLE);
            myProgress.setVisibility(View.INVISIBLE);
            myTextViewNoTrucks.setVisibility(View.VISIBLE);
            return;
        }else {
            //
            // If we now have data, set the progress and "no nearby trucks" view to
            //  be invisible to prevent obstructing the ListView of trucks.
            //
            myTextViewNoTrucks.setVisibility(View.INVISIBLE);
            myTextView.setVisibility(View.INVISIBLE);
            myProgress.setVisibility(View.INVISIBLE);
        }

        //
        // Set the dataReceived as a member variable
        //
        mTheDataReceived = theDataReceived;

        //
        // Refresh the adapter with the new data to populate the view
        //
        foodTruckDataAdapter.setFoodTruckDataArrayList(theDataReceived);
    }


    //
    // Initialize the locationManager and sensorManager in onCreate
    //
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        valuesAccelerometer = new float[3];
        valuesMagneticField = new float[3];

        //
        // Set the matrix float[] sizes to utilize the remapOrientationMatrix method
        //
        matrixR = new float[9];
        matrixI = new float[9];
        matrixValues = new float[3];
        matrixRremapped = new float[9];
    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            Log.d("NEWLOC", "Got new location");

            //
            //launches the network request(s)
            //
            setNewLocation(location);

            //
            // Remove the listener you previously added
            //
            locationManager.removeUpdates(this);
        }
    }

    //
    // LocationListener interface methods
    //
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    //
    // Set float values for the ACCELEROMETER and the TYPE_MAGNETIC_FIELD
    // rather ROTATION_VECTOR because not everyone has a fancy phone.
    //
    //TODO: explain how using getRotation is deprecated and wrong, and your method below is better
    //

    @Override
    public void onSensorChanged(SensorEvent event) {
        //
        // Ensure proper mathematical sequence to properly remap the new rotation matrix
        // based on user device's current rotation
        //
        synchronized (this) {
            //
            // Set X, Y, and Z sensor values per sensor
            // float[] valuesAccelerometer
            // float[] valuesMagneticField
            //
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                for (int i = 0; i < 3; i++) {
                    valuesAccelerometer[i] = event.values[i];
                }
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                for (int i = 0; i < 3; i++) {
                    valuesMagneticField[i] = event.values[i];
                }
            }

            //
            // success = true if we get the rotation matrix
            //
            boolean success = SensorManager.getRotationMatrix(
                    matrixR,
                    matrixI,
                    valuesAccelerometer,
                    valuesMagneticField);

            //
            // if successfully get rotation matrix
            //
            if (success) {
                //
                // determine the rotation of the device by using the Surface interface
                // located in Android's view package
                //
                switch (rotation) {
                    //
                    //  Note: Counter-intuitive use of each axis to ensure accurate remapping
                    //        of coordinate system for each of the 4 possible device rotations
                    //
                    //  Also, the comments below apply to each rotation - differences:
                    //      1) Current device rotation retrieved using Surface.ROTATION_X
                    //      2) The values for X and Y (sign, and order) depend on the rotation
                    //         relative to the default coordinate system of the device (getRotationMatrix)
                    //         determined by user's device sensor values (accelerometer and magnetic_field)
                    //
                    //
                    // Once Surface orientation is detected, call appropriate method "remapCoordinateSystem" by passing:
                    //  the rotation matrix,
                    //  the rotation-appropriate "X value" = (-)X-axis or (-)Y-axis,
                    //  the rotation-appropriate "Y value" = (-)X-axis or (-)Y-axis,
                    //  and an empty float[9] matrixRremapped
                    //
                    //  Method SensorManager.remapCoordinateSystem when called SETS
                    //      the empty initialized float[9] matrixRremapped to
                    //          the new matrix (remapped coordinate system)
                    //          depending on the current device rotation [90, 180, 270, 0]
                    //
                    //  After, call getOrientation and pass remapped matrix, along with empty float[3]
                    //  so getOrientation sets appropriate axis values to float[3] matrixValues
                    //
                    // ROTATION_0 is the default orientation (rotation)
                    // Note: This is regardless of device, since
                    // some devices (e.g. some tablets)
                    // have a native LANDSCAPE orientation (vs. Portrait)
                    // Android's SensorManager.getRotationMatrix,
                    //           SensorManager.remapRotationMatrix,
                    //           SensorManager.getOrientation, and
                    //           Sensor.ROTATION_X
                    //  is designed to account for the device's native orientation (landscape or portrait)
                    //  to properly remap the coordinate system for reliable results.
                    //
                    case Surface.ROTATION_90:
                        SensorManager.remapCoordinateSystem(matrixR, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, matrixRremapped);
                        SensorManager.getOrientation(matrixRremapped, matrixValues);
                        break;
                    case Surface.ROTATION_180:
                        SensorManager.remapCoordinateSystem(matrixR, SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Y, matrixRremapped);
                        SensorManager.getOrientation(matrixRremapped, matrixValues);
                        break;
                    case Surface.ROTATION_270:
                        SensorManager.remapCoordinateSystem(matrixR, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, matrixRremapped);
                        SensorManager.getOrientation(matrixRremapped, matrixValues);
                        break;
                    case Surface.ROTATION_0:
                        //default rotation, continue to default case:
                    default:
                        SensorManager.getOrientation(matrixR, matrixValues);
                        break;
                }
            }
        }
    }

    @Override
    public float getDirection() {
        //
        // Obtain and return the azimuth (azimuth = direction = rotation around the Z-axis)
        //
        return matrixValues[0]; //this is from the sensor updating
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onStart() {
        super.onStart();

        // Used for Analytics
//        EasyTracker.getInstance(getActivity()).activityStart(getActivity());

        // Make sure we can get the location
        if(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
            setNewLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }else if (locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null){
            setNewLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
        }else{
            setNewLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }
        //get updates to user location with the following settings:
        //
        //* @param minTime minimum time interval between location updates: 1 millisecond
        //* @param minDistance minimum distance between location updates: 1 meter
        //
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);

        //Sensor stuff
        mySensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mySensorMagnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


    }

    @Override
    public void onResume() {
        super.onResume();
        //
        // adView needs its own lifecycle method called here before the super method is called
        //
        if (adView != null) {
            adView.resume();
        }

        //
        // Register listeners with sensorManager
        //      make sure registerListeners to onResume and not
        //      onStart() because it will only notice both registered listeners if it is in OnResume
        //

        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);

        if(getActivity() != null){
            Object myObject = getActivity().getSystemService(Context.WINDOW_SERVICE);
            WindowManager windowManager = (WindowManager) myObject;
            Display display = windowManager.getDefaultDisplay();
            rotation = display.getRotation();
        }

        // Retrieve persistent storage and use it if we have it.
        retrievePersistenceAndUse();
    }

    private void retrievePersistenceAndUse(){
        // Retrieve persistent storage and use it if we have it.
        ArrayList<FoodTruckData> savedData = FoodTruckStorage.getMyFoodTruckData(getActivity());
        if(savedData != null && savedData.size() > 0){
            onDataReceived(savedData);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // Used for Analytics
//        EasyTracker.getInstance(getActivity()).activityStop(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_truck_nearby, container, false);

        myTextView = (TextView) rootView.findViewById(R.id.progressTextId2);
        myProgress = (ProgressBar) rootView.findViewById(R.id.progressBar);
        myTextViewNoTrucks = (TextView) rootView.findViewById(R.id.failMessage);
        AdView adView = (AdView) rootView.findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice("D149CABC89242EE6DC2A3A6F61908262")
                .build();
        adView.loadAd(adRequest);
//        addAdMobToRootView(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        //  For the first time, start with blank data.
        //
        ArrayList<FoodTruckData> startData = new ArrayList<FoodTruckData>();
        foodTruckDataAdapter = new FoodTruckDataAdapter(getActivity(), R.layout.food_truck_rows, startData);
        setListAdapter(foodTruckDataAdapter);

        //
        // Setter for sensorListener to FoodTruckDataAdapter that will display RealCompassView
        //
        foodTruckDataAdapter.setSensorListener(this);
    }


    //
    // TODO future implementation of ExpandableListView will require use of this method or
    // TODO a custom method that functions similarly to this ListFragment method
    //
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    }

    @Override
    public void onPause() {
        //
        // adView needs its own lifecycle method called here before the super method is called
        //
        if (adView != null) {
            adView.pause();
        }
        super.onPause();

        //
        // Remove location updates and unregister sensor changes
        //  because we don't need either one when the app is not in focus
        //  and we dont want to waste battery
        //
        locationManager.removeUpdates(this);
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onDestroyView() {
        //
        // adView needs its own lifecycle method called here before the super method is called
        //
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroyView();
    }


    public void setNewLocation(Location aLocation) {
        //
        // return if we don't have a location to work with
        //
        if (aLocation == null) {
            return;
        }

        lastUserLocation = aLocation;


        //
        // Use double's 64-bit precision when setting the user's current latitude and longitude.
        //
        double userLatitudeDouble = aLocation.getLatitude();
        double userLongitudeDouble = aLocation.getLongitude();


        //
        // Use String manipulation to create String representation of latitude and longitude
        //  to the 2nd decimal place.
        //
        String latitudeString = String.format("%.2f", userLatitudeDouble);
        String longitudeString = String.format("%.2f", userLongitudeDouble);
        String latitudeLongitude = latitudeString + "," + longitudeString;

        // Check if the manipulated "latitude,longitude" is different at the 2nd decimal place
        // AND ensure there is a context.
        if(!latitudeLongitude.equals(currentLocation) && getActivity() != null) {
//            Log.v("Location Definitely Changed", "Will Launch FoodTruckDataGetter");

            //
            // Set the currentLocation to String (manipulated) latitudeLongitude.
            // Next pass will compare the previously set userLocation.
            //
            currentLocation = latitudeLongitude;

            //
            // We only need 1 network request per location change.
            // Utilize singleton and call method to start the first network request.
            //
            FoodTruckDataGetter.getInstance().performSearchRequest(getActivity(), this, currentLocation, userLatitudeDouble, userLongitudeDouble);

            //
            // only one device and one user means we don't need more than one value for the
            //  user's latitude and longitude.
            // Set lat and lng using static setter and original double value from Location.
            // User location needed to calculate distance using spherical law of cosines.
            //
            FoodTruckData.setUserLatitude(userLatitudeDouble);
            FoodTruckData.setUserLongitude(userLongitudeDouble);
        }else {
//            Log.v("GETMYFOODTRUCKDATA", "REACHED AND CHECK FOR ERROR");
            if (getActivity() != null) {
                retrievePersistenceAndUse();
            }
        }
    }

}
