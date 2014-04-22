package com.justin.truckfinder.app;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class FoodTruckNearbyActivity extends FragmentActivity implements FoodTruckListViewFragment.OnItemSelectedListener{
//    private AdView adView;
    /* Your ad unit id. Replace with your actual ad unit id. */
//    private static final String AD_UNIT_ID = "ca-app-pub-0711386608786016/2778837213";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_food_truck_nearby);
        if (findViewById(R.id.container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            FoodTruckListViewFragment foodTruckListViewFragment = new FoodTruckListViewFragment();
            foodTruckListViewFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new FoodTruckListViewFragment())
                    .commit();
        }

    }

//    @Override
//     public void onStart() {
//        super.onStart();
//        ... // The rest of your onStart() code.
//        EasyTracker.getInstance(this).activityStart(this);  // Add this method.
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        ... // The rest of your onStop() code.
//        EasyTracker.getInstance(this).activityStop(this);  // Add this method.
//    }

    @Override
    public void OnItemSelected(){
        Fragment mFoodTruckListFragment = new FoodTruckDetailsFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, mFoodTruckListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.food_truck_nearby, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }



}
