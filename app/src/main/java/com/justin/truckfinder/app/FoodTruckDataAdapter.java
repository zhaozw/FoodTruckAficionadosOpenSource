package com.justin.truckfinder.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/*
 * Created by justindelta on 3/17/14.
 */
public class FoodTruckDataAdapter extends ArrayAdapter<FoodTruckData> {
    // necessary?
    private static final String TAG = FoodTruckDataAdapter.class.getSimpleName();
    // necessary.
    private Context context;
    private int mLayoutResourceId;
    private ArrayList<FoodTruckData> foodTruckDataArrayList;
    private LayoutInflater layoutInflater;
    private MyCompassView.SensorDataRequestListener sensorListener;

    public FoodTruckDataAdapter(Context aContext, int aResource, ArrayList<FoodTruckData> aFoodTruckList) {
        super(aContext, aResource);
        this.context = aContext;
        this.layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutResourceId = aResource;
        this.foodTruckDataArrayList = aFoodTruckList;
    }

    public void setFoodTruckDataArrayList(ArrayList<FoodTruckData> foodTruckDataArrayList) {
        this.foodTruckDataArrayList = foodTruckDataArrayList;
        notifyDataSetChanged();
    }

    public void setSensorListener(MyCompassView.SensorDataRequestListener listener) {
        sensorListener = listener;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class FoodTruckDataHolder {
        public TextView placeNameView;
        public TextView placeNameViewTwo;
        public TextView placeNameViewThree;
        public TextView placeDistanceMilesView;
        public TextView placeDistanceFeetView;
        public TextView placeRatingView;
        public TextView placePriceView;
        public TextView placeAddressView;
        public TextView placePhoneView;
        public TextView placeOpenNowView;
        public TextView foursquareMenuView;
        public TextView placeExtraView;
        public MyCompassView myCompassView;
        public NetworkImageView placeNetworkImageView;
        public ImageButton mapsImageButton;
        public ImageButton phoneImageButton;
    }

    @Override
    public int getCount() {
        return foodTruckDataArrayList.size();
    }


    @Override
    public View getView(int aPosition, View aConvertView, ViewGroup aParent) {
        FoodTruckDataHolder foodTruckDataHolder;

        View row = aConvertView;

        //inflate layout for a single view

        if (aConvertView == null) {
            row = layoutInflater.inflate(mLayoutResourceId, aParent, false);

            RelativeLayout aRelativeLayout = (RelativeLayout) row;

            int boxSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 85, context.getResources().getDisplayMetrics());
            int rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());

            MyCompassView myCompassView;
            myCompassView = new MyCompassView(context);
            RelativeLayout.LayoutParams compassLayout = new RelativeLayout.LayoutParams(boxSize, boxSize);

            compassLayout.setMargins(0, 0, rightMargin, 0);
            compassLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            compassLayout.addRule(RelativeLayout.BELOW, R.id.phoneButtonImplicit);

            myCompassView.setLayoutParams(compassLayout);
            myCompassView.setBackgroundResource(R.drawable.newcompass);
            myCompassView.setSensorDataCallback(sensorListener);
            aRelativeLayout.addView(myCompassView);

            // new class that draws something (line point or square) to screen FIRST. then add
            // how I want it to draw

            foodTruckDataHolder = new FoodTruckDataHolder();
            foodTruckDataHolder.placeNameView = (TextView) row.findViewById(R.id.placeNameView);
            foodTruckDataHolder.placeNameViewTwo = (TextView) row.findViewById(R.id.placeNameViewTwo);
            foodTruckDataHolder.placeNameViewThree = (TextView) row.findViewById(R.id.placeNameViewThree);
            foodTruckDataHolder.placeDistanceMilesView = (TextView) row.findViewById(R.id.placeDistanceMilesView);
            foodTruckDataHolder.placeDistanceFeetView = (TextView) row.findViewById(R.id.placeDistanceFeetView);
            foodTruckDataHolder.placePriceView = (TextView) row.findViewById(R.id.placePriceView);
            foodTruckDataHolder.placeRatingView = (TextView) row.findViewById(R.id.placeRatingView);
            foodTruckDataHolder.placeAddressView = (TextView) row.findViewById(R.id.placeAddressView);
            foodTruckDataHolder.placeOpenNowView = (TextView) row.findViewById(R.id.placeOpenNowView);
            foodTruckDataHolder.placePhoneView = (TextView) row.findViewById(R.id.placePhoneView);
            foodTruckDataHolder.foursquareMenuView = (TextView) row.findViewById(R.id.placePostalView);
            foodTruckDataHolder.placeExtraView = (TextView) row.findViewById(R.id.placeExtraView);
            foodTruckDataHolder.placeNetworkImageView = (NetworkImageView) row.findViewById(R.id.googlePlacesView);
            foodTruckDataHolder.myCompassView = myCompassView;
            foodTruckDataHolder.mapsImageButton = (ImageButton) row.findViewById(R.id.mapsButtonImplicit);
            foodTruckDataHolder.phoneImageButton = (ImageButton) row.findViewById(R.id.phoneButtonImplicit);

            row.setTag(foodTruckDataHolder);

        } else {
            foodTruckDataHolder = (FoodTruckDataHolder) row.getTag();
        }


        FoodTruckData foodTruck = this.foodTruckDataArrayList.get(aPosition);

        Integer rowPosition = aPosition;
        foodTruckDataHolder.mapsImageButton.setTag(rowPosition);
        foodTruckDataHolder.mapsImageButton.setOnClickListener(mapClickListener);
        foodTruckDataHolder.phoneImageButton.setTag(rowPosition);
        foodTruckDataHolder.phoneImageButton.setOnClickListener(phoneIconClickListener);
        foodTruckDataHolder.myCompassView.setTag(rowPosition);
        foodTruckDataHolder.myCompassView.setOnClickListener(compassClickListener);
        foodTruckDataHolder.foursquareMenuView.setTag(rowPosition);
        foodTruckDataHolder.foursquareMenuView.setOnClickListener(foursquareMenuClickListener);



        if (foodTruck.getPlaceName() != null) {
            foodTruckDataHolder.placeNameViewThree.setText(foodTruck.getPlaceName());
        } else {
            foodTruckDataHolder.placeNameViewThree.setText(foodTruck.getFourSquareName());
        }
        // String.format here is allowing me to use "%.2f" to indicate that the float will be converted to a string to 2 decimal places
        foodTruckDataHolder.placeDistanceMilesView.setText(String.format("%.2f", foodTruck.getDistanceCalculatedMiles()) + " Miles");
        foodTruckDataHolder.placeDistanceFeetView.setText(Integer.valueOf((int) foodTruck.getDistanceCalculatedFeet()) + " Feet");

        if (foodTruck.getPriceLevel() == 9) {
            foodTruckDataHolder.placePriceView.setText("Price: Unavailable");
        } else {
            foodTruckDataHolder.placePriceView.setText(String.valueOf("Price: " + (int) foodTruck.getPriceLevel() + " of 4"));
        }

        // for UI readability
        if (foodTruck.getRating() == 9) {
            foodTruckDataHolder.placeRatingView.setText("Rating: Unavailable");
        } else {
            foodTruckDataHolder.placeRatingView.setText(String.valueOf("Rating: " + foodTruck.getRating() + " of 5"));
        }

        // every string with vicinity address was ending with ", Austin" or , city which is 8 or city.length+2 characters in length, thus
        // the successful removal of a substring that is already too long
        String city = "";
        if(foodTruck.getFsCity() != null) {
            city = foodTruck.getFsCity();
        }else {
            city = " ";
        }
        if (foodTruck.getVicinityAddress() != null && foodTruck.getVicinityAddress().contains(", " + city)) {
            foodTruckDataHolder.placeAddressView.setText(foodTruck.getVicinityAddress().substring(0, foodTruck.getVicinityAddress().length() - city.length()+2));

        } else if (foodTruck.getVicinityAddress() != null) {
            foodTruckDataHolder.placeAddressView.setText(foodTruck.getVicinityAddress());
        } else if (foodTruck.getVicinityAddress() == null && foodTruck.getFourSquareName() != null) {

            if (foodTruck.getFoursquareAddress() != null && foodTruck.getFoursquareAddress().contains(", " + city)) {
                foodTruckDataHolder.placeAddressView.setText(foodTruck.getFoursquareAddress().substring(0, foodTruck.getFoursquareAddress().length() - city.length()+2));

            } else if (foodTruck.getFoursquareAddress() != null) {
                foodTruckDataHolder.placeAddressView.setText(foodTruck.getFoursquareAddress());
            } else {
                foodTruckDataHolder.placeAddressView.setText("Address Unavailable");
            }
        }


        //TODO need to make sure that phone numbers provided by fourSquare are being saved to the ArrayList for each corresponding foodTruckData object
        if (foodTruck.getPhoneNumberFormatted() == null) {
            foodTruckDataHolder.placePhoneView.setText("Phone Unavailable");
        } else {
            foodTruckDataHolder.placePhoneView.setText(foodTruck.getPhoneNumberFormatted());
        }

        if (foodTruck.getIsOpenNow() == true) {
            foodTruckDataHolder.placeOpenNowView.setText("Hours: Open");
        }
        if (foodTruck.getIsOpenNow() == false) {
            foodTruckDataHolder.placeOpenNowView.setText("Hours: Closed");
        }
        if (foodTruck.getIsOpenNow() == Boolean.parseBoolean(null)) {
            foodTruckDataHolder.placeOpenNowView.setText("Hours: Unavailable");
        }


        if (foodTruck.getFsMenuUrl() != null) {
            foodTruckDataHolder.foursquareMenuView.setText("Open Menu");
        } else {
            foodTruckDataHolder.foursquareMenuView.setText("Open Browser Menu Search");
        }

        if (foodTruck.getFsTwitter() != null) {
            foodTruckDataHolder.placeExtraView.setText(foodTruck.getFsTwitter());

        } else {
            foodTruckDataHolder.placeExtraView.setText("");
        }

        foodTruckDataHolder.myCompassView.setDirections(FoodTruckData.getUserLatitude(), FoodTruckData.getUserLongitude(), foodTruck.getLatitude(), foodTruck.getLongitude());
        //todo: give myCompassView the sensor data to do the calculation (you already gave it the vectors)

        return row;
    }

    // touching maps icon will offer option to launch to geo compatible implicit intent
    View.OnClickListener mapClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            String truckName = "";
            String address = "";
            String postalCode = "";
            Integer rowPosition = (Integer) view.getTag();
            FoodTruckData foodTruck = foodTruckDataArrayList.get(rowPosition);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            String start = String.format("geo:%s,%s", foodTruck.getUserLatitude(), foodTruck.getUserLongitude());
            if (foodTruck.getPlaceName() != null) {
                truckName = foodTruck.getPlaceName();
            } else {
                truckName = foodTruck.getFourSquareName();
            }
            if (foodTruck.getVicinityAddress() != null) {
                address = foodTruck.getVicinityAddress();
            } else if (foodTruck.getFoursquareAddress() != null){
                address = foodTruck.getFoursquareAddress();
            }else {
                address = "";
            }

            if (foodTruck.getPostalCode() !=null && foodTruck.getFsCity() != null && foodTruck.getFsState() != null){
                postalCode = foodTruck.getFsCity() + "," + foodTruck.getFsState() + "," + foodTruck.getPostalCode();
            }else {
                postalCode = "";
            }

            String withQuery = start + String.format("?q=%s,%s(%s)", foodTruck.getLatitude(), foodTruck.getLongitude(), truckName + " " + address + postalCode);
            Intent geoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(withQuery));
            context.startActivity(geoIntent);
        }
    };

    // touching compass icon will offer option to launch to geo compatible implicit intent
    View.OnClickListener compassClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            String truckName = "";
            String address = "";
            String postalCode = "";
            Integer rowPosition = (Integer) view.getTag();
            FoodTruckData foodTruck = foodTruckDataArrayList.get(rowPosition);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            String start = String.format("geo:%s,%s", foodTruck.getUserLatitude(), foodTruck.getUserLongitude());
            if (foodTruck.getPlaceName() != null) {
                truckName = foodTruck.getPlaceName();
            } else {
                truckName = foodTruck.getFourSquareName();
            }
            if (foodTruck.getVicinityAddress() != null) {
                address = foodTruck.getVicinityAddress();
            } else if (foodTruck.getFoursquareAddress() != null){
                address = foodTruck.getFoursquareAddress();
            }else {
                address = "";
            }

            if (foodTruck.getPostalCode() !=null && foodTruck.getFsCity() != null && foodTruck.getFsState() != null){
                postalCode = foodTruck.getFsCity() + "," + foodTruck.getFsState() + "," + foodTruck.getPostalCode();
            }else {
                postalCode = "";
            }

            String withQuery = start + String.format("?q=%s,%s(%s)", foodTruck.getLatitude(), foodTruck.getLongitude(), truckName + " " + address + postalCode);
            Intent geoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(withQuery));
            context.startActivity(geoIntent);

        }
    };

    // touching displayed phone number will offer option to call via implicit intent
    View.OnClickListener phoneIconClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            Integer rowPosition = (Integer) view.getTag();
            FoodTruckData foodTruck = foodTruckDataArrayList.get(rowPosition);

            if (foodTruck.getPhone() != null) {
                String phoneCall = "tel:" + foodTruck.getPhone();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(phoneCall));
                context.startActivity(callIntent);

            } else {
                Toast.makeText(context, "Phone Number Unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    };

    // touching see foursquare menu will do it's thing
    View.OnClickListener foursquareMenuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            String fsMenuURL = "";
            String truckName = "";
            String address = "";
            Integer rowPosition = (Integer) view.getTag();
            FoodTruckData foodTruck = foodTruckDataArrayList.get(rowPosition);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);

            if (foodTruck.getPlaceName() != null) {
                truckName = foodTruck.getPlaceName();
            } else {
                truckName = foodTruck.getFourSquareName();
            }

            if (foodTruck.getVicinityAddress() != null) {
                address = foodTruck.getVicinityAddress();
            } else if (foodTruck.getFoursquareAddress() != null){
                address = foodTruck.getFoursquareAddress();
            }else {
                address = "";
            }

            if (foodTruck.getFsMobileUrl() != null) {
                fsMenuURL = foodTruck.getFsMobileUrl();
            } else if (foodTruck.getFsMobileUrl() == null && foodTruck.getPostalCode() != null) {
                String postalCode = foodTruck.getPostalCode();
                fsMenuURL = "http://www.google.com/search?ie=UTF-8&oe=UTF-8&sourceid=navclient&btnI=1&q=" + truckName + "+Menu" + "+" + address;
            }
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fsMenuURL));
            context.startActivity(browserIntent);

        }
    };


    //TODO find out why there is a recursion warning and a better way of implementing the following, if any
    public FoodTruckData getFoodTruckData() {
        FoodTruckData foodTruckNew = getFoodTruckData();
        return foodTruckNew;
    }


}