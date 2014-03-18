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

    Context context;
    private int mLayoutResourceId;
    private ArrayList<FoodTruckData> foodTruckData;


    public FoodTruckDataAdapter(Context aContext, int aResource, ArrayList<FoodTruckData> aFoodTruckData) {
        super(aContext, aResource, aFoodTruckData);
        this.context = aContext;
        this.mLayoutResourceId = aResource;
        this.foodTruckData = aFoodTruckData;
    }

//    public FoodTruckDataAdapter(Context aContext, int mLayoutResourceId, ArrayList<FoodTruckData> foodTruckDataArrayList){
//        super(aContext, 0, foodTruckDataArrayList);
//        this.context = aContext;
//        this.foodTruckData = foodTruckDataArrayList;
//    }

//    public FoodTruckDataAdapter(Context context, int resource, List<ArrayList<FoodTruckData>> objects, Context context1, int mLayoutResourceId, ArrayList<FoodTruckData> foodTruckData) {
//        super(context, resource, objects);
//        this.context = context1;
//        this.mLayoutResourceId = mLayoutResourceId;
//        this.foodTruckData = |;
//    }


    @Override
    public int getCount() {
        if (foodTruckData.size() == 0) {
            return 12;
        }else {
            return foodTruckData.size();
        }
    }

    @Override
    public int getPosition(FoodTruckData item) {
        return super.getPosition(item);
    }

    @Override
    public FoodTruckData getItem(int position) {
        return super.getItem(position);
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
//        public NetworkImageView placeThumbnail;
    }

    public View getView(int aPosition, View aConvertView, ViewGroup aParent){
        FoodTruckDataHolder foodTruckDataHolder;


        View row = aConvertView;

        //inflate layout for a single view
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        if(row == null){
            row = layoutInflater.inflate(mLayoutResourceId, aParent, false);

            foodTruckDataHolder = new FoodTruckDataHolder();

            foodTruckDataHolder.placeNameView = (TextView) row.findViewById(R.id.placeNameView);
            foodTruckDataHolder.placeDistanceView = (TextView) row.findViewById(R.id.placeAddressView);
            foodTruckDataHolder.placePriceView = (TextView) row.findViewById(R.id.placePriceView);
            foodTruckDataHolder.placeRatingView = (TextView) row.findViewById(R.id.placeRatingView);
            foodTruckDataHolder.placeAddressView = (TextView) row.findViewById(R.id.placeAddressView);
//            foodTruckDataHolder.placeThumbnailView = (NetworkImageView) row.findViewById(R.id.placeThumbnailView);
            row.setTag(foodTruckDataHolder);

        }else{
            foodTruckDataHolder = (FoodTruckDataHolder) row.getTag();
        }

        FoodTruckData foodTruck = foodTruckData.get(aPosition);

        foodTruckDataHolder.placeNameView.setText(foodTruck.getPlaceName());
        foodTruckDataHolder.placeDistanceView.setText(foodTruck.getVicinityAddress());
        foodTruckDataHolder.placePriceView.setText(String.valueOf(foodTruck.getPriceLevel()));
        foodTruckDataHolder.placeRatingView.setText(String.valueOf(foodTruck.getRating()));
        foodTruckDataHolder.placeAddressView.setText(foodTruck.getVicinityAddress());
//        foodTruckDataHolder.placeThumbnail.setImageUrl(foodTruck.getIconUrl(),imageLoader);


        return row;
    }

}
