package com.justin.truckfinder.app;

/*
 * Created by justindelta on 3/25/14.
 */

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * LruCache: For caches that do not override sizeOf(K, V), this is the maximum number of entries in the cache.
 * For all other caches, this is the maximum sum of the sizes of the entries in this cache.
 * Ref:http://developer.android.com/reference/android/util/LruCache.html#LruCache(int)
 *
 * Currently, the cache keeps maximum of 100 Cache entries.
 *
 *
 */
public class BitmapLruCache extends LruCache<String,Bitmap> implements ImageLoader.ImageCache {
    public BitmapLruCache(int maxSize) {
        super(maxSize);
        //or setLimit(Runtime.getRuntime().maxMemory()/4);
    }

    /*@Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }*/

    @Override
    public Bitmap getBitmap(String url) {
        System.out.println("######## BitmapLruCache GET ######## "+url);
        return (Bitmap)get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        System.out.println("######## BitmapLruCache PUT ######## "+url);
        put(url, bitmap);
    }
}