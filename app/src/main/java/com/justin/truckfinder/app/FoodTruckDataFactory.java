package com.justin.truckfinder.app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by justindelta on 8/1/14.
 */
public class FoodTruckDataFactory {

    private static final String TAG = "FTFACTORY";

    private static String getStringFromRoot(JSONObject rootJSON, String key){
        String result = "";

        //check for proper data before processing
        if(rootJSON == null){
            return result;
        }

        try {
            result = rootJSON.getString(key);
        } catch (JSONException e) {
            Log.d(TAG, key + " parse from rootJSON failed");
        }
        return result;
    }

    private static double getDoubleFromRoot(JSONObject rootJSON, String key){
        double result = -1;

        try {
            result = rootJSON.getDouble(key);
        } catch (JSONException e) {
            Log.d(TAG, key + " parse from rootJSON failed");
        }
        return result;
    }

    private static int getIntFromRoot(JSONObject rootJSON, String key){
        int result = -1;

        try {
            result = rootJSON.getInt(key);
        } catch (JSONException e) {
            Log.d(TAG, key + " parse from rootJSON failed");
        }
        return result;
    }

    private static boolean getBooleanFromRoot(JSONObject rootJSON, String key){
        boolean result = false;

        try {
            result = rootJSON.getBoolean(key);
        } catch (JSONException e) {
            Log.d(TAG, key + " parse from rootJSON failed");
        }
        return result;
    }


    public static FoodTruckData createFromJSONFourSquare(JSONObject inputJSON){


        FoodTruckData foodTruckDataFromFS = new FoodTruckData();

        JSONObject categoryId = null;
        JSONObject location = null;
        JSONObject contact = null;
        JSONObject menu = null;



        try{
            // FourSquare Venues Data
            location = inputJSON.getJSONObject("location");
            contact = inputJSON.getJSONObject("contact");

            JSONArray resultArrayCategories = inputJSON.getJSONArray("categories");
            categoryId = resultArrayCategories.getJSONObject(0);

            menu = inputJSON.getJSONObject("menu");

        }catch(JSONException e){
            Log.d(TAG,"parser failed during creation");
        }

        // FourSquare Data parse and set
        foodTruckDataFromFS.setFourSquareName(getStringFromRoot(inputJSON,"name"));

        foodTruckDataFromFS.setFsTwitter(getStringFromRoot(inputJSON, "twitter"));

        foodTruckDataFromFS.setFsTruckId(getStringFromRoot(inputJSON, "id"));
        foodTruckDataFromFS.setFsCategoryId(getStringFromRoot(categoryId, "id"));

        foodTruckDataFromFS.setLatitude(getDoubleFromRoot(location, "lat"));
        foodTruckDataFromFS.setLongitude(getDoubleFromRoot(location, "lng"));
        foodTruckDataFromFS.setPostalCode(getStringFromRoot(location, "postalCode"));
        foodTruckDataFromFS.setFsCity(getStringFromRoot(location, "city"));
        foodTruckDataFromFS.setFsState(getStringFromRoot(location, "state"));
        foodTruckDataFromFS.setFoursquareAddress(getStringFromRoot(location, "address"));

        foodTruckDataFromFS.setPhoneNumberFormatted(getStringFromRoot(contact,"formattedPhone"));
        foodTruckDataFromFS.setPhone(getStringFromRoot(contact,"phone"));

        foodTruckDataFromFS.setFsMenuUrl(getStringFromRoot(menu, "url"));
        foodTruckDataFromFS.setFsMobileUrl(getStringFromRoot(menu,"mobileUrl"));
        foodTruckDataFromFS.setFsTruckWebsite(getStringFromRoot(inputJSON, "url"));

        return foodTruckDataFromFS;
    }

    public static FoodTruckData createFromJSONGoogle(JSONObject inputJSON){


        FoodTruckData foodTruckDataFromGoogle = new FoodTruckData();

        JSONObject geometry;
        JSONObject locationGoogle = null;

        JSONObject openingHours = null;

        try{
            // Google Places Data JSONObject Keys
            geometry = inputJSON.getJSONObject("geometry");
            locationGoogle = geometry.getJSONObject("location");

            openingHours = inputJSON.getJSONObject("opening_hours");

        }catch(JSONException e){
            Log.d(TAG,"parser failed during creation");
        }

        // Google Places Data parse and set
        foodTruckDataFromGoogle.setPlaceName(getStringFromRoot(inputJSON, "name"));

        foodTruckDataFromGoogle.setLatitude(getDoubleFromRoot(locationGoogle, "lat"));
        foodTruckDataFromGoogle.setLongitude(getDoubleFromRoot(locationGoogle, "lng"));
        foodTruckDataFromGoogle.setVicinityAddress(getStringFromRoot(inputJSON, "vicinity"));

        foodTruckDataFromGoogle.setOpenNow(getBooleanFromRoot(openingHours, "open_now"));

        foodTruckDataFromGoogle.setRating(getDoubleFromRoot(inputJSON, "rating"));
        foodTruckDataFromGoogle.setPriceLevel(getIntFromRoot(inputJSON, "price_level"));

        return foodTruckDataFromGoogle;
    }



}