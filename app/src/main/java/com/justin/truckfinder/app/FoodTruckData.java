package com.justin.truckfinder.app;

import android.location.Location;

/**
 * Created by justindelta on 3/17/14.
 */
public class FoodTruckData {


    //This is the data i want to display. Encapsulating only the data that I need.


    private double latitude = 0.0;
    private double longitude = 0.0;
    private String iconUrl = "insert string or drawable reference";
    private String placeName = "Unavailable";
    private String fourSquareName = "Unknown";
    private boolean openNow = true;
    private int priceLevel = 0;
    private double rating = 0.0;
    private String vicinityAddress = "Unknown address";
    private double distanceToPlace;
    private Location userLocation;

    public FoodTruckData(String fourSquareName) {
        this.fourSquareName = fourSquareName;
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

    public boolean isOpenNow() {
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

    public Location getLocation() {
        return userLocation;
    }

    public void setLocation(Location aUserlocation) {
        this.userLocation = aUserlocation;
    }

    public double getDistanceToPlace() {
        return distanceToPlace;
    }

    private double degreesToRadians(double degree) {
        return (degree * Math.PI / 180.0);
    }

    private double radiansToDegrees(double radians) {
        return (radians * 180 / Math.PI);
    }



    public double setDistanceToPlace(double distanceToPlace) {

        double latitudeDoubleStart = userLocation.getLatitude();
        double longitudeDoubleStart = userLocation.getLongitude();

        // Location of Destination in GPS coordinates
        double latitudeDoubleEnd = getLatitude();
        double longitudeDoubleEnd = getLongitude();

        double theta = longitudeDoubleStart - longitudeDoubleEnd;

        distanceToPlace = Math.sin(degreesToRadians(latitudeDoubleStart))
                * Math.sin(degreesToRadians(latitudeDoubleEnd))
                + Math.cos(degreesToRadians(latitudeDoubleStart))
                * Math.cos(degreesToRadians(latitudeDoubleEnd))
                * Math.cos(degreesToRadians(theta));

        distanceToPlace = Math.acos(distanceToPlace);

        distanceToPlace = radiansToDegrees(distanceToPlace);

        distanceToPlace = distanceToPlace * 60 * 1.1515;
        // Statute Miles (NOT Nautical) are what we consider "miles" in non-mathematical terms.
        String units = "Miles";
        // not necessary unless people outside the USA want Kilometers.
        if (units.equals("Kilometers")) {
            distanceToPlace = distanceToPlace * 1.609344;
            // not necessary unless people are on water which is HIGHLY UNLIKELY, but code can re-used.
        }
        return distanceToPlace;
    }

}
