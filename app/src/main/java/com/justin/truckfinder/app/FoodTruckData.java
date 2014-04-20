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
    private int priceLevel = 9;
    private double rating = 9;
    private String vicinityAddress = null;
    private String foursquareAddress;
    private String postalCode;
    private String phoneNumberFormatted;
    private String phone = null;
    private String photoPlacesReference;
    private String photoPlacesURL;
    private String placeDetailsReference;
    private String fsTwitter;
    private String fsTruckId;
    private String fsCategoryId;
    private String fsMenuUrl;
    private String fsMobileUrl;
    private String fsCity;
    private String fsState;
    private String fsTruckWebsite;
    private static double userLatitude;
    private static double userLongitude;
    // necessary if using image network request (volley)
    private ImageLoader imageLoader;
    private int dayOfWeek[];
    private String openHoursEachDay[];
    private String closedHoursEachDAy[];

    //TODO add try/catch in Google FoodTruckDataGetter for future scalability using Place Details
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

    public String getPlaceDetailsReference() {
        return placeDetailsReference;
    }

    public void setPlaceDetailsReference(String placeDetailsReference) {
        this.placeDetailsReference = placeDetailsReference;
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

    public String getFsCategoryId() {
        return fsCategoryId;
    }

    public void setFsCategoryId(String fsCategoryId) {
        this.fsCategoryId = fsCategoryId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFoursquareAddress() {
        return foursquareAddress;
    }

    public void setFoursquareAddress(String foursquareAddress) {
        this.foursquareAddress = foursquareAddress;
    }

    public String getFsTruckWebsite() {
        return fsTruckWebsite;
    }

    public void setFsTruckWebsite(String fsTruckWebsite) {
        this.fsTruckWebsite = fsTruckWebsite;
    }

    public String getFsCity() {
        return fsCity;
    }

    public void setFsCity(String fsCity) {
        this.fsCity = fsCity;
    }

    public String getFsState() {
        return fsState;
    }

    public void setFsState(String fsState) {
        this.fsState = fsState;
    }

    public int[] getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int[] dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String[] getOpenHoursEachDay() {
        return openHoursEachDay;
    }

    public void setOpenHoursEachDay(String[] openHoursEachDay) {
        this.openHoursEachDay = openHoursEachDay;
    }

    public String[] getClosedHoursEachDAy() {
        return closedHoursEachDAy;
    }

    public void setClosedHoursEachDAy(String[] closedHoursEachDAy) {
        this.closedHoursEachDAy = closedHoursEachDAy;
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

    public String getFsTwitter() {
        return fsTwitter;
    }

    public void setFsTwitter(String fsTwitter) {
        this.fsTwitter = fsTwitter;
    }

    public String getFsTruckId() {
        return fsTruckId;
    }

    public void setFsTruckId(String fsTruckId) {
        this.fsTruckId = fsTruckId;
    }

    public String getFsMenuUrl() {
        return fsMenuUrl;
    }

    public void setFsMenuUrl(String fsMenuUrl) {
        this.fsMenuUrl = fsMenuUrl;
    }

    public String getFsMobileUrl() {
        return fsMobileUrl;
    }

    public void setFsMobileUrl(String fsMobileUrl) {
        this.fsMobileUrl = fsMobileUrl;
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
