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
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by justindelta on 3/17/14.
 */
public class FoodTruckListViewFragment extends ListFragment implements LocationListener, FoodTruckDataGetter.OnDataReceivedListener, SensorEventListener {


    private FoodTruckDataAdapter foodTruckDataAdapter;
    private LocationManager locationManager;
    private SensorManager sensorManager;
    private OnItemSelectedListener selectedListenerCallback;
    private String currentLocation;  //Format: "123.12341234,-1234.11341234"
    private boolean gotLocation = false;
    private Sensor mySensorAccelerometer;
    private Sensor mySensorMagnetometer;
    private MyCompassView myCompassView;
    private Location userLocation;

    float[] matrixR = {};
    float[] matrixI = {};
    float[] valuesAccelerometer = {};
    float[] valuesMagneticField = {};
    float[] matrixValues = {};
    protected ArrayList<FoodTruckData> theDataReceivedSensor;

//    private float[] mOrientation;
//    private float[] mRotationMatrix;
//    private boolean mTracking;
//    private float mHeading;
//    private float mPitch;
//    private Location mLocation;
//    private GeomagneticField mGeomagneticField;
//    private boolean mHasInterference;

    public FoodTruckListViewFragment() {
    }

    @Override
    public void onDataReceived(ArrayList<FoodTruckData> theDataReceived) {
        theDataReceivedSensor = theDataReceived;
        foodTruckDataAdapter.setFoodTruckDataArrayList(theDataReceived);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(gotLocation){
            gotLocation = true;
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

        switch(event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                for(int i =0; i < 3; i++){
                    valuesAccelerometer[i] = event.values[i];
                    FoodTruckData.setValuesAccelerometer(valuesAccelerometer);
                }
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                for(int i =0; i < 3; i++){
                    valuesMagneticField[i] = event.values[i];
                    FoodTruckData.setValuesMagneticField(valuesMagneticField);
                }
                break;
        }

        boolean success = SensorManager.getRotationMatrix(
                matrixR,
                matrixI,
                valuesAccelerometer,
                valuesMagneticField);

        if(success){
//            SensorManager.remapCoordinateSystem(matrixR, userLocation.getLatitude(), userLocation.getLongitude(), matrixI);
            SensorManager.getOrientation(matrixR, matrixValues);


            FoodTruckData.setAzimuthIsDirection(matrixValues[0]);
            FoodTruckData.setMatrixR(matrixR);
            FoodTruckData.setMatrixI(matrixI);
            FoodTruckData.setValuesAccelerometer(valuesAccelerometer);
            FoodTruckData.setValuesMagneticField(valuesMagneticField);
            FoodTruckData.setMatrixValues(matrixValues);


        }
        if (success && theDataReceivedSensor != null){
            foodTruckDataAdapter.setFoodTruckDataArrayList(theDataReceivedSensor);

        }

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
        }catch (Exception e){
            e.printStackTrace();
            Log.v("ONSTART","network provider ERROR");
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
        //Sensor stuff
        mySensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mySensorMagnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, mySensorAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, mySensorMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        foodTruckDataAdapter = new FoodTruckDataAdapter(getActivity(), R.layout.food_truck_listfragment_rows, new ArrayList<FoodTruckData>());
        setListAdapter(foodTruckDataAdapter);

    }

    public void settingAdapterOnCreateView(){
        foodTruckDataAdapter = new FoodTruckDataAdapter(getActivity(), R.layout.food_truck_listfragment_rows, new ArrayList<FoodTruckData>());
        setListAdapter(foodTruckDataAdapter);
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        valuesAccelerometer = new float[3];
        valuesMagneticField = new float[3];
        matrixR = new float[9];
        matrixI = new float[9];
        matrixValues = new float[3];

    }

    public void setNewLocation(Location location){
        if (location == null){
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


}
