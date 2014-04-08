package com.justin.truckfinder.app;

import com.android.volley.toolbox.ImageLoader;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by justindelta on 3/17/14.
 */
public class FoodTruckData implements Serializable{


    //This is the data i want to display. Encapsulating only the data that I need.
    //necessary
    private double latitude = 0;
    private double longitude = 0;
    private String placeName;
    private String fourSquareName = "--";
    private boolean openNow = Boolean.parseBoolean(null);
    private int priceLevel = 0;
    private double rating = 0;
    private String vicinityAddress;
    private String postalCode;
    private String phoneNumberFormatted;
    private String photoPlacesReference;
    private String photoPlacesURL;
    private static double userLatitude;
    private static double userLongitude;
    // necessary if using image network request (volley)
    private ImageLoader imageLoader;
    //TODO add try/catch in Google FoodTruckDataGetter for future scalability using Place Details
    private String detailsPlacesReference;
    //TODO determine if rotateDegrees and rotatePlaceDegrees are necessary
    private static float rotateDegrees;
    private static float rotatePlaceDegrees;
    protected static float[] valuesAccelerometer;
    protected static float[] valuesMagneticField;

    // necessary
    private double calculateDistanceFeet;
    private double calculateDistanceMiles;


    public FoodTruckData(String fourSquareName) {
        this.fourSquareName = fourSquareName;
    }

    public FoodTruckData() {
    }



    //TODO getters/setters used by myCompassView necessary for rotate and accel/magnetic?
    public static double getRotateDegrees() {
        return rotateDegrees;
    }

    public static void setRotateDegrees(float aRotateDegrees) {
        rotateDegrees = aRotateDegrees;
    }

    public static double getRotatePlaceDegrees() {
        return rotatePlaceDegrees;
    }

    public static void setRotatePlaceDegrees(float aRotatePlaceDegrees) {
        rotatePlaceDegrees = aRotatePlaceDegrees;
    }
    public static float[] getValuesAccelerometer() {
        return valuesAccelerometer;
    }

    public static void setValuesAccelerometer(float[] valuesAccelerometer) {
        FoodTruckData.valuesAccelerometer = valuesAccelerometer;
    }

    public static float[] getValuesMagneticField() {
        return valuesMagneticField;
    }

    public static void setValuesMagneticField(float[] valuesMagneticField) {
        FoodTruckData.valuesMagneticField = valuesMagneticField;
    }

    // TODO If using image volley, these are necessary.
    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public String getPhotoPlacesURL() {
        return photoPlacesURL;
    }

    public void setPhotoPlacesURL(String photoPlacesURL) {
        this.photoPlacesURL = photoPlacesURL;
    }

    public String getPhotoPlacesReference() {
        return photoPlacesReference;
    }

    public void setPhotoPlacesReference(String photoPlacesReference) {
        this.photoPlacesReference = photoPlacesReference;
    }

    // These are always necessary.
    public static double getUserLatitude() {
        return userLatitude;
    }

    public static void  setUserLatitude(double userLat) {
        userLatitude = userLat;
    }

    public static double getUserLongitude() {
        return userLongitude;
    }

    public static void setUserLongitude(double userLng) {
        userLongitude = userLng;
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

    public double degreesToRadians(double degree) {
        return (degree * Math.PI / 180.0);
    }

    public double radiansToDegrees(double radians) {
        return (radians * 180 / Math.PI);
    }

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
