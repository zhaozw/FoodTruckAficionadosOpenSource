package com.justin.truckfinder.app;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by justinmac on 4/6/14.
 */
public class NetworkImageGetter {
    ImageView mImg;
    protected NetworkImageView networkImageView;
    protected VolleyImageLoaderHelper mVolleyImageLoaderHelper;
    protected Context context;


}


