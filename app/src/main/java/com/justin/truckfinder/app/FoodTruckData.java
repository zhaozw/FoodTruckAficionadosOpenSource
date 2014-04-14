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
    private String placeName; // google places name
    private String fourSquareName = "--";
    private boolean openNow = Boolean.parseBoolean(null);
    private int priceLevel = 0;
    private double rating = 0;
    private String vicinityAddress;
    private String foursquareAddress;
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
    private int tagValue;

    public FoodTruckData() {
    }

    public FoodTruckData(String fourSquareName) {
        this.fourSquareName = fourSquareName;
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

    public int getTagValue() {
        return tagValue;
    }

    public void setTagValue(int tagValue) {
        this.tagValue = tagValue;
    }

    public String getFoursquareAddress() {
        return foursquareAddress;
    }

    public void setFoursquareAddress(String foursquareAddress) {
        this.foursquareAddress = foursquareAddress;
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



    public double getDistanceCalculatedFeet() {
        return getDistanceCalculatedMiles() * 5280;
    }

    public double getDistanceCalculatedMiles() {


        // Location of Destination in GPS coordinates
        double placeLatitudeEnd = getLatitude();
        double placeLongitudeEnd = getLongitude();

        double calculateDistanceMiles;

        double theta = getUserLongitude() - placeLongitudeEnd;

        calculateDistanceMiles = Math.sin(degreesToRadians(getUserLatitude()))
                * Math.sin(degreesToRadians(placeLatitudeEnd))
                + Math.cos(degreesToRadians(getUserLatitude()))
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
//        if (units.equals("Kilometers")) {
//            calculateDistanceKilometers = calculateDistanceMiles * 1.609344;
//        }

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
