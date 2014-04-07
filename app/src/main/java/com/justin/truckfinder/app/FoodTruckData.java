package com.justin.truckfinder.app;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
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
    private String iconUrl = "unknown";
    private String placeName;
    private String fourSquareName = "--";
    private boolean openNow = true;
    private int priceLevel = 0;
    private double rating = 0;
    private String vicinityAddress = "--";
    private double distanceToPlaceFoursquare;
    private LatLng userLatLng;
    private String postalCode;
    private String phoneNumberFormatted;
    private String photoPlacesReference;
    private String photoPlacesURL;
    private String detailsPlacesReference;
    private NetworkImageView photoPlacesVolleyNetworkImageView;
    private static double userLatitude;
    private static double userLongitude;
    private static float[] valuesAccelerometer;
    private static float[] valuesMagneticField;
    private static float rotateDegrees;
    private static float rotatePlaceDegrees;
    private ImageLoader imageLoader;
    private static boolean isPhotoReference = false;
    private static float[] matrixR;
    private static float[] matrixI;
    private static float[] matrixValues;
    private static double azimuthIsDirection;

    private double calculateDistanceFeet;
    private double calculateDistanceMiles;


    public static boolean isIsPhotoReference() {
        return isPhotoReference;
    }

    public static void setIsPhotoReference(boolean isPhotoReference) {
        FoodTruckData.isPhotoReference = isPhotoReference;
    }

    public FoodTruckData(String fourSquareName) {
        this.fourSquareName = fourSquareName;
    }

    public FoodTruckData() {
    }

    public static float getRotateDegrees() {
        return rotateDegrees;
    }

    public static void setRotateDegrees(float rotateDegrees) {
        FoodTruckData.rotateDegrees = rotateDegrees;
    }

    public static float getRotatePlaceDegrees() {
        return rotatePlaceDegrees;
    }

    public static void setRotatePlaceDegrees(float rotatePlaceDegrees) {
        FoodTruckData.rotatePlaceDegrees = rotatePlaceDegrees;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public LatLng getUserLatLng() {
        return userLatLng;
    }

    public void setUserLatLng(LatLng userLatLng) {
        this.userLatLng = userLatLng;
    }



    public String getDetailsPlacesReference() {
        return detailsPlacesReference;
    }

    public String getPhotoPlacesURL() {
        return photoPlacesURL;
    }

    public void setPhotoPlacesURL(String photoPlacesURL) {
        this.photoPlacesURL = photoPlacesURL;
    }

    public void setDetailsPlacesReference(String detailsPlacesReference) {
        this.detailsPlacesReference = detailsPlacesReference;
    }

    public String getPhotoPlacesReference() {
        return photoPlacesReference;
    }

    public void setPhotoPlacesReference(String photoPlacesReference) {
        this.photoPlacesReference = photoPlacesReference;
        FoodTruckData.isPhotoReference = true;
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
