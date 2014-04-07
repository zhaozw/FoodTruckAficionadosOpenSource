package com.justin.truckfinder.app;

import android.content.Context;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by justinmac on 4/6/14.
 */
public class VolleyImageLoaderHelper {

    // ImageLoader From Volley
    private ImageLoader sImageLoader;

    /**
     *
     * @return Volley ImageLoader
     */
    public ImageLoader get() {
        return sImageLoader;
    }

    public void initImageLoader(Context context) {
        if (sImageLoader != null) {
            return;
        }

        sImageLoader = new ImageLoader(
                Volley.newRequestQueue(context), 	// Volley Request
                new VolleyBitmapLruCache(context)			// Volley BitmapLruCacheHepler
        );

    }
}