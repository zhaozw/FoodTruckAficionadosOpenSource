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

/*
 * Created by justindelta on 3/17/14.
 */
//todo: Note the comment below regarding the Moto G Sensors (6 of them)
// [{Sensor name="LIS3DH 3-axis Accelerometer", vendor="ST Micro", version=1, type=1, maxRange=39.24, resolution=0.009810001, power=0.25, minDelay=0},
// {Sensor name="AK8963 3-axis Magnetic field sensor", vendor="Asahi Kasei", version=1, type=2, maxRange=2000.0, resolution=0.0625, power=6.8, minDelay=0},
// {Sensor name="AK8963 Orientation sensor", vendor="Asahi Kasei", version=1, type=3, maxRange=360.0, resolution=0.015625, power=7.05, minDelay=0},
// {Sensor name="CT406 Proximity sensor", vendor="TAOS", version=1, type=8, maxRange=100.0, resolution=100.0, power=3.0, minDelay=0},
// {Sensor name="CT406 Light sensor", vendor="TAOS", version=1, type=5, maxRange=27000.0, resolution=1.0, power=0.175, minDelay=20000},
// {Sensor name="Display Rotation sensor", vendor="Motorola", version=1, type=31, maxRange=4.0, resolution=1.0, power=0.0, minDelay=0}]
//todo: Note the comment below regarding he LG G2 Sensors (25 + 2 of them)
//[[{Sensor name="LGE Accelerometer Sensor Accelerometer", vendor="STMicroelectronics", version=1, type=1, maxRange=39.226593, resolution=0.0011901855, power=0.28, minDelay=8333},
// {Sensor name="LGE Magnetometer Sensor Magnetometer", vendor="AKM", version=1, type=2, maxRange=4911.9995, resolution=0.14953613, power=5.0, minDelay=16666},
// {Sensor name="LGE Magnetometer Sensor Magnetometer Uncalibrated", vendor="AKM", version=1, type=14, maxRange=4911.9995, resolution=0.14953613, power=5.0, minDelay=16666},
// {Sensor name="LGE Gyroscope Sensor Gyroscope", vendor="STMicroelectronics", version=1, type=4, maxRange=34.906586, resolution=0.0012207031, power=6.1, minDelay=5000},
// {Sensor name="LGE Gyroscope Sensor Gyroscope Uncalibrated", vendor="STMicroelectronics", version=1, type=16, maxRange=34.906586, resolution=0.0012207031, power=6.1, minDelay=5000},
// {Sensor name="LGE Proximity Sensor Proximity", vendor="Avago", version=2, type=8, maxRange=5.000305, resolution=0.10070801, power=12.675, minDelay=0},
// {Sensor name="LGE Light Sensor Light", vendor="Avago", version=2, type=5, maxRange=10000.0, resolution=0.009994507, power=0.175, minDelay=0},
// {Sensor name="Gravity", vendor="Qualcomm", version=1, type=9, maxRange=39.226593, resolution=0.0011901855, power=6.38, minDelay=8333},
// {Sensor name="Linear Acceleration", vendor="Qualcomm", version=1, type=10, maxRange=39.226593, resolution=0.0011901855, power=6.38, minDelay=8333},
// {Sensor name="Rotation Vector", vendor="Qualcomm", version=1, type=11, maxRange=1.0, resolution=5.9604645E-8, power=11.38, minDelay=8333},
// {Sensor name="Step Detector", vendor="Qualcomm", version=1, type=18, maxRange=1.0, resolution=1.0, power=0.28, minDelay=0}, {
// Sensor name="Step Counter", vendor="Qualcomm", version=1, type=19, maxRange=1.0, resolution=1.0, power=0.28, minDelay=0},
// {Sensor name="Significant Motion Detector", vendor="Qualcomm", version=1, type=17, maxRange=1.0, resolution=1.0, power=0.28, minDelay=-1},
// {Sensor name="Game Rotation Vector", vendor="Qualcomm", version=1, type=15, maxRange=1.0, resolution=5.9604645E-8, power=6.38, minDelay=8333},
// {Sensor name="Geomagnetic Rotation Vector", vendor="Qualcomm", version=1, type=20, maxRange=1.0, resolution=5.9604645E-8, power=5.28, minDelay=16666},
// {Sensor name="Orientation", vendor="Qualcomm", version=1, type=3, maxRange=360.0, resolution=0.1, power=11.38, minDelay=8333},
// {Sensor name="AMD", vendor="Qualcomm", version=1, type=33171006, maxRange=1.0, resolution=1.0, power=1.0, minDelay=0},
// {Sensor name="RMD", vendor="Qualcomm", version=1, type=33171007, maxRange=1.0, resolution=1.0, power=1.0, minDelay=0},
// {Sensor name="VMD", vendor="Qualcomm", version=1, type=33171008, maxRange=1.0, resolution=1.0, power=1.0, minDelay=0},
// {Sensor name="Basic Gestures", vendor="Qualcomm", version=1, type=33171000, maxRange=7.0, resolution=1.0, power=0.28, minDelay=0},
// {Sensor name="Facing", vendor="Qualcomm", version=1, type=33171002, maxRange=3.0, resolution=1.0, power=0.28, minDelay=0},
// {Sensor name="Tilt", vendor="Qualcomm", version=1, type=33171003, maxRange=180.0, resolution=0.1, power=6.1, minDelay=10000},
// {Sensor name="Pedometer", vendor="Qualcomm", version=1, type=33171009, maxRange=1.0, resolution=1.0, power=0.28, minDelay=0},
// {Sensor name="PEDESTRIAN-ACTIVITY-MONITOR", vendor="Qualcomm", version=1, type=33171010, maxRange=65535.0, resolution=1.0, power=0.28, minDelay=0},
// {Sensor name="Motion Accel", vendor="Qualcomm", version=1, type=33171011, maxRange=39.226593, resolution=0.0011901855, power=0.28, minDelay=0}],
//TODO: NOTE THAT ONLY TYPE 1 (TYPE_ACCELEROMETER) and TYPE 2 (TYPE_MAGNETIC_FIELD) are common among the two in relation to the compass use. The Moto G is
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
        if (gotLocation) {
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

        // tried using a switch statement, also did not reach the second case where getType() == Sensor.TYPE_MAGNETIC_FIELD
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
//            SensorManager.remapCoordinateSystem(matrixR, ?????, ?????, matrixI);
                // converted to degrees in foodTruckData when being set as "setAzimuthIsDirection"
                SensorManager.getOrientation(matrixR, matrixValues);
                FoodTruckData.setAzimuthIsDirection(matrixValues[0]);
                FoodTruckData.setMatrixR(matrixR);
                FoodTruckData.setMatrixI(matrixI);
                FoodTruckData.setValuesAccelerometer(valuesAccelerometer);
                FoodTruckData.setValuesMagneticField(valuesMagneticField);
                FoodTruckData.setMatrixValues(matrixValues);
            }
            if (success && theDataReceivedSensor != null) {
                foodTruckDataAdapter.setFoodTruckDataArrayList(theDataReceivedSensor);
            }
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
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("ONSTART", "network provider ERROR");
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
        //Sensor stuff
        mySensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mySensorMagnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
// Moved code below to onResume because it will only notice both registered listeners if it is in OnResume
//        sensorManager.registerListener(this, mySensorAccelerometer, SensorManager.SENSOR_DELAY_GAME);
//        sensorManager.registerListener(this, mySensorMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        foodTruckDataAdapter = new FoodTruckDataAdapter(getActivity(), R.layout.food_truck_listfragment_rows, new ArrayList<FoodTruckData>());
        setListAdapter(foodTruckDataAdapter);

    }

    public void settingAdapterOnCreateView() {
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
//        setRetainInstance(true);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        valuesAccelerometer = new float[3];
        valuesMagneticField = new float[3];
        matrixR = new float[9];
        matrixI = new float[9];
        matrixValues = new float[3];

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

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);

    }
}
