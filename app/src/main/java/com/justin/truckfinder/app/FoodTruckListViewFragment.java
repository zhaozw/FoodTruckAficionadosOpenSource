package com.justin.truckfinder.app;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

/*
 * Created by justindelta on 3/17/14.
 */

public class FoodTruckListViewFragment extends ListFragment implements LocationListener, FoodTruckDataGetter.OnDataReceivedListener, SensorEventListener, MyCompassView.SensorDataRequestListener {
    ProgressBar progress;

    protected float from;
    protected float  to;
    private FoodTruckDataAdapter foodTruckDataAdapter;
    private LocationManager locationManager;
    protected String currentLocation;  //Format: "30.12341234,-90.11341234"
    private boolean retrievedLocation = false;
    private OnItemSelectedListener selectedListenerCallback;
    private SensorManager sensorManager;
    protected Sensor mySensorAccelerometer;
    protected Sensor mySensorMagnetometer;
    private Context context;

    float[] matrixR = {};
    float[] matrixI = {};
    float[] valuesAccelerometer = {};
    float[] valuesMagneticField = {};
    float[] matrixValues = {};

    public FoodTruckListViewFragment() {
    }

    @Override
    public void onDataReceived(ArrayList<FoodTruckData> theDataReceived) {
        if (theDataReceived == null) {
            foodTruckDataAdapter.setFoodTruckDataArrayList(FoodTruckStorage.getMyData(context));
        }else {
            foodTruckDataAdapter.setFoodTruckDataArrayList(theDataReceived);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

//        setRetainInstance(true);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        valuesAccelerometer = new float[3];
        valuesMagneticField = new float[3];
        matrixR = new float[9];
        matrixI = new float[9];
        matrixValues = new float[3];

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

        // tried using a switch statement, also did not reach the second case where getType() == Sensor.TYPE_MAGNETIC_FIELD
        //TODO: Consider using a switch statement instead and checking to see if it works after having added callbacks to have this
        //TODO: (cont.) listFragment communicate with the ListView.
        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                for (int i = 0; i < 3; i++) {
                    valuesAccelerometer[i] = event.values[i];
                    FoodTruckData.setValuesAccelerometer(valuesAccelerometer);
                }
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                for (int i = 0; i < 3; i++) {
                    valuesMagneticField[i] = event.values[i];
                    FoodTruckData.setValuesMagneticField(valuesMagneticField);
                }
            }

            boolean success = SensorManager.getRotationMatrix(
                    matrixR,
                    matrixI,
                    valuesAccelerometer,
                    valuesMagneticField);

            if (success) {
                SensorManager.getOrientation(matrixR, matrixValues);
            }
        }
    }

    @Override
    public double getDirection() {
        //here is where you should give the direction,
        //but also take into consideration the current orientation of landscape or portrait and
        //change the variable accordingly.
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
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("ONSTART", "network provider location ERROR");
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
        //Sensor stuff
        mySensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mySensorMagnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
// Moved code registerListeners to onResume because it will only notice both registered listeners if it is in OnResume
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        foodTruckDataAdapter = new FoodTruckDataAdapter(getActivity(), R.layout.food_truck_listfragment_rows, new ArrayList<FoodTruckData>());
        setListAdapter(foodTruckDataAdapter);
        foodTruckDataAdapter.setSensorListener(this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//            FoodTruckData foodTruckData;
//        //call back to the parent activity with the selected item
//            // put callback code --> map fragment
//            selectedListenerCallback.OnItemSelected();
//            foodTruckData = ((FoodTruckDataAdapter)getListAdapter()).getItem(position);
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
        sensorManager.unregisterListener(this);
        FoodTruckStorage.getMyData(getActivity());
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

    /**
     * Calculates {@code a mod b} in a way that respects negative values (for example,
     * {@code mod(-1, 5) == 4}, rather than {@code -1}).
     *
     * @param a the dividend
     * @param b the divisor
     * @return {@code a mod b}
     */
    public static float mod(float a, float b) {
        return (a % b + b) % b;
    }

    public class ProgressBarAnimation extends Animation {
        private ProgressBar progressBar;
        private float from;
        private float  to;

        public ProgressBarAnimation(ProgressBar progressBar, float from, float to) {
            super();
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            progressBar.setProgress((int) value);
        }

    }
}
