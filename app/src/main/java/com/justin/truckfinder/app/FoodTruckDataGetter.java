package com.justin.truckfinder.app;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by justindelta on 3/17/14.
 * <p/>
 * //    *
 * //            * Client ID - foursquare app - MEOCEVXLA0SLUOIMYMJLFEYERRFS0AQH0XS3N3OKSYXQ1ONY
 * //    * Client secret foursquare app - 3UZ1VCKBDYULMTB24TUSS4BSJ3WO5X033X3WVS0QZ12OL3E2
 * //    * <p/>
 * //            * Access Token URL
 * //    * https://foursquare.com/oauth2/access_token
 * //            * <p/>
 * //            * Authorize URL
 * //    * https://foursquare.com/oauth2/authorize
 * //            * <p/>
 * //
 */

public class FoodTruckDataGetter {

    private static ArrayList<FoodTruckData> listOfFoodTrucks;
    private static ArrayList<FoodTruckData> incompleteFoodTrucks;
    private static OnDataReceivedListener callback;
    private static String myAPIGoogle;
    private static String myAPIFoursquare;
    private static Context context;
    private static String GPSLocation;
    private static RequestQueue requestQueue;

    //
    // Singleton pattern here:
    //
    private static FoodTruckDataGetter FOOD_GETTER_REFERENCE;
    private FoodTruckDataGetter(){

    }
    protected static FoodTruckDataGetter getInstance() {
        if (FOOD_GETTER_REFERENCE == null) {
            FOOD_GETTER_REFERENCE = new FoodTruckDataGetter();
        }
        return FOOD_GETTER_REFERENCE;
    }
    // SANITY CHECK https://api.foursquare.com/v2/venues/search?ll=30.256496,-97.747128&radius=750&categoryId=4bf58dd8d48988d1cb941735&client_id=MEOCEVXLA0SLUOIMYMJLFEYERRFS0AQH0XS3N3OKSYXQ1ONY&client_secret=3UZ1VCKBDYULMTB24TUSS4BSJ3WO5X033X3WVS0QZ12OL3E2&v=20140310

    // SANITY CHECK 2 (placement of parameters)
    // String myAPI = "https://api.foursquare.com/v2/venues/search?&radius=750&categoryId=4bf58dd8d48988d1cb941735&client_id=MEOCEVXLA0SLUOIMYMJLFEYERRFS0AQH0XS3N3OKSYXQ1ONY&client_secret=3UZ1VCKBDYULMTB24TUSS4BSJ3WO5X033X3WVS0QZ12OL3E2&v=20140310&ll=30.256496,-97.74712";

    public interface OnDataReceivedListener {
        public void onDataReceived(ArrayList<FoodTruckData> theDataReceived);

    }

    private static String getCurrentDateString() {
        long expireTimeView = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date resultTime = new Date(expireTimeView);
        return simpleDateFormat.format(resultTime);
    }

    public void performSearchRequest(Context aContext, OnDataReceivedListener aCallback , String aDeviceLocation) { //throws InterruptedException {
//        aDeviceLocation = String.valueOf(aDeviceLocation.getLatitude() + "," + aDeviceLocation.getLongitude());
        this.context = aContext;
        this.callback = aCallback;
        requestQueue = Volley.newRequestQueue(context);
        GPSLocation = aDeviceLocation;
        incompleteFoodTrucks = new ArrayList<FoodTruckData>();
        listOfFoodTrucks = new ArrayList<FoodTruckData>();
        performFoursquareFoodTruckRequestFoursquare();

    }

    private static void notifyOfDataChanged(){
        callback.onDataReceived(listOfFoodTrucks);
    }

    private static final String categoryID = "4bf58dd8d48988d1cb941735"; // foursquare Food Truck category ID
    private static final String clientID = "MEOCEVXLA0SLUOIMYMJLFEYERRFS0AQH0XS3N3OKSYXQ1ONY";
    private static final String clientSecret = "3UZ1VCKBDYULMTB24TUSS4BSJ3WO5X033X3WVS0QZ12OL3E2";
    private static final String myAPIfoursquarePartial = "https://api.foursquare.com/v2/venues/search?&radius=750&categoryId="+categoryID+"&client_id="+clientID+"&client_secret="+ clientSecret;

    private static void performFoursquareFoodTruckRequestFoursquare() {

        try {
//"ll=30.256496,-97.74712"

            try {
                StringBuilder stringBuilder = new StringBuilder(myAPIfoursquarePartial);
                stringBuilder.append("&v=" + getCurrentDateString());
                stringBuilder.append("&ll=" + URLEncoder.encode(GPSLocation, "utf8"));


                myAPIFoursquare = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    myAPIFoursquare,
                    null,
                    createMyFoursquareReqSuccessListener(),
                    createMyFoursquareReqErrorListener());

            requestQueue.add(jsonObjectRequest); //hey go get the data

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static Response.Listener<JSONObject> createMyFoursquareReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    // Extract the Place descriptions from the results
                    //Parsing the JSON

                    JSONObject jsonInitial = response;
                    JSONObject jsonResponse = jsonInitial.getJSONObject("response");
                    JSONArray resultArray = jsonResponse.getJSONArray("venues");
                    incompleteFoodTrucks = new ArrayList<FoodTruckData>(resultArray.length());
                    for (int i = 0; i < resultArray.length(); i++) {

                        FoodTruckData foodTruckData = new FoodTruckData("unknown");
                        JSONObject aResult = resultArray.getJSONObject(i);

                        try {
                            String fourSquareName = aResult.getString("name");
                            foodTruckData.setFourSquareName(fourSquareName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "foursquare name catch JSONException error");
                        }


                        incompleteFoodTrucks.add(foodTruckData);
                        //this is where the next thing should happen.

                    }
                    performAdditionalGoogleSearches();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        };

    }

    private static void performAdditionalGoogleSearches(){

        Iterator<FoodTruckData> i = incompleteFoodTrucks.iterator();
        while (i.hasNext()){
            FoodTruckData foodTruckData = i.next();
            nearbySearchGooglePlaces(foodTruckData.getFourSquareName());
            i.remove();
        }
    }


    private static Response.ErrorListener createMyFoursquareReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("VOLLEY", "FOURSQUARE RESPONSE ERROR");
            }
        };
    }

    //        class GoogleAPI {
    // SANITY CHECK:
    // https://maps.googleapis.com/maps/api/place/nearbysearch/json?sensor=true&key=AIzaSyDkyvjwKz4ZcJgUbDF7n-_OtLL0Rxe4M9E&location=30.256496,-97.747128&radius=750&keyword=truck,food


    private static final String GOOGLE_PLACES_API_KEY = "AIzaSyDkyvjwKz4ZcJgUbDF7n-_OtLL0Rxe4M9E";
    private static final String SENSOR = "true";
    private static final String myAPIGooglePartial = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?sensor="+SENSOR+"&key="+ GOOGLE_PLACES_API_KEY;
//      private static final String myAPIGooglePartial = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?sensor=true&key=AIzaSyDkyvjwKz4ZcJgUbDF7n-_OtLL0Rxe4M9E&location=30.256496,-97.747128&radius=750&keyword=food&name=torchys";
    private static void nearbySearchGooglePlaces(String aFoursquareName) {


        // &location=30.256496,-97.747128
        // &radius=750
        // &keyword=truck,food"


        try {
            StringBuilder stringBuilder = new StringBuilder(myAPIGooglePartial);
            stringBuilder.append("&location=" + GPSLocation);
            stringBuilder.append("&radius=750");
//            stringBuilder.append("&keyword=" +
            stringBuilder.append("&keyword=food");
//            stringBuilder.append("&name=" + URLEncoder.encode(aFoursquareName, "utf8"));
            stringBuilder.append("&name=" + aFoursquareName);


            myAPIGoogle = stringBuilder.toString();
;
//            myAPIGoogle.replace("+",",");
//            myAPIGoogle.replace("%27","");
//            myAPIGoogle.replace("%2","");

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    myAPIGoogle,
                    null,
                    createMyGooglePlacesReqSuccessListener(),
                    createGooglePlacesReqErrorListener());

            requestQueue.add(jsonObjectRequest); //hey go get the data
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static Response.Listener<JSONObject> createMyGooglePlacesReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Extract the Place descriptions from the results
                    //Parsing the JSON
                    ArrayList<FoodTruckData> someFoodTrucks;
                    JSONObject jsonInitial = response;
                    JSONArray resultArray = jsonInitial.getJSONArray("results");
                    someFoodTrucks = new ArrayList<FoodTruckData>(resultArray.length());
                    for (int i = 0; i < resultArray.length(); i++) {

                        FoodTruckData foodTruckData = new FoodTruckData();

                        JSONObject aResult = resultArray.getJSONObject(i);
                        JSONObject geometry = aResult.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");
                        Double latitude = location.getDouble("lat");
                        foodTruckData.setLatitude(latitude.doubleValue());

                        Double longitude = location.getDouble("lng");
                        foodTruckData.setLongitude(longitude.doubleValue());

                        try {
                            foodTruckData.setDistanceToPlace(foodTruckData.calculateDistanceToPlace());
                        }catch (Exception e){
                            e.printStackTrace();
                            Log.e("Calculating distance" , "there was an error");
                        }

                        String iconUrl = aResult.getString("icon");
                        foodTruckData.setIconUrl(iconUrl);

                        String placeName = aResult.getString("name");
                        foodTruckData.setPlaceName(placeName);

                        try {
                            JSONObject openingHours = aResult.getJSONObject("opening_hours");
                            boolean openNowBool = openingHours.getBoolean("open_now");
                            foodTruckData.setOpenNow(openNowBool);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "open_now error");
                        }

                        try {
                            Double ratingValue = aResult.getDouble("rating");
                            foodTruckData.setRating(ratingValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "rating error");
                        }

                        String vicinityAddress = aResult.getString("vicinity");
                        foodTruckData.setVicinityAddress(vicinityAddress);

                        try {
                            int priceLevelInt = aResult.getInt("price_level");
                            foodTruckData.setPriceLevel(priceLevelInt);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "price_level error");
                        }
                        //FoodTruckDataGetter.this.listOfFoodTrucks.add(foodTruckDataArrayList);
                        someFoodTrucks.add(foodTruckData);

                    }
                    listOfFoodTrucks.addAll(someFoodTrucks);
                    notifyOfDataChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private static Response.ErrorListener createGooglePlacesReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("VOLLEY", "GOOGLE PLACES RESPONSE ERROR");
            }
        };
    }


}
