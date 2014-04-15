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
public class FoodTruckDataAdapter extends ArrayAdapter<FoodTruckData>{
    // necessary?
    private static final String TAG = FoodTruckDataAdapter.class.getSimpleName();
    // necessary.
    private Context context;
    private int mLayoutResourceId;
    private ArrayList<FoodTruckData> foodTruckDataArrayList;
    private LayoutInflater layoutInflater;
    private MyCompassView.SensorDataRequestListener sensorListener;
    public int mPosition;
    public FoodTruckNearbyActivity foodTruckNearbyActivity;
    public IntentsData intentsData;


    public interface IntentListener{
        public void passIntent();
    }

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

    public void setSensorListener(MyCompassView.SensorDataRequestListener listener){
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
        public TextView placePostalView;
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

            MyCompassView myCompassView;
            myCompassView = new MyCompassView(context);
            RelativeLayout.LayoutParams compassLayout = new RelativeLayout.LayoutParams(boxSize, boxSize);
            compassLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            compassLayout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
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
            foodTruckDataHolder.placePostalView = (TextView) row.findViewById(R.id.placePostalView);
            foodTruckDataHolder.placeExtraView = (TextView) row.findViewById(R.id.placeExtraView);
            foodTruckDataHolder.placeNetworkImageView = (NetworkImageView) row.findViewById(R.id.googlePlacesView);
            foodTruckDataHolder.myCompassView = myCompassView;
            foodTruckDataHolder.mapsImageButton = (ImageButton) row.findViewById(R.id.mapsButtonImplicit);
            foodTruckDataHolder.phoneImageButton = (ImageButton) row.findViewById(R.id.phoneButtonImplicit);

            row.setTag(foodTruckDataHolder);

        } else {
            foodTruckDataHolder = (FoodTruckDataHolder) row.getTag();
        }

        mPosition = aPosition;

        final FoodTruckData foodTruck = this.foodTruckDataArrayList.get(mPosition);

        foodTruckDataHolder.mapsImageButton.setTag(foodTruckDataHolder);
        foodTruckDataHolder.placePhoneView.setTag(foodTruckDataHolder);
        foodTruckDataHolder.phoneImageButton.setTag(foodTruckDataHolder);
        foodTruckDataHolder.myCompassView.setTag(foodTruckDataHolder);

        // touching maps icon will offer option to launch to geo compatible implicit intent
        foodTruckDataHolder.mapsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                FoodTruckDataHolder foodTruckDataHolder = (FoodTruckDataHolder) view.getTag();
                foodTruckDataHolder.mapsImageButton.setImageResource(R.drawable.mapsiconcustom);
                Toast.makeText(context, "Image Button Row # " + mPosition + " " + foodTruck.getPlaceName(),
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                String start = String.format("geo:%s,%s", foodTruck.getUserLatitude(), foodTruck.getUserLongitude());


                String withQuery = start + String.format("?q=%s,%s(%s)", foodTruck.getLatitude(), foodTruck.getLongitude(), "LAbel");
                Intent geoItent = new Intent(Intent.ACTION_VIEW, Uri.parse(withQuery));
                context.startActivity(geoItent);

            }
        });

        // touching compass icon will offer option to launch to geo compatible implicit intent
        foodTruckDataHolder.myCompassView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                FoodTruckDataHolder foodTruckDataHolder = (FoodTruckDataHolder) view.getTag();
                foodTruckDataHolder.myCompassView.setVisibility(View.VISIBLE);
//                Toast.makeText(context, "Image Button Row # " + mPosition + " " + foodTruck.getPlaceName(),
//                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                String start = String.format("geo:%s,%s", foodTruck.getUserLatitude(), foodTruck.getUserLongitude());


                String withQuery = start + String.format("?q=%s,%s(%s)", foodTruck.getLatitude(), foodTruck.getLongitude(), "LAbel");
                Intent geoItent = new Intent(Intent.ACTION_VIEW, Uri.parse(withQuery));
                context.startActivity(geoItent);

            }
        });

        // touching displayed phone number will offer option to call via implicit intent
        foodTruckDataHolder.placePhoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                FoodTruckDataHolder foodTruckDataHolder = (FoodTruckDataHolder) view.getTag();
                foodTruckDataHolder.mapsImageButton.setImageResource(R.id.placePhoneView);
//                Toast.makeText(context, "TextView Row #" + mPosition + foodTruck.getPlaceName(),
//                        Toast.LENGTH_SHORT).show();

                String phoneCall = "tel:" + foodTruck.getPhone();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(phoneCall));
                context.startActivity(callIntent);

            }
        });

        // touching displayed phone number will offer option to call via implicit intent
        foodTruckDataHolder.phoneImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                FoodTruckDataHolder foodTruckDataHolder = (FoodTruckDataHolder) view.getTag();
                foodTruckDataHolder.phoneImageButton.setImageResource(R.drawable.phonetemp);
//                Toast.makeText(context, "TextView Row #" + mPosition + foodTruck.getPlaceName(),
//                        Toast.LENGTH_SHORT).show();
                String phoneCall = "tel:" + foodTruck.getPhone();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(phoneCall));
                context.startActivity(callIntent);

            }
        });


        String foodTruckNameLineOne = "";
        String foodTruckNameLineTwo = "";

        // necessary to ensure that the UI isn't being widened too much by devices with smaller screens
        // FOR ALL THE FOLLOWING SETTEXT, IT IS IMPERATIVE THAT "sp" be used as the textSize type in the XML
        // TO ENSURE THAT THE SIZE OF THE TEXT WILL BE TRANSFORMED IN ACCORDANCE WITH ANY CHANGES IN ACCESSIBILITY SETTINGS
        // FOR THE OS FONT SIZE
//        if(foodTruck.getPlaceName().length() > 17){
//            foodTruckNameLineOne = foodTruck.getPlaceName().substring(0,17);
//            foodTruckNameLineTwo = foodTruck.getPlaceName().substring(17,foodTruck.getPlaceName().length());
//            foodTruckDataHolder.placeNameView.setText(foodTruckNameLineOne + "...");
//            foodTruckDataHolder.placeNameViewTwo.setText("..." + foodTruckNameLineTwo);
//            foodTruckDataHolder.placeNameViewThree.setText("");
//            // necessary otherwise other placeNames were being overwritten or scrambled
//            foodTruckNameLineOne = "";
//            foodTruckNameLineTwo = "";
//        }else if (foodTruck.getPlaceName().length() <= 17){
//            foodTruckDataHolder.placeNameViewThree.setText((foodTruck.getPlaceName()));
//            // necessary otherwise other placeNames were being overwritten or scrambled
//            foodTruckDataHolder.placeNameView.setText("");
//            foodTruckDataHolder.placeNameViewTwo.setText("");
//        }
        if (foodTruck.getPlaceName() != null) {
            foodTruckDataHolder.placeNameViewThree.setText(foodTruck.getPlaceName());
        }else {
            foodTruckDataHolder.placeNameViewThree.setText(foodTruck.getFourSquareName());
        }
        // String.format here is allowing me to use "%.2f" to indicate that the float will be converted to a string to 2 decimal places
        foodTruckDataHolder.placeDistanceMilesView.setText(String.format("%.2f", foodTruck.getDistanceCalculatedMiles()) + " miles");
        foodTruckDataHolder.placeDistanceFeetView.setText(Integer.valueOf((int) foodTruck.getDistanceCalculatedFeet()) + " ft");

        if (String.valueOf((int) foodTruck.getPriceLevel()) == null) {
            foodTruckDataHolder.placePriceView.setText("- -");
        } else {
            foodTruckDataHolder.placePriceView.setText(String.valueOf("Price: " + foodTruck.getPriceLevel() + " of 4"));
        }

        // for UI readability
        if (String.valueOf((int) foodTruck.getRating()) == null) {
            foodTruckDataHolder.placeRatingView.setText("- -");
        } else {
            foodTruckDataHolder.placeRatingView.setText(String.valueOf("Rating: " + foodTruck.getRating() + " of 5"));
        }

        // every string with vicinity address was ending with ", Austin" which is 8 characters in length, thus
        // the successful removal of a substring that is already too long
        if (foodTruck.getVicinityAddress() == null && foodTruck.getFourSquareName() != null){
            foodTruckDataHolder.placeAddressView.setText(foodTruck.getFoursquareAddress());
        }

        if (foodTruck.getVicinityAddress() != null && foodTruck.getVicinityAddress().contains(", Austin")){
            foodTruckDataHolder.placeAddressView.setText(foodTruck.getVicinityAddress().substring(0, foodTruck.getVicinityAddress().length() - 8));
        } else if (foodTruck.getVicinityAddress() !=null) {
            foodTruckDataHolder.placeAddressView.setText(foodTruck.getVicinityAddress());
        }


        //TODO need to make sure that phone numbers provided by fourSquare are being saved to the ArrayList for each corresponding foodTruckData object
        if(foodTruck.getPhoneNumberFormatted() == null){
            foodTruckDataHolder.placePhoneView.setText("Phone Unavailable");
        }else {
            foodTruckDataHolder.placePhoneView.setText("Call " + foodTruck.getPhoneNumberFormatted());
        }

        if(foodTruck.getIsOpenNow() == true){
            foodTruckDataHolder.placeOpenNowView.setText("Currently Open");
        }
        if(foodTruck.getIsOpenNow() == false){
            foodTruckDataHolder.placeOpenNowView.setText("Currently Closed");
        }
        if(foodTruck.getIsOpenNow() == Boolean.parseBoolean(null)){
            foodTruckDataHolder.placeOpenNowView.setText("Status Unavailable");
        }

        if(foodTruck.getPostalCode() != null) {
            foodTruckDataHolder.placePostalView.setText(foodTruck.getPostalCode());
        }else{
            foodTruckDataHolder.placePostalView.setText(" ");
        }
        foodTruckDataHolder.placeExtraView.setText(" ");

//        if(foodTruck.getPhotoPlacesURL() == null){
//            foodTruckDataHolder.placeNetworkImageView.setDefaultImageResId(R.drawable.ic_launcher);
//        }else if(foodTruck.getPhotoPlacesURL() != null) {
//            FoodTruckData foodTruckImage = this.foodTruckDataArrayList.get(mPosition + foodTruckDataArrayList.size());
//            foodTruckDataHolder.placeNetworkImageView.setImageUrl(foodTruckImage.getPhotoPlacesURL(), foodTruckImage.getImageLoader());
//        }
        foodTruckDataHolder.myCompassView.setDirections(FoodTruckData.getUserLatitude(), FoodTruckData.getUserLongitude(), foodTruck.getLatitude(), foodTruck.getLongitude());
        //todo: give myCompassView the sensor data to do the calculation (you already gave it the vectors)

        return row;
    }

    View.OnClickListener PopupListener = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            Integer viewPosition = (Integer) view.getTag();
        }
    };

    //TODO find out why there is a recursion warning and a better way of implementing the following, if any
    public FoodTruckData getFoodTruckData(){
        FoodTruckData foodTruckNew = getFoodTruckData();
        return foodTruckNew;
    }

    public void launchIntent(){

    }

}
