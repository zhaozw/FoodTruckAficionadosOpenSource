package com.justin.truckfinder.app;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.ArrayList;

/*
 * Created by justindelta on 3/17/14.
 */
//TODO ROTATION FIX FOR ALL 4 DIFFERENT ORIENTATIONS. IMPLICIT INTENT TO MAPS. UI ADJUSTMENTS. OPTIMIZATIONS
public class FoodTruckListViewFragment extends ListFragment implements LocationListener, FoodTruckDataGetter.OnDataReceivedListener, SensorEventListener, MyCompassView.SensorDataRequestListener{

    ProgressBar mProgressBar;
    static final String SOME_VALUE = "int_value";
    static final String SOME_OTHER_VALUE = "string_value";
    protected float from;
    protected float  to;
    private FoodTruckDataAdapter foodTruckDataAdapter;
    private LocationManager locationManager;
    protected Location lastUserLocation;
    protected String currentLocation;  //Format: "30.12341234,-90.11341234"
    private boolean retrievedLocation = false;
    private OnItemSelectedListener selectedListenerCallback;
    private SensorManager sensorManager;
    protected Sensor mySensorAccelerometer;
    protected Sensor mySensorMagnetometer;
    //protected Context context;
    protected ArrayList<FoodTruckData> mTheDataReceived;
    protected Bundle mSavedState;
    protected Button mReturnButton = null;
    protected Button mPerformButton = null;
    protected Spinner mSpinner = null;
    float[] matrixR = {};
    float[] matrixRremapped = {};
    float[] matrixI = {};
    float[] valuesAccelerometer = {};
    float[] valuesMagneticField = {};
    float[] matrixValues = {};

    boolean ROTATE_90 = false;
    boolean ROTATE_270 = false;
    boolean ROTATE_180 = false;



    public FoodTruckListViewFragment() {
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Save custom values into the bundle
        int someIntValue = 1;
        String someStringValue = "string";

        outState.putInt(SOME_VALUE, someIntValue);
        outState.putString(SOME_OTHER_VALUE, someStringValue);

        //
        //
        //outState.putSerializable("MyKey",thingYouWantToSave);
        //
        //
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //
        //
        //mTheDataReceived = savedInstanceState.getSerializable("MyKey");
        //
        //

    }

    @Override
    public void onDataReceived(ArrayList<FoodTruckData> theDataReceived) {
        //
        //  Check to see if you have more than 0 data. if so, then
        //

//       get the progress bar and the quote
//        myProgress.setVisibility(View.INVISIBLE);


        //if we now have data, kill the loading screen.
        mTheDataReceived = theDataReceived;
        if (theDataReceived == null) {
            foodTruckDataAdapter.setFoodTruckDataArrayList(FoodTruckStorage.getMyFoodTruckData(getActivity()));
        }else {
            foodTruckDataAdapter.setFoodTruckDataArrayList(theDataReceived);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        valuesAccelerometer = new float[3];
        valuesMagneticField = new float[3];
        matrixR = new float[9];
        matrixI = new float[9];
        matrixValues = new float[3];
        matrixRremapped = new float[9];
        setRetainInstance(true);

    }

    @Override
    public void setListShown(boolean shown) {
        super.setListShown(shown);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onLocationChanged(Location location) {
        if (retrievedLocation) {
            retrievedLocation = true;
            Log.e("NEWLOC", "Got new location");
            setNewLocation(location);
            // Remove the listener you previously added
            locationManager.removeUpdates(this);
        }
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
    public void onSensorChanged(SensorEvent event) {

        synchronized (this) {
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

            boolean success = SensorManager.getRotationMatrix(
                    matrixR,
                    matrixI,
                    valuesAccelerometer,
                    valuesMagneticField);

            if (success) {
            /*public int getRotation ()

                        Added in API level 8
                        Returns the rotation of the screen from its "natural" orientation.
                        The returned value may be Surface.ROTATION_0 (no rotation), S
                        urface.ROTATION_90, Surface.ROTATION_180, or
                        Surface.ROTATION_270.
                        For example, if a device has a naturally tall screen,
                        and the user has turned it on its side to go into a landscape orientation,
                        the value returned here may be either Surface.ROTATION_90 or
                        Surface.ROTATION_270 depending on the direction it was turned.
                        The angle is the rotation of the drawn graphics on the screen,
                        which is the opposite direction of the physical rotation of the device.
                        For example, if the device is rotated 90 degrees counter-clockwise,
                        to compensate rendering will be rotated by 90 degrees clockwise and thus
                        the returned value here will be Surface.ROTATION_90.
                        */
                //TODO test to see if the remapping works. Then test to see if it's remapping correctly.


                if(ROTATE_90){
                    //this is the right one for 90
                    //SensorManager.remapCoordinateSystem(matrixR, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, matrixRremapped);
                    SensorManager.remapCoordinateSystem(matrixR, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, matrixRremapped);
                    SensorManager.getOrientation(matrixRremapped, matrixValues);
                }else if(ROTATE_270){
                    //this is the right one for 270
                    //SensorManager.remapCoordinateSystem(matrixR, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, matrixRremapped);
                    SensorManager.remapCoordinateSystem(matrixR, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, matrixRremapped);
                    SensorManager.getOrientation(matrixRremapped, matrixValues);
                }else if(ROTATE_180){
                    //this is the right one for 180
                    SensorManager.remapCoordinateSystem(matrixR, SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Y, matrixRremapped);
                    SensorManager.getOrientation(matrixRremapped, matrixValues);
                }else{
                    SensorManager.getOrientation(matrixR, matrixValues);
                }

            }
        }
    }



    @Override
    public float getDirection() {
        return GeoUtils.getModifiedTrueNorth(lastUserLocation,matrixValues[0]);
        //return matrixValues[0]; //this is from the sensor updating
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected interface OnItemSelectedListener {
        public void OnItemSelected();
    }

    protected interface OnIntentSelectedListener {
        public void OnIntentReceived();
    }

    @Override
    public void onStart() {
        super.onStart();
        setNewLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("ONSTART", "network provider location ERROR");
        }
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1, 1, this);
        //Sensor stuff
        mySensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mySensorMagnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
// Moved code registerListeners to onResume because it will only notice both registered listeners if it is in OnResume
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_FASTEST);

        if(getActivity() != null){
            Object myObject = getActivity().getSystemService(Context.WINDOW_SERVICE);
            WindowManager wm = (WindowManager) myObject;
            Display d = wm.getDefaultDisplay();
            int rotation = d.getRotation();

            //Clean up this code to use a single int instead of multiple booleans.

            switch (rotation) {
                case Surface.ROTATION_90:
                    ROTATE_90 = true;
                    ROTATE_270 = false;
                    ROTATE_180 = false;
                    Log.e("ROT","ROTATION_90");
                    break;
                case Surface.ROTATION_180:
                    ROTATE_90 = false;
                    ROTATE_270 = false;
                    ROTATE_180 = true;
                    Log.e("ROT", "ROTATION_180");
                    break;
                case Surface.ROTATION_270:
                    ROTATE_90 = false;
                    ROTATE_270 = true;
                    ROTATE_180 = false;
                    Log.e("ROT", "ROTATION_270");
                    break;
                case Surface.ROTATION_0:
                default:
                    Log.e("ROT", "ROTATION_0");
                    ROTATE_90 = false;
                    ROTATE_270 = false;
                    ROTATE_180 = false;
                    break;
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //for the first time, see if they have data while you're waiting for new data.
        if(savedInstanceState != null){

        }
        foodTruckDataAdapter = new FoodTruckDataAdapter(getActivity(), R.layout.food_truck_rows, new ArrayList<FoodTruckData>());
        setListAdapter(foodTruckDataAdapter);
        foodTruckDataAdapter.setSensorListener(this);

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

    }


    @Override
    public void onPause() {
        super.onPause();

        locationManager.removeUpdates(this);
        sensorManager.unregisterListener(this);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            selectedListenerCallback = (OnItemSelectedListener) activity;
            // if a class cast exception is thrown, catch it and
        } catch (ClassCastException e) {
            //throw a new error indicating the following, AND don't break the app
            throw new ClassCastException(activity.toString() + "meet implement OnItemSelectedListener!");
        }
    }

    public void setNewLocation(Location location) {
        if (location == null) {
            return;
        }

        lastUserLocation = location;

        double userLatitudeDouble = location.getLatitude();
        double userLongitudeDouble = location.getLongitude();

        String latitudeString = String.valueOf(userLatitudeDouble);
        String longitudeString = String.valueOf(userLongitudeDouble);

        String latitudeLongitude = latitudeString + "," + longitudeString;
        Log.v("gps", latitudeLongitude);
        currentLocation = latitudeLongitude;

        FoodTruckDataGetter.getInstance().performSearchRequest(getActivity(), this, currentLocation, userLatitudeDouble, userLongitudeDouble);
        FoodTruckData.setUserLatitude(userLatitudeDouble);
        FoodTruckData.setUserLongitude(userLongitudeDouble);
    }
}
