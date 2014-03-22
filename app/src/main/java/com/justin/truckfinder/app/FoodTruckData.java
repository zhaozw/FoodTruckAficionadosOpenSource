package com.justin.truckfinder.app;

/**
 * Created by justindelta on 3/17/14.
 */
public class FoodTruckData {


    //This is the data i want to display. Encapsulating only the data that I need.


    private double latitude = 0.0;
    private double longitude = 0.0;
    private String iconUrl = "unavailable";
    private String placeName = "unavailable";
    private String fourSquareName = "unavailable";
    private boolean openNow = true;
    private int priceLevel = 0;
    private double rating = 0.0;
    private String vicinityAddress = "unavailable";
    private double distanceToPlaceFoursquare = 0;
    private double distanceCalculated;
    private String postalCode = "unavailable";
    private String phoneNumberFormatted = "unavailable";
    private double userLatitude = 0.0;
    private double userLongitude = 0.0;

    private double calculateDistance;

    public FoodTruckData() {

    }

    public double getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(double userLatitude) {
        this.userLatitude = userLatitude;
    }

    public double getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(double userLongitude) {
        this.userLongitude = userLongitude;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public double getDistanceCalculated() {
        return calculateDistance;
    }

    public void setDistanceCalculated(double aCalculateDistance) {
        this.calculateDistance = aCalculateDistance;
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
        return this.distanceToPlaceFoursquare = aDistanceToPlace;
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
    public double setCalculatedDistanceToPlace(double aLatitude, double aLongitude) {

        double latitudeDoubleStart = aLatitude;
        double longitudeDoubleStart = aLongitude;

        // Location of Destination in GPS coordinates
        double latitudeDoubleEnd = getLatitude();
        double longitudeDoubleEnd = getLongitude();



        double theta = longitudeDoubleStart - longitudeDoubleEnd;

        calculateDistance = Math.sin(degreesToRadians(latitudeDoubleStart))
                            * Math.sin(degreesToRadians(latitudeDoubleEnd))
                            + Math.cos(degreesToRadians(latitudeDoubleStart))
                            * Math.cos(degreesToRadians(latitudeDoubleEnd))
                            * Math.cos(degreesToRadians(theta));

        calculateDistance = Math.acos(calculateDistance);

        calculateDistance = radiansToDegrees(calculateDistance);
        // TODO include reference for why we're multiplyin' by 60, then * 1.1515
        calculateDistance = calculateDistance * 60 * 1.1515;
        // Statute Miles (NOT Nautical) are what we consider "miles" (i.e. mph).
        // Note: Nautical = distance * 0.8684.
        String units = "Miles";
        // not necessary unless people outside the USA want Kilometers.
        if (units.equals("Kilometers")) {
            calculateDistance = calculateDistance * 1.609344;
        }
        return calculateDistance;
    }

}
