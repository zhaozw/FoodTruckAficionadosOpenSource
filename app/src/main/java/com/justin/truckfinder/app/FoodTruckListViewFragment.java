package com.justin.truckfinder.app;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
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
public class FoodTruckListViewFragment extends ListFragment implements LocationListener, FoodTruckDataGetter.OnDataReceivedListener {


    private FoodTruckDataAdapter foodTruckDataAdapter;
    private LocationManager locationManager;
    private OnItemSelectedListener selectedListenerCallback;
    private String currentLocation;  //Format: "123.12341234,-1234.11341234"
    private boolean gotLocation = false;
    private FoodTruckData foodTruckData;
    public double mUserLatitudeDouble;
    public double mUserLongitudeDouble;

    @Override
    public void onDataReceived(ArrayList<FoodTruckData> theDataReceived) {
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
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        foodTruckDataAdapter = new FoodTruckDataAdapter(getActivity(), R.layout.food_truck_listfragment_rows, new ArrayList<FoodTruckData>());
        setListAdapter(foodTruckDataAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
            FoodTruckData foodTruckData;
        //call back to the parent activity with the selected item
            // put callback code --> map fragment
            selectedListenerCallback.OnItemSelected();
            foodTruckData = ((FoodTruckDataAdapter)getListAdapter()).getItem(position);


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
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

    }

    public void setNewLocation(Location location){
        if (location == null){
            return;
        }

        mUserLatitudeDouble = location.getLatitude();
        mUserLongitudeDouble = location.getLongitude();

        String latitudeString = String.valueOf(mUserLatitudeDouble);
        String longitudeString = String.valueOf(mUserLongitudeDouble);

        String latitudeLongitude = latitudeString + "," + longitudeString;
        Log.v("gps", latitudeLongitude);
        currentLocation = latitudeLongitude;

        foodTruckData.setUserLatitude(mUserLatitudeDouble);
        foodTruckData.setUserLongitude(mUserLongitudeDouble);

        FoodTruckDataGetter.getInstance().performSearchRequest(getActivity(),this, currentLocation);
    }



}
