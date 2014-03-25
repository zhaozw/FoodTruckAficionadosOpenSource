package com.justin.truckfinder.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/*
 * Created by justindelta on 3/17/14.
 */
public class FoodTruckMapsFragment extends MapFragment implements FoodTruckListViewFragment.OnItemSelectedListener{

//    AIzaSyDkyvjwKz4ZcJgUbDF7n-_OtLL0Rxe4M9E
    private static Context context;
    public FoodTruckData foodTruckData;
    private LatLng USER = foodTruckData.getUserLatLng();
    private static LatLng PLACE;
    private GoogleMap truckMap;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.map_fragment_food_truck, container, false);


        truckMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();


        Marker user = truckMap.addMarker(new MarkerOptions().position(USER)
                .title("Hamburg"));
        Marker place = truckMap.addMarker(new MarkerOptions()
                .position(PLACE)
                .title("Kiel")
                .snippet("Kiel is cool")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_launcher)));

        // Move the camera instantly to hamburg with a zoom of 15.
        truckMap.moveCamera(CameraUpdateFactory.newLatLngZoom(USER, 15));

        // Zoom in, animating the camera.
        truckMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        //...

        return view;
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (truckMap == null) {
            truckMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (truckMap != null) {
                // The Map is verified. It is now safe to manipulate the map.

            }
        }
    }

    @Override
    public void OnItemSelected() {

    }
}
