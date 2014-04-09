package com.justin.truckfinder.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.List;


public class FoodTruckNearbyActivity extends Activity implements FoodTruckListViewFragment.OnItemSelectedListener, FoodTruckListViewFragment.OnIntentSelectedListener{
    ProgressBar mProgressBar;

    private Button mReturnButton = null;
    private Button mPerformButton = null;
    private Spinner mSpinner = null;

    @Override
    public void setContentView(View view) {
        init().addView(view);
    }

    @Override
    public void setContentView(int layoutResID) {
        getLayoutInflater().inflate(layoutResID,init(),true);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        init().addView(view,params);
    }

    private ViewGroup init(){
        super.setContentView(R.layout.progress);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        return (ViewGroup) findViewById(R.id.activity_frame);
    }

    protected ProgressBar getProgressBar(){
        return mProgressBar;
    }

    public void setProgressBar(ProgressBar mProgressBar) {
        this.mProgressBar = mProgressBar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setProgressBar(getProgressBar());
        setContentView(R.layout.activity_food_truck_nearby);
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

//                Bundle extras = getActivity().getIntent().getExtras();
//
//        if(extras != null){
//            String detailValue = extras.getString("KeyForSending");
//            if(detailValue != null){
//                Toast.makeText(this, Integer.parseInt(detailValue), Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        mSpinner = (Spinner) findViewById(R.id.spinnerSelection);
//        mReturnButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Intent returnIntent = new Intent();
//                String mySelection = mSpinner.getSelectedItem().toString();
//                returnIntent.putExtra("KeyForReturning",mySelection);
//                setResult(RESULT_OK, returnIntent);
//                finish();
//            }
//        });
//
//        mPerformButton = (Button) findViewById(R.id.mapsButtonImplicit);
//        mPerformButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//            int position = mSpinner.getSelectedItemPosition();
//            Intent implicitIntent = null;
//            switch (position){
//                case 0:
//                    //nothing selected
//                    break;
//                case 1:
//                    //go to GEO
//                    implicitIntent = new Intent(Intent.ACTION_VIEW, Uri.parse());
//                    break;
//                case 2:
//                    // call food truck
//                    implicitIntent = new Intent(Intent.ACTION_VIEW,
//                            Uri.parse("geotel:"));
//                    break;
//            }
//            if(implicitIntent != null){
//                if(isIntentAvailable(implicitIntent) == true){
//                    startActivity(implicitIntent);
//                }else {
//                    Toast.makeText(view.getContext(), "no application is available", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        }
//        });
    }

    public boolean isIntentAvailable(Intent intent){
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isIntentSafe = activities.size() > 0;
        return  isIntentSafe;
    }

    public void startActivityForIntent(Intent intent){
        startActivity(intent);

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

    @Override
    public void OnIntentReceived() {
//        startActivity(FoodTruckData.getIntentMap());
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
        Fragment mFoodTruckListFragment = new FoodTruckDetailsFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, mFoodTruckListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
