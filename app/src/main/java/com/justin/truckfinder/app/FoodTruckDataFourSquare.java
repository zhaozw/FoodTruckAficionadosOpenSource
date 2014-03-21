package com.justin.truckfinder.app;

/**
 * Created by justindelta on 3/21/14.
 */
public class FoodTruckDataFoursquare {

    private String fourSquareName = "Name Unavailable";
    private String addressFs = "Address Unavailable";
    private double latitudeFs = 0.00;
    private double longitudeFs = 0.00;
    private int distanceInMeters = 00;
    private String phoneNumberFsRaw = "Phone Unavailable";
    private String postalCode = "78701";

    public FoodTruckDataFoursquare(String fourSquareName, String addressFs, double latitudeFs, double longitudeFs, int distanceInMeters, String phoneNumberFsRaw, String postalCode) {
        this.fourSquareName = fourSquareName;
        this.addressFs = addressFs;
        this.latitudeFs = latitudeFs;
        this.longitudeFs = longitudeFs;
        this.distanceInMeters = distanceInMeters;
        this.phoneNumberFsRaw = phoneNumberFsRaw;
        this.postalCode = postalCode;
    }

    public String getFourSquareName() {
        return fourSquareName;
    }

    public void setFourSquareName(String fourSquareName) {
        this.fourSquareName = fourSquareName;
    }

    public String getAddressFs() {
        return addressFs;
    }

    public void setAddressFs(String addressFs) {
        this.addressFs = addressFs;
    }

    public double getLatitudeFs() {
        return latitudeFs;
    }

    public void setLatitudeFs(double latitudeFs) {
        this.latitudeFs = latitudeFs;
    }

    public double getLongitudeFs() {
        return longitudeFs;
    }

    public void setLongitudeFs(double longitudeFs) {
        this.longitudeFs = longitudeFs;
    }

    public int getDistanceInMeters() {
        return distanceInMeters;
    }

    public void setDistanceInMeters(int distanceInMeters) {
        this.distanceInMeters = distanceInMeters;
    }

    public String getPhoneNumberFsRaw() {
        return phoneNumberFsRaw;
    }

    public void setPhoneNumberFsRaw(String phoneNumberFsRaw) {
        this.phoneNumberFsRaw = phoneNumberFsRaw;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
