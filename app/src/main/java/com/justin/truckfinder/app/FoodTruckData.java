package com.justin.truckfinder.app;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by justindelta on 3/17/14.
 */
public class FoodTruckData implements Serializable{


    //This is the data i want to display. Encapsulating only the data that I need.


    private double latitude = 0;
    private double longitude = 0;
    private String iconUrl = "unavailable";
    private String placeName;
    private String fourSquareName = "unavailable";
    private boolean openNow = true;
    private int priceLevel = 0;
    private double rating = 0;
    private String vicinityAddress = "unavailable";
    private double distanceToPlaceFoursquare;
    private LatLng userLatLng;
    private String postalCode;
    private String phoneNumberFormatted;
    private static double userLatitude;
    private static double userLongitude;
    private static float[] accelerometerTest = {1,2,3};
    private static float testX = 4;
    private static float testY = 5;
    private static float testZ = 6;

    private double calculateDistanceFeet;
    private double calculateDistanceMiles;

    public static float[] getAccelerometerTest() {
        return accelerometerTest;
    }

    public static void setAccelerometerTest(float[] accelerometerTesting) {
        if(accelerometerTesting != null) {
            accelerometerTest = accelerometerTesting;
            setAccelValues(accelerometerTest);
        }else {
            float[] floats = {11,12,13};
            accelerometerTest = floats;
            setAccelValues(accelerometerTest);
        }

    }

    public static void setAccelValues(float[] accelValues){
        setTestX(accelValues[0]);
        setTestY(accelValues[1]);
        setTestZ(accelValues[2]);
    }

    public static float getTestX() {
        return testX;
    }

    public static void setTestX(float testXx) {
        testX = testXx;
    }

    public static float getTestY() {
        return testY;
    }

    public static void setTestY(float testYy) {
        testY = testYy;
    }

    public static float getTestZ() {
        return testZ;
    }

    public static void setTestZ(float testZz) {
        testZ = testZz;
    }






    public FoodTruckData(String fourSquareName) {
        this.fourSquareName = fourSquareName;
    }

    public FoodTruckData() {

    }

    public LatLng getUserLatLng() {
        return userLatLng;
    }

    public void setUserLatLng(LatLng userLatLng) {
        this.userLatLng = userLatLng;
    }

    public static double getUserLatitude() {
        return userLatitude;
    }

    public static void  setUserLatitude(double userlat) {
        userLatitude = userlat;
    }

    public static double getUserLongitude() {
        return userLongitude;
    }

    public static void setUserLongitude(double userLong) {
        userLongitude = userLong;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }



    public String getPhoneNumberFormatted() {
        return phoneNumberFormatted;
    }

    public void setPhoneNumberFormatted(String phoneNumberFormatted) {
        this.phoneNumberFormatted = phoneNumberFormatted;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getFourSquareName() {
        return fourSquareName;
    }

    public void setFourSquareName(String fourSquareName) {
        this.fourSquareName = fourSquareName;
    }

    public boolean getIsOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public double getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(int priceLevel) {
        this.priceLevel = priceLevel;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getVicinityAddress() {
        return vicinityAddress;
    }

    public void setVicinityAddress(String vicinityAddress) {
        this.vicinityAddress = vicinityAddress;
    }

    public double getDistanceToPlaceFourSquare() {
        return distanceToPlaceFoursquare;
    }

    public double setDistanceToPlaceFourSquare(double aDistanceToPlace){
        return this.distanceToPlaceFoursquare = aDistanceToPlace * 3.2808;
    }

    private double degreesToRadians(double degree) {
        return (degree * Math.PI / 180.0);
    }

    private double radiansToDegrees(double radians) {
        return (radians * 180 / Math.PI);
    }

    //TODO may still need to account for negative values where
    // TODO southern latitudes are negatives and
    // TODO eastern latitudes are positive
    public void setCalculatedDistanceToPlace(double aUserLatitudeStart, double aUserLongitudeStart) {

        // Location of Destination in GPS coordinates
        double placeLatitudeEnd = getLatitude();
        double placeLongitudeEnd = getLongitude();

        double calculateDistanceMiles;

        double calculateDistanceFeet;

        double calculateDistanceKilometers;

        double theta = aUserLongitudeStart - placeLongitudeEnd;

        calculateDistanceMiles = Math.sin(degreesToRadians(aUserLatitudeStart))
                * Math.sin(degreesToRadians(placeLatitudeEnd))
                + Math.cos(degreesToRadians(aUserLatitudeStart))
                * Math.cos(degreesToRadians(placeLatitudeEnd))
                * Math.cos(degreesToRadians(theta));

        calculateDistanceMiles = Math.acos(calculateDistanceMiles);

        calculateDistanceMiles = radiansToDegrees(calculateDistanceMiles);
        // here it actually is converted into miles
        calculateDistanceMiles = calculateDistanceMiles * 60 * 1.1515;
        // Statute Miles (NOT Nautical) are what we consider "miles" (i.e. mph).
        // Note: Nautical = distance * 0.8684.
        String units = "Miles";
        // not necessary unless people outside the USA want Kilometers.
        if (units.equals("Kilometers")) {
            calculateDistanceKilometers = calculateDistanceMiles * 1.609344;
        }

        setDistanceCalculatedMiles(calculateDistanceMiles);
        //convert to feet 1 mile = 5280 feet
        calculateDistanceFeet = calculateDistanceMiles * 5280;

        setDistanceCalculatedFeet(calculateDistanceFeet);

    }

    public void setDistanceCalculatedFeet(double aCalculateDistance) {
        this.calculateDistanceFeet = aCalculateDistance;
    }

    public double getDistanceCalculatedFeet() {
        return calculateDistanceFeet;
    }

    public void setDistanceCalculatedMiles(double aCalculateDistance) {
        this.calculateDistanceMiles = aCalculateDistance;
    }

    public double getDistanceCalculatedMiles() {
        return calculateDistanceMiles;
    }


    private void readObject(ObjectInputStream anInputStream)
            throws ClassNotFoundException, IOException {
        // always perform the default de-serialization first
        anInputStream.defaultReadObject();
    }

    /**
     * This is the default implementation of writeObject. Customise if necessary.
     */
    private void writeObject(ObjectOutputStream anOutputStream)
            throws IOException {
        // perform the default serialization for all non-transient, non-static fields
        anOutputStream.defaultWriteObject();
    }

}
