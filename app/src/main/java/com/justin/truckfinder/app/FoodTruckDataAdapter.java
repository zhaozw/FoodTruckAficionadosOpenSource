package com.justin.truckfinder.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by justindelta on 3/17/14.
 */
public class FoodTruckDataAdapter extends ArrayAdapter<FoodTruckData>{
    private static final String TAG = FoodTruckDataAdapter.class.getSimpleName();
    private Context context;
    private int mLayoutResourceId;
    private ArrayList<FoodTruckData> foodTruckDataArrayList;
    private LayoutInflater layoutInflater;
    private FoodTruckData foodTruckData;


    public FoodTruckDataAdapter(Context aContext, int aResource, ArrayList<FoodTruckData> aFoodTruckList) {
        super(aContext, aResource);
        this.context = aContext;
        this.layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutResourceId = aResource;
        this.foodTruckDataArrayList = aFoodTruckList;
    }

    public void setFoodTruckDataArrayList(ArrayList<FoodTruckData> foodTruckDataArrayList) {
        this.foodTruckDataArrayList = foodTruckDataArrayList;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class FoodTruckDataHolder {
        public TextView placeNameView;
        public TextView placeDistanceView;
        public TextView placeRatingView;
        public TextView placePriceView;
        public TextView placeAddressView;
        public TextView placePhoneView;
        public TextView placeOpenNowView;
        public TextView placePostalView;
//        public MyCompassView placeCompassView;
//        public NetworkImageView placeThumbnail;
    }

    @Override
    public int getCount() {
        return foodTruckDataArrayList.size();
    }

    @Override
    public View getView(int aPosition, View aConvertView, ViewGroup aParent){
        FoodTruckDataHolder foodTruckDataHolder;


        View row = aConvertView;

        //inflate layout for a single view
//        LayoutInflater layoutInflater = LayoutInflater.from(context);

        if(aConvertView == null){
            row = layoutInflater.inflate(mLayoutResourceId, aParent, false);

//            MyCompassView myCompassView = new MyCompassView(context);
//
//            //new class that draws something (line point or square) to screen FIRST. then add
//            // how I want it to draw
//            ((RelativeLayout)row).addView(myCompassView);



            foodTruckDataHolder = new FoodTruckDataHolder();
            foodTruckDataHolder.placeNameView = (TextView) row.findViewById(R.id.placeNameView);
            foodTruckDataHolder.placeDistanceView = (TextView) row.findViewById(R.id.placeDistanceView);
            foodTruckDataHolder.placePriceView = (TextView) row.findViewById(R.id.placePriceView);
            foodTruckDataHolder.placeRatingView = (TextView) row.findViewById(R.id.placeRatingView);
            foodTruckDataHolder.placeAddressView = (TextView) row.findViewById(R.id.placeAddressView);
            foodTruckDataHolder.placeOpenNowView = (TextView) row.findViewById(R.id.placeOpenNowView);
            foodTruckDataHolder.placePhoneView = (TextView) row.findViewById(R.id.placePhoneView);
            foodTruckDataHolder.placePostalView = (TextView) row.findViewById(R.id.placePostalView);
//            foodTruckDataHolder.placeCompassView = (MyCompassView) row.findViewById(R.id.placeThumbnailView);
//            foodTruckDataHolder.placeThumbnailView = (NetworkImageView) row.findViewById(R.id.placeThumbnailView);
            row.setTag(foodTruckDataHolder);

        }else{
            foodTruckDataHolder = (FoodTruckDataHolder) row.getTag();
        }

        FoodTruckData foodTruck = this.foodTruckDataArrayList.get(aPosition);

//        FoodTruckData foodTruck = getItem(aPosition);


        foodTruckDataHolder.placeNameView.setText(foodTruck.getPlaceName());
        foodTruckDataHolder.placeDistanceView.setText(String.format("%.2f", foodTruck.getDistanceCalculatedMiles()) + "mi (" + Integer.valueOf((int) foodTruck.getDistanceCalculatedFeet()) + " ft)");


        foodTruckDataHolder.placePriceView.setText(String.valueOf("Price: " + foodTruck.getPriceLevel()));
        foodTruckDataHolder.placeRatingView.setText(String.valueOf("Rating: " + foodTruck.getRating()));
        foodTruckDataHolder.placeAddressView.setText(foodTruck.getVicinityAddress());

        foodTruckDataHolder.placePhoneView.setText(foodTruck.getPhoneNumberFormatted());
        foodTruckDataHolder.placeOpenNowView.setText("Open: " + String.valueOf(foodTruck.getIsOpenNow()));
        foodTruckDataHolder.placePostalView.setText(foodTruck.getPostalCode());
//        foodTruckDataHolder.placeCompassView.setBackgroundResource(R.drawable.newcompass);


//        foodTruckDataHolder.placeThumbnail.setImageUrl(foodTruck.getIconUrl(),imageLoader);


        return row;
    }

}
