package com.justin.truckfinder.app;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class SecondaryActivityToBeParsedOut extends Activity implements LocationListener {

    double placeLatitude;
    double placeLongitude;
    Location userLocation;
    boolean gotLocation = false;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_truck_nearby); // // LAYOUT HERE MAY CAUSE ERRORS IF RUN
        if (gotLocation) {
            gotLocation = true;
            Log.e("NEWLOC", "Got new location");
            setNewLocation(userLocation);
            // Remove the listener you previously added
            locationManager.removeUpdates(this);
        }

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

    }

    @Override
    protected void onStart() {
        super.onStart();

        setNewLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("ONSTART", "network provider ERROR");
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.food_truck_nearby, menu); // LAYOUT HERE MAY CAUSE ERRORS IF RUN
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public double determineDirection() {
        //location of Torchy's
        placeLatitude = 30.25306507248696;
        placeLongitude = -97.74808133834131;

        double nonNegPlaceDegLat = convertToDegreesOnCircle(placeLatitude);
        double nonNegPlaceDegLng = convertToDegreesOnCircle(placeLongitude);

        double nonNegUserDegLat = convertToDegreesOnCircle(userLocation.getLatitude());
        double nonNegUserDegLng = convertToDegreesOnCircle(userLocation.getLongitude());


//        double distance = setCalculatedDistanceToPlace(userLocation.getLatitude(), userLocation.getLongitude(), placeLatitude, placeLongitude);
//        double radius = distance;

        double x1 = userLocation.getLatitude();
        double y1 = userLocation.getLongitude();
        double x2 = placeLatitude;
        double y2 = placeLongitude;

        double r;

        double destinationX = (userLocation.getLatitude() - placeLatitude);
        double destinationY = (userLocation.getLongitude() - placeLongitude);

        destinationX = Math.cos(destinationX);
        destinationY = Math.sin(destinationY);





        double theta = y1 - y2;
        double calculateDistance = Math.sin(degreesToRadians(x1));
        calculateDistance = calculateDistance * Math.sin(degreesToRadians(x2));
        calculateDistance = calculateDistance + Math.cos(degreesToRadians(x1));
        calculateDistance = calculateDistance * Math.cos(degreesToRadians(y1));
        calculateDistance = calculateDistance * Math.cos(degreesToRadians(theta));

        calculateDistance = Math.acos(calculateDistance);

        calculateDistance = radiansToDegrees(calculateDistance);

        //TODO use this is the degree Value from a 0'ed location on a circle relative to North CW on circle
        double directionInDegreesOnCircle = calculateDistance;

        return 0;


    }

    private double convertToDegreesOnCircle(double degree){
        if(degree <= 0) {
            return (degree+=360);
        }else{
            return degree;
        }
    }

    private double degToRadToDeg(double degree) {
        degree = degreesToRadians(degree);
        degree = radiansToDegrees(degree);
        return degree;
    }

    private double degreesToRadians(double degree) {
        return (degree * (Math.PI / 180.0));
    }

    private double radiansToDegrees(double radians) {
        return (radians * (180 / Math.PI));
    }

    public double setCalculatedDistanceToPlace(double aUserLatitudeStart, double aUserLongitudeStart, double aPlaceLatitudeEnd, double aPlaceLongitudeEnd) {

        // Location of Destination in GPS coordinates
        // 30.25306507248696
        // -97.74808133834131
//        double placeLatitudeEnd = aPlaceLatitudeEnd;
//               aPlaceLatitudeEnd = placeLatitude;
//        double placeLongitudeEnd = aPlaceLongitudeEnd;
//                aPlaceLongitudeEnd = placeLongitude;
        double calculateDistanceMiles;
//
//        double calculateDistanceFeet;
//
//        double calculateDistanceKilometers;

        double theta = aUserLongitudeStart - aPlaceLongitudeEnd;

        calculateDistanceMiles = Math.sin(degreesToRadians(aUserLatitudeStart))
                * Math.sin(degreesToRadians(aPlaceLatitudeEnd))
                + Math.cos(degreesToRadians(aUserLatitudeStart))
                * Math.cos(degreesToRadians(aPlaceLatitudeEnd))
                * Math.cos(degreesToRadians(theta));

        calculateDistanceMiles = Math.acos(calculateDistanceMiles); // 1.4280818115359556E-4

        calculateDistanceMiles = radiansToDegrees(calculateDistanceMiles);


        return calculateDistanceMiles = calculateDistanceMiles * 60 * 1.1515;
    }
        // Statute Miles (NOT Nautical) are what we consider "miles" (i.e. mph).
        // Note: Nautical = distance * 0.8684.
//        String units = "Miles";
        // not necessary unless people outside the USA want Kilometers.
//        if (units.equals("Kilometers")) {
//            calculateDistanceKilometers = calculateDistanceMiles * 1.609344;
//        }
//
////        setDistanceCalculatedMiles(calculateDistanceMiles);
//        //convert to feet 1 mile = 5280 feet
//        calculateDistanceFeet = calculateDistanceMiles * 5280;

//        setDistanceCalculatedFeet(calculateDistanceFeet);



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
        userLocation = location;

        determineDirection();
        setCalculatedDistanceToPlace(userLatitudeDouble, userLongitudeDouble, placeLatitude, placeLongitude);
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
}
