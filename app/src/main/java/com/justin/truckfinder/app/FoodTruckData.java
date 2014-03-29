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
    private String postalCode = "unavailable";
    private String phoneNumberFormatted = "unavailable";
    private static double userLatitude;
    private static double userLongitude;

    private double calculateDistanceFeet;
    private double calculateDistanceMiles;

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

    }

        public double distanceBetween(double aStartLatitude, double aStartLongitude, double anEndLatitude, double anEndLongitude){

        double calculateDistance;
        double radiansToDegrees = Math.PI / 180.0;
        double degreesToRadians = 180.0 / Math.PI;

        double theta = aStartLongitude - anEndLongitude;
        // converting to radians and using the 64-bit precision a double primitive
        // this is classically similar to the spherical law of cosine
        calculateDistance = Math.sin(aStartLatitude * radiansToDegrees);
        calculateDistance = calculateDistance * Math.sin(anEndLatitude * degreesToRadians);
        calculateDistance = calculateDistance + Math.cos(aStartLatitude * degreesToRadians);
        calculateDistance = calculateDistance * Math.cos(anEndLatitude * degreesToRadians);
        calculateDistance = calculateDistance * Math.cos(theta * degreesToRadians);
        calculateDistance = Math.acos(calculateDistance);
        calculateDistance = radiansToDegrees(calculateDistance);

        double calculateDistanceMiles = calculateDistance * 60;
        calculateDistanceMiles = calculateDistanceMiles * 1.1515;
        // Note: Nautical Miles = calculateDistanceMiles * 0.8684.
        // double calculateDistanceKilometers = calculateDistanceMiles * 1.609344
        // double calculateDistanceFeet = calculateDistanceMiles * 5280;

       setDistanceCalculatedFeet(calculateDistanceFeet);
        return calculateDistanceMiles;
    }

//    public double distanceBetween(double startLatitude, double startLongitude, double endLatitude, double endLongitude){
//        return 0.00;
//    }


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
