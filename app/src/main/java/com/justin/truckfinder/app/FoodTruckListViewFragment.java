package com.justin.truckfinder.app;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by justindelta on 3/17/14.
 */

public class FoodTruckListViewFragment extends ListFragment implements LocationListener, FoodTruckDataGetter.OnDataReceivedListener, SensorEventListener, MyCompassView.SensorDataRequestListener{
    ProgressBar mProgressBar;

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
    protected Context context;
    protected ArrayList<FoodTruckData> mTheDataReceived;
    protected Bundle mSavedState;
    protected Button mReturnButton = null;
    protected Button mPerformButton = null;
    protected Spinner mSpinner = null;
    float[] matrixR = {};
    float[] matrixI = {};
    float[] valuesAccelerometer = {};
    float[] valuesMagneticField = {};
    float[] matrixValues = {};

    public FoodTruckListViewFragment() {
    }

    @Override
    public void onDataReceived(ArrayList<FoodTruckData> theDataReceived) {
        //if we now have data, kill the loading screen.
        mTheDataReceived = theDataReceived;
        if (theDataReceived == null) {
            foodTruckDataAdapter.setFoodTruckDataArrayList(FoodTruckStorage.getMyFoodTruckData(context));
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

//        Bundle extras = getActivity().getIntent().getExtras();
//
//        if(extras != null){
//            String detailValue = extras.getString("KeyForSending");
//            if(detailValue != null){
//                Toast.makeText(this, Integer.parseInt(detailValue), Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        mSpinner = (Spinner) findViewById(R.id.spinnerSelection);
//        mReturnButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Intent returnIntent = new Intent();
//                String mySelection = mSpinner.getSelectedItem().toString();
//                returnIntent.putExtra("KeyForReturning",mySelection);
//                setResult(RESULT_OK, returnIntent);
//                finish();
//            }
//        });
//
//        mPerformButton = (Button) findViewById(R.id.mapsButtonImplicit);
//        mPerformButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//            int position = mSpinner.getSelectedItemPosition();
//            Intent implicitIntent = null;
//            switch (position){
//                case 0:
//
//                    implicitIntent = new Intent(Intent.ACTION_VIEW,
//                            Uri.parse("geo:");
//                    break;
//
//            }
//
//            if(implicitIntent != null){
//                if(isIntentAvailable(implicitIntent) == true){
//                    startActivity(implicitIntent);
//                }else {
//                    Toast.makeText(view.getContext(), "no application is available", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        });

    }

    public PackageManager packageManager;

    public PackageManager getPackageManager() {
        return packageManager;
    }

    public boolean isIntentAvailable(Intent intent){
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isIntentSafe = activities.size() > 0;
        return  isIntentSafe;
    }

    @Override
    public void setListShown(boolean shown) {
        super.setListShown(shown);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

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
    public float getDirection() {
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
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_FASTEST);
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
