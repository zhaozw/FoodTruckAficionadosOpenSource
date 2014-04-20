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
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/*
 * Created by justindelta on 3/17/14.
 */
//TODO ROTATION FIX FOR ALL 4 DIFFERENT ORIENTATIONS. IMPLICIT INTENT TO MAPS. UI ADJUSTMENTS. OPTIMIZATIONS
public class FoodTruckListViewFragment extends ListFragment implements LocationListener, FoodTruckDataGetter.OnDataReceivedListener, SensorEventListener, MyCompassView.SensorDataRequestListener{

    ProgressBar myProgress;
    TextView myTextView;
    TextView myTextViewNoTrucks;
    private FoodTruckDataAdapter foodTruckDataAdapter;
    private LocationManager locationManager;
    protected Location lastUserLocation;
    protected String currentLocation;  //Format: "30.12341234,-90.11341234"
    private boolean retrievedLocation = false;
    protected OnItemSelectedListener selectedListenerCallback;
    private SensorManager sensorManager;
    protected Sensor mySensorAccelerometer;
    protected Sensor mySensorMagnetometer;
    protected ArrayList<FoodTruckData> mTheDataReceived;
    float[] matrixR = {};
    float[] matrixRremapped = {};
    float[] matrixI = {};
    float[] valuesAccelerometer = {};
    float[] valuesMagneticField = {};
    float[] matrixValues = {};
    protected int rotation = 0;


    public FoodTruckListViewFragment() {
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Save custom values into the bundle
        outState.putString("MyKey", currentLocation);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);
    }

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

    @Override
    public void onDataReceived(ArrayList<FoodTruckData> theDataReceived) {
        //
        //  Check to see if you have more than 0 data. if so, then
        //

//       get the progress bar and the quote


        if(theDataReceived == null){
            return;
        }

        myTextView.setVisibility(View.INVISIBLE);
        myProgress.setVisibility(View.INVISIBLE);

        //if we now have data, kill the loading screen.
        mTheDataReceived = theDataReceived;
        foodTruckDataAdapter.setFoodTruckDataArrayList(theDataReceived);
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
//        setRetainInstance(true);

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

        if (location != null) {
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
                switch (rotation) {
                    case Surface.ROTATION_90:
                        //this is the right one for 90
                        //SensorManager.remapCoordinateSystem(matrixR, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, matrixRremapped);
                        SensorManager.remapCoordinateSystem(matrixR, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, matrixRremapped);
                        SensorManager.getOrientation(matrixRremapped, matrixValues);
                        break;
                    case Surface.ROTATION_180:
                        //this is the right one for 180
                        SensorManager.remapCoordinateSystem(matrixR, SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Y, matrixRremapped);
                        SensorManager.getOrientation(matrixRremapped, matrixValues);
                        break;
                    case Surface.ROTATION_270:
                        //this is the right one for 270
                        SensorManager.remapCoordinateSystem(matrixR, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, matrixRremapped);
                        SensorManager.getOrientation(matrixRremapped, matrixValues);
                        break;
                    case Surface.ROTATION_0:
                    default:
                        SensorManager.getOrientation(matrixR, matrixValues);
                        break;
                }

            }
        }
    }

    @Override
    public float getDirection() {
//        return GeoUtils.getModifiedTrueNorth(lastUserLocation,matrixValues[0]);
        return matrixValues[0]; //this is from the sensor updating
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected interface OnItemSelectedListener {
        public void OnItemSelected();
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
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);

        if(getActivity() != null){
            Object myObject = getActivity().getSystemService(Context.WINDOW_SERVICE);
            WindowManager windowManager = (WindowManager) myObject;
            Display display = windowManager.getDefaultDisplay();
            rotation = display.getRotation();
        }

        ArrayList<FoodTruckData> savedData = FoodTruckStorage.getMyFoodTruckData(getActivity());
        if(savedData != null && savedData.size() > 0){
            onDataReceived(savedData);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_truck_nearby, container, false);
        myTextView = (TextView) rootView.findViewById(R.id.progressTextId2);
        myProgress = (ProgressBar) rootView.findViewById(R.id.progressBar);
//        myTextView = (TextView) rootView.findViewById(R.id.progressTextNoTrucks);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //for the first time, see if they have data while you're waiting for new data.
        ArrayList<FoodTruckData> startData = new ArrayList<FoodTruckData>();
        foodTruckDataAdapter = new FoodTruckDataAdapter(getActivity(), R.layout.food_truck_rows,startData);
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

        FoodTruckStorage.saveMyFoodTruckData(getActivity(), mTheDataReceived);
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

        String latitudeString = String.format("%.4f", userLatitudeDouble);
        String longitudeString = String.format("%.4f", userLongitudeDouble);
        String latitudeLongitude = latitudeString + "," + longitudeString;
        String userLatLong = String.valueOf(userLatitudeDouble + "," + String.valueOf(userLongitudeDouble));


        if(!latitudeLongitude.equals(currentLocation) && getActivity() != null) {
            Log.v("Location Definitely Changed", "Will Launch FoodTruckDataGetter");
            currentLocation = latitudeLongitude;
            FoodTruckDataGetter.getInstance().performSearchRequest(getActivity(), this, currentLocation, userLatitudeDouble, userLongitudeDouble);
            FoodTruckData.setUserLatitude(userLatitudeDouble);
            FoodTruckData.setUserLongitude(userLongitudeDouble);
        }else {
            Log.v("GETMYFOODTRUCKDATA", "REACHED AND CHECK FOR ERROR");
            onDataReceived(FoodTruckStorage.getMyFoodTruckData(getActivity()));
        }
    }
}
