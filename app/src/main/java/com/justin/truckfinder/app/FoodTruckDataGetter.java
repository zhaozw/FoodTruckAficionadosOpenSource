package com.justin.truckfinder.app;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/*
 * Created by justindelta on 3/17/14.
 * //
 * //    * FOURSQUARE API

 * //
 * //    * Access Token URL
 * //    * https://foursquare.com/oauth2/access_token
 * //
 * //    * Authorize URL
 * //    * https://foursquare.com/oauth2/authorize
 * //
 * //
 */

public class FoodTruckDataGetter {

    private static ArrayList<FoodTruckData> listOfFoodTrucks;
    private ArrayList<FoodTruckData> listOfFoodTrucksNotStatic;
    private static OnDataReceivedListener callback;
    private static String myAPIFoursquare;
    private static String myYelpApiUrl;
    private static Context context;
    private static String GPSLocation;
    private static RequestQueue requestQueue;
    private static double userLatitude;
    private static double userLongitude;
    // LatLng are from the volley library, allowing one object to contain latitude and Longitude
    private static ImageLoader mImageLoader;
    private static int indexPosition;
    private static String uniqueID;
    private static String foursquareNameStatic;
    public static HashMap<String,Integer> newHashMap;
    //
    // Singleton pattern here:
    //
    private static FoodTruckDataGetter foodTruckDataGetter;

    private FoodTruckDataGetter() {
    }

    protected static FoodTruckDataGetter getInstance() {
        if (foodTruckDataGetter == null) {
            foodTruckDataGetter = new FoodTruckDataGetter();
        }
        return foodTruckDataGetter;
    }


    // SANITY CHECK https://api.foursquare.com/v2/venues/search?ll=30.256496,-97.747128&radius=750&categoryId=4bf58dd8d48988d1cb941735&client_id=MEOCEVXLA0SLUOIMYMJLFEYERRFS0AQH0XS3N3OKSYXQ1ONY&client_secret=3UZ1VCKBDYULMTB24TUSS4BSJ3WO5X033X3WVS0QZ12OL3E2&v=20140310


    public interface OnDataReceivedListener {
        public void onDataReceived(ArrayList<FoodTruckData> theDataReceived);
    }

    private static String getCurrentDateString() {
        long expireTimeView = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date resultTime = new Date(expireTimeView);
        return simpleDateFormat.format(resultTime);
    }

    public void performSearchRequest(Context aContext, OnDataReceivedListener aCallback, String aDeviceLocation, double aUserLatitude, double aUserLongitude) {

        this.context = aContext;
        this.callback = aCallback;
        this.userLatitude = aUserLatitude;
        this.userLongitude = aUserLongitude;
        requestQueue = Volley.newRequestQueue(context);
        GPSLocation = aDeviceLocation;
        listOfFoodTrucks = new ArrayList<FoodTruckData>();
        performFoursquareFoodTruckRequestFoursquare();

        //TODO remove after attempt
//        performYelpFoodTruckRequest();
    }

    private static void notifyOfDataChanged() {
        callback.onDataReceived(listOfFoodTrucks);
    }

    private static final String foodTruckFScategoryID = "4bf58dd8d48988d1cb941735"; // foursquare Food Truck category ID

    // THESE DON'T WORK, PROVIDE YOUR OWN
    private static final String clientID = "MEOCEVXLA0SLUOIMYMJLFEYERRFS0AQH0XS3N3OKSYXQ1ONY";
    private static final String clientSecret = "3UZ1VCKBDYULMTB24TUSS4BSJ3WO5X033X3WVS0QZ12OL3E2";



    private static int radiusInMeters = 650;
    private static final String myAPIfoursquarePartial = "https://api.foursquare.com/v2/venues/search?&radius=" + String.valueOf(radiusInMeters) +"&categoryId=" + foodTruckFScategoryID + "&client_id=" + clientID + "&client_secret=" + clientSecret;

    private static void performFoursquareFoodTruckRequestFoursquare() {

        try {
            try {
                StringBuilder stringBuilder = new StringBuilder(myAPIfoursquarePartial);

                //
                // TODO Foursquare's new API may result in errors for current parameter "v"
                // TODO     probab
                //
                stringBuilder.append("&v=" + getCurrentDateString());
                stringBuilder.append("&ll=" + URLEncoder.encode(GPSLocation, "utf8"));

                myAPIFoursquare = stringBuilder.toString();
            } catch (IOException e) {
                //e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    myAPIFoursquare,
                    null,
                    createMyFoursquareReqSuccessListener(),
                    errorListener);
            requestQueue.add(jsonObjectRequest); //hey go get the data
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    private static Response.Listener<JSONObject> createMyFoursquareReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Extract the Place descriptions from the results
                    // Parsing the JSON
                    JSONObject jsonResponse = response.getJSONObject("response");
                    JSONArray resultArray = jsonResponse.getJSONArray("venues");

                    //
                    // Reset listOfFoodTrucks to a new array in case we're refreshing data
                    //
                    listOfFoodTrucks = new ArrayList<FoodTruckData>(resultArray.length());

                    for (int i = 0; i < resultArray.length(); i++) {

                        JSONObject aResult = resultArray.getJSONObject(i);
                        FoodTruckData foodTruckData = FoodTruckDataFactory.createFromJSONFourSquare(aResult);

                        FoodTruckData.setUserLatitude(userLatitude);
                        FoodTruckData.setUserLongitude(userLongitude);

                        listOfFoodTrucks.add(foodTruckData);
                    }

                    // starting at 650; 650 + 650 (done 29 times is 19500. 30 is 20,150;
                    // FS limits radius to 20,000 meters maximum, let's avoid errors in requests
                    if(radiusInMeters < 19500) {
                        if (listOfFoodTrucks.size() <= 0 || listOfFoodTrucks == null) {
                            radiusInMeters++;
                            performFoursquareFoodTruckRequestFoursquare();
                        }else {
                            removeDuplicates();
                            performAdditionalGoogleSearches();
                        }
                    }else {
                        if(listOfFoodTrucks.size() <= 0 || listOfFoodTrucks == null){
                            // there are no food trucks available in the area
                            notifyOfDataChanged();
                        }
                    }

//    wasn't using in original code notifyOfDataChanged();
//                    removeDuplicates();
//                    performAdditionalGoogleSearches();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
    }

    private static void performAdditionalGoogleSearches() {

        Iterator<FoodTruckData> i = listOfFoodTrucks.iterator();
        while (i.hasNext()) {
            FoodTruckData foodTruckData = i.next();

            nearbySearchGooglePlaces(foodTruckData);
        }
    }

    private static void removeDuplicates(){

    }

    // SANITY CHECK:
    // https://maps.googleapis.com/maps/api/place/nearbysearch/json?sensor=true&key=PROVIDE_YOUR_OWN_KEY&location=30.256496,-97.747128&radius=1000&keyword=truck,food&name=torchys

    private static final String GOOGLE_PLACES_API_KEY = "PROVIDE_YOUR_OWN_KEY";
    private static final String SENSOR = "true";
    private static final String myAPIGooglePartial = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?sensor=" + SENSOR + "&key=" + GOOGLE_PLACES_API_KEY;



    private static void nearbySearchGooglePlaces(FoodTruckData partialFoodTruck) {

        foursquareNameStatic = partialFoodTruck.getFourSquareName() + partialFoodTruck.getFsTruckId();

        String aFoursquareName = partialFoodTruck.getFourSquareName();
        String aFoursquareUniqueID = partialFoodTruck.getFsTruckId();


        String myAPIGoogle = "ERROR";
        StringBuilder stringBuilderGoog = new StringBuilder(myAPIGooglePartial);

        try {
            stringBuilderGoog.append("&location=" + partialFoodTruck.getLatitude() + "," + partialFoodTruck.getLongitude());
            stringBuilderGoog.append("&radius=300");
            stringBuilderGoog.append("&keyword=food");
            stringBuilderGoog.append("&name=" + URLEncoder.encode(aFoursquareName, "utf8"));

            myAPIGoogle = stringBuilderGoog.toString();
        } catch (Exception e) {
//            e.printStackTrace();
        }

        try {
            CustomJSONRequest jsonObjectRequest = new CustomJSONRequest(Request.Method.GET,
                    myAPIGoogle,
                    null,
                    createMyGooglePlacesReqSuccessListener(),
                    errorListener);


            indexPosition = listOfFoodTrucks.indexOf(partialFoodTruck);
            uniqueID = aFoursquareUniqueID;
            jsonObjectRequest.setTag(indexPosition);

            requestQueue.add(jsonObjectRequest); //hey go get the data
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }


    private static Response.Listener<JSONObject> createMyGooglePlacesReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Extract the Place descriptions from the results
                    // Parsing the JSON
//                    Log.e("ERROR" , response.toString());
                    JSONObject jsonInitial = response;
                    JSONObject jsonTag = response;
                    int intTag = jsonTag.getInt("RESPONSEKEY");
//                    storeJson(response, String.valueOf(intTag)+foursquareNameStatic);
                    JSONArray resultArray = jsonInitial.getJSONArray("results");

                    for (int i = 0; i < resultArray.length(); i++) {

                        //Get the particular foodTruckData object from incomplete food trucks
                        FoodTruckData foodTruckDataReference = listOfFoodTrucks.get(intTag);
                        FoodTruckData foodTruckData = listOfFoodTrucks.set(intTag, foodTruckDataReference);

                        JSONObject aResultArray = resultArray.getJSONObject(i);

                        FoodTruckDataFactory.createFromJSONGoogle(aResultArray);

                        foodTruckData.setTagValue(intTag);


                    }
                    FoodTruckStorage.getInstance().saveMyFoodTruckData(context, listOfFoodTrucks);
                    notifyOfDataChanged();
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        };
    }

    protected static Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
//            Log.v("VOLLEY", "ERROR WITH API");
        }
    };

}
