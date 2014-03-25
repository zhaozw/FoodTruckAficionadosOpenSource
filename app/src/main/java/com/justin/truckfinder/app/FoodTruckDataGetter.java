package com.justin.truckfinder.app;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

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
    private String myAPIGoogle;
    private static String myAPIFoursquare;
    private static Context context;
    private static String GPSLocation;
    private static RequestQueue requestQueue;
    private static double userLatitude;
    private static double userLongitude;
    private static LatLng userLatLng;
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

    public void performSearchRequest(Context aContext, OnDataReceivedListener aCallback , String aDeviceLocation, double aUserLatitude, double aUserLongitude) { //throws InterruptedException {
//        aDeviceLocation = String.valueOf(aDeviceLocation.getLatitude() + "," + aDeviceLocation.getLongitude());
        this.context = aContext;
        this.callback = aCallback;
        this.userLatitude = aUserLatitude;
        this.userLongitude = aUserLongitude;
        requestQueue = Volley.newRequestQueue(context);
        GPSLocation = aDeviceLocation;
        incompleteFoodTrucks = new ArrayList<FoodTruckData>();
        listOfFoodTrucks = new ArrayList<FoodTruckData>();
        userLatLng = new LatLng(userLatitude, userLongitude);
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
                    errorListener);

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
                            foodTruckData.setPlaceName(fourSquareName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "foursquare name catch JSONException error");
                        }

                        try {
                            String fourSquareName = aResult.getString("name");
                            foodTruckData.setFourSquareName(fourSquareName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "foursquare name catch JSONException error");
                        }

                        try {
                            JSONObject contact = aResult.getJSONObject("contact");
                            String formattedPhone = contact.getString("formattedPhone");
                            foodTruckData.setPhoneNumberFormatted(formattedPhone);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "phoneNumber catch JSONException error");
                            foodTruckData.setPhoneNumberFormatted("Phone Unavailable");
                        }

//                        try {
//                            JSONObject location = aResult.getJSONObject("location");
//                            Double latitude = location.getDouble("lat");
//                            foodTruckData.setLatitude(latitude.doubleValue());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.v("VOLLEY", "Latitude catch JSONException error");
//                            foodTruckData.setLatitude(0);
//                        }
//
//                        try {
//                            JSONObject location = aResult.getJSONObject("location");
//                            Double longitude = location.getDouble("lng");
//                            foodTruckData.setLongitude(longitude.doubleValue());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.v("VOLLEY", "Longitude catch JSONException error");
//                            foodTruckData.setLongitude(0);
//                        }


                        try {
                            JSONObject location = aResult.getJSONObject("location");
                            Integer distance = location.getInt("distance");
                            foodTruckData.setDistanceToPlaceFourSquare(distance);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "distanceInMeters catch JSONException error");
                            foodTruckData.setDistanceToPlaceFourSquare(0);
                        }

                        try {
                            JSONObject location = aResult.getJSONObject("location");
                            String postalCode = location.getString("postalCode");
                            foodTruckData.setPostalCode(postalCode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "PostalCode catch JSONException error");
                            foodTruckData.setPostalCode("78701");
                        }

                        try {
                            JSONObject location = aResult.getJSONObject("location");
                            String address = location.getString("address");
                            foodTruckData.setVicinityAddress(address);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "Address catch JSONException error");
                            foodTruckData.setVicinityAddress("See Map");
                        }

                        try {
                            foodTruckData.setCalculatedDistanceToPlace(userLatitude, userLongitude);
                        }catch (Exception e) {
                            e.printStackTrace();
                            Log.e("Calculatingdistance", "there was an error");
                        }

                        try {
                            foodTruckData.setUserLatitude(userLatitude);
                        }catch (Exception e) {
                            e.printStackTrace();
                            Log.e("USERLAT", "there was an error with USerLat");
                        }

                        try {
                            foodTruckData.setUserLongitude(userLongitude);
                        }catch (Exception e) {
                            e.printStackTrace();
                            Log.e("USERLONG", "there was an error with USerLong");
                        }

                        incompleteFoodTrucks.add(foodTruckData);
                        //this is where the next thing should happen.

                    }
                    incompleteFoodTrucks.addAll(listOfFoodTrucks);
                    notifyOfDataChanged();
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

    //        class GoogleAPI {
    // SANITY CHECK:
    // https://maps.googleapis.com/maps/api/place/nearbysearch/json?sensor=true&key=AIzaSyDkyvjwKz4ZcJgUbDF7n-_OtLL0Rxe4M9E&location=30.256496,-97.747128&radius=750&keyword=truck,food


    private static final String GOOGLE_PLACES_API_KEY = "AIzaSyDkyvjwKz4ZcJgUbDF7n-_OtLL0Rxe4M9E";
    private static final String SENSOR = "true";
    private static final String myAPIGooglePartial = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?sensor="+SENSOR+"&key="+ GOOGLE_PLACES_API_KEY;

//      private static final String myAPIGooglePartial = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?sensor=true&key=AIzaSyDkyvjwKz4ZcJgUbDF7n-_OtLL0Rxe4M9E&location=30.256496,-97.747128&radius=750&keyword=food&name=torchys";
    private static void nearbySearchGooglePlaces(String aFoursquareName) {

    String myAPIGoogle = "ERROR";
        StringBuilder stringBuilderGoog = new StringBuilder(myAPIGooglePartial);
        // &location=30.256496,-97.747128
        // &radius=750
        // &keyword=truck,food"
        try {
//            StringBuilder stringBuilder = new StringBuilder(myAPIGooglePartial);
            stringBuilderGoog.append("&location=" + GPSLocation);
            stringBuilderGoog.append("&radius=750");
//            stringBuilder.append("&keyword=" +
            stringBuilderGoog.append("&keyword=food");
//            stringBuilder.append("&name=" + URLEncoder.encode(aFoursquareName, "utf8"));
            stringBuilderGoog.append("&name=" + URLEncoder.encode(aFoursquareName, "utf8"));


            myAPIGoogle = stringBuilderGoog.toString();

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
                    errorListener);

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


                        try {
                            foodTruckData.setUserLatitude(userLatitude);
                        }catch (Exception e) {
                            e.printStackTrace();
                            Log.e("USERLAT", "there was an error with USerLat");
                        }

                        try {
                            foodTruckData.setUserLongitude(userLongitude);
                        }catch (Exception e) {
                            e.printStackTrace();
                            Log.e("USERLONG", "there was an error with USerLong");
                        }

                        try {

                            foodTruckData.setUserLatLng(userLatLng);
                        }catch (Exception e) {
                            e.printStackTrace();
                            Log.e("USERLAT", "there was an error with USerLat");
                        }



                        try {
                            Double latitude = location.getDouble("lat");
                            foodTruckData.setLatitude(latitude.doubleValue());
                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.v("VOLLEY", "google latitude exception");
                        }

                        try {
                            Double longitude = location.getDouble("lng");
                            foodTruckData.setLongitude(longitude.doubleValue());
                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.v("VOLLEY", "google longitude exception");
                        }


                        if(foodTruckData.getIconUrl().equals("unknown")) {
                            try {
                                String iconUrl = aResult.getString("icon");
                                foodTruckData.setIconUrl(iconUrl);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.v("VOLLEY", "Icon URL exception");
                                foodTruckData.setIconUrl("not available");
                            }
                        }

//                        if(foodTruckData.getPlaceName().equals("unknown")) {
                            try {
                                String placeName = aResult.getString("name");
                                foodTruckData.setPlaceName(placeName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.v("VOLLEY", "placeName FS error");
                            }
//                        }
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

                        try {
                            String vicinityAddress = aResult.getString("vicinity");
                            foodTruckData.setVicinityAddress(vicinityAddress);
                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.v("VOLLEY", "vicinity address error");
                        }
                        try {
                            int priceLevelInt = aResult.getInt("price_level");
                            foodTruckData.setPriceLevel(priceLevelInt);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "price_level error");
                        }
                        //FoodTruckDataGetter.this.listOfFoodTrucks.add(foodTruckDataArrayList);
                        someFoodTrucks.add(foodTruckData);

                        try {
                            foodTruckData.setCalculatedDistanceToPlace(userLatitude, userLongitude);
                        }catch (Exception e) {
                            e.printStackTrace();
                            Log.e("Calculating distance", "there was an error");
                        }

                    }
                    listOfFoodTrucks.addAll(someFoodTrucks);
                    FoodTruckStorage.saveMyData(context, listOfFoodTrucks);
                    notifyOfDataChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }



    protected static Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.v("VOLLEY", "ERROR WITH API");
        }
    };




}
