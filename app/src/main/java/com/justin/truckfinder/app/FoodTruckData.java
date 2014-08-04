package com.justin.truckfinder.app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by justindelta on 3/17/14.
 */
public class FoodTruckData implements Serializable {

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
    private int tagValue;

    public FoodTruckData() {
    }

    public FoodTruckData(String fourSquareName) {
        this.fourSquareName = fourSquareName;
    }

    // These user latitude and longitude getters and setters are always necessary.
    public static double getUserLatitude() {
        return userLatitude;
    }

    public static void setUserLatitude(double userLat) {
        userLatitude = userLat;
    }

    public static double getUserLongitude() {
        return userLongitude;
    }

    public static void setUserLongitude(double userLng) {
        userLongitude = userLng;
    }

    //
    // Tag value sent w/ volley request to append foursquare results with Goog Places API results
    // in the appropriate FoodTruckData object in the ArrayList<FoodTruckData>.
    //
    public int getTagValue() {
        return tagValue;
    }

    public void setTagValue(int tagValue) {
        this.tagValue = tagValue;
    }

    //
    // FourSquare category ID from response to remove non-food truck categorization
    //
    public String getFsCategoryId() {
        return fsCategoryId;
    }

    public void setFsCategoryId(String fsCategoryId) {
        this.fsCategoryId = fsCategoryId;
    }

    //
    // Food Truck Phone number, not formatted
    //
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    //
    // FourSquare street address
    //
    public String getFoursquareAddress() {
        return foursquareAddress;
    }

    public void setFoursquareAddress(String foursquareAddress) {
        this.foursquareAddress = foursquareAddress;
    }

    //
    // FourSquare response with food truck website URL
    //
    public String getFsTruckWebsite() {
        return fsTruckWebsite;
    }

    public void setFsTruckWebsite(String fsTruckWebsite) {
        this.fsTruckWebsite = fsTruckWebsite;
    }

    //
    // FourSquare Food Truck city
    //
    public String getFsCity() {
        return fsCity;
    }

    public void setFsCity(String fsCity) {
        this.fsCity = fsCity;
    }

    //
    // FourSquare Food Truck state
    //
    public String getFsState() {
        return fsState;
    }

    public void setFsState(String fsState) {
        this.fsState = fsState;
    }

    //
    // Zip code from foursquare or google
    //
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }


    //
    // Formatted phone number for display purposes
    //
    public String getPhoneNumberFormatted() {
        return phoneNumberFormatted;
    }

    public void setPhoneNumberFormatted(String phoneNumberFormatted) {
        this.phoneNumberFormatted = phoneNumberFormatted;
    }

    //
    // Crucial longitude and latitude from FourSquare or Google
    //
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

    //
    // FourSquare response indicating Food Truck twitter handle
    //
    public String getFsTwitter() {
        return fsTwitter;
    }

    public void setFsTwitter(String fsTwitter) {
        this.fsTwitter = fsTwitter;
    }

    //
    // Foursquare food truck id for truck-specific API requests
    //
    public String getFsTruckId() {
        return fsTruckId;
    }

    public void setFsTruckId(String fsTruckId) {
        this.fsTruckId = fsTruckId;
    }

    //
    // Foursquare-provided url link to food truck menu (most often uses UrbanSpoon)
    //
    public String getFsMenuUrl() {
        return fsMenuUrl;
    }

    public void setFsMenuUrl(String fsMenuUrl) {
        this.fsMenuUrl = fsMenuUrl;
    }

    //
    // Foursquare-provided MOBILE url link to food truck
    //
    public String getFsMobileUrl() {
        return fsMobileUrl;
    }

    public void setFsMobileUrl(String fsMobileUrl) {
        this.fsMobileUrl = fsMobileUrl;
    }

    //
    // Separately saved Google Places name (vs. Foursquare name)
    // TODO optimize since only one name is needed
    //
    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    //
    // Separately saved Foursquare Venues Food Truck name (vs. Foursquare name)
    // IMPORTANT, FS name is used to make Google Places API request per food truck
    //
    public String getFourSquareName() {
        return fourSquareName;
    }

    public void setFourSquareName(String fourSquareName) {
        this.fourSquareName = fourSquareName;
    }


    //
    // Returns true if open at time of API response, false if closed
    //
    public boolean getIsOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }


    //
    // Google Places price-level rating 1-4
    //
    public double getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(int priceLevel) {
        this.priceLevel = priceLevel;
    }


    //
    // Google Places rating uses 10th decimal place 0-4
    //
    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }


    //
    // Google Places "vicinity" street address for returned place result/response
    //
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


    //
    // Calculate distance in miles and convert to return in feet
    //
    public double getDistanceCalculatedFeet() {
        return getDistanceCalculatedMiles() * 5280;
    }


    //
    // Calculate distance using the Spherical Law of Cosines
    // Known to produce inaccurate results of place is within a few feet or thousands of miles away
    //
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

        // Here it actually is converted into miles
        calculateDistanceMiles = calculateDistanceMiles * 60 * 1.1515;

        // Statute Miles (NOT Nautical) are what we consider "miles" (i.e. mph).
        // Note: Nautical = distance * 0.8684.
        //        String units = "Miles";
        //
        // not necessary unless people outside the USA want Kilometers.
        // if (units.equals("Kilometers")) {
        //    calculateDistanceKilometers = calculateDistanceMiles * 1.609344;
        // }

        return calculateDistanceMiles;

    }


    //
    // This is the default implementation of readObject. Customise if necessary.
    //
    private void readObject(ObjectInputStream anInputStream)
            throws ClassNotFoundException, IOException {
        // always perform the default de-serialization first
        anInputStream.defaultReadObject();
    }

    //
    // This is the default implementation of writeObject. Customise if necessary.
    //
    private void writeObject(ObjectOutputStream anOutputStream)
            throws IOException {
        // perform the default serialization for all non-transient, non-static fields
        anOutputStream.defaultWriteObject();
    }

}
