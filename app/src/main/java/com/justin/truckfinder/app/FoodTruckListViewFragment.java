package com.justin.truckfinder.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by justindelta on 3/17/14.
 */
public class FoodTruckListViewFragment extends ListFragment implements LocationListener, FoodTruckDataGetter.FoodTruckReceiver {


    ArrayList<FoodTruckData> foodTruckDataArrayList;
    FoodTruckData foodTruckData;
    FoodTruckDataAdapter foodTruckDataAdapter;
    LocationManager locationManager;
    OnMapsSelectedListener mFoodTruckListViewCallback;
    String currentLocation;
    FoodTruckDataGetter.FoodTruckReceiver mCallback;
    FoodTruckDataGetter foodTruckDataGetter;
    ListView list;
    Location location;



    @Override
    public void onDataReceived() {

    }
    // The container Activity must implement this interface so the frag can deliver messages



    @Override
    public void onLocationChanged(Location location) {
        makeUseOfNewLocation(location);
        // Remove the listener you previously added
        locationManager.removeUpdates(this);
    }

    public void makeUseOfNewLocation(Location location) {

        if (location == null) {
            return;
        }

        locationManager.removeUpdates(this);

        double latDouble = location.getLatitude();
        double longDouble = location.getLongitude();

        String latString = String.valueOf(latDouble);
        String longString = String.valueOf(longDouble);

        String latLong = latString + "," + longString;
        Log.e("gps", latLong);
        currentLocation = latLong;
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


    protected interface OnMapsSelectedListener{
        public void displayFoodTruckMapsFragment();
    }


    @Override
    public void onStart() {
        super.onStart();

        Fragment fragment = getFragmentManager().findFragmentById(R.id.food_map_fragment);
        ListView view = getListView();

        //checking to see if both the article and headline fragment are both active
        if (fragment != null && view != null) {
            view.setChoiceMode(ListView.CHOICE_MODE_SINGLE);  //make each list item sticky
        }

        foodTruckDataGetter.performSearchRequest(location);
    }

    @Override
    public void onActivityCreated(Bundle aSavedInstanceState) {
        super.onActivityCreated(aSavedInstanceState);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        foodTruckDataAdapter = new FoodTruckDataAdapter(getActivity(), R.layout.food_truck_listfragment_rows, foodTruckDataArrayList);

        setListAdapter(foodTruckDataAdapter);



    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        //call back to the parent activity with the selected item

            foodTruckData = ((FoodTruckDataAdapter)getListAdapter()).getItem(position);
            mCallback.onDataReceived();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mFoodTruckListViewCallback = (FoodTruckListViewFragment.OnMapsSelectedListener) activity;
            // if a class cast exception is thrown, catch it and
        } catch (ClassCastException e) {
            //throw a new error indicating the following, AND don't break the app
            throw new ClassCastException(activity.toString() + "meet implement OnMapsSelectedListener!");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        makeUseOfNewLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
//        try {
//            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        Log.v("location manager", "network provider");

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);

    }

    public void newLocation(Location location){
        if (location == null){
            return;
        }

        locationManager.removeUpdates(this);

        double latitudeDouble = location.getLatitude();
        double longitudeDouble = location.getLongitude();

        String latitudeString = String.valueOf(latitudeDouble);
        String longitudeString = String.valueOf(longitudeDouble);

        String latitudeLongitude = latitudeString + "," + longitudeString;
        Log.v("gps", latitudeLongitude);
        currentLocation = latitudeLongitude;

    }


}
