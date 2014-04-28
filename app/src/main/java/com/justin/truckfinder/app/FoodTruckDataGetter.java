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
import org.json.JSONException;
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
 * //    * Client ID - foursquare app - MEOCEVXLA0SLUOIMYMJLFEYERRFS0AQH0XS3N3OKSYXQ1ONY
 * //    * Client secret foursquare app - 3UZ1VCKBDYULMTB24TUSS4BSJ3WO5X033X3WVS0QZ12OL3E2
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
    private static FoodTruckDataGetter FOOD_GETTER_REFERENCE;

    private FoodTruckDataGetter() {
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

    public void performSearchRequest(Context aContext, OnDataReceivedListener aCallback, String aDeviceLocation, double aUserLatitude, double aUserLongitude) {

        this.context = aContext;
        this.callback = aCallback;
        this.userLatitude = aUserLatitude;
        this.userLongitude = aUserLongitude;
        requestQueue = Volley.newRequestQueue(context);
        GPSLocation = aDeviceLocation;
        listOfFoodTrucks = new ArrayList<FoodTruckData>();
        performFoursquareFoodTruckRequestFoursquare();
    }

    private static void notifyOfDataChanged() {
        callback.onDataReceived(listOfFoodTrucks);
    }

    private static final String categoryID = "4bf58dd8d48988d1cb941735"; // foursquare Food Truck category ID
    private static final String clientID = "MEOCEVXLA0SLUOIMYMJLFEYERRFS0AQH0XS3N3OKSYXQ1ONY";
    private static final String clientSecret = "3UZ1VCKBDYULMTB24TUSS4BSJ3WO5X033X3WVS0QZ12OL3E2";
    private static int radiusInMeters = 650;
    private static final String myAPIfoursquarePartial = "https://api.foursquare.com/v2/venues/search?&radius=" + String.valueOf(radiusInMeters) +"&categoryId=" + categoryID + "&client_id=" + clientID + "&client_secret=" + clientSecret;

    private static void performFoursquareFoodTruckRequestFoursquare() {

        try {
            try {
                StringBuilder stringBuilder = new StringBuilder(myAPIfoursquarePartial);
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
                    //Parsing the JSON
                    JSONObject jsonInitial = response;
                    JSONObject jsonResponse = jsonInitial.getJSONObject("response");
                    JSONArray resultArray = jsonResponse.getJSONArray("venues");
                    listOfFoodTrucks = new ArrayList<FoodTruckData>(resultArray.length());

                    for (int i = 0; i < resultArray.length(); i++) {
    //TODO DETERMINE WHAT DATA CAN BE CARRIED OVER AND USED WITH GOOGLE PLACES, ESPECIALLY PHONE NUMBER
                        FoodTruckData foodTruckData = new FoodTruckData("unknown");
                        JSONObject aResult = resultArray.getJSONObject(i);

                        JSONObject location = aResult.getJSONObject("location");
                        JSONObject contact = aResult.getJSONObject("contact");

                        JSONArray resultArrayCategories = aResult.getJSONArray("categories");

                        JSONObject categoryId = resultArrayCategories.getJSONObject(0);

                        try {
                            String fourSquareName = aResult.getString("name");
                            foodTruckData.setFourSquareName(fourSquareName);
                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.v("VOLLEY", "foursquare name catch JSONException error");
                        }


                        try{
                            String fsFoodTruckTwitter = aResult.getString("twitter");
                            foodTruckData.setFsTwitter(fsFoodTruckTwitter);
                        }catch (JSONException e){
                            //e.printStackTrace();
                            //Log.v("VOLLEY", "fs Twitter error");
                        }

                        try{
                            String fsCategoryId = categoryId.getString("id");
                            foodTruckData.setFsCategoryId(fsCategoryId);

                        }catch (JSONException e){
//                            e.printStackTrace();
//                            Log.v("VOLLEY", "foursquare categoryID catch JSONException error");
                        }


                        try{
                            double latitude = location.getDouble("lat");
                            foodTruckData.setLatitude(latitude);
                        }catch (JSONException e){
                           // e.printStackTrace();
                            //Log.v("FSVOLLEY", "FS Volley JSON Lat error");
                        }

                        try{
                            double longitude = location.getDouble("lng");
                            foodTruckData.setLongitude(longitude);
                        }catch (JSONException e){
                            //e.printStackTrace();
                            //Log.v("FSVOLLEY", "FS Volley JSON Lng error");
                        }

                        try{
                            String postalCode = location.getString("postalCode");
                            foodTruckData.setPostalCode(postalCode);
                        }catch (JSONException e){
                            //e.printStackTrace();
                            //Log.v("FSVOLLEY", "FS Volley JSON postal error");
                        }

                        try{
                            String city = location.getString("city");
                            foodTruckData.setFsCity(city);
                        }catch (JSONException e){
                            //e.printStackTrace();
                            //Log.v("FSVOLLEY", "FS Volley JSON city error");
                        }

                        try{
                            String state = location.getString("state");
                            foodTruckData.setFsCity(state);
                        }catch (JSONException e){
                            //e.printStackTrace();
                            //Log.v("FSVOLLEY", "FS Volley JSON state error");
                        }



                        try {
                            String formattedPhone = contact.getString("formattedPhone");
                            foodTruckData.setPhoneNumberFormatted(formattedPhone);
                        } catch (JSONException e) {
                            //e.printStackTrace();
                            //Log.v("VOLLEY", "phoneNumberFormatted catch JSONException error");
                        }

                        try {
                            String phone = contact.getString("phone");
                            foodTruckData.setPhone(phone);
                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.v("VOLLEY", "phoneNumber catch JSONException error");
                        }

                        try{
                            String twitter = contact.getString("twitter");
                            foodTruckData.setFsTwitter(twitter);
                            }catch (JSONException e){
//                            e.printStackTrace();
//                            Log.v("VOLLEY", "Twitter catch JSONException error");
                        }

                        try{
                            String truckUrl = aResult.getString("url");
                            foodTruckData.setFsTruckWebsite(truckUrl);
                        }catch (JSONException e){
//                            e.printStackTrace();
//                            Log.v("Volley", "fsWEBSITE url catch JSON error");
                        }

                        try {
                            String fsId = aResult.getString("id");
                            foodTruckData.setFsTruckId(fsId);
                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.v("VOLLEY", "fsID catch JSONException error");
                        }

                        try {
                            JSONObject fsmenu = aResult.getJSONObject("menu");
                            String menuUrl = fsmenu.getString("url");
                            foodTruckData.setFsMenuUrl(menuUrl);
                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.v("VOLLEY", "fsMenuURL catch JSONException error");
                        }

                        try {
                            JSONObject fsmenu = aResult.getJSONObject("menu");
                            String mobileUrl = fsmenu.getString("mobileUrl");
                            foodTruckData.setFsMobileUrl(mobileUrl);
                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.v("VOLLEY", "fsMobileURL catch JSONException error");
                        }

                        try {
                            String address = location.getString("address");
                            foodTruckData.setFoursquareAddress(address);
                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.v("VOLLEY", "Address catch JSONException error");

                        }

                        try {
                            foodTruckData.setUserLatitude(userLatitude);
                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.e("USERLAT", "there was an error with USerLat");
                        }

                        try {
                            foodTruckData.setUserLongitude(userLongitude);
                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.e("USERLONG", "there was an error with USerLong");
                        }

                        listOfFoodTrucks.add(foodTruckData);
                    }
                    // starting at 650; 650 + 650 (done 29 times is 19500. 30 is 20,150; FS limits radius to 20,000 meters maximum, let's avoid errors in requests
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
                        }
                    }

//    wasn't using in original code notifyOfDataChanged();
//                    removeDuplicates();
//                    performAdditionalGoogleSearches();

                } catch (Exception e) {
//                    e.printStackTrace();
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
        for (Iterator<FoodTruckData> foodTruckDataIteratorLoop = listOfFoodTrucks.iterator(); foodTruckDataIteratorLoop.hasNext();) {
            FoodTruckData foodTruckDataInitial = foodTruckDataIteratorLoop.next();
            FoodTruckData foodTruckDataNext = foodTruckDataIteratorLoop.next();
            if (!foodTruckDataInitial.getFsCategoryId().equals(foodTruckDataNext.getFsCategoryId()) || foodTruckDataInitial.getFourSquareName().trim().contains(foodTruckDataNext.getFourSquareName().trim())) {
                foodTruckDataIteratorLoop.remove();
            }

        }

    }

    // SANITY CHECK:
    // https://maps.googleapis.com/maps/api/place/nearbysearch/json?sensor=true&key=AIzaSyDkyvjwKz4ZcJgUbDF7n-_OtLL0Rxe4M9E&location=30.256496,-97.747128&radius=1000&keyword=truck,food&name=torchys

    private static final String GOOGLE_PLACES_API_KEY = "AIzaSyDkyvjwKz4ZcJgUbDF7n-_OtLL0Rxe4M9E";
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
            if(myAPIGoogle.contains("Poop")){
                myAPIGoogle = myAPIGoogle.replace("Poop","");

            }

            if(myAPIGoogle.contains("poop")){
                myAPIGoogle = myAPIGoogle.replace("poop","");
            }

            if(myAPIGoogle.contains("Parking")){
                myAPIGoogle = myAPIGoogle.replace("Parking","");
            }


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
                    //Parsing the JSON
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
                        JSONObject geometry = aResultArray.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");

                        try {
                            String placeName = aResultArray.getString("name");
                            foodTruckData.setPlaceName(placeName);
                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.v("VOLLEY", "placeName Google Places error");
                        }

                        try {
                            Double latitude = location.getDouble("lat");
                            foodTruckData.setLatitude(latitude.doubleValue());
                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.v("VOLLEY", "google latitude exception");
                        }

                        try {
                            Double longitude = location.getDouble("lng");
                            foodTruckData.setLongitude(longitude.doubleValue());
                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.v("VOLLEY", "google longitude exception");
                        }


                        try {
                            JSONObject openingHours = aResultArray.getJSONObject("opening_hours");
                            boolean openNowBool = openingHours.getBoolean("open_now");
                            foodTruckData.setOpenNow(openNowBool);
                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.v("VOLLEY", "Google open_now error");

                        }

                        try {
                            Double ratingValue = aResultArray.getDouble("rating");
                            foodTruckData.setRating(ratingValue);
                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.v("VOLLEY", "rating error");
                            foodTruckData.setRating(Integer.parseInt("--"));
                        }

                        try {
                            String vicinityAddress = aResultArray.getString("vicinity");
                            foodTruckData.setVicinityAddress(vicinityAddress);
                            foodTruckData.setFoursquareAddress(vicinityAddress);
                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.v("VOLLEY", "vicinity address error");
                        }

                        try {
                            String placeDetailsId = aResultArray.getString("reference");
                            foodTruckData.setPlaceDetailsReference(placeDetailsId);
                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.v("VOLLEY", "placeDetailsId error");
                            foodTruckData.setPriceLevel(Integer.valueOf("--"));
                        }

                        try {
                            int priceLevelInt = aResultArray.getInt("price_level");
                            foodTruckData.setPriceLevel(priceLevelInt);
                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.v("VOLLEY", "price_level error");
                            foodTruckData.setPriceLevel(Integer.valueOf("--"));
                        }

                        try {
                            JSONArray jsonArrayPhoto = aResultArray.getJSONArray("photos");
                            JSONObject jsonObject = jsonArrayPhoto.getJSONObject(i);
                            String placesReferencePhoto = jsonObject.getString("photo_reference");
                            foodTruckData.setPhotoPlacesReference(placesReferencePhoto);
                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.v("VOLLEY", "new photo reference error");
                        }

                        try {
                            foodTruckData.setTagValue(intTag);
                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.v("JSONPARSE", "saveTagId error");
                        }

                    }
//                    Log.v("ERROR", "DUPE error");
//
//                    Log.v("CLEAR", "DUPE clear");
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
