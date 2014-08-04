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
 * Created by justin on 3/17/14.
 */
public class FoodTruckDataAdapter extends ArrayAdapter<FoodTruckData> {

    // necessary?
    private static final String TAG = FoodTruckDataAdapter.class.getSimpleName();
    // necessary.
    private Context context;
    private int mLayoutResourceId;
    private ArrayList<FoodTruckData> foodTruckDataArrayList;
    private RealCompassView.SensorDataRequestListener sensorListener;

    public FoodTruckDataAdapter(Context aContext, int aResource, ArrayList<FoodTruckData> aFoodTruckList) {
        super(aContext, aResource);
        this.context = aContext;
        this.mLayoutResourceId = aResource;
        this.foodTruckDataArrayList = aFoodTruckList;
    }

    public void setFoodTruckDataArrayList(ArrayList<FoodTruckData> foodTruckDataArrayList) {
        this.foodTruckDataArrayList = foodTruckDataArrayList;
        notifyDataSetChanged();
    }

    public void setSensorListener(RealCompassView.SensorDataRequestListener listener) {
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

        public RealCompassView realCompassView;

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
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            row = layoutInflater.inflate(mLayoutResourceId, aParent, false);

            RelativeLayout aRelativeLayout = (RelativeLayout) row;

            int boxSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 85, context.getResources().getDisplayMetrics());
            int rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());

            RealCompassView realCompassView;
            realCompassView = new RealCompassView(context);
            RelativeLayout.LayoutParams compassLayout = new RelativeLayout.LayoutParams(boxSize, boxSize);

            compassLayout.setMargins(0, 0, rightMargin, 0);
            compassLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            compassLayout.addRule(RelativeLayout.BELOW, R.id.phoneButtonImplicit);

            realCompassView.setLayoutParams(compassLayout);
            realCompassView.setBackgroundResource(R.drawable.newcompass);
            realCompassView.setSensorDataCallback(sensorListener);
            aRelativeLayout.addView(realCompassView);

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
            foodTruckDataHolder.realCompassView = realCompassView;
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
        foodTruckDataHolder.realCompassView.setTag(rowPosition);
        foodTruckDataHolder.realCompassView.setOnClickListener(compassClickListener);
        foodTruckDataHolder.foursquareMenuView.setTag(rowPosition);
        foodTruckDataHolder.foursquareMenuView.setOnClickListener(foursquareMenuClickListener);


        //
        // TODO: rather than do a bunch of if else, nest your if else in another method that describes the overall process
        // (take a foodtruckdataholder and the data as parameters, call it "applyDataToViewHolder" and have it do all this junk.)
        //

        applyDataToViewHolder(foodTruckDataHolder, foodTruck);

        return row;
    }

    // TODO MAKE VIEWS INVISIBLE IF THERE IS NO DATA FROM EITHER API
    private FoodTruckDataHolder applyDataToViewHolder(FoodTruckDataHolder aFoodTruckDataHolder, FoodTruckData aFoodTruck){

        if (aFoodTruck.getPlaceName() != null) {
            aFoodTruckDataHolder.placeNameViewThree.setText(aFoodTruck.getPlaceName());
        } else {
            aFoodTruckDataHolder.placeNameViewThree.setText(aFoodTruck.getFourSquareName());
        }
        // String.format here is allowing me to use "%.2f" to indicate that the float will be converted to a string to 2 decimal places
        aFoodTruckDataHolder.placeDistanceMilesView.setText(String.format("%.2f", aFoodTruck.getDistanceCalculatedMiles()) + " Miles");
        aFoodTruckDataHolder.placeDistanceFeetView.setText(Integer.valueOf((int) aFoodTruck.getDistanceCalculatedFeet()) + " Feet");

        if (aFoodTruck.getPriceLevel() == 9) {
            aFoodTruckDataHolder.placePriceView.setText("Price: Unavailable");
        } else {
            aFoodTruckDataHolder.placePriceView.setText(String.valueOf("Price: " + (int) aFoodTruck.getPriceLevel() + " of 4"));
        }

        // for UI readability
        if (aFoodTruck.getRating() == 9) {
            aFoodTruckDataHolder.placeRatingView.setText("Rating: Unavailable");
        } else {
            aFoodTruckDataHolder.placeRatingView.setText(String.valueOf("Rating: " + aFoodTruck.getRating() + " of 5"));
        }

        // every string with vicinity address was ending with ", Austin" or , city which is 8 or city.length+2 characters in length, thus
        // the successful removal of a substring that is already too long
        String city = "";
        if(aFoodTruck.getFsCity() != null) {
            city = aFoodTruck.getFsCity();
        }else {
            city = " ";
        }
        if (aFoodTruck.getVicinityAddress() != null && aFoodTruck.getVicinityAddress().contains(", " + city)) {
            aFoodTruckDataHolder.placeAddressView.setText(aFoodTruck.getVicinityAddress().substring(0, aFoodTruck.getVicinityAddress().length() - city.length()+2));

        } else if (aFoodTruck.getVicinityAddress() != null) {
            aFoodTruckDataHolder.placeAddressView.setText(aFoodTruck.getVicinityAddress());
        } else if (aFoodTruck.getVicinityAddress() == null && aFoodTruck.getFourSquareName() != null) {

            if (aFoodTruck.getFoursquareAddress() != null && aFoodTruck.getFoursquareAddress().contains(", " + city)) {
                aFoodTruckDataHolder.placeAddressView.setText(aFoodTruck.getFoursquareAddress().substring(0, aFoodTruck.getFoursquareAddress().length() - city.length()+2));

            } else if (aFoodTruck.getFoursquareAddress() != null) {
                aFoodTruckDataHolder.placeAddressView.setText(aFoodTruck.getFoursquareAddress());
            } else {
                aFoodTruckDataHolder.placeAddressView.setText("Address Unavailable");
            }
        }


        //TODO need to make sure that phone numbers provided by fourSquare are being saved to the ArrayList for each corresponding foodTruckData object
        if (aFoodTruck.getPhoneNumberFormatted() == null) {
            aFoodTruckDataHolder.placePhoneView.setText("Phone Unavailable");
        } else {
            aFoodTruckDataHolder.placePhoneView.setText(aFoodTruck.getPhoneNumberFormatted());
        }

        if (aFoodTruck.getIsOpenNow()) {
            aFoodTruckDataHolder.placeOpenNowView.setText("Hours: Open Now");
        }
        if (!aFoodTruck.getIsOpenNow()) {
            aFoodTruckDataHolder.placeOpenNowView.setText("Hours: Currently Closed");
        }
        if (aFoodTruck.getIsOpenNow() == Boolean.parseBoolean(null)) {
            aFoodTruckDataHolder.placeOpenNowView.setText("Hours: Currently Unavailable");
        }


        if(aFoodTruck.getFsMenuUrl() !=null && aFoodTruck.getFourSquareName() != null){
            aFoodTruckDataHolder.foursquareMenuView.setText("Open " + aFoodTruck.getFourSquareName() + " Menu");
        }else if (aFoodTruck.getFsMenuUrl() != null) {
            aFoodTruckDataHolder.foursquareMenuView.setText("Touch to Open Menu");
        }else {
            aFoodTruckDataHolder.foursquareMenuView.setText("Touch to Open Browser Menu Search");
        }

        if (aFoodTruck.getFsTwitter() != null) {
            aFoodTruckDataHolder.placeExtraView.setText("Twitter: " + aFoodTruck.getFsTwitter());

        } else {
            aFoodTruckDataHolder.placeExtraView.setText("");
        }

        aFoodTruckDataHolder.realCompassView.setDirections(FoodTruckData.getUserLatitude(), FoodTruckData.getUserLongitude(), aFoodTruck.getLatitude(), aFoodTruck.getLongitude());

        return aFoodTruckDataHolder;
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
            String start = String.format("geo:%s,%s", FoodTruckData.getUserLatitude(), FoodTruckData.getUserLongitude());
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
            String start = String.format("geo:%s,%s", FoodTruckData.getUserLatitude(), FoodTruckData.getUserLongitude());
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

    public FoodTruckData getFoodTruckData() {
        FoodTruckData foodTruckNew = getFoodTruckData();
        return foodTruckNew;
    }


}