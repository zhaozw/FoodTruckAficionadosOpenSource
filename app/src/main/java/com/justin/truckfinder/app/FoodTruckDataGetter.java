package com.justin.truckfinder.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

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

    private static ArrayList<FoodTruckData> listOfFoodTrucksOld;
    private static ArrayList<FoodTruckData> listOfFoodTrucks;
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
        listOfFoodTrucksOld = new ArrayList<FoodTruckData>();
        performFoursquareFoodTruckRequestFoursquare();
    }

    private static void notifyOfDataChanged() {
        callback.onDataReceived(listOfFoodTrucks);
    }

    private static final String categoryID = "4bf58dd8d48988d1cb941735"; // foursquare Food Truck category ID
    private static final String clientID = "MEOCEVXLA0SLUOIMYMJLFEYERRFS0AQH0XS3N3OKSYXQ1ONY";
    private static final String clientSecret = "3UZ1VCKBDYULMTB24TUSS4BSJ3WO5X033X3WVS0QZ12OL3E2";
    private static final String myAPIfoursquarePartial = "https://api.foursquare.com/v2/venues/search?&radius=750&categoryId=" + categoryID + "&client_id=" + clientID + "&client_secret=" + clientSecret;

    private static void performFoursquareFoodTruckRequestFoursquare() {

        try {
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
                    listOfFoodTrucks = new ArrayList<FoodTruckData>(resultArray.length());
                    for (int i = 0; i < resultArray.length(); i++) {
    //TODO DETERMINE WHAT DATA CAN BE CARRIED OVER AND USED WITH GOOGLE PLACES, ESPECIALLY PHONE NUMBER
                        FoodTruckData foodTruckData = new FoodTruckData("unknown");
                        JSONObject aResult = resultArray.getJSONObject(i);

                        JSONObject location = aResult.getJSONObject("location");
                        JSONObject contact = aResult.getJSONObject("contact");

                        try {
                            String fourSquareName = aResult.getString("name");
                            foodTruckData.setFourSquareName(fourSquareName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "foursquare name catch JSONException error");
                        }

                        try{
                            double latitude = location.getDouble("lat");
                            foodTruckData.setLatitude(latitude);
                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.v("FSVOLLEY", "FS Volley JSON Lat error");
                        }

                        try{
                            double longitude = location.getDouble("lng");
                            foodTruckData.setLongitude(longitude);
                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.v("FSVOLLEY", "FS Volley JSON Lng error");
                        }

                        try {
                            String formattedPhone = contact.getString("formattedPhone");
                            foodTruckData.setPhoneNumberFormatted(formattedPhone);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "phoneNumberFormatted catch JSONException error");
                            foodTruckData.setPhoneNumberFormatted("Phone Unavailable");
                        }

                        try {
                            String phone = contact.getString("phone");
                            foodTruckData.setPhone(phone);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "phoneNumber catch JSONException error");
                            foodTruckData.setPhoneNumberFormatted("Phone Unavailable");
                        }

                        try {
                            String address = location.getString("address");
                            foodTruckData.setFoursquareAddress(address);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "Address catch JSONException error");
                            foodTruckData.setFoursquareAddress("See Map");
                        }

                        try {
                            foodTruckData.setUserLatitude(userLatitude);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("USERLAT", "there was an error with USerLat");
                        }

                        try {
                            foodTruckData.setUserLongitude(userLongitude);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("USERLONG", "there was an error with USerLong");
                        }

                        listOfFoodTrucks.add(foodTruckData);
                    }
                    performAdditionalGoogleSearches();
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

    // SANITY CHECK:
    // https://maps.googleapis.com/maps/api/place/nearbysearch/json?sensor=true&key=AIzaSyDkyvjwKz4ZcJgUbDF7n-_OtLL0Rxe4M9E&location=30.256496,-97.747128&radius=1000&keyword=truck,food&name=torchys

    private static final String GOOGLE_PLACES_API_KEY = "AIzaSyDkyvjwKz4ZcJgUbDF7n-_OtLL0Rxe4M9E";
    private static final String SENSOR = "true";
    private static final String myAPIGooglePartial = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?sensor=" + SENSOR + "&key=" + GOOGLE_PLACES_API_KEY;

    private static void nearbySearchGooglePlaces(FoodTruckData partialFoodTruck) {


        String aFoursquareName = partialFoodTruck.getFourSquareName();
        String aFormattedPhoneNumber = partialFoodTruck.getPhoneNumberFormatted();


        String myAPIGoogle = "ERROR";
        StringBuilder stringBuilderGoog = new StringBuilder(myAPIGooglePartial);

        try {
            stringBuilderGoog.append("&location=" + GPSLocation);
            stringBuilderGoog.append("&radius=750");
            stringBuilderGoog.append("&keyword=food");
            stringBuilderGoog.append("&name=" + URLEncoder.encode(aFoursquareName, "utf8"));

            myAPIGoogle = stringBuilderGoog.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            CustomJSONRequest jsonObjectRequest = new CustomJSONRequest(Request.Method.GET,
                    myAPIGoogle,
                    null,
                    createMyGooglePlacesReqSuccessListener(),
                    errorListener);


            indexPosition = listOfFoodTrucks.indexOf(partialFoodTruck);
            jsonObjectRequest.setTag(indexPosition);

//            jsonObjectRequest.setTag("HEREISMYTAG");
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
                    Log.e("ERROR" , response.toString());
                    JSONObject jsonInitial = response;
                    JSONObject jsonTag = response;
                    int intTag = jsonTag.getInt("RESPONSEKEY");
                    JSONArray resultArray = jsonInitial.getJSONArray("results");
                    for (int i = 0; i < resultArray.length(); i++) {

                        //Get the particular foodTruckData object from incomplete food trucks
                        FoodTruckData foodTruckDataReference = listOfFoodTrucks.get(intTag);
                        FoodTruckData foodTruckData = listOfFoodTrucks.set(intTag, foodTruckDataReference);

                        JSONObject aResultArray = resultArray.getJSONObject(i);
                        JSONObject geometry = aResultArray.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");

                        try {
                            Double latitude = location.getDouble("lat");
                            foodTruckData.setLatitude(latitude.doubleValue());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "google latitude exception");
                        }

                        try {
                            Double longitude = location.getDouble("lng");
                            foodTruckData.setLongitude(longitude.doubleValue());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "google longitude exception");
                        }

                        try {
                            String placeName = aResultArray.getString("name");
                            foodTruckData.setPlaceName(placeName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "placeName Google Places error");
                        }

                        try {
                            JSONObject openingHours = aResultArray.getJSONObject("opening_hours");
                            boolean openNowBool = openingHours.getBoolean("open_now");
                            foodTruckData.setOpenNow(openNowBool);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "Google open_now error");

                        }

                        try {
                            Double ratingValue = aResultArray.getDouble("rating");
                            foodTruckData.setRating(ratingValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "rating error");
                            foodTruckData.setRating(Integer.parseInt("--"));
                        }

                        try {
                            String vicinityAddress = aResultArray.getString("vicinity");
                            foodTruckData.setVicinityAddress(vicinityAddress);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "vicinity address error");
                        }
                        try {
                            int priceLevelInt = aResultArray.getInt("price_level");
                            foodTruckData.setPriceLevel(priceLevelInt);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "price_level error");
                            foodTruckData.setPriceLevel(Integer.valueOf("--"));
                        }

                        try {
                            JSONArray jsonArrayPhoto = aResultArray.getJSONArray("photos");
                            JSONObject jsonObject = jsonArrayPhoto.getJSONObject(i);
                            String placesReferencePhoto = jsonObject.getString("photo_reference");
                            foodTruckData.setPhotoPlacesReference(placesReferencePhoto);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("VOLLEY", "new photo reference error");
                        }

                        try{
                            foodTruckData.setTagValue(intTag);
                        }catch (Exception e){
                            e.printStackTrace();
                            Log.v("JSONPARSE", "saveTagId error");
                        }

                        // check placement

                    }

//                    listOfFoodTrucks.addAll(someFoodTrucks);
                    //remove it from incomplete.

                    ///OR
                    //
                    // dot use incomplete at all, just use listOfFoodTrucksOld. and then always just
                    // pull in new data into it, pick items out, perform search, and then update THE SAME
                    // OBJECT with extra data.

                    FoodTruckStorage.saveMyFoodTruckData(context, listOfFoodTrucks);
                    //TODO add feature that uses the code below to retrieve images
//                    performGooglePhotosRequests();
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

    //TODO delete after showing to spawrks
    private static void performGooglePhotosRequests() {

        Iterator<FoodTruckData> i = listOfFoodTrucksOld.iterator();
        while (i.hasNext()) {
            FoodTruckData foodTruckData = i.next();
            volleyGooglePlacesGetter(foodTruckData.getPhotoPlacesReference());
        }
    }
    // SANITY CHECK https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRnAAAAYz2WamgLIFUvOUHss4JQlIm4UUeIx4hnTyZJnJVmAMDofX_JuAjFR5ZezzEMqykbcHOheCr3-OVCooEui8651Ah2fzmazGGiuPt_54qTdNQwLe9Azi6WRkQlOvRQMxx_Gf1heF9gMlXYrD8yXLKZEhIQ53kT8bhMLhVVB8vSf9v7ghoU5ZpLEMxMA3dzsm-SJ7Abzd_u14g&sensor=true&key=AIzaSyDkyvjwKz4ZcJgUbDF7n-_OtLL0Rxe4M9E

    private static final String GOOGLE_PLACES_PHOTOS = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    private static final String GOOGLE_SENSOR = "&sensor=true";


    public static void volleyGooglePlacesGetter(String aPlacesPhotoReferenceKey) {


        String myGooglePlacesPhotoAPI = GOOGLE_PLACES_PHOTOS + aPlacesPhotoReferenceKey + GOOGLE_SENSOR + "&key=" + GOOGLE_PLACES_API_KEY;

        RequestQueue queue = Volley.newRequestQueue(context);


        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET, myGooglePlacesPhotoAPI, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // TODO Auto-generated method stub

                FoodTruckData foodTruckDataGooglePhotos = new FoodTruckData();
                ArrayList<FoodTruckData> googlePlacePhotos;

                googlePlacePhotos = new ArrayList<FoodTruckData>(listOfFoodTrucksOld.size());
                try {
                    foodTruckDataGooglePhotos.setPhotoPlacesURL(response.toString());
                    foodTruckDataGooglePhotos.setImageLoader(mImageLoader);
                    googlePlacePhotos.add(foodTruckDataGooglePhotos);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                listOfFoodTrucksOld.addAll(googlePlacePhotos);
                FoodTruckStorage.saveMyFoodTruckData(context, listOfFoodTrucksOld);
                notifyOfDataChanged();
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
            }
        });

        queue.add(jsonObjRequest);
        mImageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);

            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }

            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }




}
