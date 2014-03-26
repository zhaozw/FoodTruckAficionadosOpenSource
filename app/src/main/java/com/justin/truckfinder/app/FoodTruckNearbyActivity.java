package com.justin.truckfinder.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


public class FoodTruckNearbyActivity extends Activity implements FoodTruckListViewFragment.OnItemSelectedListener{

    FoodTruckDataGetter foodTruckDataGetter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_truck_nearby);
        setContentView(android.R.layout.simple_expandable_list_item_2);

        RelativeLayout r = new RelativeLayout(this);


        LayoutInflater myLayoutInflater = LayoutInflater.from(this);

        View myView = myLayoutInflater.inflate(R.layout.map_fragment_food_truck,null);


        myView.set

        if (findViewById(R.id.container) != null) {

            if (savedInstanceState != null) {
                return;
            }


            FoodTruckListViewFragment foodTruckListViewFragment = new FoodTruckListViewFragment();


            foodTruckListViewFragment.setArguments(getIntent().getExtras());


            getFragmentManager().beginTransaction()
                    .add(R.id.container, new FoodTruckListViewFragment())
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.food_truck_nearby, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_food_truck_nearby, container, false);
            return rootView;
        }
    }


    @Override
    public void OnItemSelected(){
        Fragment mFoodTruckMapsFragment = new FoodTruckMapsFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, mFoodTruckMapsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
