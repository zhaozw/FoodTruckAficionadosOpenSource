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
    Context context;
    private int mLayoutResourceId;
    ArrayList<FoodTruckData> foodTruckDataArrayList;
    LayoutInflater layoutInflater;
    FoodTruckData foodTruckData;
    FoodTruckDataGetter foodTruckDataGetter;

    public FoodTruckDataAdapter(Context aContext, int aResource) {
        super(aContext, aResource);
        this.context = aContext;
        this.layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutResourceId = aResource;
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

    @Override
    public View getView(int aPosition, View aConvertView, ViewGroup aParent){
        FoodTruckDataHolder foodTruckDataHolder;


        View row = aConvertView;

        //inflate layout for a single view
//        LayoutInflater layoutInflater = LayoutInflater.from(context);

        if(aConvertView == null){
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

        FoodTruckData foodTruck = this.foodTruckDataArrayList.get(aPosition);

//        FoodTruckData foodTruck = getItem(aPosition);

        foodTruckDataHolder.placeNameView.setText(foodTruck.getPlaceName());
        foodTruckDataHolder.placeDistanceView.setText(foodTruck.getVicinityAddress());
        foodTruckDataHolder.placePriceView.setText(String.valueOf(foodTruck.getPriceLevel()));
        foodTruckDataHolder.placeRatingView.setText(String.valueOf(foodTruck.getRating()));
        foodTruckDataHolder.placeAddressView.setText(foodTruck.getVicinityAddress());
//        foodTruckDataHolder.placeThumbnail.setImageUrl(foodTruck.getIconUrl(),imageLoader);


        return row;
    }

}
